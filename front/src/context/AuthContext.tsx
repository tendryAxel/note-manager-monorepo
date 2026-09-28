"use client";

import { createContext, useContext, useState, useEffect, ReactNode } from "react";
import {
  OpenAPI,
  AuthenticationService,
  NotesService,
  AuthResponse,
  User,
  Note,
  LoginRequest,
  RegisterRequest,
  CreateNoteRequest,
} from "@/generated";

const API_URL = process.env.NEXT_PUBLIC_API_URL || "http://localhost:8080/api/v1";

OpenAPI.BASE = API_URL;

OpenAPI.TOKEN = async () => {
  if (typeof window !== "undefined") {
    return localStorage.getItem("token") || "";
  }
  return "";
};

interface AuthContextType {
  user: User | null;
  notes: Note[];
  login: (email: string, password: string) => Promise<void>;
  register: (name: string, email: string, password: string) => Promise<void>;
  logout: () => void;
  addNote: (title: string, content: string) => Promise<void>;
  deleteNote: (id: string) => Promise<void>;
  isLoading: boolean;
}

const AuthContext = createContext<AuthContextType | undefined>(undefined);

function getStoredAuth(): { user: User | null; token: string | null } {
  if (typeof window === "undefined") return { user: null, token: null };
  const userStr = localStorage.getItem("user");
  const token = localStorage.getItem("token");
  return {
    user: userStr ? JSON.parse(userStr) : null,
    token,
  };
}

function setStoredAuth(user: User | null, token: string | null) {
  if (typeof window === "undefined") return;
  if (user) {
    localStorage.setItem("user", JSON.stringify(user));
  } else {
    localStorage.removeItem("user");
  }
  if (token) {
    localStorage.setItem("token", token);
  } else {
    localStorage.removeItem("token");
  }
}

export function AuthProvider({ children }: { children: ReactNode }) {
  const [user, setUser] = useState<User | null>(null);
  const [notes, setNotes] = useState<Note[]>([]);
  const [isLoading, setIsLoading] = useState(true);

  useEffect(() => {
    const { user: storedUser } = getStoredAuth();
    setUser(storedUser);
    setIsLoading(false);
  }, []);

  const login = async (email: string, password: string) => {
    const request: LoginRequest = { email, password };
    const data: AuthResponse = await AuthenticationService.login({ requestBody: request });

    setUser(data.user);
    setStoredAuth(data.user, data.token);

    await fetchNotes();
  };

  const register = async (name: string, email: string, password: string) => {
    const request: RegisterRequest = { name, email, password };
    const data: AuthResponse = await AuthenticationService.register({ requestBody: request });

    setUser(data.user);
    setStoredAuth(data.user, data.token);
    setNotes([]);
  };

  const logout = () => {
    setUser(null);
    setNotes([]);
    setStoredAuth(null, null);
  };

  const fetchNotes = async () => {
    try {
      const notesData = await NotesService.getNotes();
      setNotes(notesData);
    } catch (error) {
      console.error("Failed to fetch notes:", error);
      setNotes([]);
    }
  };

  const addNote = async (title: string, content: string) => {
    const request: CreateNoteRequest = { title, content };
    const newNote = await NotesService.createNote({ requestBody: request });
    setNotes([newNote, ...notes]);
  };

  const deleteNote = async (id: string) => {
    await NotesService.deleteNote({ id });
    setNotes(notes.filter(note => note.id !== id));
  };

  useEffect(() => {
    if (user) {
      fetchNotes();
    }
  }, [user]);

  return (
    <AuthContext.Provider value={{ user, notes, login, register, logout, addNote, deleteNote, isLoading }}>
      {children}
    </AuthContext.Provider>
  );
}

export function useAuth() {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error("useAuth must be used within an AuthProvider");
  }
  return context;
}

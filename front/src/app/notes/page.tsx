"use client";

import { useState, useEffect } from "react";
import { useRouter } from "next/navigation";
import { useAuth } from "@/context/AuthContext";
import { NoteForm } from "@/components/NoteForm";
import { NoteList } from "@/components/NoteList";
import { motion, AnimatePresence } from "framer-motion";

export default function NotesPage() {
  const router = useRouter();
  const { user, notes, logout, isLoading } = useAuth();
  const [showForm, setShowForm] = useState(false);

  useEffect(() => {
    if (!isLoading && !user) {
      router.push("/login");
    }
  }, [user, isLoading, router]);

  if (isLoading) {
    return (
      <div className="min-h-screen flex items-center justify-center bg-gray-50 dark:bg-gray-900">
        <motion.div
          animate={{ rotate: 360 }}
          transition={{ duration: 1, repeat: Infinity, ease: "linear" }}
          className="h-12 w-12 border-4 border-blue-600 border-t-transparent rounded-full"
        />
      </div>
    );
  }

  if (!user) return null;

  const pageVariants = {
    initial: { opacity: 0, y: 20 },
    animate: { opacity: 1, y: 0, transition: { duration: 0.5, ease: "easeOut" as const } },
    exit: { opacity: 0, y: -20, transition: { duration: 0.3 } },
  };

  const headerVariants = {
    hidden: { opacity: 0, y: -20 },
    visible: { opacity: 1, y: 0, transition: { duration: 0.4, ease: "easeOut" as const } },
  };

  const contentVariants = {
    hidden: { opacity: 0, y: 20 },
    visible: { opacity: 1, y: 0, transition: { duration: 0.5, ease: "easeOut" as const, staggerChildren: 0.1 } },
  };

  const itemVariants = {
    hidden: { opacity: 0, y: 20 },
    visible: { opacity: 1, y: 0, transition: { duration: 0.4, ease: "easeOut" as const } },
  };

  const buttonVariants = {
    hidden: { opacity: 0, scale: 0.9 },
    visible: { opacity: 1, scale: 1, transition: { duration: 0.4, ease: "easeOut" as const } },
    hover: { scale: 1.02, y: -2 },
    tap: { scale: 0.98 },
  };

  return (
    <motion.div
      initial="initial"
      animate="animate"
      exit="exit"
      variants={pageVariants}
      className="min-h-screen bg-gray-50 dark:bg-gray-900"
    >
      <div className="fixed inset-0 bg-[radial-gradient(ellipse_at_top_right,_var(--tw-gradient-stops))] from-blue-500/5 via-transparent to-transparent pointer-events-none" />
      
      <motion.header
        variants={headerVariants}
        className="glass dark:glass-dark border-b border-white/20 dark:border-white/10 sticky top-0 z-40"
        style={{ backdropFilter: 'blur(20px)' }}
      >
        <div className="max-w-4xl mx-auto px-4 py-4 flex items-center justify-between">
          <motion.h1 variants={itemVariants} className="text-2xl font-bold text-gray-900 dark:text-white">
            My Notes
          </motion.h1>
          <motion.div variants={itemVariants} style={{ transitionDelay: '0.1s' }} className="flex items-center gap-4">
            <span className="text-gray-600 dark:text-gray-400 hidden sm:block">Welcome, {user.name}</span>
            <motion.button
              onClick={logout}
              variants={buttonVariants}
              whileHover="hover"
              whileTap="tap"
              className="glass-button-secondary px-4 py-2 text-sm font-medium text-gray-700 dark:text-gray-300 rounded-xl"
              style={{ background: 'linear-gradient(135deg, rgba(239, 68, 68, 0.9), rgba(220, 38, 38, 0.9))', boxShadow: '0 4px 16px 0 rgba(239, 68, 68, 0.3), inset 0 1px 0 rgba(255, 255, 255, 0.2)' }}
            >
              Logout
            </motion.button>
          </motion.div>
        </div>
      </motion.header>

      <motion.main
        variants={contentVariants}
        className="max-w-4xl mx-auto px-4 py-8"
      >
        <motion.div variants={itemVariants} className="flex justify-between items-center mb-6">
          <motion.h2 className="text-xl font-semibold text-gray-900 dark:text-white">
            {notes.length === 0 ? "No notes yet" : `Your Notes (${notes.length})`}
          </motion.h2>
          <motion.button
            onClick={() => setShowForm(true)}
            variants={buttonVariants}
            whileHover="hover"
            whileTap="tap"
            className="glass-button px-4 py-2 text-sm font-medium text-white rounded-xl"
          >
            + New Note
          </motion.button>
        </motion.div>

        <AnimatePresence mode="wait">
          {showForm && (
            <motion.div
              key="note-form"
              initial={{ opacity: 0, y: 20 }}
              animate={{ opacity: 1, y: 0 }}
              exit={{ opacity: 0, y: -20 }}
              transition={{ duration: 0.3, ease: "easeOut" as const }}
              className="mb-6"
            >
              <NoteForm onClose={() => setShowForm(false)} />
            </motion.div>
          )}
        </AnimatePresence>

        <NoteList notes={notes} />
      </motion.main>
    </motion.div>
  );
}
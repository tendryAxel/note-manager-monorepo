"use client";

import { useAuth } from "@/context/AuthContext";
import { Note } from "@/generated";
import { motion, AnimatePresence } from "framer-motion";

interface NoteListProps {
  notes: Note[];
}

export function NoteList({ notes }: NoteListProps) {
  const { deleteNote } = useAuth();

  const containerVariants = {
    hidden: { opacity: 0 },
    visible: {
      opacity: 1,
      transition: { staggerChildren: 0.08, delayChildren: 0.1 },
    },
  };

  const itemVariants = {
    hidden: { opacity: 0, y: 20, scale: 0.98 },
    visible: { 
      opacity: 1, 
      y: 0, 
      scale: 1, 
      transition: { duration: 0.4, ease: "easeOut" as const } 
    },
    exit: { 
      opacity: 0, 
      y: -20, 
      scale: 0.98, 
      transition: { duration: 0.2, ease: "easeIn" as const } 
    },
    hover: { 
      y: -4, 
      scale: 1.01,
      transition: { duration: 0.2, ease: "easeOut" as const } 
    },
  };

  const deleteButtonVariants = {
    hidden: { opacity: 0, scale: 0.8 },
    visible: { opacity: 1, scale: 1, transition: { duration: 0.2, ease: "easeOut" as const } },
    hover: { scale: 1.1, rotate: 90 },
    tap: { scale: 0.9 },
  };

  if (notes.length === 0) {
    return (
      <motion.div
        initial={{ opacity: 0, y: 20 }}
        animate={{ opacity: 1, y: 0 }}
        className="text-center py-12 glass-card rounded-2xl"
      >
        <motion.div
          initial={{ scale: 0, rotate: -180 }}
          animate={{ scale: 1, rotate: 0 }}
          transition={{ duration: 0.6, ease: "easeOut" as const }}
          className="mx-auto h-12 w-12 text-gray-400 dark:text-gray-600"
        >
          <svg fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M9 5H7a2 2 0 00-2 2v12a2 2 0 002 2h10a2 2 0 002-2V7a2 2 0 00-2-2h-2M9 5a2 2 0 002 2h2a2 2 0 002-2M9 5a2 2 0 012-2h2a2 2 0 012 2m-6 9l2 2 4-4" />
          </svg>
        </motion.div>
        <motion.h3
          initial={{ opacity: 0, y: 10 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ delay: 0.2 }}
          className="mt-2 text-sm font-medium text-gray-900 dark:text-white"
        >
          No notes
        </motion.h3>
        <motion.p
          initial={{ opacity: 0, y: 10 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ delay: 0.3 }}
          className="mt-1 text-sm text-gray-500 dark:text-gray-400"
        >
          Get started by creating a new note.
        </motion.p>
      </motion.div>
    );
  }

  return (
    <AnimatePresence mode="popLayout">
      <motion.div
        variants={containerVariants}
        initial="hidden"
        animate="visible"
        className="space-y-3"
      >
        {notes.map((note) => (
          <motion.div
            key={note.id}
            variants={itemVariants}
            whileHover="hover"
            layout
            className="glass-card rounded-xl p-4 relative overflow-hidden group"
          >
            <div className="absolute inset-0 glass-shimmer opacity-0 group-hover:opacity-100 transition-opacity duration-300" />
            
            <motion.div
              layout
              className="relative z-10 flex justify-between items-start"
            >
              <div className="flex-1 pr-4">
                <motion.h3 className="text-lg font-semibold text-gray-900 dark:text-white mb-1">
                  {note.title}
                </motion.h3>
                <motion.p className="text-gray-600 dark:text-gray-400 whitespace-pre-wrap line-clamp-3">
                  {note.content}
                </motion.p>
                <motion.p className="mt-2 text-xs text-gray-400 dark:text-gray-500">
                  Created: {new Date(note.createdAt).toLocaleDateString()}
                </motion.p>
              </div>
              <AnimatePresence>
                <motion.button
                  onClick={() => deleteNote(note.id)}
                  variants={deleteButtonVariants}
                  initial="hidden"
                  animate="visible"
                  exit="hidden"
                  whileHover="hover"
                  whileTap="tap"
                  className="ml-4 p-2 text-gray-400 hover:text-red-600 dark:hover:text-red-400 rounded-xl glass-button-secondary"
                  aria-label="Delete note"
                >
                  <svg className="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16" />
                  </svg>
                </motion.button>
              </AnimatePresence>
            </motion.div>
          </motion.div>
        ))}
      </motion.div>
    </AnimatePresence>
  );
}
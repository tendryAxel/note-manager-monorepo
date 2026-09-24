package com.note.api.note_manager.integrations;

import static org.junit.jupiter.api.Assertions.*;

import com.note.api.note_manager.config.TestSetup;
import com.note.api.note_manager.rest.api.NotesApi;
import com.note.api.note_manager.rest.client.ApiException;
import com.note.api.note_manager.rest.model.CreateNoteRequest;
import org.junit.jupiter.api.Test;

public class NotesIT extends TestSetup {
  @Test
  void get_notes_ok() throws ApiException {
    var credentials = registerUser();
    var noteApi = new NotesApi(anApiClient(credentials.getToken()));

    var notes = noteApi.getNotes();

    assertEquals(0, notes.size());
  }

  @Test
  void create_notes_ok() throws ApiException {
    var credentials = registerUser();
    var noteApi = new NotesApi(anApiClient(credentials.getToken()));
    var createNoteRequest = new CreateNoteRequest().title("title").content("Content");

    var note = noteApi.createNote(createNoteRequest);

    assertEquals(createNoteRequest.getTitle(), note.getTitle());
    assertEquals(createNoteRequest.getContent(), note.getContent());
  }

  @Test
  void delete_notes_ok() throws ApiException {
    var credentials = registerUser();
    var noteApi = new NotesApi(anApiClient(credentials.getToken()));
    var createNoteRequest = new CreateNoteRequest().title("title").content("Content");

    var note = noteApi.createNote(createNoteRequest);

    noteApi.deleteNote(note.getId());

    var notes = noteApi.getNotes();

    assertEquals(0, notes.size());
  }

  @Test
  void wrong_user_delete_notes_ko() throws ApiException {
    var credentials1 = registerUser();
    var credentials2 = registerUser();
    var noteApi1 = new NotesApi(anApiClient(credentials1.getToken()));
    var noteApi2 = new NotesApi(anApiClient(credentials2.getToken()));
    var createNoteRequest = new CreateNoteRequest().title("title").content("Content");

    var note = noteApi1.createNote(createNoteRequest);

    assertThrows(Exception.class, () -> noteApi2.deleteNote(note.getId()));
  }
}

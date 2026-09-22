package com.note.api.note_manager.integrations;

import com.note.api.note_manager.config.TestSetup;
import com.note.api.note_manager.rest.api.NotesApi;
import org.junit.jupiter.api.Test;

public class NotesIT extends TestSetup {
    @Test
    void get_notes_ok() {
        var noteApi = new NotesApi(anApiClient());
    }

    @Test
    void create_notes_ok() {
    }

    @Test
    void delete_notes_ok() {
    }

    @Test
    void wrong_user_get_notes_ko() {
    }

    @Test
    void wrong_user_delete_notes_ko() {
    }
}

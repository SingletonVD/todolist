package com.singletonvd.todolist;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class MainViewModel extends AndroidViewModel {
    private final NoteDatabase noteDatabase = NoteDatabase.getInstance(getApplication());
    private final NotesDao notesDao = noteDatabase.getNotesDao();

    private LiveData<List<Note>> notes;

    public MainViewModel(@NonNull Application application) {
        super(application);
        notes = notesDao.getNotes();
    }

    public void remove(Note note) {
        Thread thread = new Thread(() -> notesDao.remove(note.getId()));
        thread.start();
    }

    public LiveData<List<Note>> getNotes() {
        return notes;
    }
}

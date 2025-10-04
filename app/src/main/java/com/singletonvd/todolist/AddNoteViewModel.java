package com.singletonvd.todolist;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class AddNoteViewModel extends AndroidViewModel {
    private final NoteDatabase noteDatabase = NoteDatabase.getInstance(getApplication());
    private final NotesDao notesDao = noteDatabase.getNotesDao();
    private final MutableLiveData<Boolean> shouldCloseScreen = new MutableLiveData<>();

    public AddNoteViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<Boolean> getShouldCloseScreen() {
        return shouldCloseScreen;
    }

    public void saveNote(Note note) {
        Thread thread = new Thread(() -> {
            notesDao.add(note);
            shouldCloseScreen.postValue(true);
        });
        thread.start();
    }
}

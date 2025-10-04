package com.singletonvd.todolist;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class AddNoteViewModel extends AndroidViewModel {
    private final NoteDatabase noteDatabase = NoteDatabase.getInstance(getApplication());
    private final NotesDao notesDao = noteDatabase.getNotesDao();
    private final MutableLiveData<Boolean> shouldCloseScreen = new MutableLiveData<>();
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    public AddNoteViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<Boolean> getShouldCloseScreen() {
        return shouldCloseScreen;
    }

    public void saveNote(Note note) {
        Disposable disposable = saveNoteRx(note)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(() -> shouldCloseScreen.postValue(true));
        compositeDisposable.add(disposable);
    }

    private Completable saveNoteRx(Note note) {
        return Completable.fromAction(() -> notesDao.add(note));
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.dispose();
    }
}

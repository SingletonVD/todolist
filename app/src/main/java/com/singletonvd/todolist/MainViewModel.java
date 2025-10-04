package com.singletonvd.todolist;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MainViewModel extends AndroidViewModel {
    private final NoteDatabase noteDatabase = NoteDatabase.getInstance(getApplication());
    private final NotesDao notesDao = noteDatabase.getNotesDao();

    private final MutableLiveData<List<Note>> notes = new MutableLiveData<>();
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    public MainViewModel(@NonNull Application application) {
        super(application);
        refreshList();
    }

    public void remove(Note note) {
        Disposable disposable = removeRx(note)
                .subscribeOn(Schedulers.io())
                .subscribe(this::refreshList);
        compositeDisposable.add(disposable);
    }

    private Completable removeRx(Note note) {
        return Completable.fromAction(() -> notesDao.remove(note.getId()));
    }

    public LiveData<List<Note>> getNotes() {
        return notes;
    }

    public void refreshList() {
        Disposable disposable = getNotesRx()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(notes::postValue);
        compositeDisposable.add(disposable);
    };

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.dispose();
    }

    private Single<List<Note>> getNotesRx() {
        return Single.fromCallable(notesDao::getNotes);
    }
}

package com.singletonvd.todolist;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerViewNotes;
    private FloatingActionButton buttonAddNote;
    private NotesAdapter notesAdapter;
    private final Handler handler = new Handler(Looper.getMainLooper());

    private NotesDao notesDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        NoteDatabase noteDatabase = NoteDatabase.getInstance(getApplication());
        notesDao = noteDatabase.getNotesDao();
        initView();
        notesAdapter = new NotesAdapter();
        recyclerViewNotes.setAdapter(notesAdapter);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(
                new ItemTouchHelper.SimpleCallback(
                        0,
                        ItemTouchHelper.LEFT
                ) {
                    @Override
                    public boolean onMove(
                            @NonNull RecyclerView recyclerView,
                            @NonNull RecyclerView.ViewHolder viewHolder,
                            @NonNull RecyclerView.ViewHolder target
                    ) {
                        return false;
                    }

                    @Override
                    public void onSwiped(
                            @NonNull RecyclerView.ViewHolder viewHolder,
                            int direction
                    ) {
                        if ((direction & ItemTouchHelper.LEFT) > 0) {
                            int position = viewHolder.getBindingAdapterPosition();
                            Note note = notesAdapter.getNotes().get(position);
                            Thread thread = new Thread(() -> {
                                notesDao.remove(note.getId());
                                handler.post(() -> showNotes());
                            });
                            thread.start();
                        }
                    }
                });
        itemTouchHelper.attachToRecyclerView(recyclerViewNotes);

        buttonAddNote.setOnClickListener(view -> {
            Intent intent = AddNoteActivity.makeIntent(this);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        showNotes();
    }

    private void initView() {
        buttonAddNote = findViewById(R.id.buttonAddNote);
        recyclerViewNotes = findViewById(R.id.recyclerViewNotes);
    }

    private void showNotes() {
        Thread thread = new Thread(() -> {
            List<Note> notes = notesDao.getNotes();
            handler.post(() -> notesAdapter.setNotes(notes));
        });
        thread.start();
    }
}

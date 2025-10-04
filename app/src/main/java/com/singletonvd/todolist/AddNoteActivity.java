package com.singletonvd.todolist;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

public class AddNoteActivity extends AppCompatActivity {

    public static Intent makeIntent(Context context) {
        return new Intent(context, AddNoteActivity.class);
    }

    private EditText editTextNote;
    private RadioButton radioButtonLowPriority;
    private RadioButton radioButtonMediumPriority;
    private Button buttonSaveNote;
    private AddNoteViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        viewModel = new ViewModelProvider(this).get(AddNoteViewModel.class);
        initViews();
        radioButtonMediumPriority.setChecked(true);

        buttonSaveNote.setOnClickListener(view -> {
            saveNote();
        });
        viewModel.getShouldCloseScreen().observe(this, shouldCloseScreen -> {
            if (shouldCloseScreen) {
                finish();
            }
        });
    }

    private void initViews() {
        editTextNote = findViewById(R.id.editTextNote);
        radioButtonLowPriority = findViewById(R.id.radioButtonLowPriority);
        radioButtonMediumPriority = findViewById(R.id.radioButtonMediumPriority);
        buttonSaveNote = findViewById(R.id.buttonSaveNote);
    }

    private void saveNote() {
        String text = editTextNote.getText().toString().trim();


        if (text.isEmpty()) {
            Toast.makeText(this, R.string.enter_note_text, Toast.LENGTH_SHORT).show();
        } else {
            int priority = getPriority();
            Note note = new Note(text, priority);
            viewModel.saveNote(note);
        }
    }

    private int getPriority() {
        int priority;

        if (radioButtonLowPriority.isChecked()) {
            priority = 0;
        } else if (radioButtonMediumPriority.isChecked()) {
            priority = 1;
        } else {
            priority = 2;
        }

        return priority;
    }

}
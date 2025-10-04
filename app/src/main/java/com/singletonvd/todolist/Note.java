package com.singletonvd.todolist;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "notes")
public class Note {

    @PrimaryKey(autoGenerate = true)
    private final int id;
    private final String text;
    private final int priority;

    public Note(int id, String text, int priority) {
        this.text = text;
        this.id = id;
        this.priority = priority;
    }

    public int getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public int getPriority() {
        return priority;
    }
}

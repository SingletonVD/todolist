package com.singletonvd.todolist;

public class Note {
    private int id;
    private String text;
    private int priority;

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

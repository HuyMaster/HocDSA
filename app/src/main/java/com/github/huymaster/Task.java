package com.github.huymaster;

interface Action {
    void execute();
}

class Task {
    final String key;
    final String description;
    final Action action;

    private Task(String key, String description, Action action) {
        this.key = key;
        this.description = description;
        this.action = action;
    }

    public static Task newTask(String key, String description, Action action) {
        return new Task(key, description, action);
    }

    public void execute() {
        action.execute();
    }
}
package com.codemover.xplanner.Converter;

import java.io.Serializable;

public class CompleteRequest implements Serializable{
    private static final long serialVersionUID = 13501665081524835L;

    public boolean completed;

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
}

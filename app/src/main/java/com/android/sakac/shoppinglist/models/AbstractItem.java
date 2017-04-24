package com.android.sakac.shoppinglist.models;

import android.os.Parcelable;

public abstract class AbstractItem {

    private String name;
    private boolean isComplete;

    public void setName(String name) {
        this.name = name;
    }

    public void setComplete(boolean complete) {
        isComplete = complete;
    }

    public String getName() {
        return name;
    }

    public boolean isComplete() {
        return isComplete;
    }

    protected AbstractItem(String name) {
        this.name = name;
        this.isComplete = false;
    }
}

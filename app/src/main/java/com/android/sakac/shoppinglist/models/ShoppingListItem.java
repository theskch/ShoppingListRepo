package com.android.sakac.shoppinglist.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class ShoppingListItem extends AbstractItem implements Parcelable {

    private long id;
    private boolean isLocked;
    private String password;
    private ArrayList<ShoppingItem> itemList;

    public ArrayList<ShoppingItem> getItemList() {
        return itemList;
    }

    public void setItemList(ArrayList<ShoppingItem> itemList) {
        this.itemList = itemList;
    }

    public long getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isLocked() {
        return isLocked;
    }

    public void setLocked(boolean locked) {
        isLocked = locked;
    }

    public ShoppingListItem(String name, long id) {
        super(name);
        this.id = id;
        this.itemList = new ArrayList<>();
    }

    protected ShoppingListItem(Parcel in) {
        super(in.readString());
        this.id = in.readLong();
        this.setComplete(in.readByte() != 0);
        itemList = new ArrayList<>();
        in.readTypedList(itemList, ShoppingItem.CREATOR);
        this.setLocked(in.readByte() != 0);
        this.setPassword(in.readString());
    }

    @Override
    public int describeContents() {
        return this.hashCode();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.getName());
        dest.writeLong(this.id);
        dest.writeByte((byte) (this.isComplete() ? 1 : 0));
        dest.writeTypedList(itemList);
        dest.writeByte((byte) (this.isLocked() ? 1 : 0));
        dest.writeString(this.getPassword());
    }

    public static final Creator<ShoppingListItem> CREATOR = new Creator<ShoppingListItem>() {
        @Override
        public ShoppingListItem createFromParcel(Parcel in) {
            return new ShoppingListItem(in);
        }

        @Override
        public ShoppingListItem[] newArray(int size) {
            return new ShoppingListItem[size];
        }
    };
}

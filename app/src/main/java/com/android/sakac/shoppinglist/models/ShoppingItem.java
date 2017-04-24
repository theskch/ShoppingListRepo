package com.android.sakac.shoppinglist.models;

import android.os.Parcel;
import android.os.Parcelable;

public class ShoppingItem extends AbstractItem implements Parcelable{

    private int amount;

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public ShoppingItem(String name, int amount) {
        super(name);
        this.amount = amount;
    }

    protected ShoppingItem(Parcel in) {
        super(in.readString());
        this.setComplete(in.readByte() != 0);
        amount = in.readInt();
    }

    @Override
    public int describeContents() {
        return this.hashCode();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.getName());
        dest.writeByte((byte) (this.isComplete() ? 1 : 0));
        dest.writeInt(this.getAmount());
    }

    public static final Creator<ShoppingItem> CREATOR = new Creator<ShoppingItem>() {
        @Override
        public ShoppingItem createFromParcel(Parcel in) {
            return new ShoppingItem(in);
        }

        @Override
        public ShoppingItem[] newArray(int size) {
            return new ShoppingItem[size];
        }
    };
}

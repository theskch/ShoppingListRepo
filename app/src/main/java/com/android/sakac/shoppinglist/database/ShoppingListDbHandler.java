package com.android.sakac.shoppinglist.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.android.sakac.shoppinglist.models.ShoppingItem;
import com.android.sakac.shoppinglist.models.ShoppingListItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ShoppingListDbHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "shoppingListDatabase";
    private static final String TABLE_NAME = "shoppingListsTable";

    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_COMPLETED = "completed";
    private static final String COLUMN_ITEMS = "items";
    private static final String COLUMN_LOCKED = "locked";
    private static final String COLUMN_PASSWORD = "password";

    public ShoppingListDbHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_SHOPPING_LIST_TABLE = "CREATE TABLE " + TABLE_NAME + " ("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_NAME + " TEXT,"
                + COLUMN_COMPLETED + " TEXT," + COLUMN_LOCKED + " TEXT,"
                + COLUMN_PASSWORD + " TEXT," + COLUMN_ITEMS + " TEXT)";
        db.execSQL(CREATE_SHOPPING_LIST_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void addShoppingList(ShoppingListItem item){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, item.getName());
        values.put(COLUMN_COMPLETED, String.valueOf(item.isComplete()));
        values.put(COLUMN_LOCKED, String.valueOf(item.isLocked()));
        values.put(COLUMN_PASSWORD, item.getPassword());

        try {
            JSONArray array = new JSONArray();
            for(int i=0; i<item.getItemList().size(); i++){
                ShoppingItem shoppingItem = item.getItemList().get(i);
                JSONObject jo = new JSONObject();
                jo.put("name", shoppingItem.getName());
                jo.put("amount", shoppingItem.getAmount());
                jo.put("isComplete", shoppingItem.isComplete());
                array.put(jo);
            }
            JSONObject mainObj = new JSONObject();
            mainObj.put("shoppingItems", array);
            values.put(COLUMN_ITEMS, mainObj.toString());
        }
        catch (JSONException e) {
            e.printStackTrace();
        }

        db.insert(TABLE_NAME ,null, values);
        db.close();
    }

    public ArrayList<ShoppingListItem> getAllShoppingLists() {
        ArrayList<ShoppingListItem> items = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        while(cursor.moveToNext()){
            long id = Long.parseLong(cursor.getString(0));
            String name = cursor.getString(1);
            ShoppingListItem item = new ShoppingListItem(name, id);
            item.setComplete(Boolean.parseBoolean(cursor.getString(2)));
            item.setLocked(Boolean.parseBoolean(cursor.getString(3)));
            item.setPassword(cursor.getString(4));

            try {
                JSONObject mainObj = new JSONObject(cursor.getString(5));
                JSONArray array = mainObj.getJSONArray("shoppingItems");
                for(int i=0; i<array.length(); i++){
                    JSONObject obj = array.getJSONObject(i);
                    ShoppingItem shoppingItem = new ShoppingItem(obj.getString("name"), obj.getInt("amount"));
                    shoppingItem.setComplete(obj.getBoolean("isComplete"));
                    item.getItemList().add(shoppingItem);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            items.add(item);
        }

        cursor.close();
        return items;
    }

    public long getShoppingListCount(){
        String countQuery = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        long count = cursor.getCount();
        cursor.close();
        return count;
    }

    public void updateShoppingList(ShoppingListItem item){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, item.getName());
        values.put(COLUMN_COMPLETED, String.valueOf(item.isComplete()));
        values.put(COLUMN_LOCKED, String.valueOf(item.isLocked()));
        values.put(COLUMN_PASSWORD, item.getPassword());

        try {
            JSONArray array = new JSONArray();
            for(int i=0; i<item.getItemList().size(); i++){
                ShoppingItem shoppingItem = item.getItemList().get(i);
                JSONObject jo = new JSONObject();
                jo.put("name", shoppingItem.getName());
                jo.put("amount", shoppingItem.getAmount());
                jo.put("isComplete", shoppingItem.isComplete());
                array.put(jo);
            }
            JSONObject mainObj = new JSONObject();
            mainObj.put("shoppingItems", array);
            values.put(COLUMN_ITEMS, mainObj.toString());
        }
        catch (JSONException e) {
            e.printStackTrace();
        }

        db.update(TABLE_NAME, values, COLUMN_ID + " = ?", new String[] {String.valueOf(item.getId())});
        db.close();
    }

    public void deleteShoppingList(ShoppingListItem item){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, COLUMN_ID + " =?", new String[] {String.valueOf(item.getId())});
        db.close();;
    }
}

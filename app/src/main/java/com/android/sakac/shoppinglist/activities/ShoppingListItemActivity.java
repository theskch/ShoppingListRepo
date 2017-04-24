package com.android.sakac.shoppinglist.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.android.sakac.shoppinglist.R;
import com.android.sakac.shoppinglist.adapters.ShoppingItemArrayAdapter;
import com.android.sakac.shoppinglist.database.ShoppingListDbHandler;
import com.android.sakac.shoppinglist.models.ShoppingItem;
import com.android.sakac.shoppinglist.models.ShoppingListItem;

import java.util.ArrayList;

public class ShoppingListItemActivity extends AppCompatActivity {

    public static final String EXISTING_ITEM = "com.android.sakac.shoppinglist.EXISTING_ITEM";

    private ShoppingListItem item;
    private ShoppingItemArrayAdapter adapter;
    private ShoppingListDbHandler dbHandler;
    private ArrayList<ShoppingItem> shoppingItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list_item);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createShoppingListItem();
            }
        });

        final Button saveButton = (Button) findViewById(R.id.saveShoppingListButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveNewShoppingListItem();
            }
        });

        final Button cancelButton = (Button) findViewById(R.id.cancelShoppingListButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelNewShoppingListItem();
            }
        });

        final CheckBox checkBox = (CheckBox) findViewById(R.id.lockCheckBox);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                ((TextView) findViewById(R.id.passwordText)).setEnabled(isChecked);
            }
        });

        dbHandler = new ShoppingListDbHandler(this);
        Intent intent = getIntent();
        if(intent != null) {
            item = intent.getParcelableExtra(EXISTING_ITEM);
            if(item != null) {
                ((TextView) findViewById(R.id.shoppingListName)).setText(item.getName());
                ((TextView) findViewById(R.id.passwordText)).setText(item.getPassword());
                checkBox.setChecked(item.isLocked());
                shoppingItems = item.getItemList();
            }else{
                shoppingItems = new ArrayList<>();
            }
        }

        final ListView listView = (ListView) findViewById(R.id.shoppingListListView);
        adapter = new ShoppingItemArrayAdapter(this, R.layout.shopping_item_row, shoppingItems);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                editShoppingListItem(shoppingItems.get(position));
            }
        });
    }

    private void saveNewShoppingListItem() {
        TextView name = (TextView) findViewById(R.id.shoppingListName);
        Intent mainActivityIntent = new Intent(this, MainActivity.class);
        if(item == null){
            item = new ShoppingListItem(name.getText().toString(), 0);
            item.setItemList(shoppingItems);
            item.setComplete(checkIfShoppingListIsComplete());
            item.setLocked(((CheckBox) findViewById(R.id.lockCheckBox)).isChecked());
            item.setPassword(((TextView) findViewById(R.id.passwordText)).getText().toString());
            dbHandler.addShoppingList(item);
        }else{
            item.setName(name.getText().toString());
            item.setComplete(checkIfShoppingListIsComplete());
            item.setLocked(((CheckBox) findViewById(R.id.lockCheckBox)).isChecked());
            item.setPassword(((TextView) findViewById(R.id.passwordText)).getText().toString());
            dbHandler.updateShoppingList(item);
        }

        startActivity(mainActivityIntent);
    }

    private void cancelNewShoppingListItem(){
        Intent mainActivityIntent = new Intent(this, MainActivity.class);
        startActivity(mainActivityIntent);
    }

    private void createShoppingListItem(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.add_shopping_item_dialog, null))
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText name = (EditText) ((AlertDialog) dialog).findViewById(R.id.shoppingItemName);
                        EditText amount = (EditText) ((AlertDialog) dialog).findViewById(R.id.shoppingItemAmount);
                        CheckBox checkBox = (CheckBox)((AlertDialog) dialog).findViewById(R.id.checkBoxDone);
                        int amountInt = 0;
                        try {
                            amountInt = Integer.parseInt(amount.getText().toString());
                        }catch(NumberFormatException ex){
                            System.out.println("invalid amount data format");
                        }
                        ShoppingItem newItem = new ShoppingItem(name.getText().toString(), amountInt);
                        newItem.setComplete(checkBox.isChecked());
                        adapter.add(newItem);
                    }
                })
                .setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //empty
                    }
                });
        builder.setTitle("New shopping item");
        builder.create().show();
    }

    private void editShoppingListItem(final ShoppingItem shoppingItem){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.add_shopping_item_dialog, null))
                .setPositiveButton("Apply", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText name = (EditText) ((AlertDialog) dialog).findViewById(R.id.shoppingItemName);
                        EditText amount = (EditText) ((AlertDialog) dialog).findViewById(R.id.shoppingItemAmount);
                        CheckBox checkBox = (CheckBox)((AlertDialog) dialog).findViewById(R.id.checkBoxDone);
                        int amountInt = 0;
                        try {
                            amountInt = Integer.parseInt(amount.getText().toString());
                        }catch(NumberFormatException ex){
                            System.out.println("invalid amount data format");
                        }
                        shoppingItem.setName(name.getText().toString());
                        shoppingItem.setAmount(amountInt);
                        shoppingItem.setComplete(checkBox.isChecked());
                        adapter.notifyDataSetChanged();
                    }
                })
                .setNegativeButton("Remove", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        adapter.remove(shoppingItem);
                    }
                })
                .setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //empty
                    }
                });

        AlertDialog dialog = builder.show();
        ((EditText)((AlertDialog) dialog).findViewById(R.id.shoppingItemName)).setText(shoppingItem.getName());
        ((EditText)((AlertDialog) dialog).findViewById(R.id.shoppingItemAmount)).setText(Integer.toString(shoppingItem.getAmount()));
        ((CheckBox)((AlertDialog) dialog).findViewById(R.id.checkBoxDone)).setChecked(shoppingItem.isComplete());
    }

    private boolean checkIfShoppingListIsComplete(){
        ArrayList<ShoppingItem> items = item.getItemList();
        for(int i=0; i<items.size(); i++){
            if(!items.get(i).isComplete()){
                return false;
            }
        }

        return true;
    }
}

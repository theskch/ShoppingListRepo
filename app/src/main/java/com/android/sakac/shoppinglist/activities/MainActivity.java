package com.android.sakac.shoppinglist.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.sakac.shoppinglist.R;
import com.android.sakac.shoppinglist.adapters.ShoppingListArrayAdapter;
import com.android.sakac.shoppinglist.database.ShoppingListDbHandler;
import com.android.sakac.shoppinglist.models.ShoppingListItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class MainActivity extends AppCompatActivity {

    private ListView listView;
    private ArrayList<ShoppingListItem> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.fab:
                        addItem();
                        break;
                }
            }
        });

        data = new ShoppingListDbHandler(this).getAllShoppingLists();
        final ShoppingListArrayAdapter adapter = new ShoppingListArrayAdapter(this, R.layout.shopping_list_row, data);

        Collections.sort(data, new Comparator<ShoppingListItem>() {
            @Override
            public int compare(ShoppingListItem o1, ShoppingListItem o2) {
                boolean b1 = o1.isComplete();
                boolean b2 = o2.isComplete();
                if( b1 && ! b2 ) {
                    return +1;
                }
                if( ! b1 && b2 ) {
                    return -1;
                }
                return 0;
            }
        });

        listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                editItem(position);
            }
        });
    }

    private void addItem() {
        Intent addNewItemIntent = new Intent(this, ShoppingListItemActivity.class);
        startActivity(addNewItemIntent);
    }

    private void editItem(final int itemPosition){
        final ShoppingListItem item = data.get(itemPosition);
        if(item.isLocked()){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Password required");
            LayoutInflater inflater = this.getLayoutInflater();
            builder.setView(inflater.inflate(R.layout.password_dialog, null))
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent editItemIntent = new Intent(MainActivity.this, ShoppingListItemActivity.class);
                            editItemIntent.putExtra(ShoppingListItemActivity.EXISTING_ITEM, item);
                            startActivity(editItemIntent);
                        }
                    })
                    .setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //empty
                        }
                    });

            final AlertDialog dialog = builder.create();
            dialog.show();
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
            ((TextView)dialog.findViewById(R.id.shoppingListPassworn)).addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    //empty
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    //empty
                }

                @Override
                public void afterTextChanged(Editable s) {
                    if(TextUtils.equals(s, item.getPassword())){
                        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
                    }else{
                        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                    }
                }
            });
        }else{
            Intent editItemIntent = new Intent(this, ShoppingListItemActivity.class);
            editItemIntent.putExtra(ShoppingListItemActivity.EXISTING_ITEM, item);
            startActivity(editItemIntent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

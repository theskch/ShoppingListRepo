package com.android.sakac.shoppinglist.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.sakac.shoppinglist.R;
import com.android.sakac.shoppinglist.models.ShoppingItem;

import java.util.ArrayList;

public class ShoppingItemArrayAdapter extends ArrayAdapter<ShoppingItem> {

    private Context context;
    private int layoutResourceId;
    private ArrayList<ShoppingItem> data = null;

    public ShoppingItemArrayAdapter(Context context, int resource, ArrayList<ShoppingItem> objects) {
        super(context, resource, objects);

        this.context = context;
        this.layoutResourceId = resource;
        this.data = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        ViewHolder viewHolder = null;

        if(convertView == null){
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            convertView = inflater.inflate(layoutResourceId, null);
            viewHolder = new ViewHolder();
            viewHolder.setNameTextView((TextView) convertView.findViewById(R.id.shoppingItemName));
            viewHolder.setAmountTextView((TextView) convertView.findViewById(R.id.shoppingItemAmount));
            viewHolder.setCompletedImageView((ImageView) convertView.findViewById(R.id.isCompletedImage));
            convertView.setTag(viewHolder);
        }
        else{
            viewHolder = (ViewHolder)convertView.getTag();
        }

        ShoppingItem item = data.get(position);
        if(item != null){
            viewHolder.getNameTextView().setText(item.getName());
            viewHolder.getAmountTextView().setText(Integer.toString(item.getAmount()));
            viewHolder.getCompletedImageView().setVisibility(item.isComplete() ? View.VISIBLE : View.INVISIBLE);
        }

        return convertView;
    }

    private class ViewHolder{

        private TextView nameTextView;
        private TextView amountTextView;
        private ImageView completedImageView;

        public TextView getAmountTextView() {
            return amountTextView;
        }

        public void setAmountTextView(TextView amountTextView) {
            this.amountTextView = amountTextView;
        }

        public TextView getNameTextView() {

            return nameTextView;
        }

        public void setNameTextView(TextView textView) {

            this.nameTextView = textView;
        }

        public ImageView getCompletedImageView() {
            return completedImageView;
        }

        public void setCompletedImageView(ImageView completedImageView) {
            this.completedImageView = completedImageView;
        }

        private ViewHolder() {
            //empty
        }
    }
}

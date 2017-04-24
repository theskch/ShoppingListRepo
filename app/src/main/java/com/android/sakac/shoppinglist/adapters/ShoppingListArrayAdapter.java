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
import com.android.sakac.shoppinglist.models.AbstractItem;
import com.android.sakac.shoppinglist.models.ShoppingListItem;

import java.util.ArrayList;

public class ShoppingListArrayAdapter extends ArrayAdapter<ShoppingListItem> {

    private Context context;
    private int layoutResourceId;
    private ArrayList<ShoppingListItem> data = null;

    public ShoppingListArrayAdapter(Context context, int resource, ArrayList<ShoppingListItem> objects) {
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
            viewHolder.setTextView((TextView) convertView.findViewById(R.id.textViewItem));
            viewHolder.setCompletedImageView((ImageView) convertView.findViewById(R.id.isCompletedImage));
            viewHolder.setLockedImageView((ImageView) convertView.findViewById(R.id.isLockedImage));
            convertView.setTag(viewHolder);
        }
        else{
            viewHolder = (ViewHolder)convertView.getTag();
        }

        ShoppingListItem item = data.get(position);
        if(item != null){
            viewHolder.getTextView().setText(item.getName());
            viewHolder.getCompletedImageView().setVisibility(item.isComplete() ? View.VISIBLE : View.INVISIBLE);
            viewHolder.getLockedImageView().setVisibility(item.isLocked() ? View.VISIBLE : View.INVISIBLE);
        }

        return convertView;
    }

    private class ViewHolder{

        private TextView textView;
        private ImageView completedImageView;
        private ImageView lockedImageView;

        public TextView getTextView() {
            return textView;
        }

        public void setTextView(TextView textView) {
            this.textView = textView;
        }

        public ImageView getCompletedImageView() {
            return completedImageView;
        }

        public void setCompletedImageView(ImageView completedImageView) {
            this.completedImageView = completedImageView;
        }

        public ImageView getLockedImageView() {
            return lockedImageView;
        }

        public void setLockedImageView(ImageView lockedImageView) {
            this.lockedImageView = lockedImageView;
        }


        private ViewHolder() {
            //empty
        }


    }
}



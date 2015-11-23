package com.travel_guide.ketan.travel_guide;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * Created by ketan on 10/16/15.
 */
public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {
    private List<String> mDataset;
    private List<Bitmap> mImageset;
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // each data item is just a string in this case
        public TextView mTextView;
        public ImageView mImageView;
        private Context context = null;

        public ViewHolder(View v) {
            super(v);
            mTextView = (TextView) v.findViewById(R.id.location);
            mImageView = (ImageView) v.findViewById(R.id.image);
            context = v.getContext();
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            final Intent intent;
            intent =  new Intent(context, SpotActivity.class);
            Bundle bundle = new Bundle();
            String name = mTextView.getText().toString();
            bundle.putString("Name", name);
            intent.putExtras(bundle);
        /*  mImageView.buildDrawingCache();
            Bitmap photo = mImageView.getDrawingCache();
            Bundle extras = new Bundle();
            extras.putParcelable("Photo", photo);
            intent.putExtras(extras); */
            context.startActivity(intent);
        }
    }
    public ItemAdapter(List<String> myDataset, List<Bitmap> mImageView) {
        mDataset = myDataset;
        mImageset = mImageView;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.mylist, parent, false);
        ViewHolder vh  = new ViewHolder(v);
        return vh;
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mTextView.setText(mDataset.get(position));
        holder.mImageView.setImageBitmap(mImageset.get(position));
    }
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}

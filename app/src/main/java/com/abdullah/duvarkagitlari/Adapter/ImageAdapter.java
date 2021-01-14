package com.abdullah.duvarkagitlari.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.abdullah.duvarkagitlari.Activity.ViewActivity;
import com.abdullah.duvarkagitlari.Model.Model;
import com.abdullah.duvarkagitlari.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {

    Context mContext;
    List<Model> mImages;

    public ImageAdapter(Context context, List<Model> images) {

        this.mContext = context;
        this.mImages = images;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(mContext).inflate(R.layout.custom_layout, parent, false);

        return new ImageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {

        Model image = mImages.get(position);

        Picasso.get().load(image.getUrl()).into(holder.imageView);
    }

    @Override
    public int getItemCount() {

        return mImages.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView imageView;

        public ImageViewHolder(View itemView) {

            super(itemView);

            itemView.setOnClickListener(this);

            imageView = itemView.findViewById(R.id.imageView);
        }

        @Override
        public void onClick(View v) {

            Intent intent = new Intent(mContext, ViewActivity.class);
            intent.putExtra("images", mImages.get(getAdapterPosition()).getUrl());
            mContext.startActivity(intent);
        }
    }
}
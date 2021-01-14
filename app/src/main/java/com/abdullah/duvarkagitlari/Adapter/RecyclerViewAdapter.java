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
import com.abdullah.duvarkagitlari.Model.CustomItems;
import com.abdullah.duvarkagitlari.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ImageViewHolder> {

    List<CustomItems> itemsList;
    Context context;

    public RecyclerViewAdapter(List<CustomItems> itemsList, Context context) {

        this.itemsList = itemsList;
        this.context = context;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(context);

        View view = layoutInflater.inflate(R.layout.custom_layout, parent, false);

        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {

        Picasso.get().load(itemsList.get(position).getUrl()).into(holder.imageView);
    }

    @Override
    public int getItemCount() {

        return itemsList.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView imageView;

        public ImageViewHolder(@NonNull View itemView) {

            super(itemView);
            itemView.setOnClickListener(this);
            imageView = itemView.findViewById(R.id.imageView);
        }

        @Override
        public void onClick(View v) {

            Intent intent = new Intent(context, ViewActivity.class);
            intent.putExtra("images", itemsList.get(getAdapterPosition()).getUrl());
            context.startActivity(intent);
        }
    }
}
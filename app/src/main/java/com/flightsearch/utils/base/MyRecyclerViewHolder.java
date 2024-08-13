package com.flightsearch.utils.base;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyRecyclerViewHolder<T> extends RecyclerView.ViewHolder {

    public MyRecyclerViewHolder(@NonNull View itemView) {
        super(itemView);
    }

    public void bindTo(T item, int position) {

    }

}

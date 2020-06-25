package com.robin.theandroidcrew.movies.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.robin.theandroidcrew.movies.R;
import com.robin.theandroidcrew.movies.model.Trailer;

import java.util.List;

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.ViewHolder> {
    private List<Trailer> items;
    OnClickHandler clickListener;

    public TrailerAdapter(List<Trailer> items, OnClickHandler clickHandler) {
        this.items = items;
        this.clickListener = clickHandler;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(
            ViewGroup parent,
            int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.trailer_item_list, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Trailer item = items.get(position);
        holder.bind(item, position);
    }

    @Override
    public int getItemCount() {
        if (items == null) {
            return 0;
        }
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.trailer_name);

        }

        public void bind(Trailer item,  int position) {
            textView.setText(item.getName());
            itemView.setOnClickListener(v -> {

                        clickListener.onClickTrailer(position);

                    }
            );
        }
    }

    public interface OnClickHandler {
        void onClickTrailer(int position);
    }
}
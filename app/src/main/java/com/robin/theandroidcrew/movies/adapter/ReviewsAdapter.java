package com.robin.theandroidcrew.movies.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.robin.theandroidcrew.movies.R;
import com.robin.theandroidcrew.movies.model.Review;

import java.util.List;

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ViewHolder> {
    private List<Review> items;
    OnClickHandler clickListener;


    public ReviewsAdapter(List<Review> items, OnClickHandler cilckHandler) {
        this.items = items;
        this.clickListener = cilckHandler;
    }

    @NonNull
    @Override
    public ReviewsAdapter.ViewHolder onCreateViewHolder(
            ViewGroup parent,
            int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.reviews_item_list, parent, false);

        return new ReviewsAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ReviewsAdapter.ViewHolder holder, int position) {
        Review item = items.get(position);
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
        TextView author;
        TextView content;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            author = itemView.findViewById(R.id.tv_author);
            content = itemView.findViewById(R.id.tv_review);
        }

        public void bind(Review item, int position) {
            author.setText(item.getAuthor());
            content.setText(item.getContent());
            itemView.setOnClickListener(v -> {
                clickListener.onClickReview(position);
                    }
            );

        }
    }
    public interface OnClickHandler {
        void onClickReview(int position);
    }
}
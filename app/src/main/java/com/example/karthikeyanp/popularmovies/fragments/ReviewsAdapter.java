package com.example.karthikeyanp.popularmovies.fragments;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.karthikeyanp.popularmovies.R;
import com.example.karthikeyanp.popularmovies.Reviews;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by karthikeyanp on 5/18/2017.
 */

class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ReviewViewHolder> {
    private List<Reviews> reviewsList;

    public ReviewsAdapter(List<Reviews> reviewsList) {
        this.reviewsList = reviewsList;
    }

    @Override
    public ReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_card_view, parent, false);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReviewViewHolder holder, int position) {
        if (holder != null) {
            holder.author.setText(reviewsList.get(position).author);
            holder.content.setText(reviewsList.get(position).content);
        }
    }

    @Override
    public int getItemCount() {
        if (reviewsList != null) {
            return reviewsList.size();
        } else {
            return 0;
        }
    }

    public void updateReviews(List<Reviews> reviewsList) {
        this.reviewsList = reviewsList;
        notifyDataSetChanged();
    }

    public class ReviewViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.author)
        TextView author;
        @BindView(R.id.content)
        TextView content;

        public ReviewViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }


}

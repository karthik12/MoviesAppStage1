package com.example.karthikeyanp.popularmovies.fragments;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.karthikeyanp.popularmovies.R;
import com.example.karthikeyanp.popularmovies.Videos;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by karthikeyanp on 5/18/2017.
 */

class VideosAdapter extends RecyclerView.Adapter<VideosAdapter.ReviewViewHolder> {
    private OnVideoViewItemClickListener clickListener;
    private List<Videos> videosList;
    private Context context;

    public VideosAdapter(Context context, List<Videos> videosList, OnVideoViewItemClickListener clickListener) {
        this.videosList = videosList;
        this.context = context;
        this.clickListener = clickListener;
    }

    @Override
    public ReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.video_card_view, parent, false);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReviewViewHolder holder, int position) {
        if (holder != null) {
            String url = getPosterUrl(position);
            Picasso.with(context)
                    .load(url)
                    .error(R.drawable.ic_error)
                    .into(holder.imageView);
            holder.name.setText(videosList.get(position).name);
        }
    }

    private String getPosterUrl(int position) {
        return "http://img.youtube.com/vi/" + videosList.get(position).key + "/0.jpg";
    }

    @Override
    public int getItemCount() {
        if (videosList != null) {
            return videosList.size();
        } else {
            return 0;
        }
    }

    public void updateVideos(List<Videos> reviewsList) {
        this.videosList = reviewsList;
        notifyDataSetChanged();
    }

    public class ReviewViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.video_image)
        ImageView imageView;
        @BindView(R.id.video_name)
        TextView name;

        public ReviewViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            imageView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            clickListener.videoClicked(getLayoutPosition());
        }
    }


    public interface OnVideoViewItemClickListener {
        void videoClicked(int position);
    }
}

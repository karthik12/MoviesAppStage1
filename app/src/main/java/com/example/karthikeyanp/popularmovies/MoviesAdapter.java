package com.example.karthikeyanp.popularmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by karthikeyanp on 2/12/2017.
 */
public class MoviesAdapter extends android.support.v7.widget.RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private OnRecycleViewItemClickListener recycleItemClickListener;
    private Context context;
    private List<Movie> movieList;


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_view, parent, false);
        return new ImageViewHolder(view);
    }

    public MoviesAdapter(Context context, OnRecycleViewItemClickListener clickListener, List<Movie> movieList) {
        this.context = context;
        recycleItemClickListener = clickListener;
        this.movieList = movieList;
    }



    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder != null && holder instanceof ImageViewHolder) {
            ImageViewHolder imageViewHolder = (ImageViewHolder) holder;
            String url = getPosterUrl(position);
            Picasso.with(context)
                    .load(url)
                    .error(R.drawable.ic_error)
                    .placeholder(R.drawable.progress_animation)
                    .into(imageViewHolder.imageView);
        }

    }

    private String getPosterUrl(int position) {
        Movie movie = movieList.get(position);
        String posterPath = movie.posterPath;
        return MovieUtils.IMAGE_URL + posterPath;
    }

    @Override
    public int getItemCount() {
        if (movieList != null) {
            return movieList.size();
        } else {
            return 0;
        }
    }

    public void updateMovies(List<Movie> movieList) {
        this.movieList = movieList;
        notifyDataSetChanged();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imageView;

        public ImageViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.mv_image_view);
            imageView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            recycleItemClickListener.recyclerViewListClicked(getLayoutPosition());
        }
    }

    public interface OnRecycleViewItemClickListener {
        void recyclerViewListClicked(int position);
    }
}

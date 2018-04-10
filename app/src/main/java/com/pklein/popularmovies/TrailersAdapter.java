package com.pklein.popularmovies;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pklein.popularmovies.data.Review;
import com.pklein.popularmovies.data.Trailer;

import java.util.List;

public class TrailersAdapter extends RecyclerView.Adapter<TrailersAdapter.TrailersAdapterViewHolder> {

    private List<Trailer> mTrailerData;

    public TrailersAdapter() {
    }

    public class TrailersAdapterViewHolder extends RecyclerView.ViewHolder {

        public final TextView tvName;
        public final TextView tvURL;

        public TrailersAdapterViewHolder(View view) {
            super(view);
            tvName = view.findViewById(R.id.tv_name);
            tvURL = view.findViewById(R.id.tv_URL);
        }
    }

    @Override
    public TrailersAdapter.TrailersAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.trailer;
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutIdForListItem, viewGroup, false);
        return new TrailersAdapter.TrailersAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder( TrailersAdapter.TrailersAdapterViewHolder trailersAdapterViewHolder, int position) {

        final Context context = trailersAdapterViewHolder.itemView.getContext();
        final Trailer trailerSelected = mTrailerData.get(position);

        trailersAdapterViewHolder.tvName.setText(trailerSelected.getmName());
        trailersAdapterViewHolder.tvURL.setText("https://www.youtube.com/watch?v="+trailerSelected.getmKey());

    }

    @Override
    public int getItemCount() {
        if (null == mTrailerData) return 0;
        return mTrailerData.size();
    }

    public void setReviewData(List<Trailer> trailerData) {
        mTrailerData = trailerData;
        notifyDataSetChanged();
    }
}
package com.pklein.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pklein.popularmovies.data.Trailer;
import com.pklein.popularmovies.tools.NetworkUtils;

import java.util.List;

public class TrailersAdapter extends RecyclerView.Adapter<TrailersAdapter.TrailersAdapterViewHolder> {

    private List<Trailer> mTrailerData;

    public TrailersAdapter() {
    }

    public class TrailersAdapterViewHolder extends RecyclerView.ViewHolder {

        public final TextView tvName;
        public final TextView tvLanguage;
        public final TextView tvClip;

        public TrailersAdapterViewHolder(View view) {
            super(view);
            tvName = view.findViewById(R.id.tv_name);
            tvLanguage = view.findViewById(R.id.tv_language);
            tvClip = view.findViewById(R.id.tv_clip);
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
        trailersAdapterViewHolder.tvLanguage.setText(context.getString(R.string.language_label)+" "+trailerSelected.getmIso_639_1());
        trailersAdapterViewHolder.tvClip.setText(trailerSelected.getmType()+" "+trailerSelected.getmSite()+" ("+trailerSelected.getmSize()+"p)");

        trailersAdapterViewHolder.itemView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if(trailerSelected != null)
                {
                    Uri webpage = NetworkUtils.getYoutubeUri(trailerSelected.getmKey());
                    Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
                    if (intent.resolveActivity(context.getPackageManager()) != null) {
                        context.startActivity(intent);
                    }
                }
            }
        });

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
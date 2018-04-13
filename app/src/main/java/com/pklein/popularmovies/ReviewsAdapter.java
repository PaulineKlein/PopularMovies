package com.pklein.popularmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pklein.popularmovies.data.Review;

import java.util.List;

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ReviewsAdapterViewHolder> {

    private List<Review> mReviewData;

    public ReviewsAdapter() {
    }

    public class ReviewsAdapterViewHolder extends RecyclerView.ViewHolder {

        public final TextView tvAuthor;
        public final TextView tvContent;

        public ReviewsAdapterViewHolder(View view) {
            super(view);
            tvAuthor = view.findViewById(R.id.tv_author);
            tvContent = view.findViewById(R.id.tv_content);
        }
    }

    @Override
    public ReviewsAdapter.ReviewsAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.review;
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutIdForListItem, viewGroup, false);
        return new ReviewsAdapter.ReviewsAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder( ReviewsAdapter.ReviewsAdapterViewHolder reviewsAdapterViewHolder, int position) {

        final Review ReviewSelected = mReviewData.get(position);

        reviewsAdapterViewHolder.tvAuthor.setText(ReviewSelected.getmAuthor());
        reviewsAdapterViewHolder.tvContent.setText(ReviewSelected.getmContent());
    }

    @Override
    public int getItemCount() {
        if (null == mReviewData) return 0;
        return mReviewData.size();
    }

    public void setReviewData(List<Review> reviewData) {
        mReviewData = reviewData;
        notifyDataSetChanged();
    }
}

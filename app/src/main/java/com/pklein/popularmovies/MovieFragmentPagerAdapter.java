package com.pklein.popularmovies;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

// with the Help of : https://guides.codepath.com/android/google-play-style-tabs-using-tablayout
public class MovieFragmentPagerAdapter extends FragmentPagerAdapter {
    final int PAGE_COUNT = 3;
    private String tabTitles[] = new String[] { "Info", "Reviews", "Trailers" };
    private Context context;

    public MovieFragmentPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return MovieInformation.newInstance();
            case 1:
                return MovieReviews.newInstance();
            case 2:
                return MovieTrailers.newInstance();
            default:
                return MovieInformation.newInstance();
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position :
        return tabTitles[position];
    }
}

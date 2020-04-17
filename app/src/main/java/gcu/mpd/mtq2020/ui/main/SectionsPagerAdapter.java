package gcu.mpd.mtq2020.ui.main;

import android.app.Application;
import android.content.Context;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import gcu.mpd.mtq2020.EventType;
import gcu.mpd.mtq2020.FetchRSSFeed;
import gcu.mpd.mtq2020.R;
import gcu.mpd.mtq2020.TrafficURL;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.tab_text_1, R.string.tab_text_2};
    private final Context mContext;

    public SectionsPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
//        FetchRSSFeed fetchRSSFeed = null;
//        switch (position) {
//            case 0:
//                fetchRSSFeed = new FetchRSSFeed(mContext, TrafficURL.currentIncidents, EventType.CURRENT_INCIDENT);
//            case 1:
//                fetchRSSFeed = new FetchRSSFeed(mContext, TrafficURL.ongoingRoadworks, EventType.ONGOING_ROADWORK);
//            case 2:
//                fetchRSSFeed = new FetchRSSFeed(mContext, TrafficURL.plannedRoadworks, EventType.PLANNED_ROADWORK);
//            fetchRSSFeed.execute();
//        }

        return PlaceholderFragment.newInstance(position + 1);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }

    @Override
    public int getCount() {
        // Show 2 total pages.
        return 2;
    }
}
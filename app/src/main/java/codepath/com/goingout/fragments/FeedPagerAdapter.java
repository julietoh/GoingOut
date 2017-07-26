package codepath.com.goingout.fragments;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by acamara on 7/22/17.
 */

public class FeedPagerAdapter extends FragmentPagerAdapter {
    public static String tabTitles[] = new String [] {"Trending", "Filtered", "Crew"};
    private Context context;
    public ConcurrentHashMap<Integer, EventsListFragment> mPageReferenceMap = new ConcurrentHashMap();


    public FeedPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0)
        {
            FilteredFragment r = new FilteredFragment();
            mPageReferenceMap.put(position, r);
            return r;

        } else if (position == 1) {
            TrendingFragment r = new TrendingFragment();
            mPageReferenceMap.put(position, r);
            return r;
        } else {
            return null;
        }

    }

    @Override
    public int getCount() {
        return tabTitles.length;
    }
}

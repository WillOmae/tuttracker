package dev.wilburomae.tuttracker.views.adapters;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import dev.wilburomae.tuttracker.R;
import dev.wilburomae.tuttracker.views.fragments.ArchiveFragment;
import dev.wilburomae.tuttracker.views.fragments.InboxFragment;
import dev.wilburomae.tuttracker.views.fragments.OutboxFragment;
import dev.wilburomae.tuttracker.views.fragments.StatsFragment;

public class SectionsPagerAdapter extends FragmentPagerAdapter {
    private Context mContext;
    private final int[] mTabTitles;
    private final Fragment[] mFragments;

    public SectionsPagerAdapter(Context context, FragmentManager fm) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        mContext = context;
        mTabTitles = new int[]{
                R.string.home_tab_inbox, R.string.home_tab_outbox, R.string.home_tab_archive, R.string.home_tab_stats
        };
        mFragments = new Fragment[]{
                new InboxFragment(),
                new OutboxFragment(),
                new ArchiveFragment(),
                new StatsFragment()
        };
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return mFragments[position];
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(mTabTitles[position]);
    }

    @Override
    public int getCount() {
        return mTabTitles.length;
    }
}

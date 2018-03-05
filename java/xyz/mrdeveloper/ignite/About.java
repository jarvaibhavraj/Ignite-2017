package xyz.mrdeveloper.ignite;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static xyz.mrdeveloper.ignite.MainActivity.dayTabSelected;
import static xyz.mrdeveloper.ignite.MainActivity.isDetailsOpened;

/**
 * Created by Vaibhav on 26-01-2017.
 */

public class About extends Fragment {
    public int mImageResIds;
    public String title;
    public String description;
    public String time;
    public String date;
    public String fee;
    public String prize;
    public String venue;
    public String day;
    public String category;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    public static About newInstance() {
        return new About();
    }

    public About() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.schedule, container, false);

        viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        tabLayout = (TabLayout) view.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        setupViewPager(viewPager);
        setTab();

        setupTabIcons();
        return view;
    }

    public void setupTabIcons() {
        //Typeface typeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/Calibri.ttf");
        TextView tabTwo = (TextView) LayoutInflater.from(getActivity()).inflate(R.layout.custom_tab, null);
        tabTwo.setText("Mr. Developer");
        tabLayout.getTabAt(0).setCustomView(tabTwo);
        //changeTabsFont(tabLayout,typeface);
//        tabLayout.getTabAt(1).setText("Mr. Developer");\

        TextView tabOne = (TextView) LayoutInflater.from(getActivity()).inflate(R.layout.custom_tab, null);
        tabOne.setText("Ignite");
        tabLayout.getTabAt(1).setCustomView(tabOne);
        //changeTabsFont(tabLayout,typeface);
//        tabLayout.getTabAt(0).setText("Ignite");
    }

    private void changeTabsFont(TabLayout tabLayout, Typeface typeface) {
        ViewGroup vg = (ViewGroup) tabLayout.getChildAt(0);
        int tabsCount = vg.getChildCount();
        for (int j = 0; j < tabsCount; j++) {
            ViewGroup vgTab = (ViewGroup) vg.getChildAt(j);
            int tabChildsCount = vgTab.getChildCount();
            for (int i = 0; i < tabChildsCount; i++) {
                View tabViewChild = vgTab.getChildAt(i);
                if (tabViewChild instanceof TextView) {
                    ((TextView) tabViewChild).setTypeface(typeface);
                }
            }
        }
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.addFragment(new AboutMrDeveloper(), "Mr. Developer");
        adapter.addFragment(new AboutIgnite(), "Ignite");
        //adapter.addFragment(new Day2("Day2", "https://api.myjson.com/bins/6kjqz", "day2events", "https://api.myjson.com/bins/k75sb", "day2eventdetails"), "DAY 2");
        viewPager.setAdapter(adapter);
    }

    public void setTab() {
        viewPager.setCurrentItem(dayTabSelected);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}

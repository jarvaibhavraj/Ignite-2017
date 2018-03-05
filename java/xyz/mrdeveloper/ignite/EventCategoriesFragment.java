package xyz.mrdeveloper.ignite;

import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static xyz.mrdeveloper.ignite.MainActivity.eventCategoryTabSelected;
import static xyz.mrdeveloper.ignite.MainActivity.isDetailsOpened;

/**
 * Created by Vaibhav on 02-01-2017.
 */

public class EventCategoriesFragment extends Fragment {
    //
//    private TabLayout tabLayout;
//    private ViewPager viewPager;
    public View view;
    public ViewHolderForEventCategories holder;

    Typeface typeface;

    public static EventCategoriesFragment newInstance() {
        return new EventCategoriesFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (view == null) {
            view = inflater.inflate(R.layout.event_categories, container, false);

//            Log.i("debug", "YESSSSSSS");
            holder = new ViewHolderForEventCategories();

            holder.viewPager = (ViewPager) view.findViewById(R.id.event_categories_viewpager);
            holder.viewPager.setOffscreenPageLimit(5);
            holder.tabLayout = (TabLayout) view.findViewById(R.id.event_categories_tab);
            holder.tabLayout.setupWithViewPager(holder.viewPager);
            setupViewPager(holder.viewPager);

            holder.tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    eventCategoryTabSelected = holder.tabLayout.getSelectedTabPosition();
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {
                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {
                }
            });

            view.setTag(holder);
            setTab();
            setupTabIcons();
        } else {
            holder = (ViewHolderForEventCategories) view.getTag();
        }

        return view;
    }

    public void setupTabIcons() {
//        typeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/Quicksand-Regular.ttf");
        //typeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/Calibri.ttf");

        Drawable tempDrawable;

        TextView tabOne = (TextView) LayoutInflater.from(getActivity()).inflate(R.layout.custom_tab, null);
        tabOne.setText("Robotics");
//        tabOne.setTypeface(typeface);
       /* tempDrawable = getResources().getDrawable(R.drawable.robotics);
        tempDrawable.setBounds(0, 0, 100, 100);*/
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            tempDrawable.setTint(getResources().getColor(R.color.colorAccent));
        }*/
//        tabOne.setCompoundDrawables(tempDrawable, null, null, null);
        holder.tabLayout.getTabAt(0).setCustomView(tabOne);

        TextView tabTwo = (TextView) LayoutInflater.from(getActivity()).inflate(R.layout.custom_tab, null);
        tabTwo.setText("Coding");
//        tabTwo.setTypeface(typeface);
       /* tempDrawable = getResources().getDrawable(R.drawable.coding);
        tempDrawable.setBounds(0, 0, 100, 100);*/
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            tempDrawable.setTint(getResources().getColor(R.color.colorAccent));
        }*/
//        tabTwo.setCompoundDrawables(tempDrawable, null, null, null);
        holder.tabLayout.getTabAt(1).setCustomView(tabTwo);

        TextView tabThree = (TextView) LayoutInflater.from(getActivity()).inflate(R.layout.custom_tab, null);
        tabThree.setText("Gaming");
//        tabThree.setTypeface(typeface);
       /* tempDrawable = getResources().getDrawable(R.drawable.gaming);
        tempDrawable.setBounds(0, 0, 100, 100);*/
       /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            tempDrawable.setTint(getResources().getColor(R.color.colorAccent));
        }*/
//        tabThree.setCompoundDrawables(tempDrawable, null, null, null);
        holder.tabLayout.getTabAt(2).setCustomView(tabThree);

        TextView tabFour = (TextView) LayoutInflater.from(getActivity()).inflate(R.layout.custom_tab, null);
        tabFour.setText("Fun");
//        tabFour.setTypeface(typeface);
        /*tempDrawable = getResources().getDrawable(R.drawable.presentation);
        tempDrawable.setBounds(0, 0, 100, 100);*/
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            tempDrawable.setTint(getResources().getColor(R.color.colorAccent));
        }*/
//        tabFour.setCompoundDrawables(tempDrawable, null, null, null);
        holder.tabLayout.getTabAt(3).setCustomView(tabFour);

        TextView tabFive = (TextView) LayoutInflater.from(getActivity()).inflate(R.layout.custom_tab, null);
        tabFive.setText("Presentation");
//        tabFive.setTypeface(typeface);
        /*tempDrawable = getResources().getDrawable(R.drawable.quiz);
       /* tempDrawable.setBounds(0, 0, 100, 100);*//*
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            tempDrawable.setTint(getResources().getColor(R.color.colorAccent));
        }*/
//        tabFive.setCompoundDrawables(tempDrawable, null, null, null);
        holder.tabLayout.getTabAt(4).setCustomView(tabFive);

        TextView tabSix = (TextView) LayoutInflater.from(getActivity()).inflate(R.layout.custom_tab, null);
        tabSix.setText("Miscellaneous");
//        tabSix.setTypeface(typeface);
       /* tempDrawable = getResources().getDrawable(R.drawable.misc);
        tempDrawable.setBounds(0, 0, 100, 100);*/
       /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            tempDrawable.setTint(getResources().getColor(R.color.colorAccent));
        }*/
//        tabSix.setCompoundDrawables(tempDrawable, null, null, null);
        holder.tabLayout.getTabAt(5).setCustomView(tabSix);

        //changeTabsFont(holder.tabLayout, typeface);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());

        EventList eventList = new EventList();
        eventList.setEventList("1", 2);
        adapter.addFragment(eventList, "Robotics");
        eventList = new EventList();
        eventList.setEventList("2", 2);
        adapter.addFragment(eventList, "Coding");
        eventList = new EventList();
        eventList.setEventList("3", 2);
        adapter.addFragment(eventList, "Gaming");
        eventList = new EventList();
        eventList.setEventList("4", 2);
        adapter.addFragment(eventList, "Fun");
        eventList = new EventList();
        eventList.setEventList("5", 2);
        adapter.addFragment(eventList, "Presentation");
        eventList = new EventList();
        eventList.setEventList("6", 2);
        adapter.addFragment(eventList, "Miscellaneous");

        /*adapter.addFragment(new EventList("Schedule", "https://api.myjson.com/bins/bxg6z", "day1events", "https://api.myjson.com/bins/151byj", "day1eventdetails"), "Robotics");
        adapter.addFragment(new Day2("Day2", "https://api.myjson.com/bins/6kjqz", "day2events", "https://api.myjson.com/bins/k75sb", "day2eventdetails"), "Coding");
        adapter.addFragment(new EventList("Schedule", "https://api.myjson.com/bins/bxg6z", "day1events", "https://api.myjson.com/bins/151byj", "day1eventdetails"), "Gaming");
        adapter.addFragment(new Day2("Day2", "https://api.myjson.com/bins/6kjqz", "day2events", "https://api.myjson.com/bins/k75sb", "day2eventdetails"), "Presentation");
        adapter.addFragment(new EventList("Schedule", "https://api.myjson.com/bins/bxg6z", "day1events", "https://api.myjson.com/bins/151byj", "day1eventdetails"), "Fun");
        adapter.addFragment(new Day2("Day2", "https://api.myjson.com/bins/6kjqz", "day2events", "https://api.myjson.com/bins/k75sb", "day2eventdetails"), "Miscellaneous");*/
        viewPager.setAdapter(adapter);
    }

    public void setTab() {
        holder.viewPager.setCurrentItem(eventCategoryTabSelected);
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

    private class ViewHolderForEventCategories {
        TabLayout tabLayout;
        ViewPager viewPager;
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

    @Override
    public void onDetach() {
        super.onDetach();

        try {
            Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
            childFragmentManager.setAccessible(true);
            childFragmentManager.set(this, null);

        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}


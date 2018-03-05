package xyz.mrdeveloper.ignite;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.lang.reflect.Field;
import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Vaibhav on 11-Jan-17.
 */

public class WishListFragment extends Fragment {

    public ListView mListView;
    public LayoutInflater mInflater;
    public ViewGroup mContainer;
    public String wishedEvents;
    public View view;
    public boolean isWishlistEmpty;

    TextView textViewWishlistEmpty;

    public static WishListFragment newInstance() {
        return new WishListFragment();
    }

    public WishListFragment() {
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.wishlist, container, false);

        textViewWishlistEmpty = (TextView) view.findViewById(R.id.textViewEmptyWishlist);

        mContainer = container;
        mInflater = inflater;
        doRemainingTasks();

        return view;
    }

    public void doRemainingTasks() {
        try {
            SharedPreferences pref = getActivity().getSharedPreferences("WishList", MODE_PRIVATE);
            wishedEvents = pref.getString("WishList", null);
            Log.d("Check", "hehe 123 " + wishedEvents);

        } catch (Exception e) {
            e.printStackTrace();
        }

        WishlistAdapter wishlistAdapter = new WishlistAdapter(getActivity());

        final ArrayList<EventData> wishlist = wishlistAdapter.getWishlistFromFile(wishedEvents, getActivity());

        final WishlistAdapter adapter = new WishlistAdapter(getActivity(), wishlist, this);

        isWishlistEmpty = wishedEvents == null || wishedEvents.isEmpty();
        if (wishedEvents != null) {
            String[] events = wishedEvents.split(",");
            int k = 0;
            for (String event : events) {
                Log.d("Check", "hehe Here event name: " + event);
                if (event.length() > 1) {
                    Log.d("Check", "hehe Here event name234: " + event);
                    isWishlistEmpty = false;
                    k++;
                    break;
                }
            }
            if (k == 0) {
                isWishlistEmpty = true;
            }
        }

        if (isWishlistEmpty) {
            textViewWishlistEmpty.setVisibility(View.VISIBLE);
        } else {
            textViewWishlistEmpty.setVisibility(View.GONE);
        }

        // Create list view
        mListView = (ListView) view.findViewById(R.id.listWishList);

        mListView.setAdapter(new SlideExpandableListAdapter(
                adapter,
                R.id.expandable_toggle_button1,
                R.id.expandable_wishlist
        ));

//        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Log.d("Check", "hehe check");
//            }
//        });
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

package xyz.mrdeveloper.ignite;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import java.lang.reflect.Field;

import static xyz.mrdeveloper.ignite.UpdateFromFirebase.leaderboardToShow;

/**
 * Created by Vaibhav on 14-02-2017.
 */

public class LeaderboardFragment extends Fragment {
    public ListView mListView;
    public View view;
    View leaderboardView;
    ViewHolderForLeaderboard viewHolderForLeaderboard;
    LeaderboardAdapter adapter;

    public LeaderboardFragment() {
    }

    public static LeaderboardFragment newInstance() {
        return new LeaderboardFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.game_leaderboard, container, false);

        // create a new "Holder" with subviews
        viewHolderForLeaderboard = new ViewHolderForLeaderboard();

        viewHolderForLeaderboard.nameTextView = (TextView) view.findViewById(R.id.leaderboard_name);
        viewHolderForLeaderboard.rankTextView = (TextView) view.findViewById(R.id.leaderboard_rank);
        viewHolderForLeaderboard.scoreTextView = (TextView) view.findViewById(R.id.leaderboard_score);

        doRemainingTask();

        return view;
    }

    public void doRemainingTask() {

        adapter = new LeaderboardAdapter(getContext());
        adapter.mDataSource = leaderboardToShow;

        // Create list view
        mListView = (ListView) view.findViewById(R.id.leaderboard_list);

        if (leaderboardToShow != null)
            mListView.setAdapter(adapter);

//        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                selectedEventData = timeTable.get(position);
//
//                isDetailsOpened = true;
//
//                eventDetails.setVisibility(View.VISIBLE);
//                FillDetails(selectedEventData);
//
////                getChildFragmentManager()
////                        .beginTransaction()
////                        .replace(R.id.event_details_container, EventDetailsFragment.newInstance(selectedEventData), "EventData")
////                        .commitAllowingStateLoss();
//            }
//        });
    }

    private class ViewHolderForLeaderboard {
        TextView nameTextView;
        TextView rankTextView;
        TextView scoreTextView;
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

package xyz.mrdeveloper.ignite;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.lang.reflect.Field;

import static xyz.mrdeveloper.ignite.UpdateFromFirebase.areResultsAnnounced;
import static xyz.mrdeveloper.ignite.UpdateFromFirebase.resultsList;

/**
 * Created by Vaibhav on 11-01-2017.
 */

public class ResultsFragment extends Fragment {

    public ListView mListView;
    public LayoutInflater mInflater;
    public ViewGroup mContainer;
    public View view;
    public String title;
    public String first;
    public String second;
    public String third;
    public int mImageResIds;

    public ResultsFragment() {
    }

    public static ResultsFragment newInstance() {
        return new ResultsFragment();
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.result_list, container, false);
        mContainer = container;
        mInflater = inflater;

     /*   Fragment myFragment = getChildFragmentManager().findFragmentByTag("Results");
        if (myFragment != null && myFragment.isVisible()) {
            // add your code here
            Log.d("Check", "hehe Here I got the Loaded Fragment!!!!!!");
        }*/
/*
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.findFragmentByTag("Results");

        if (fragmentManager != null && fragmentManager.isVisible()) {
            // add your code here
        }*/

        doRemainingTask();

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    public void doRemainingTask() {

        TextView textViewResultNoInternet = (TextView) view.findViewById(R.id.textViewResultNoInternet);
        TextView textViewResultNotAnnounced = (TextView) view.findViewById(R.id.textViewResultNotAnnounced);

        if (resultsList == null) {
            textViewResultNoInternet = (TextView) view.findViewById(R.id.textViewResultNoInternet);
            textViewResultNoInternet.setVisibility(View.VISIBLE);
            textViewResultNotAnnounced.setVisibility(View.GONE);

        } else if (!areResultsAnnounced) {
            textViewResultNotAnnounced = (TextView) view.findViewById(R.id.textViewResultNotAnnounced);
            textViewResultNotAnnounced.setVisibility(View.VISIBLE);
            textViewResultNoInternet.setVisibility(View.GONE);

        } else {
            textViewResultNotAnnounced.setVisibility(View.GONE);
            textViewResultNoInternet.setVisibility(View.GONE);

            final ResultAdapter adapter = new ResultAdapter(getActivity(), resultsList);
            // Create list view
            mListView = (ListView) view.findViewById(R.id.result_list_view);

            mListView.setAdapter(new SlideExpandableListAdapter(
                    adapter,
                    R.id.expandable_toggle_button2,
                    R.id.expandable_result
            ));

//            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                @Override
//                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//               /* ResultsFragment selectedEvent = results.get(position);
//                String selectedEventTitle = selectedEvent.title;
//
//                Log.d("Check", "hehe01" +selectedEventTitle);
//
//                EventDetailsAdapter
//                        eventDetailsAdapter = new EventDetailsAdapter(getActivity(), view, mContainer, mInflater, mListView, selectedEventTitle);
//                isDetailsOpened = true;
//                isChildDetailsOpened = true;*/
//                }
//            });
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

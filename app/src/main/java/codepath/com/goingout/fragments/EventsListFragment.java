package codepath.com.goingout.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import codepath.com.goingout.R;
import codepath.com.goingout.adapters.FeedAdapter;
import codepath.com.goingout.models.Event;

/**
 * Created by acamara on 7/22/17.
 */


public abstract class EventsListFragment extends Fragment implements FeedAdapter.FeedAdapterListener {

    FeedAdapter feedAdapter;
    ArrayList<Event> events;
    RecyclerView rvEvents;
//    Eventful client;
//    public SwipeRefreshLayout swipeContainer;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragments_events_list, container, false);

        rvEvents = (RecyclerView) v.findViewById(R.id.rvEvents);
        RecyclerView.ItemDecoration itemDecoration = new
                DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        rvEvents.addItemDecoration(itemDecoration);
        events = new ArrayList<>();
        feedAdapter = new FeedAdapter(events, getContext());
        rvEvents.setLayoutManager(new LinearLayoutManager(getContext()));
        rvEvents.setAdapter(feedAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());


//        // Lookup the swipe container view
//        swipeContainer = (SwipeRefreshLayout) v.findViewById(R.id.swipeContainer);
//        // Setup refresh listener which triggers new data loading
//        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                // Your code to refresh the list here.
//                // Make sure you call swipeContainer.setRefreshing(false)
//                // once the network request has completed successfully.
//                fetchTimelineAsync(0);
//            }
//        });
//        // Configure the refreshing colors
//        swipeContainer.setColorSchemeResources(R.color.twitterColor);

//        scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
//            @Override
//            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
//                // Triggered only when new data needs to be appended to the list
//                // Add whatever code is needed to append new items to the bottom of the list
//                loadNextDataFromApi(page);
//            }
//
//
//        };
//        // Adds the scroll listener to RecyclerView
//        rvTweets.addOnScrollListener(scrollListener);

        return v;
    }
}

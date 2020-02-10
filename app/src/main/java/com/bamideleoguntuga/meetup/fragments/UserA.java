package com.bamideleoguntuga.meetup.fragments;

import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bamideleoguntuga.meetup.R;
import com.bamideleoguntuga.meetup.adapter.PaginationAdapter;
import com.bamideleoguntuga.meetup.model.users.Datum;
import com.bamideleoguntuga.meetup.model.users.Users;
import com.bamideleoguntuga.meetup.networking.api.Service;
import com.bamideleoguntuga.meetup.networking.generator.DataGenerator;
import com.bamideleoguntuga.meetup.utils.PaginationScrollListener;
import com.google.android.material.textfield.TextInputLayout;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.bamideleoguntuga.meetup.utils.Constants.BASE_URL;

public class UserA extends Fragment {
    private static final int PAGE_START = 1;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private int TOTAL_PAGES = 5;
    private int currentPage = PAGE_START;

    @BindView(R.id.main_recycler)
    RecyclerView main_recycler;

    @BindView(R.id.main_progress)
    ProgressBar main_progress;

    PaginationAdapter adapter;
    private Service service;

    GridLayoutManager gridLayoutManager;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.user_a, container, false);
        ButterKnife.bind(this, view);

        adapter = new PaginationAdapter(getContext());

        gridLayoutManager = new GridLayoutManager(getContext(), 2);
        main_recycler.setLayoutManager(gridLayoutManager);
        main_recycler.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        main_recycler.setItemAnimator(new DefaultItemAnimator());
        main_recycler.setAdapter(adapter);

        main_recycler.addOnScrollListener(new PaginationScrollListener(gridLayoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                currentPage += 1;

                // mocking network delay for API call
                new Handler().postDelayed(() -> loadNextPage(), 1000);
            }

            @Override
            public int getTotalPageCount() {
                return TOTAL_PAGES;
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });

        //init service and load data
        service = DataGenerator.createService(Service.class, BASE_URL);

        loadFirstPage();

        return view;
    }

    private List<Datum> fetchResults(Response<Users> response) {
        Users users = response.body();
        return users.getData();
    }

    private void loadFirstPage() {

        callUsers().enqueue(new Callback<Users>() {
            @Override
            public void onResponse(Call<Users> call, Response<Users> response) {
                // Got data. Send it to adapter

                List<Datum> results = fetchResults(response);
                main_progress.setVisibility(View.GONE);
                adapter.addAll(results);

                if (currentPage <= TOTAL_PAGES) adapter.addLoadingFooter();
                else isLastPage = true;
            }

            @Override
            public void onFailure(Call<Users> call, Throwable t) {
                t.printStackTrace();

            }
        });

    }

    private void loadNextPage() {

        callUsers().enqueue(new Callback<Users>() {
            @Override
            public void onResponse(Call<Users> call, Response<Users> response) {
                adapter.removeLoadingFooter();
                isLoading = false;

                List<Datum> results = fetchResults(response);
                adapter.addAll(results);

                if (currentPage != TOTAL_PAGES) adapter.addLoadingFooter();
                else isLastPage = true;
            }

            @Override
            public void onFailure(Call<Users> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    /**
     * RecyclerView item decoration - give equal margin around grid item
     */
    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    private Call<Users> callUsers() {
        return service.getUsers(
                currentPage
        );
    }
}

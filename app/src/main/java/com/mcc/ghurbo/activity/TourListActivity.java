package com.mcc.ghurbo.activity;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mcc.ghurbo.R;
import com.mcc.ghurbo.adapter.TourListAdapter;
import com.mcc.ghurbo.api.helper.RequestTours;
import com.mcc.ghurbo.api.http.ResponseListener;
import com.mcc.ghurbo.data.constant.AppConstants;
import com.mcc.ghurbo.listener.ItemClickListener;
import com.mcc.ghurbo.model.SearchTourModel;
import com.mcc.ghurbo.model.TourModel;
import com.mcc.ghurbo.utility.ActivityUtils;

import java.util.ArrayList;

public class TourListActivity extends BaseActivity{

    private TextView infoText;
    private SearchTourModel searchTourModel;
    private RecyclerView recyclerView;
    private TourListAdapter adapter;
    private ArrayList<TourModel> arrayList;
    private LinearLayoutManager mLayoutManager;

    private ProgressBar pbLoadMore;

    private int page = 1;
    private boolean loading = true;

    private final String LIST_STATE_KEY = "list_state";
    private final String LIST_DATA_KEY = "list_data";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initVariables();
        initView();
        initFunctionality();
        loadData(savedInstanceState, page);
        initListener();

    }

    private void initVariables() {
        Intent intent = getIntent();

        if(intent.hasExtra(AppConstants.BUNDLE_KEY_TOUR_MODEL)) {
            searchTourModel = intent.getParcelableExtra(AppConstants.BUNDLE_KEY_TOUR_MODEL);
        }

        arrayList = new ArrayList<>();
    }

    private void initView() {
        setContentView(R.layout.activity_list);
        initToolbar();
        enableBackButton();
        initLoader();

        infoText = (TextView) findViewById(R.id.info_text);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        pbLoadMore = (ProgressBar) findViewById(R.id.pb_load_more);
    }

    private void initFunctionality() {
        mLayoutManager = new LinearLayoutManager(TourListActivity.this);
        adapter = new TourListAdapter(getApplicationContext(), arrayList);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(adapter);

        invokeMessenger();
    }

    private void initListener() {
        adapter.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClick(int position, View v) {

                searchTourModel.setTourPackageName(arrayList.get(position).getTourTitle());
                searchTourModel.setTourPackageId(arrayList.get(position).getTourId());
                searchTourModel.setImageUrl(arrayList.get(position).getThumbnailImage());

                ImageView imageView = v.findViewById(R.id.icon);
                ActivityUtils.getInstance().invokeTourDetailsActivity(TourListActivity.this, searchTourModel, imageView);
            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {
                    onScrolledMore();
                }
            }
        });
    }

    private void onScrolledMore() {
        int visibleItemCount, totalItemCount, pastVisibleItems;
        visibleItemCount = mLayoutManager.getChildCount();
        totalItemCount = mLayoutManager.getItemCount();
        pastVisibleItems = mLayoutManager.findFirstVisibleItemPosition();

        if (loading) {
            if ((visibleItemCount + pastVisibleItems) >= totalItemCount) {
                loading = false;
                pbLoadMore.setVisibility(View.VISIBLE);
                page = page + 1;
                loadData(null, page);
            }
        }
    }

    private void loadData(Bundle savedInstanceState, int page) {

        if(savedInstanceState == null) {
            if (page == 1) {
                showLoader();
            }
            pbLoadMore.setVisibility(View.VISIBLE);

            RequestTours requestTours = new RequestTours(getApplicationContext());
            requestTours.buildParams(searchTourModel.getLocationId(), searchTourModel.getType(), page);
            requestTours.setResponseListener(new ResponseListener() {
                @Override
                public void onResponse(Object data) {

                    if (data != null) {
                        loadListData((ArrayList<TourModel>) data);
                    } else {
                        infoText.setText(getString(R.string.no_tour));
                        showEmptyView();
                    }
                    pbLoadMore.setVisibility(View.GONE);
                    loading = true;

                }
            });
            requestTours.execute();
        }
    }

    private void loadListData(ArrayList<TourModel> data) {
        arrayList.addAll(data);
        if (!arrayList.isEmpty()) {
            hideLoader();
            adapter.notifyDataSetChanged();
        } else {
            infoText.setText(getString(R.string.no_tour));
            showEmptyView();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle state) {
        super.onSaveInstanceState(state);
        state.putParcelable(LIST_STATE_KEY, mLayoutManager.onSaveInstanceState());
        state.putParcelableArrayList(LIST_DATA_KEY, arrayList);
    }

    @Override
    protected void onRestoreInstanceState(Bundle state) {
        super.onRestoreInstanceState(state);
        if(state != null) {
            ArrayList<TourModel> restoredList = state.getParcelableArrayList(LIST_DATA_KEY);
            loadListData(restoredList);

            mLayoutManager.onRestoreInstanceState(state.getParcelable(LIST_STATE_KEY));
        }
    }
}

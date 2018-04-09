package com.mcc.ghurbo.activity;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mcc.ghurbo.R;
import com.mcc.ghurbo.adapter.TourAmenitiesAdapter;
import com.mcc.ghurbo.api.helper.RequestHotelDetails;
import com.mcc.ghurbo.api.helper.RequestTourDetails;
import com.mcc.ghurbo.api.http.ResponseListener;
import com.mcc.ghurbo.data.constant.AppConstants;
import com.mcc.ghurbo.model.AmenityModel;
import com.mcc.ghurbo.model.SearchHotelModel;
import com.mcc.ghurbo.model.SearchTourModel;
import com.mcc.ghurbo.model.TourDetailsModel;
import com.mcc.ghurbo.utility.ActivityUtils;
import com.mcc.ghurbo.utility.Utils;

import java.util.ArrayList;

public class HotelDetailsActivity extends BaseActivity {

    private SearchHotelModel searchHotelModel;

    private CollapsingToolbarLayout collapsingToolbar;
    private TextView title, adultPrice, adultMax, childPrice, childMax,
            infantPrice, infantMax, tvDuration, location;
    private ImageButton ibMap;
    private RatingBar rating;
    private WebView webView;
    private Button btnBookNow;
    private FloatingActionButton fabFav;
    private ImageView ivTransitionImg;

    private RecyclerView recyclerView;
    private ArrayList<AmenityModel> amenityModels;
    private TourAmenitiesAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initVariables();
        initView();
        initFunctionality();
        loadData();
        initListener();

    }

    private void initVariables() {
        Intent intent = getIntent();

        if (intent.hasExtra(AppConstants.BUNDLE_KEY_HOTEL_MODEL)) {
            searchHotelModel = intent.getParcelableExtra(AppConstants.BUNDLE_KEY_HOTEL_MODEL);
        }

        amenityModels = new ArrayList<>();

    }

    private void initView() {
        setContentView(R.layout.activity_hotel_details);
        initToolbar();
        enableBackButton();
        initLoader();

        collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        title = (TextView) findViewById(R.id.title);
        rating = (RatingBar) findViewById(R.id.rating);
        adultPrice = (TextView) findViewById(R.id.adult_price);
        adultMax = (TextView) findViewById(R.id.adult_max);
        childPrice = (TextView) findViewById(R.id.child_price);
        childMax = (TextView) findViewById(R.id.child_max);
        infantPrice = (TextView) findViewById(R.id.infant_price);
        infantMax = (TextView) findViewById(R.id.infant_max);
        tvDuration = (TextView) findViewById(R.id.tv_duration);
        location = (TextView) findViewById(R.id.location);
        ibMap = (ImageButton) findViewById(R.id.ib_map);
        webView = (WebView) findViewById(R.id.web_view);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        btnBookNow = (Button) findViewById(R.id.btn_book_now);
        fabFav = (FloatingActionButton) findViewById(R.id.fab_fav);
        ivTransitionImg = (ImageView) findViewById(R.id.iv_transition);

    }

    private void initFunctionality() {
        invokeMessenger();
        webView.getSettings().setTextZoom(85);
        webView.setBackgroundColor(Color.TRANSPARENT);
        webView.setVerticalScrollBarEnabled(false);

        adapter = new TourAmenitiesAdapter(getApplicationContext(), amenityModels);
        recyclerView.setLayoutManager(new LinearLayoutManager(HotelDetailsActivity.this, LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(adapter);

        Glide.with(getApplicationContext())
                .load(searchHotelModel.getImageUrl())
                .error(R.color.placeholder)
                .into(ivTransitionImg);
    }

    private void initListener() {
        btnBookNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        fabFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    private void loadData() {

        showLoader();
        RequestHotelDetails requestHotelDetails = new RequestHotelDetails(getApplicationContext());
        requestHotelDetails.buildParams(searchHotelModel.getHotelId(), searchHotelModel.getCheckIn(), searchHotelModel.getCheckOut());
        requestHotelDetails.setResponseListener(new ResponseListener() {
            @Override
            public void onResponse(Object data) {

                TourDetailsModel tourDetailsModel = (TourDetailsModel) data;
                if (tourDetailsModel != null) {
                    hideLoader();
                    setDataToUi(tourDetailsModel);
                } else {
                    showEmptyView();
                }
            }
        });
        requestHotelDetails.execute();
    }

    private void setDataToUi(final TourDetailsModel tourDetailsModel) {

        collapsingToolbar.setTitle(tourDetailsModel.getTourTitle());
        title.setText(tourDetailsModel.getTourTitle());

        String ratingStr = tourDetailsModel.getTourStars();
        if (ratingStr != null && !ratingStr.isEmpty()) {
            rating.setRating(Float.parseFloat(ratingStr));
        } else {
            rating.setVisibility(View.GONE);
        }

        adultPrice.setText(tourDetailsModel.getAdultPrice() + getString(R.string.currency));
        adultMax.setText(tourDetailsModel.getMaxAdults() + getString(R.string.person_max));

        String childStatus = tourDetailsModel.getChildStatus();
        if (childStatus != null && childStatus.equals("1")) {
            childPrice.setText(tourDetailsModel.getChildPrice() + getString(R.string.currency));
            childMax.setText(tourDetailsModel.getMaxChild() + getString(R.string.person_max));
        } else {
            childPrice.setText(getString(R.string.na));
        }

        String infantStatus = tourDetailsModel.getInfantStatus();
        if (infantStatus != null && infantStatus.equals("1")) {
            infantPrice.setText(tourDetailsModel.getInfantPrice() + getString(R.string.currency));
            infantMax.setText(tourDetailsModel.getMaxInfant() + getString(R.string.person_max));
        } else {
            infantPrice.setText(getString(R.string.na));
        }

        tvDuration.setText(tourDetailsModel.getTourDays() + getString(R.string.days) +" "+ tourDetailsModel.getTourNights() + getString(R.string.nights));
        location.setText(tourDetailsModel.getLocation());

        ibMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.invokeMap(HotelDetailsActivity.this, tourDetailsModel.getLatitude(), tourDetailsModel.getLongitude());
            }
        });

        webView.loadData(tourDetailsModel.getTourDesc(), "text/html; charset=utf-8", "UTF-8");

        amenityModels.clear();
        amenityModels.addAll(tourDetailsModel.getAmenities());
        if(!amenityModels.isEmpty()) {
            recyclerView.setVisibility(View.VISIBLE);
            adapter.notifyDataSetChanged();
        } else {
            recyclerView.setVisibility(View.GONE);
        }

        ivTransitionImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityUtils.getInstance().invokeImages(HotelDetailsActivity.this, tourDetailsModel.getAllPhotos(), ivTransitionImg);
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            supportFinishAfterTransition();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

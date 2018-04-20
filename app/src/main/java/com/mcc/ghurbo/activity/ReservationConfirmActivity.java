package com.mcc.ghurbo.activity;


import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mcc.ghurbo.R;
import com.mcc.ghurbo.api.helper.RequestReserveHotel;
import com.mcc.ghurbo.api.helper.RequestReserveTour;
import com.mcc.ghurbo.api.http.ResponseListener;
import com.mcc.ghurbo.data.constant.AppConstants;
import com.mcc.ghurbo.data.preference.AppPreference;
import com.mcc.ghurbo.data.preference.PrefKey;
import com.mcc.ghurbo.model.SearchHotelModel;
import com.mcc.ghurbo.model.SearchTourModel;
import com.mcc.ghurbo.utility.ActivityUtils;
import com.mcc.ghurbo.utility.BitmapEmailUtils;
import com.mcc.ghurbo.utility.PermissionUtils;
import com.mcc.ghurbo.utility.PrintUtils;
import com.mcc.ghurbo.utility.Utils;

public class ReservationConfirmActivity extends BaseActivity {

    private SearchTourModel searchTourModel;
    private SearchHotelModel searchHotelModel;

    private TextView hotelName, tvBookingNumber, checkin, tvLocation,
            checkout, tvRoomName, tvRateAdult, tvTotalAdult, tvCall,
            tvRateChild, tvTotalChild, date, tvTotal, tvRooms, tvRate, infoText;
    private RelativeLayout btnLocation, btnCall, roomInfoView;
    private LinearLayout roomsView, checkInOutPanel, datePanel, adultRatePanel,
            adultCountPanel, childRatePanel, childCountPanel, ratePanel, parentView;
    private Button btnEmail, btnPrint;
    private ImageView ivRoom;

    private String callTo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initVar();
        initView();
        //initFunctionality();
        initListeners();
        invokeMessenger();

    }

    private void initVar() {
        Intent intent = getIntent();

        if (intent.hasExtra(AppConstants.BUNDLE_KEY_HOTEL_MODEL)) {
            searchHotelModel = intent.getParcelableExtra(AppConstants.BUNDLE_KEY_HOTEL_MODEL);
        }

        if (intent.hasExtra(AppConstants.BUNDLE_KEY_TOUR_MODEL)) {
            searchTourModel = intent.getParcelableExtra(AppConstants.BUNDLE_KEY_TOUR_MODEL);
        }
    }

    private void initView() {
        setContentView(R.layout.activity_reservation_confirm);
        initToolbar();
        enableBackButton();
        initLoader();

        hotelName = (TextView) findViewById(R.id.hotel_name);
        tvBookingNumber = (TextView) findViewById(R.id.tv_booking_number);
        checkin = (TextView) findViewById(R.id.checkin);
        checkout = (TextView) findViewById(R.id.checkout);
        tvRoomName = (TextView) findViewById(R.id.tv_room_name);
        tvRateAdult = (TextView) findViewById(R.id.tv_rate_adult);
        tvTotalAdult = (TextView) findViewById(R.id.tv_total_adult);
        tvRateChild = (TextView) findViewById(R.id.tv_rate_child);
        tvTotalChild = (TextView) findViewById(R.id.tv_total_child);
        tvTotal = (TextView) findViewById(R.id.tv_total);
        tvRooms = (TextView) findViewById(R.id.tv_rooms);
        tvRate = (TextView) findViewById(R.id.tv_rate);
        date = (TextView) findViewById(R.id.date);
        infoText = (TextView) findViewById(R.id.info_text);
        tvLocation = (TextView) findViewById(R.id.tv_location);
        tvCall = (TextView) findViewById(R.id.tv_call);

        btnLocation = (RelativeLayout) findViewById(R.id.btn_location);
        btnCall = (RelativeLayout) findViewById(R.id.btn_call);
        roomInfoView = (RelativeLayout) findViewById(R.id.room_info_view);

        roomsView = (LinearLayout) findViewById(R.id.rooms_view);
        checkInOutPanel = (LinearLayout) findViewById(R.id.check_in_out_panel);
        datePanel = (LinearLayout) findViewById(R.id.date_panel);
        ratePanel = (LinearLayout) findViewById(R.id.rate_panel);
        adultRatePanel = (LinearLayout) findViewById(R.id.adult_rate_panel);
        adultCountPanel = (LinearLayout) findViewById(R.id.adult_count_panel);
        childRatePanel = (LinearLayout) findViewById(R.id.child_rate_panel);
        childCountPanel = (LinearLayout) findViewById(R.id.child_count_panel);

        btnEmail = (Button) findViewById(R.id.btn_email);
        btnPrint = (Button) findViewById(R.id.btn_print);

        ivRoom = (ImageView) findViewById(R.id.iv_room);
        parentView = (LinearLayout) findViewById(R.id.parent_view);

    }



    private void initListeners() {
        btnEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                emailInvoice();
            }
        });

        btnPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                printInvoice();
            }
        });

        btnLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (searchHotelModel != null) {
                    Utils.invokeMap(ReservationConfirmActivity.this, searchHotelModel.getLatitude(), searchHotelModel.getLongitude());
                } else {
                    Utils.invokeMap(ReservationConfirmActivity.this, searchTourModel.getLatitude(), searchTourModel.getLongitude());
                }
            }
        });

        btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.makePhoneCall(ReservationConfirmActivity.this, callTo);
            }
        });
    }

    private void printInvoice() {
        btnEmail.setVisibility(View.INVISIBLE);
        btnPrint.setVisibility(View.INVISIBLE);
        PrintUtils printUtils = new PrintUtils(parentView, ReservationConfirmActivity.this, getString(R.string.ghurbo_print));
        printUtils.setTaskListener(new PrintUtils.TaskListener() {
            @Override
            public void onTaskComplete() {
                btnEmail.setVisibility(View.VISIBLE);
                btnPrint.setVisibility(View.VISIBLE);
            }
        });
        printUtils.execute();
    }

    private void emailInvoice() {
        if (PermissionUtils.isPermissionGranted(ReservationConfirmActivity.this, PermissionUtils.SD_WRITE_PERMISSIONS, PermissionUtils.REQUEST_WRITE_STORAGE)) {
            btnEmail.setVisibility(View.INVISIBLE);
            btnPrint.setVisibility(View.INVISIBLE);
            BitmapEmailUtils bitmapEmailUtils = new BitmapEmailUtils(parentView, ReservationConfirmActivity.this, getString(R.string.ghurbo_print));
            bitmapEmailUtils.setTaskListener(new BitmapEmailUtils.TaskListener() {
                @Override
                public void onTaskComplete() {
                    btnEmail.setVisibility(View.VISIBLE);
                    btnPrint.setVisibility(View.VISIBLE);
                }
            });
            bitmapEmailUtils.execute();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (PermissionUtils.isPermissionResultGranted(grantResults)) {
            if (requestCode == PermissionUtils.REQUEST_CALL) {
                Utils.makePhoneCall(ReservationConfirmActivity.this, callTo);
            } else if (requestCode == PermissionUtils.REQUEST_WRITE_STORAGE) {
                emailInvoice();
            }
        } else {
            Utils.showToast(ReservationConfirmActivity.this, getString(R.string.permission_not_granted));
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home){
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        ActivityUtils.getInstance().invokeActivity(ReservationConfirmActivity.this, MainActivity.class, true);
    }
}
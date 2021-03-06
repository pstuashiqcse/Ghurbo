package com.mcc.ghurbo.utility;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.ImageView;

import com.mcc.ghurbo.activity.BookingDetailsActivity;
import com.mcc.ghurbo.activity.HotelDetailsActivity;
import com.mcc.ghurbo.activity.HotelListActivity;
import com.mcc.ghurbo.activity.ImageViewActivity;
import com.mcc.ghurbo.activity.LoginActivity;
import com.mcc.ghurbo.activity.NotificationDetailsActivity;
import com.mcc.ghurbo.activity.ProfilingActivity;
import com.mcc.ghurbo.activity.BookNowActivity;
import com.mcc.ghurbo.activity.RoomDetailsActivity;
import com.mcc.ghurbo.activity.TourDetailsActivity;
import com.mcc.ghurbo.activity.TourListActivity;
import com.mcc.ghurbo.activity.WebPageActivity;
import com.mcc.ghurbo.data.constant.AppConstants;
import com.mcc.ghurbo.login.LoginModel;
import com.mcc.ghurbo.model.MyBookingModel;
import com.mcc.ghurbo.model.NotificationModel;
import com.mcc.ghurbo.model.RoomDetailsModel;
import com.mcc.ghurbo.model.SearchHotelModel;
import com.mcc.ghurbo.model.SearchTourModel;

import java.util.ArrayList;

public class ActivityUtils {

    private static ActivityUtils sActivityUtils = null;

    public static ActivityUtils getInstance() {
        if (sActivityUtils == null) {
            sActivityUtils = new ActivityUtils();
        }
        return sActivityUtils;
    }

    public void invokeActivity(Activity activity, Class<?> tClass, boolean shouldFinish) {
        Intent intent = new Intent(activity, tClass);
        activity.startActivity(intent);
        if (shouldFinish) {
            activity.finish();
        }
    }

    public void invokeWebPageActivity(Activity activity, String pageTitle, String url) {
        Intent intent = new Intent(activity, WebPageActivity.class);
        intent.putExtra(AppConstants.BUNDLE_KEY_TITLE, pageTitle);
        intent.putExtra(AppConstants.BUNDLE_KEY_URL, url);
        activity.startActivity(intent);
    }

    public void invokeTourListActivity(Activity activity, SearchTourModel searchTourModel) {
        Intent intent = new Intent(activity, TourListActivity.class);
        intent.putExtra(AppConstants.BUNDLE_KEY_TOUR_MODEL, searchTourModel);
        activity.startActivity(intent);
    }

    public void invokeHotelListActivity(Activity activity, SearchHotelModel searchHotelModel) {
        Intent intent = new Intent(activity, HotelListActivity.class);
        intent.putExtra(AppConstants.BUNDLE_KEY_HOTEL_MODEL, searchHotelModel);
        activity.startActivity(intent);
    }

    public void invokeTourDetailsActivity(Activity activity, SearchTourModel searchTourModel, ImageView imageView) {
        Intent intent = new Intent(activity, TourDetailsActivity.class);
        intent.putExtra(AppConstants.BUNDLE_KEY_TOUR_MODEL, searchTourModel);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Bundle bundle = ActivityOptions.makeSceneTransitionAnimation(activity, imageView, imageView.getTransitionName()).toBundle();
            activity.startActivity(intent, bundle);
        } else {
            activity.startActivity(intent);
        }
    }

    public void invokeProfilingActivity(Activity activity, LoginModel loginModel) {
        Intent intent = new Intent(activity, ProfilingActivity.class);
        intent.putExtra(AppConstants.BUNDLE_KEY_LOGIN_MODEL, loginModel);
        activity.startActivity(intent);
        activity.finish();
    }

    public void invokeProfilingActivity(Activity activity, LoginModel loginModel, SearchTourModel searchTourModel, SearchHotelModel searchHotelModel) {
        Intent intent = new Intent(activity, ProfilingActivity.class);
        intent.putExtra(AppConstants.BUNDLE_KEY_LOGIN_MODEL, loginModel);
        intent.putExtra(AppConstants.BUNDLE_KEY_HOTEL_MODEL, searchHotelModel);
        intent.putExtra(AppConstants.BUNDLE_KEY_TOUR_MODEL, searchTourModel);
        intent.putExtra(AppConstants.BUNDLE_FROM_BOOKING, true);
        activity.startActivity(intent);
        activity.finish();
    }

    public void invokeImages(Activity activity, ArrayList<String> arrayList, ImageView imageView) {
        Intent intent = new Intent(activity, ImageViewActivity.class);
        intent.putExtra(AppConstants.BUNDLE_MULTI_IMAGE, arrayList);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Bundle bundle = ActivityOptions.makeSceneTransitionAnimation(activity, imageView, imageView.getTransitionName()).toBundle();
            activity.startActivity(intent, bundle);
        } else {
            activity.startActivity(intent);
        }
    }

    public void invokeHotelDetailsActivity(Activity activity, SearchHotelModel searchHotelModel, ImageView imageView) {
        Intent intent = new Intent(activity, HotelDetailsActivity.class);
        intent.putExtra(AppConstants.BUNDLE_KEY_HOTEL_MODEL, searchHotelModel);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Bundle bundle = ActivityOptions.makeSceneTransitionAnimation(activity, imageView, imageView.getTransitionName()).toBundle();
            activity.startActivity(intent, bundle);
        } else {
            activity.startActivity(intent);
        }
    }

    public void invokeRoomDetailsActivity(Activity activity, RoomDetailsModel roomDetailsModel, SearchHotelModel searchHotelModel, ImageView imageView) {
        Intent intent = new Intent(activity, RoomDetailsActivity.class);
        intent.putExtra(AppConstants.BUNDLE_ROOM_DETAILS, roomDetailsModel);
        intent.putExtra(AppConstants.BUNDLE_KEY_HOTEL_MODEL, searchHotelModel);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Bundle bundle = ActivityOptions.makeSceneTransitionAnimation(activity, imageView, imageView.getTransitionName()).toBundle();
            activity.startActivity(intent, bundle);
        } else {
            activity.startActivity(intent);
        }
    }

    public void invokeBookNowActivity(Activity activity, SearchTourModel searchTourModel, SearchHotelModel searchHotelModel, boolean fromLogin) {
        Intent intent = new Intent(activity, BookNowActivity.class);
        intent.putExtra(AppConstants.BUNDLE_KEY_HOTEL_MODEL, searchHotelModel);
        intent.putExtra(AppConstants.BUNDLE_KEY_TOUR_MODEL, searchTourModel);
        intent.putExtra(AppConstants.BUNDLE_FROM_LOGIN, fromLogin);
        activity.startActivity(intent);
        if (fromLogin) {
            activity.finish();
        }
    }

    public void invokeLoginActivity(Activity activity, SearchTourModel searchTourModel, SearchHotelModel searchHotelModel) {
        Intent intent = new Intent(activity, LoginActivity.class);
        intent.putExtra(AppConstants.BUNDLE_KEY_HOTEL_MODEL, searchHotelModel);
        intent.putExtra(AppConstants.BUNDLE_KEY_TOUR_MODEL, searchTourModel);
        intent.putExtra(AppConstants.BUNDLE_FROM_BOOKING, true);
        activity.startActivity(intent);
        activity.finish();
    }

    public void invokeLoginForFavActivity(Activity activity) {
        Intent intent = new Intent(activity, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        activity.startActivity(intent);
        activity.finish();
    }

    public void invokeBookingDetailsActivity(Activity activity, MyBookingModel myBookingModel, boolean fromHistory) {
        Intent intent = new Intent(activity, BookingDetailsActivity.class);
        intent.putExtra(AppConstants.BUNDLE_BOOKING_DETAILS, myBookingModel);
        intent.putExtra(AppConstants.BUNDLE_FROM_HISTORY, fromHistory);
        if(!fromHistory) {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        }
        activity.startActivity(intent);
    }

    public void invokeNotificationDetailsActivity(Activity activity, NotificationModel notificationModel) {
        Intent intent = new Intent(activity, NotificationDetailsActivity.class);
        intent.putExtra(AppConstants.BUNDLE_NOTI_DETAILS, notificationModel);
        activity.startActivity(intent);
    }
}

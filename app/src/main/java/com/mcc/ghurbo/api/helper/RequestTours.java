package com.mcc.ghurbo.api.helper;


import android.content.Context;

import com.mcc.ghurbo.api.http.BaseHttp;
import com.mcc.ghurbo.api.http.ResponseListener;
import com.mcc.ghurbo.api.params.AppSecret;
import com.mcc.ghurbo.api.params.HttpParams;
import com.mcc.ghurbo.api.parser.TourListParser;

import java.util.HashMap;

public class RequestTours extends BaseHttp {

    private Context mContext;
    private Object object;
    private ResponseListener responseListener;

    public RequestTours(Context context) {
        super(context, HttpParams.BASE_URL + HttpParams.API_TOURS);
        mContext = context;
    }

    public void setResponseListener(ResponseListener responseListener) {
        this.responseListener = responseListener;
    }

    public void buildParams(String locationId, String tourType, int page) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put(HttpParams.PARAM_SECRET_KEY, AppSecret.getAppSecretKey(mContext));
        hashMap.put(HttpParams.PARAM_LOCATION_ID, locationId);
        hashMap.put(HttpParams.PARAM_TOUR_TYPE, tourType);
        hashMap.put(HttpParams.PARAM_PAGE, String.valueOf(page));

        post(hashMap);
    }

    @Override
    public void onBackgroundTask(String response) {
        object = new TourListParser().getTourModels(response);
    }

    @Override
    public void onTaskComplete() {
        if (responseListener != null) {
            responseListener.onResponse(object);
        }
    }
}

package com.mcc.ghurbo.activity;


import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mcc.ghurbo.R;
import com.mcc.ghurbo.adapter.FavoriteAdapter;
import com.mcc.ghurbo.adapter.NotificationAdapter;
import com.mcc.ghurbo.data.constant.AppConstants;
import com.mcc.ghurbo.data.preference.AppPreference;
import com.mcc.ghurbo.data.preference.PrefKey;
import com.mcc.ghurbo.data.sqlite.DbConstants;
import com.mcc.ghurbo.listener.ItemClickListener;
import com.mcc.ghurbo.model.FavoriteModel;
import com.mcc.ghurbo.model.NotificationModel;
import com.mcc.ghurbo.model.SearchHotelModel;
import com.mcc.ghurbo.model.SearchTourModel;
import com.mcc.ghurbo.utility.ActivityUtils;

import java.util.ArrayList;

public class NotificationListActivity extends BaseActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    private TextView infoText;
    private RecyclerView recyclerView;
    private NotificationAdapter adapter;
    private ArrayList<NotificationModel> arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initVar();
        initView();
        initFunctionality();
        initListeners();
        invokeMessenger();

    }

    private void initVar() {
        arrayList = new ArrayList<>();
    }

    private void initView() {
        setContentView(R.layout.activity_list);
        initToolbar();
        enableBackButton();
        initLoader();
        setToolbarTitle(getString(R.string.notifications));

        infoText = (TextView) findViewById(R.id.info_text);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

    }

    private void initFunctionality() {
        adapter = new NotificationAdapter(NotificationListActivity.this, arrayList);
        recyclerView.setLayoutManager(new LinearLayoutManager(NotificationListActivity.this));
        recyclerView.setAdapter(adapter);

        getLoaderManager().initLoader(0, null, this);

    }

    private void initListeners() {
        adapter.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClick(int position, View view) {
                ActivityUtils.getInstance().invokeNotificationDetailsActivity(NotificationListActivity.this, arrayList.get(position));
            }
        });
    }

    private void loadData(Cursor c) {
        arrayList.clear();
        if (c != null && c.getCount() > 0) {

            if (c.moveToFirst()) {
                do {
                    arrayList.add(

                            new NotificationModel(
                                    c.getInt(c.getColumnIndexOrThrow(DbConstants._ID)),
                                    c.getString(c.getColumnIndexOrThrow(DbConstants.COLUMN_TITLE)),
                                    c.getString(c.getColumnIndexOrThrow(DbConstants.COLUMN_BODY)),
                                    c.getString(c.getColumnIndexOrThrow(DbConstants.COLUMN_IMAGE)),
                                    c.getString(c.getColumnIndexOrThrow(DbConstants.COLUMN_STATUS)))

                    );
                } while (c.moveToNext());
            }

            c.close();
        }
        if(adapter != null) {
            adapter.notifyDataSetChanged();
        }
        if (arrayList.isEmpty()) {
            infoText.setText(getString(R.string.no_notification));
            showEmptyView();
        } else {
            hideLoader();
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle bundle) {
        showLoader();
        String[] projection = {
                DbConstants._ID,
                DbConstants.COLUMN_TITLE,
                DbConstants.COLUMN_IMAGE,
                DbConstants.COLUMN_BODY,
                DbConstants.COLUMN_STATUS
        };
        return new CursorLoader(this, DbConstants.NOTI_CONTENT_URI, projection, null, null, DbConstants._ID + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor c) {
        loadData(c);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
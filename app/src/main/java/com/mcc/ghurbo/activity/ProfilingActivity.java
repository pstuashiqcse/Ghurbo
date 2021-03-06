package com.mcc.ghurbo.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.mcc.ghurbo.R;
import com.mcc.ghurbo.api.helper.RequestLogin;
import com.mcc.ghurbo.api.http.ResponseListener;
import com.mcc.ghurbo.data.constant.AppConstants;
import com.mcc.ghurbo.data.preference.AppPreference;
import com.mcc.ghurbo.data.preference.PrefKey;
import com.mcc.ghurbo.image.ImageProcessListener;
import com.mcc.ghurbo.image.ImageProcessor;
import com.mcc.ghurbo.login.LoginModel;
import com.mcc.ghurbo.model.SearchHotelModel;
import com.mcc.ghurbo.model.SearchTourModel;
import com.mcc.ghurbo.utility.ActivityUtils;
import com.mcc.ghurbo.utility.DialogUtils;
import com.mcc.ghurbo.utility.FilePicker;
import com.mcc.ghurbo.utility.PermissionUtils;
import com.mcc.ghurbo.utility.Utils;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfilingActivity extends BaseActivity {

    private LoginModel loginModel;
    private SearchTourModel searchTourModel;
    private SearchHotelModel searchHotelModel;
    private boolean fromBooking;

    private ProgressBar progressBar;
    private CircleImageView profileImage;
    private FloatingActionButton changePic, fabSave;
    private EditText etName, etEmail, etPhone;

    private String profilePicture, profilePictureData;

    private static final int KEY_IMAGE_PICKER = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initVars();
        initView();
        initFunctionality();
        initListener();

    }

    private void initVars() {
        Intent intent = getIntent();

        if (intent.hasExtra(AppConstants.BUNDLE_KEY_LOGIN_MODEL)) {
            loginModel = intent.getParcelableExtra(AppConstants.BUNDLE_KEY_LOGIN_MODEL);
        }

        if (intent.hasExtra(AppConstants.BUNDLE_KEY_HOTEL_MODEL)) {
            searchHotelModel = intent.getParcelableExtra(AppConstants.BUNDLE_KEY_HOTEL_MODEL);
        }

        if (intent.hasExtra(AppConstants.BUNDLE_KEY_TOUR_MODEL)) {
            searchTourModel = intent.getParcelableExtra(AppConstants.BUNDLE_KEY_TOUR_MODEL);
        }

        if (intent.hasExtra(AppConstants.BUNDLE_FROM_BOOKING)) {
            fromBooking = intent.getBooleanExtra(AppConstants.BUNDLE_FROM_BOOKING, false);
        }
    }

    private void initView() {
        setContentView(R.layout.activity_profile);
        profileImage = (CircleImageView) findViewById(R.id.profile_image);
        changePic = (FloatingActionButton) findViewById(R.id.change_pic);
        fabSave = (FloatingActionButton) findViewById(R.id.fab_save);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        etName = (EditText) findViewById(R.id.et_name);
        etEmail = (EditText) findViewById(R.id.et_email);
        etPhone = (EditText) findViewById(R.id.et_phone);
    }

    private void initFunctionality() {
        String profilePic = loginModel.getProfilePic();
        if (profilePic != null) {

            Glide.with(getApplicationContext())
                    .load(profilePic)
                    .crossFade()
                    .error(R.drawable.ic_profile)
                    .into(profileImage);
        }

        String name = loginModel.getName();
        if(name != null) {
            etName.setText(name);
            etName.setSelection(name.length());
        }
        etEmail.setText(loginModel.getEmail());
        etPhone.setText(loginModel.getPhone());
    }

    private void initListener() {
        changePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (PermissionUtils.isPermissionGranted(ProfilingActivity.this, PermissionUtils.SD_WRITE_PERMISSIONS, PermissionUtils.REQUEST_WRITE_STORAGE)) {
                    invokeImagePickerActivity();
                }
            }
        });

        fabSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                loginModel.setName(etName.getText().toString());
                loginModel.setEmail(etEmail.getText().toString());
                loginModel.setPhone(etPhone.getText().toString());

                loginGhurbo(loginModel);
            }
        });

    }


    private void loginGhurbo(LoginModel loginModel) {

        final ProgressDialog dialogUtils = DialogUtils.showProgressDialog(ProfilingActivity.this, getString(R.string.loading), false);

        RequestLogin requestLogin = new RequestLogin(getApplicationContext());
        requestLogin.buildParams(loginModel.getUserId(), loginModel.getEmail(), loginModel.getPhone(), loginModel.getName(), loginModel.getProfilePic(), loginModel.getType());
        requestLogin.setResponseListener(new ResponseListener() {
            @Override
            public void onResponse(Object data) {

                if (data != null) {
                    LoginModel responseModel = (LoginModel) data;

                    AppPreference.getInstance(getApplicationContext()).setBoolean(PrefKey.SKIPPED, false);
                    AppPreference.getInstance(getApplicationContext()).setBoolean(PrefKey.LOGIN, true);

                    AppPreference.getInstance(getApplicationContext()).setString(PrefKey.USER_ID, responseModel.getUserId());
                    AppPreference.getInstance(getApplicationContext()).setString(PrefKey.EMAIL, responseModel.getEmail());
                    AppPreference.getInstance(getApplicationContext()).setString(PrefKey.PHONE, responseModel.getPhone());
                    AppPreference.getInstance(getApplicationContext()).setString(PrefKey.NAME, responseModel.getName());

                    if (profilePicture == null) {
                        AppPreference.getInstance(getApplicationContext()).setString(PrefKey.PROFILE_PIC, responseModel.getProfilePic());
                    } else {
                        AppPreference.getInstance(getApplicationContext()).setString(PrefKey.PROFILE_PIC, profilePicture);
                    }

                    if (fromBooking) {
                        ActivityUtils.getInstance().invokeBookNowActivity(ProfilingActivity.this, searchTourModel, searchHotelModel, true);
                    } else {
                        ActivityUtils.getInstance().invokeActivity(ProfilingActivity.this, MainActivity.class, true);
                    }
                }else {
                    Utils.showToast(getApplicationContext(), getString(R.string.login_failed));
                }

                DialogUtils.dismissProgressDialog(dialogUtils);
            }
        });
        requestLogin.execute();

    }

    @Override
    protected void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            // If Image picker
            if (reqCode == KEY_IMAGE_PICKER) {
                String picturePath = FilePicker.getPickedFilePath(this, data);
                processProfileImage(picturePath);
            }
        }
    }

    private void processProfileImage(String picturePath) {

        progressBar.setVisibility(View.VISIBLE);

        ImageProcessor imageProcessor = new ImageProcessor(picturePath);
        imageProcessor.setImageProcessListener(new ImageProcessListener() {
            @Override
            public void onImageProcessed(String processedImagePath, String encodedString) {

                if (processedImagePath != null && encodedString != null) {

                    profilePicture = processedImagePath;
                    profilePictureData = encodedString;

                    Glide.with(getApplicationContext())
                            .load(processedImagePath)
                            .crossFade()
                            .error(R.drawable.ic_profile)
                            .into(profileImage);

                }

                progressBar.setVisibility(View.GONE);

            }
        });
        imageProcessor.execute();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PermissionUtils.REQUEST_WRITE_STORAGE: {
                if (PermissionUtils.isPermissionResultGranted(grantResults)) {
                    invokeImagePickerActivity();
                } else {
                    Utils.showToast(getApplicationContext(), getString(R.string.permission_not_granted));
                }
            }
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        Utils.noInternetWarning(profileImage, getApplicationContext());
    }

    private void invokeImagePickerActivity() {
        Intent chooseImageIntent = FilePicker.getPickFileIntent(ProfilingActivity.this);
        startActivityForResult(chooseImageIntent, KEY_IMAGE_PICKER);
    }
}

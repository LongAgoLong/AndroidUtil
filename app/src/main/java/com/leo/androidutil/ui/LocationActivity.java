package com.leo.androidutil.ui;

import android.Manifest;
import android.location.Address;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.text.InputFilter;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.leo.androidutil.R;
import com.leo.commonutil.calendar.DateUtil;
import com.leo.commonutil.asyn.WeakHandler;
import com.leo.commonutil.enume.UnitTime;
import com.leo.commonutil.location.OnLocationCallback;
import com.leo.commonutil.location.SystemLocationUtil;
import com.leo.system.LogUtil;
import com.tbruyelle.rxpermissions2.RxPermissions;

/**
 * 系统定位demo
 */
public class LocationActivity extends BaseActivity implements OnLocationCallback, View.OnClickListener {
    private static final String TAG = LocationActivity.class.getSimpleName();
    private TextView mResultTv;
    private Button mPermissionBtn;
    private Button mStopBtn;
    private WeakHandler weakHandler = new WeakHandler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        initActionBar();
        mResultTv = findViewById(R.id.resultTv);
        mPermissionBtn = findViewById(R.id.permissionBtn);
        mPermissionBtn.setOnClickListener(this);
        mStopBtn = findViewById(R.id.stopBtn);
        mStopBtn.setOnClickListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != weakHandler) {
            weakHandler.removeCallbacksAndMessages(null);
        }
        SystemLocationUtil.getInstance().stop();
    }

    @Override
    protected void initActionBar() {
        super.initActionBar();
        ActionBar actionBar = getSupportActionBar();
        if (null == actionBar) {
            return;
        }
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("系统定位");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void location() {
        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions.request(Manifest.permission.ACCESS_FINE_LOCATION)
                .subscribe(aBoolean -> {
                    if (aBoolean) {
                        String bestProvider = SystemLocationUtil.getInstance().getBestProvider();
                        SystemLocationUtil.getInstance().start(bestProvider,
                                10000, this);
                    } else {
                        LogUtil.e(TAG, "lack of permission");
                    }
                }, throwable -> {
                    LogUtil.e(TAG, throwable.toString());
                });
    }

    @Override
    public void onLocationChanged() {

    }

    @Override
    public void onLocationReverGeoResult() {
        weakHandler.post(() -> {
            SystemLocationUtil locationUtil = SystemLocationUtil.getInstance();
            Address addressBean = locationUtil.getAddressBean();
            if (null == addressBean) {
                return;
            }
            mResultTv.setText(DateUtil.format(UnitTime.MILLIONSECOND, System.currentTimeMillis(),
                    DateUtil.DATA_YMDHMS));
            mResultTv.append("\n");
            mResultTv.append(locationUtil.getAddressStr(addressBean));
            mResultTv.append("\n");
            Location wgs84 = locationUtil.getLocationWGS84();
            if (null != wgs84) {
                mResultTv.append("国标系 (wgs84)：");
                mResultTv.append("\n");
                mResultTv.append(wgs84.getLatitude() + " - " + wgs84.getLongitude());
                mResultTv.append("\n");
            }
            Location gcj02 = locationUtil.getLocationGCJ02();
            if (null != gcj02) {
                mResultTv.append("火星坐标系 (GCJ-02)：");
                mResultTv.append("\n");
                mResultTv.append(gcj02.getLatitude() + " - " + gcj02.getLongitude());
                mResultTv.append("\n");
            }
            Location bd09LL = locationUtil.getLocationBD09LL();
            if (null != bd09LL) {
                mResultTv.append("百度坐标系 (BD-09)：");
                mResultTv.append("\n");
                mResultTv.append(bd09LL.getLatitude() + " - " + bd09LL.getLongitude());
                mResultTv.append("\n");
            }
            mResultTv.append("反GEO：");
            mResultTv.append("\n");
            mResultTv.append(addressBean.getLatitude() + " - " + addressBean.getLongitude());
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.permissionBtn:
                location();
                break;
            case R.id.stopBtn:
                SystemLocationUtil.getInstance().stop();
                break;
        }
    }
}

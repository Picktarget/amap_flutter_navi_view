package com.stack.amap_flutter_navi_view;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;

import com.amap.api.maps.AMapException;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.model.LatLng;
import com.amap.api.navi.AMapNavi;
import com.amap.api.navi.AMapNaviListener;
import com.amap.api.navi.AMapNaviView;
import com.amap.api.navi.AMapNaviViewListener;
import com.amap.api.navi.AMapNaviViewOptions;
import com.amap.api.navi.NaviSetting;
import com.amap.api.navi.enums.NaviType;
import com.amap.api.navi.model.AMapCalcRouteResult;
import com.amap.api.navi.model.AMapLaneInfo;
import com.amap.api.navi.model.AMapModelCross;
import com.amap.api.navi.model.AMapNaviCameraInfo;
import com.amap.api.navi.model.AMapNaviCross;
import com.amap.api.navi.model.AMapNaviLocation;
import com.amap.api.navi.model.AMapNaviPath;
import com.amap.api.navi.model.AMapNaviRouteNotifyData;
import com.amap.api.navi.model.AMapNaviTrafficFacilityInfo;
import com.amap.api.navi.model.AMapServiceAreaInfo;
import com.amap.api.navi.model.AimLessModeCongestionInfo;
import com.amap.api.navi.model.AimLessModeStat;
import com.amap.api.navi.model.NaviInfo;
import com.amap.api.navi.model.NaviLatLng;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.flutter.plugin.common.BinaryMessenger;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.platform.PlatformView;


class NativeView extends FrameLayout implements PlatformView, AMapNaviViewListener, AMapNaviListener, MethodChannel.MethodCallHandler {
    private final MethodChannel methodChannel;
    private final String viewType = "amap-navi";
    @NonNull
    private AMapNaviView aMapNaviView;
    private AMapNavi aMapNavi;
    /*
     * strategyFlag转换出来的值都对应PathPlanningStrategy常量，用户也可以直接传入PathPlanningStrategy常量进行算路。
     * 如:mAMapNavi.calculateDriveRoute(mStartList, mEndList, mWayPointList,PathPlanningStrategy.DRIVING_DEFAULT);
     */
    int strategyFlag = 0;
    private List<NaviLatLng> startList = new ArrayList<NaviLatLng>();
    /**
     * 途径点坐标集合
     */
    private List<NaviLatLng> wayList = new ArrayList<NaviLatLng>();
    /**
     * 终点坐标集合［建议就一个终点］
     */
    private List<NaviLatLng> endList = new ArrayList<NaviLatLng>();

    private String keyWord = "";

    private int totalLength = 0;
    private AMapNaviPath naviPath;

    NativeView(Context context, BinaryMessenger messenger, int id, Map<String, Object> params, MethodChannel channel) throws AMapException {
        super(context);

        NaviSetting.updatePrivacyShow(context, true, true);
        NaviSetting.updatePrivacyAgree(context, true);
        aMapNaviView = new AMapNaviView(context);
        AMapNaviViewOptions options = new AMapNaviViewOptions();
        options.setLayoutVisible(false);
        aMapNaviView.setViewOptions(options);
        aMapNaviView.onCreate(new Bundle());
        aMapNaviView.setAMapNaviViewListener(this);

//        methodChannel = new MethodChannel(messenger, viewType + id);
        methodChannel = channel;
        methodChannel.setMethodCallHandler((MethodChannel.MethodCallHandler) this);

        aMapNavi = AMapNavi.getInstance(context);
        aMapNavi.addAMapNaviListener(this);
        NaviLatLng startLatlng = new NaviLatLng((Double) params.get("startLat"), (Double) params.get("startLng"));
        NaviLatLng endLatlng = new NaviLatLng((Double) params.get("endLat"), (Double) params.get("endLng"));
        startList.clear();
        endList.clear();
        // 组件参数配置
        startList.add(startLatlng);
        endList.add(endLatlng);
        // 启动组件
        strategyFlag = aMapNavi.strategyConvert(true, false, false, true, false);
        aMapNavi.setUseInnerVoice(true, false);
        aMapNavi.calculateDriveRoute(startList, endList, wayList, strategyFlag);
    }

    @NonNull
    @Override
    public View getView() {
        return aMapNaviView;
    }

    @Override
    public void dispose() {
        aMapNavi = null;
        naviPath = null;
        aMapNaviView.onDestroy();
        methodChannel.setMethodCallHandler(null);
        Log.d("dispose", "dispose: ----------------");
    }

    @Override
    public void onNaviSetting() {

    }

    @Override
    public void onNaviCancel() {
        Log.d("onNaviCancel", "onNaviCancel: ----------------");
        aMapNavi.stopNavi();
        methodChannel.invokeMethod("stopNavi", true);
    }

    @Override
    public boolean onNaviBackClick() {
        return false;
    }

    @Override
    public void onNaviMapMode(int i) {

    }

    @Override
    public void onNaviTurnClick() {

    }

    @Override
    public void onNextRoadClick() {

    }

    @Override
    public void onScanViewButtonClick() {

    }

    @Override
    public void onLockMap(boolean b) {

    }

    @Override
    public void onNaviViewLoaded() {

    }

    @Override
    public void onMapTypeChanged(int i) {

    }

    @Override
    public void onNaviViewShowMode(int i) {
    }

    @Override
    public void onInitNaviFailure() {

    }

    @Override
    public void onInitNaviSuccess() {

    }

    @Override
    public void onStartNavi(int i) {

    }

    @Override
    public void onTrafficStatusUpdate() {

    }

    @Override
    public void onLocationChange(AMapNaviLocation aMapNaviLocation) {
        // 获取导航路径对象
        naviPath = aMapNavi.getNaviPath();

        // 获取导航路径总长度
        totalLength = naviPath.getAllLength();
        // 获取起点和终点
        NaviLatLng start = naviPath.getStartPoint();
        NaviLatLng end = aMapNaviLocation.getCoord();
        // 计算起点和当前位置之间的距离
        float distance = AMapUtils.calculateLineDistance(
                new LatLng(start.getLatitude(), start.getLongitude()),
                new LatLng(end.getLatitude(), end.getLongitude()));

        // 计算行驶距离
//        float driveDistance = totalLength - distance;
        methodChannel.invokeMethod("driveDistance", distance);
    }

    @Override
    public void onGetNavigationText(int i, String s) {

    }

    @Override
    public void onGetNavigationText(String s) {

    }

    @Override
    public void onEndEmulatorNavi() {

    }

    @Override
    public void onArriveDestination() {

    }

    @Override
    public void onCalculateRouteFailure(int i) {

    }

    @Override
    public void onReCalculateRouteForYaw() {

    }

    @Override
    public void onReCalculateRouteForTrafficJam() {

    }

    @Override
    public void onArrivedWayPoint(int i) {

    }

    @Override
    public void onGpsOpenStatus(boolean b) {

    }

    @Override
    public void onNaviInfoUpdate(NaviInfo naviInfo) {

    }

    @Override
    public void updateCameraInfo(AMapNaviCameraInfo[] aMapNaviCameraInfos) {

    }

    @Override
    public void updateIntervalCameraInfo(AMapNaviCameraInfo aMapNaviCameraInfo, AMapNaviCameraInfo aMapNaviCameraInfo1, int i) {

    }

    @Override
    public void onServiceAreaUpdate(AMapServiceAreaInfo[] aMapServiceAreaInfos) {

    }

    @Override
    public void showCross(AMapNaviCross aMapNaviCross) {

    }

    @Override
    public void hideCross() {

    }

    @Override
    public void showModeCross(AMapModelCross aMapModelCross) {

    }

    @Override
    public void hideModeCross() {

    }

    @Override
    public void showLaneInfo(AMapLaneInfo[] aMapLaneInfos, byte[] bytes, byte[] bytes1) {

    }

    @Override
    public void showLaneInfo(AMapLaneInfo aMapLaneInfo) {

    }

    @Override
    public void hideLaneInfo() {

    }

    @Override
    public void onCalculateRouteSuccess(int[] ints) {
        aMapNavi.startNavi(NaviType.GPS);
    }

    @Override
    public void notifyParallelRoad(int i) {

    }

    @Override
    public void OnUpdateTrafficFacility(AMapNaviTrafficFacilityInfo[] aMapNaviTrafficFacilityInfos) {

    }

    @Override
    public void OnUpdateTrafficFacility(AMapNaviTrafficFacilityInfo aMapNaviTrafficFacilityInfo) {

    }

    @Override
    public void updateAimlessModeStatistics(AimLessModeStat aimLessModeStat) {

    }

    @Override
    public void updateAimlessModeCongestionInfo(AimLessModeCongestionInfo aimLessModeCongestionInfo) {

    }

    @Override
    public void onPlayRing(int i) {

    }

    @Override
    public void onCalculateRouteSuccess(AMapCalcRouteResult aMapCalcRouteResult) {
        aMapNavi.startNavi(NaviType.GPS);
    }

    @Override
    public void onCalculateRouteFailure(AMapCalcRouteResult aMapCalcRouteResult) {

    }

    @Override
    public void onNaviRouteNotify(AMapNaviRouteNotifyData aMapNaviRouteNotifyData) {

    }

    @Override
    public void onGpsSignalWeak(boolean b) {

    }

    @Override
    public void onMethodCall(@NonNull MethodCall call, @NonNull MethodChannel.Result result) {
        String method = call.method;
        Map<String, Object> request = (Map<String, Object>) call.arguments;
        switch (method) {
            case "search":
                keyWord = (String) request.get("keyWord");
                break;
            default:
                break;
        }
    }
}

package com.stack.amap_flutter_navi_view;

import android.app.Activity;

import androidx.annotation.NonNull;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.embedding.engine.plugins.activity.ActivityAware;
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding;
import io.flutter.plugin.common.BinaryMessenger;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;

/**
 * AmapFlutterNaviViewPlugin
 */
public class AmapFlutterNaviViewPlugin implements FlutterPlugin, MethodCallHandler, ActivityAware {
    /// The MethodChannel that will the communication between Flutter and native Android
    ///
    /// This local reference serves to register the plugin with the Flutter Engine and unregister it
    /// when the Flutter Engine is detached from the Activity
    private MethodChannel channel;
    private FlutterPluginBinding pluginBinding;

    private ActivityPluginBinding activityPluginBinding;
    private final String viewType = "amap-navi";

    @Override
    public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {
        pluginBinding = flutterPluginBinding;

    }

    @Override
    public void onMethodCall(@NonNull MethodCall call, @NonNull Result result) {
        if (call.method.equals("getPlatformVersion")) {
            result.success("Android " + android.os.Build.VERSION.RELEASE);
        } else {
            result.notImplemented();
        }
    }

    @Override
    public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
        channel.setMethodCallHandler(null);
    }

    @Override
    public void onDetachedFromActivityForConfigChanges() {

    }

    @Override
    public void onReattachedToActivityForConfigChanges(@NonNull ActivityPluginBinding binding) {

    }

    @Override
    public void onDetachedFromActivity() {
        tearDown();
    }

    private void tearDown() {
        activityPluginBinding = null;
        channel.setMethodCallHandler(null);
    }

    @Override
    public void onAttachedToActivity(@NonNull ActivityPluginBinding binding) {
        activityPluginBinding = binding;
        BinaryMessenger messenger = pluginBinding.getBinaryMessenger();

        channel = new MethodChannel(messenger, viewType);
        channel.setMethodCallHandler(new MethodChannel.MethodCallHandler() {

            @Override
            public void onMethodCall(@NonNull MethodCall call, @NonNull Result result) {
                String method = call.method;
                switch (method) {
                    case "updatePrivacy":
                        boolean isAgree = "true".equals(call.arguments);
                        break;
                    case "getAmapMsg":

                        break;
                    default:
                        break;
                }
            }
        });

        NativeViewFactory nativeViewFactory = new NativeViewFactory(messenger,channel);
        pluginBinding
                .getPlatformViewRegistry()
                .registerViewFactory(viewType, nativeViewFactory);


    }
}

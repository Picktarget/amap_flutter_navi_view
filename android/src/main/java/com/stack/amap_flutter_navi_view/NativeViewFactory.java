package com.stack.amap_flutter_navi_view;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;

import com.amap.api.maps.AMapException;

import io.flutter.plugin.common.BinaryMessenger;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.StandardMessageCodec;
import io.flutter.plugin.platform.PlatformView;
import io.flutter.plugin.platform.PlatformViewFactory;

import java.util.Map;

class NativeViewFactory extends PlatformViewFactory {
    private NativeView nativeView;
    private final BinaryMessenger messenger;
    private final MethodChannel channel;

    NativeViewFactory(BinaryMessenger messenger, MethodChannel channel) {
        super(StandardMessageCodec.INSTANCE);
        this.messenger = messenger;
        this.channel = channel;
    }

    @NonNull
    @Override
    public PlatformView create(@NonNull Context context, int id, @Nullable Object args) {
    final Map<String, Object> creationParams = (Map<String, Object>) args;
        try {
            nativeView = new NativeView(context,messenger,id,creationParams,channel);
        } catch (AMapException e) {
            throw new RuntimeException(e);
        }
        return nativeView;
    }
}

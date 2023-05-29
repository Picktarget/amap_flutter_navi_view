import 'package:plugin_platform_interface/plugin_platform_interface.dart';

import 'amap_flutter_navi_view_method_channel.dart';

abstract class AmapFlutterNaviViewPlatform extends PlatformInterface {
  /// Constructs a AmapFlutterNaviViewPlatform.
  AmapFlutterNaviViewPlatform() : super(token: _token);

  static final Object _token = Object();

  static AmapFlutterNaviViewPlatform _instance = MethodChannelAmapFlutterNaviView();

  /// The default instance of [AmapFlutterNaviViewPlatform] to use.
  ///
  /// Defaults to [MethodChannelAmapFlutterNaviView].
  static AmapFlutterNaviViewPlatform get instance => _instance;

  /// Platform-specific implementations should set this with their own
  /// platform-specific class that extends [AmapFlutterNaviViewPlatform] when
  /// they register themselves.
  static set instance(AmapFlutterNaviViewPlatform instance) {
    PlatformInterface.verifyToken(instance, _token);
    _instance = instance;
  }

  Future<String?> getPlatformVersion() {
    throw UnimplementedError('platformVersion() has not been implemented.');
  }
}

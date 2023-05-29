import 'package:flutter/foundation.dart';
import 'package:flutter/services.dart';

import 'amap_flutter_navi_view_platform_interface.dart';

/// An implementation of [AmapFlutterNaviViewPlatform] that uses method channels.
class MethodChannelAmapFlutterNaviView extends AmapFlutterNaviViewPlatform {
  /// The method channel used to interact with the native platform.
  @visibleForTesting
  final methodChannel = const MethodChannel('amap_flutter_navi_view');

  @override
  Future<String?> getPlatformVersion() async {
    final version = await methodChannel.invokeMethod<String>('getPlatformVersion');
    return version;
  }
}

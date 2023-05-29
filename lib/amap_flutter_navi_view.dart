
import 'amap_flutter_navi_view_platform_interface.dart';

class AmapFlutterNaviView {
  Future<String?> getPlatformVersion() {
    return AmapFlutterNaviViewPlatform.instance.getPlatformVersion();
  }
}

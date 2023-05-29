import 'package:flutter_test/flutter_test.dart';
import 'package:amap_flutter_navi_view/amap_flutter_navi_view.dart';
import 'package:amap_flutter_navi_view/amap_flutter_navi_view_platform_interface.dart';
import 'package:amap_flutter_navi_view/amap_flutter_navi_view_method_channel.dart';
import 'package:plugin_platform_interface/plugin_platform_interface.dart';

class MockAmapFlutterNaviViewPlatform
    with MockPlatformInterfaceMixin
    implements AmapFlutterNaviViewPlatform {

  @override
  Future<String?> getPlatformVersion() => Future.value('42');
}

void main() {
  final AmapFlutterNaviViewPlatform initialPlatform = AmapFlutterNaviViewPlatform.instance;

  test('$MethodChannelAmapFlutterNaviView is the default instance', () {
    expect(initialPlatform, isInstanceOf<MethodChannelAmapFlutterNaviView>());
  });

  test('getPlatformVersion', () async {
    AmapFlutterNaviView amapFlutterNaviViewPlugin = AmapFlutterNaviView();
    MockAmapFlutterNaviViewPlatform fakePlatform = MockAmapFlutterNaviViewPlatform();
    AmapFlutterNaviViewPlatform.instance = fakePlatform;

    expect(await amapFlutterNaviViewPlugin.getPlatformVersion(), '42');
  });
}

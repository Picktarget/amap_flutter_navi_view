import 'package:flutter/services.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:amap_flutter_navi_view/amap_flutter_navi_view_method_channel.dart';

void main() {
  MethodChannelAmapFlutterNaviView platform = MethodChannelAmapFlutterNaviView();
  const MethodChannel channel = MethodChannel('amap_flutter_navi_view');

  TestWidgetsFlutterBinding.ensureInitialized();

  setUp(() {
    channel.setMockMethodCallHandler((MethodCall methodCall) async {
      return '42';
    });
  });

  tearDown(() {
    channel.setMockMethodCallHandler(null);
  });

  test('getPlatformVersion', () async {
    expect(await platform.getPlatformVersion(), '42');
  });
}

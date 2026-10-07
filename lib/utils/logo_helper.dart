import 'dart:io';
import 'package:flutter/material.dart';
import 'package:shared_preferences/shared_preferences.dart';

class LogoHelper {
  static const String prefsKey = 'custom_logo_path';
  static final ValueNotifier<String?> customLogoNotifier = ValueNotifier<String?>(null);
  static bool _initialized = false;

  static Future<void> init() async {
    try {
      final prefs = await SharedPreferences.getInstance();
      final savedPath = prefs.getString(prefsKey);
      if (savedPath != null && File(savedPath).existsSync()) {
        customLogoNotifier.value = savedPath;
      } else {
        customLogoNotifier.value = null;
      }
      _initialized = true;
    } catch (_) {}
  }

  static Future<void> setCustomLogo(String path) async {
    try {
      final prefs = await SharedPreferences.getInstance();
      await prefs.setString(prefsKey, path);
      customLogoNotifier.value = path;
    } catch (_) {}
  }

  static Future<void> resetLogo() async {
    try {
      final prefs = await SharedPreferences.getInstance();
      await prefs.remove(prefsKey);
      customLogoNotifier.value = null;
    } catch (_) {}
  }

  static ImageProvider getAppLogo() {
    final path = customLogoNotifier.value;
    if (path != null && File(path).existsSync()) {
      return FileImage(File(path));
    }
    return const AssetImage('assets/logo.png');
  }
}

class AppLogo extends StatefulWidget {
  final double? width;
  final double? height;
  final BoxFit fit;

  const AppLogo({
    Key? key,
    this.width,
    this.height,
    this.fit = BoxFit.contain,
  }) : super(key: key);

  @override
  State<AppLogo> createState() => _AppLogoState();
}

class _AppLogoState extends State<AppLogo> {
  @override
  void initState() {
    super.initState();
    if (!LogoHelper._initialized) {
      LogoHelper.init().then((_) {
        if (mounted) setState(() {});
      });
    }
  }

  @override
  Widget build(BuildContext context) {
    return ValueListenableBuilder<String?>(
      valueListenable: LogoHelper.customLogoNotifier,
      builder: (context, customPath, _) {
        if (customPath != null && File(customPath).existsSync()) {
          return Image.file(
            File(customPath),
            width: widget.width,
            height: widget.height,
            fit: widget.fit,
            errorBuilder: (context, error, stackTrace) {
              return _buildDefaultAsset();
            },
          );
        }
        return _buildDefaultAsset();
      },
    );
  }

  Widget _buildDefaultAsset() {
    return Image.asset(
      'assets/logo.png',
      width: widget.width,
      height: widget.height,
      fit: widget.fit,
      errorBuilder: (context, error, stackTrace) {
        return Icon(
          Icons.directions_car,
          size: widget.height ?? 28,
          color: const Color(0xFF0D1B68),
        );
      },
    );
  }
}

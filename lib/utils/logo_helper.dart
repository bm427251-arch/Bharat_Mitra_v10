import 'dart:io';
import 'package:flutter/material.dart';
import 'package:shared_preferences/shared_preferences.dart';

class LogoHelper {
  static const String _key = 'custom_logo_path';
  static Future<void> init() async {
    try {
      final prefs = await SharedPreferences.getInstance();
      final customPath = prefs.getString(_key);
      if (customPath != null) {
        final file = File(customPath);
        if (file.existsSync()) {
          file.deleteSync();
        }
        await prefs.remove(_key);
      }
    } catch (_) {}
  }
  static Widget getLogo({double size = 80}) {
    return Icon(Icons.verified_user, size: size, color: Colors.orange);
  }
  static Future<String?> getCustomLogoPath() async {
    final prefs = await SharedPreferences.getInstance();
    return prefs.getString(_key);
  }
  static Future<void> saveLogoPath(String path) async {
    final prefs = await SharedPreferences.getInstance();
    await prefs.setString(_key, path);
  }
  static Future<void> clearLogoPath() async {
    final prefs = await SharedPreferences.getInstance();
    await prefs.remove(_key);
  }

  // Compatibility aliases
  static Future<void> setCustomLogo(String path) => saveLogoPath(path);
  static Future<void> resetLogo() => clearLogoPath();
}

class AppLogo extends StatelessWidget {
  final double? width;
  final double? height;
  final BoxFit fit;

  const AppLogo({
    super.key,
    this.width,
    this.height,
    this.fit = BoxFit.contain,
  });

  @override
  Widget build(BuildContext context) {
    return FutureBuilder<String?>(
      future: LogoHelper.getCustomLogoPath(),
      builder: (context, snapshot) {
        final path = snapshot.data;
        if (path != null && File(path).existsSync()) {
          return Image.file(
            File(path),
            width: width,
            height: height,
            fit: fit,
            errorBuilder: (context, error, stackTrace) => _buildDefault(),
          );
        }
        return _buildDefault();
      },
    );
  }

  Widget _buildDefault() {
    return Image.asset(
      'assets/logo.png',
      width: width,
      height: height,
      fit: fit,
      errorBuilder: (context, error, stackTrace) => LogoHelper.getLogo(size: height ?? 28),
    );
  }
}

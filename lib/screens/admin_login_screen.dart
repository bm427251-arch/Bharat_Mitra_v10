import 'dart:io';
import 'package:flutter/material.dart';
import 'package:shared_preferences/shared_preferences.dart';

class LogoHelper {
  static const String _key = 'custom_logo_path';

  static Future<String?> getCustomLogoPath() async {
    try {
      final prefs = await SharedPreferences.getInstance();
      final path = prefs.getString(_key);
      if (path != null && await File(path).exists()) {
        return path;
      }
    } catch (_) {}
    return null;
  }

  static Future<void> saveLogoPath(String path) async {
    final prefs = await SharedPreferences.getInstance();
    await prefs.setString(_key, path);
  }

  static Future<void> clearLogoPath() async {
    final prefs = await SharedPreferences.getInstance();
    await prefs.remove(_key);
  }
}

class AppLogo extends StatefulWidget {
  final double width;
  final double height;
  final BoxFit fit;
  const AppLogo({super.key, this.width = 140, this.height = 48, this.fit = BoxFit.contain});

  @override
  State<AppLogo> createState() => _AppLogoState();
}

class _AppLogoState extends State<AppLogo> {
  String? _customPath;

  @override
  void initState() {
    super.initState();
    _load();
  }

  Future<void> _load() async {
    final p = await LogoHelper.getCustomLogoPath();
    if (mounted) setState(() => _customPath = p);
  }

  @override
  Widget build(BuildContext context) {
    if (_customPath != null) {
      return Image.file(
        File(_customPath!),
        width: widget.width,
        height: widget.height,
        fit: widget.fit,
        errorBuilder: (c, e, s) => Image.asset('assets/logo.png', width: widget.width, height: widget.height, fit: widget.fit,
            errorBuilder: (c, e, s) => const Icon(Icons.shield, color: Colors.white, size: 32)),
      );
    }
    return Image.asset('assets/logo.png',
        width: widget.width,
        height: widget.height,
        fit: widget.fit,
        errorBuilder: (c, e, s) => const Icon(Icons.shield, color: Colors.white, size: 32));
  }
}

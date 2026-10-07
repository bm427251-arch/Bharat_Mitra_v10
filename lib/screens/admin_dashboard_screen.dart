import 'package:flutter/material.dart';
import 'package:image_picker/image_picker.dart';
import 'package:bharat_mitra_v10/utils/logo_helper.dart';

class AdminDashboardScreen extends StatefulWidget {
  const AdminDashboardScreen({super.key});

  @override
  State<AdminDashboardScreen> createState() => _AdminDashboardScreenState();
}

class _AdminDashboardScreenState extends State<AdminDashboardScreen> {
  String? logoPath;

  @override
  void initState() {
    super.initState();
    _loadLogo();
  }

  Future<void> _loadLogo() async {
    final path = await LogoHelper.getCustomLogoPath();
    setState(() => logoPath = path);
  }

  Future<void> _pickLogo() async {
    final picker = ImagePicker();
    final picked = await picker.pickImage(source: ImageSource.gallery);
    if (picked != null) {
      await LogoHelper.saveLogoPath(picked.path);
      setState(() => logoPath = picked.path);
    }
  }

  Future<void> _clearLogo() async {
    await LogoHelper.clearLogoPath();
    setState(() => logoPath = null);
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text('Admin Dashboard')),
      body: Column(
        children: [
          const SizedBox(height: 20),
          LogoHelper.getLogo(size: 100),
          const SizedBox(height: 20),
          if (logoPath != null) Text('Logo: $logoPath'),
          Row(
            mainAxisAlignment: MainAxisAlignment.center,
            children: [
              ElevatedButton(onPressed: _pickLogo, child: const Text('Pick Logo')),
              const SizedBox(width: 10),
              ElevatedButton(onPressed: _clearLogo, child: const Text('Clear Logo')),
            ],
          ),
        ],
      ),
    );
  }
}

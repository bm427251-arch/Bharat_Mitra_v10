import 'dart:io';
import 'package:flutter/material.dart';
import 'package:image_picker/image_picker.dart';
import 'package:shared_preferences/shared_preferences.dart';
import 'package:path_provider/path_provider.dart';
import '../utils/logo_helper.dart';

class AdminDashboardScreen extends StatefulWidget {
  const AdminDashboardScreen({super.key});
  @override
  State<AdminDashboardScreen> createState() => _AdminDashboardScreenState();
}

class _AdminDashboardScreenState extends State<AdminDashboardScreen> {
  String? _customLogoPath;

  @override
  void initState() {
    super.initState();
    _loadLogo();
  }

  Future<void> _loadLogo() async {
    final path = await LogoHelper.getCustomLogoPath();
    if (mounted) setState(() => _customLogoPath = path);
  }

  Future<void> _changeLogo() async {
    try {
      final picker = ImagePicker();
      final picked = await picker.pickImage(source: ImageSource.gallery, imageQuality: 85);
      if (picked == null) return;
      final dir = await getApplicationDocumentsDirectory();
      final saved = await File(picked.path).copy('${dir.path}/app_logo.png');
      await LogoHelper.saveLogoPath(saved.path);
      setState(() => _customLogoPath = saved.path);
      if (mounted) ScaffoldMessenger.of(context).showSnackBar(const SnackBar(content: Text('Logo Updated'), backgroundColor: Colors.green));
    } catch (e) {
      if (mounted) ScaffoldMessenger.of(context).showSnackBar(SnackBar(content: Text('Error: $e')));
    }
  }

  Future<void> _resetLogo() async {
    await LogoHelper.clearLogoPath();
    if (_customLogoPath != null) {
      try { await File(_customLogoPath!).delete(); } catch (_) {}
    }
    setState(() => _customLogoPath = null);
    if (mounted) ScaffoldMessenger.of(context).showSnackBar(const SnackBar(content: Text('Logo Reset to Default')));
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text('Admin Dashboard'), backgroundColor: const Color(0xFF0D1B68)),
      body: SingleChildScrollView(
        padding: const EdgeInsets.all(16),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Row(children: [
              Expanded(child: _card('Pending', '2', Colors.orange)),
              const SizedBox(width: 12),
              Expanded(child: _card('Blocked', '1', Colors.red)),
            ]),
            const SizedBox(height: 20),
            Card(
              elevation: 2,
              child: Padding(
                padding: const EdgeInsets.all(16),
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    const Text('App Branding', style: TextStyle(fontSize: 18, fontWeight: FontWeight.bold)),
                    const SizedBox(height: 12),
                    Center(
                      child: Container(
                        height: 80, width: 80,
                        decoration: BoxDecoration(border: Border.all(color: Colors.grey.shade300), borderRadius: BorderRadius.circular(10)),
                        child: _customLogoPath != null
                            ? Image.file(File(_customLogoPath!), fit: BoxFit.contain)
                            : Image.asset('assets/logo.png', fit: BoxFit.contain, errorBuilder: (c,e,s) => const Icon(Icons.shield, size: 40, color: Color(0xFF0D1B68))),
                      ),
                    ),
                    const SizedBox(height: 12),
                    Row(mainAxisAlignment: MainAxisAlignment.center, children: [
                      ElevatedButton.icon(onPressed: _changeLogo, icon: const Icon(Icons.upload), label: const Text('Change Logo')),
                      const SizedBox(width: 10),
                      OutlinedButton(onPressed: _resetLogo, child: const Text('Reset')),
                    ])
                  ],
                ),
              ),
            ),
            const SizedBox(height: 20),
            const Text('Bottom Nav Fix: Elite ₹29 -> Elite', style: TextStyle(fontSize: 12, color: Colors.grey)),
          ],
        ),
      ),
      bottomNavigationBar: BottomNavigationBar(
        type: BottomNavigationBarType.fixed,
        selectedItemColor: const Color(0xFF0D1B68),
        items: const [
          BottomNavigationBarItem(icon: Icon(Icons.home), label: 'Home'),
          BottomNavigationBarItem(icon: Icon(Icons.car_rental), label: 'Rent Car'),
          BottomNavigationBarItem(icon: Icon(Icons.person), label: 'Hire Driver'),
          BottomNavigationBarItem(icon: Icon(Icons.shield), label: 'Elite'),
        ],
        currentIndex: 0,
        onTap: (i) {},
      ),
    );
  }

  Widget _card(String title, String count, Color color) {
    return Card(color: color.withOpacity(0.1), child: Padding(padding: const EdgeInsets.all(16), child: Column(children: [Text(count, style: TextStyle(fontSize: 26, fontWeight: FontWeight.bold, color: color)), Text(title)])));
  }
}

import 'dart:io';
import 'package:flutter/material.dart';
import 'package:image_picker/image_picker.dart';
import 'package:shared_preferences/shared_preferences.dart';
import 'package:path_provider/path_provider.dart';
import '../utils/logo_helper.dart';

class AdminDashboardScreen extends StatefulWidget {
  const AdminDashboardScreen({Key? key}) : super(key: key);

  @override
  State<AdminDashboardScreen> createState() => _AdminDashboardScreenState();
}

class _AdminDashboardScreenState extends State<AdminDashboardScreen> {
  int _driversCount = 24;
  int _verifiedCount = 22;
  int _pendingCount = 2;
  int _blockedCount = 1;
  int _silentPushQueue = 1;
  String? _customLogoPath;

  final List<Map<String, dynamic>> _pendingDrivers = [
    {'name': 'Subhashish Mondal', 'type': 'Hire Driver (DL Only)', 'phone': '9831092812', 'status': 'Pending'},
    {'name': 'Rajib Banerjee', 'type': 'Rent Owner (Ambulance)', 'phone': '9830129841', 'status': 'Pending'},
  ];

  @override
  void initState() {
    super.initState();
    _loadCustomLogo();
  }

  Future<void> _loadCustomLogo() async {
    try {
      final prefs = await SharedPreferences.getInstance();
      final path = prefs.getString('custom_logo_path');
      if (path != null && File(path).existsSync()) {
        setState(() {
          _customLogoPath = path;
        });
      }
    } catch (_) {}
  }

  Future<void> _changeLogo() async {
    try {
      final picker = ImagePicker();
      final picked = await picker.pickImage(source: ImageSource.gallery, imageQuality: 85);
      if (picked != null) {
        final appDir = await getApplicationDocumentsDirectory();
        final savedFile = await File(picked.path).copy('${appDir.path}/app_logo.png');
        final prefs = await SharedPreferences.getInstance();
        await prefs.setString('custom_logo_path', savedFile.path);
        await LogoHelper.setCustomLogo(savedFile.path);
        setState(() {
          _customLogoPath = savedFile.path;
        });
        if (mounted) {
          ScaffoldMessenger.of(context).showSnackBar(
            const SnackBar(
              content: Text('Logo Changed Successfully - Updated Across Entire App!'),
              backgroundColor: Color(0xFF2E8B57),
            ),
          );
        }
      }
    } catch (e) {
      if (mounted) {
        ScaffoldMessenger.of(context).showSnackBar(
          SnackBar(content: Text('Failed to pick logo: $e')),
        );
      }
    }
  }

  Future<void> _resetLogo() async {
    try {
      final prefs = await SharedPreferences.getInstance();
      await prefs.remove('custom_logo_path');
      await LogoHelper.resetLogo();
      setState(() {
        _customLogoPath = null;
      });
      if (mounted) {
        ScaffoldMessenger.of(context).showSnackBar(
          const SnackBar(
            content: Text('Logo Reset to Default assets/logo.png'),
            backgroundColor: Color(0xFF0D1B68),
          ),
        );
      }
    } catch (_) {}
  }

  Widget _buildLogoPreview(double width, double height) {
    if (_customLogoPath != null && File(_customLogoPath!).existsSync()) {
      return Image.file(
        File(_customLogoPath!),
        width: width,
        height: height,
        fit: BoxFit.contain,
        errorBuilder: (c, e, s) => Image.asset(
          'assets/logo.png',
          width: width,
          height: height,
          fit: BoxFit.contain,
          errorBuilder: (c2, e2, s2) => Icon(Icons.image, size: height, color: const Color(0xFF0D1B68)),
        ),
      );
    }
    return Image.asset(
      'assets/logo.png',
      width: width,
      height: height,
      fit: BoxFit.contain,
      errorBuilder: (c, e, s) => Icon(Icons.image, size: height, color: const Color(0xFF0D1B68)),
    );
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: const Color(0xFFF8FAFC),
      appBar: AppBar(
        backgroundColor: const Color(0xFF0D1B68),
        title: Row(
          mainAxisAlignment: MainAxisAlignment.spaceBetween,
          children: [
            const Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              mainAxisSize: MainAxisSize.min,
              children: [
                Text('Admin Dashboard', style: TextStyle(fontWeight: FontWeight.bold, fontSize: 16, color: Colors.white)),
                Text('INTERNAL USE ONLY • SECURE DESK', style: TextStyle(fontSize: 10, color: Color(0xFFFFB366), fontWeight: FontWeight.bold)),
              ],
            ),
            const AppLogo(width: 90, height: 30, fit: BoxFit.contain),
          ],
        ),
      ),
      body: SingleChildScrollView(
        padding: const EdgeInsets.all(16.0),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            // Top Overview Stats (Drivers 24, Verified 22, Pending 2, Blocked 1)
            Row(
              children: [
                Expanded(child: _buildStatCard('Total Drivers', '$_driversCount', Icons.people, const Color(0xFF0D1B68))),
                const SizedBox(width: 8),
                Expanded(child: _buildStatCard('Verified', '$_verifiedCount', Icons.verified, const Color(0xFF2E8B57))),
                const SizedBox(width: 8),
                Expanded(child: _buildStatCard('Pending', '$_pendingCount', Icons.pending_actions, const Color(0xFFFF8C00))),
                const SizedBox(width: 8),
                Expanded(child: _buildStatCard('Blocked', '$_blockedCount', Icons.block, Colors.red)),
              ],
            ),
            const SizedBox(height: 16),

            // Internal Earnings & 15% Commission Split
            Container(
              width: double.infinity,
              padding: const EdgeInsets.all(16),
              decoration: BoxDecoration(
                color: const Color(0xFF0D1B68),
                borderRadius: BorderRadius.circular(16),
                boxShadow: [BoxShadow(color: Colors.black12, blurRadius: 8)],
              ),
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  Row(
                    mainAxisAlignment: MainAxisAlignment.spaceBetween,
                    children: const [
                      Text('Platform Earnings Today', style: TextStyle(color: Colors.white70, fontSize: 13)),
                      Chip(
                        label: Text('INTERNAL 15%', style: TextStyle(color: Colors.white, fontSize: 10, fontWeight: FontWeight.bold)),
                        backgroundColor: Color(0xFFFF8C00),
                      ),
                    ],
                  ),
                  const SizedBox(height: 4),
                  const Text('₹1,500', style: TextStyle(fontSize: 32, fontWeight: FontWeight.w900, color: Colors.white)),
                  const SizedBox(height: 12),
                  const Divider(color: Colors.white24),
                  const SizedBox(height: 8),
                  Row(
                    mainAxisAlignment: MainAxisAlignment.spaceBetween,
                    children: const [
                      Column(
                        crossAxisAlignment: CrossAxisAlignment.start,
                        children: [
                          Text('Company (15% Commission)', style: TextStyle(color: Color(0xFFFFB366), fontSize: 11)),
                          Text('₹225', style: TextStyle(color: Colors.white, fontWeight: FontWeight.bold, fontSize: 16)),
                        ],
                      ),
                      Column(
                        crossAxisAlignment: CrossAxisAlignment.end,
                        children: [
                          Text('Driver Payout (85%)', style: TextStyle(color: Color(0xFF81C784), fontSize: 11)),
                          Text('₹1,275', style: TextStyle(color: Colors.white, fontWeight: FontWeight.bold, fontSize: 16)),
                        ],
                      ),
                    ],
                  ),
                ],
              ),
            ),
            const SizedBox(height: 14),

            // App Branding & Dynamic Logo Management Card
            Card(
              shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(14)),
              elevation: 2,
              child: Padding(
                padding: const EdgeInsets.all(14.0),
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    Row(
                      children: [
                        Container(
                          width: 52,
                          height: 52,
                          padding: const EdgeInsets.all(6),
                          decoration: BoxDecoration(
                            color: const Color(0xFF0D1B68).withOpacity(0.08),
                            borderRadius: BorderRadius.circular(10),
                            border: Border.all(color: Colors.black12),
                          ),
                          child: _buildLogoPreview(40, 40),
                        ),
                        const SizedBox(width: 12),
                        Expanded(
                          child: Column(
                            crossAxisAlignment: CrossAxisAlignment.start,
                            children: [
                              const Text(
                                'App Branding & Logo',
                                style: TextStyle(fontWeight: FontWeight.bold, fontSize: 14, color: Color(0xFF0D1B68)),
                              ),
                              const SizedBox(height: 2),
                              Text(
                                _customLogoPath != null
                                    ? 'Custom logo active • ${_customLogoPath!.split('/').last}'
                                    : 'Default branding • assets/logo.png',
                                style: TextStyle(fontSize: 11, color: _customLogoPath != null ? const Color(0xFF2E8B57) : Colors.grey.shade600),
                              ),
                            ],
                          ),
                        ),
                      ],
                    ),
                    const SizedBox(height: 12),
                    Row(
                      children: [
                        Expanded(
                          child: ElevatedButton.icon(
                            icon: const Icon(Icons.photo_library, size: 16, color: Colors.white),
                            label: const Text('Change Logo', style: TextStyle(color: Colors.white, fontSize: 12, fontWeight: FontWeight.bold)),
                            style: ElevatedButton.styleFrom(
                              backgroundColor: const Color(0xFF0D1B68),
                              shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(8)),
                              padding: const EdgeInsets.symmetric(vertical: 10),
                            ),
                            onPressed: _changeLogo,
                          ),
                        ),
                        const SizedBox(width: 8),
                        OutlinedButton.icon(
                          icon: const Icon(Icons.refresh, size: 16, color: Colors.red),
                          label: const Text('Reset to Default', style: TextStyle(color: Colors.red, fontSize: 12, fontWeight: FontWeight.bold)),
                          style: OutlinedButton.styleFrom(
                            foregroundColor: Colors.red,
                            side: const BorderSide(color: Colors.red),
                            shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(8)),
                            padding: const EdgeInsets.symmetric(vertical: 10, horizontal: 12),
                          ),
                          onPressed: _resetLogo,
                        ),
                      ],
                    ),
                  ],
                ),
              ),
            ),
            const SizedBox(height: 14),

            // Silent Push Notifications Queue (7 seconds silent alert)
            Container(
              padding: const EdgeInsets.all(12),
              decoration: BoxDecoration(
                color: const Color(0xFFFF8C00).withOpacity(0.12),
                borderRadius: BorderRadius.circular(12),
                border: Border.all(color: const Color(0xFFFF8C00)),
              ),
              child: Row(
                children: [
                  const Icon(Icons.notifications_active, color: Color(0xFFFF8C00)),
                  const SizedBox(width: 10),
                  Expanded(
                    child: Column(
                      crossAxisAlignment: CrossAxisAlignment.start,
                      children: [
                        Text('Silent Push Queue ($_silentPushQueue Pending)', style: const TextStyle(fontWeight: FontWeight.bold, fontSize: 13, color: Color(0xFF0D1B68))),
                        const Text('Silent dispatch within 7 seconds: "Overspeed: Driver Rohan 75 km/h"', style: TextStyle(fontSize: 11, color: Colors.black87)),
                      ],
                    ),
                  ),
                  IconButton(
                    icon: const Icon(Icons.check, color: Color(0xFF2E8B57)),
                    onPressed: () {
                      setState(() => _silentPushQueue = 0);
                      ScaffoldMessenger.of(context).showSnackBar(const SnackBar(content: Text('Cleared silent push queue.')));
                    },
                  ),
                ],
              ),
            ),
            const SizedBox(height: 16),

            // Driver Verification & Block Controls
            const Text('Driver Partner Management', style: TextStyle(fontWeight: FontWeight.bold, fontSize: 15, color: Color(0xFF0D1B68))),
            const SizedBox(height: 10),

            ListView.separated(
              shrinkWrap: true,
              physics: const NeverScrollableScrollPhysics(),
              itemCount: _pendingDrivers.length,
              separatorBuilder: (_, __) => const SizedBox(height: 8),
              itemBuilder: (context, index) {
                final d = _pendingDrivers[index];
                return Card(
                  shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(12)),
                  elevation: 2,
                  child: Padding(
                    padding: const EdgeInsets.all(12.0),
                    child: Row(
                      children: [
                        CircleAvatar(
                          backgroundColor: const Color(0xFF0D1B68).withOpacity(0.12),
                          child: const Icon(Icons.badge, color: Color(0xFF0D1B68)),
                        ),
                        const SizedBox(width: 10),
                        Expanded(
                          child: Column(
                            crossAxisAlignment: CrossAxisAlignment.start,
                            children: [
                              Text(d['name'] as String, style: const TextStyle(fontWeight: FontWeight.bold, fontSize: 13)),
                              Text('${d['type']} • ${d['phone']}', style: const TextStyle(fontSize: 11, color: Colors.grey)),
                            ],
                          ),
                        ),
                        ElevatedButton(
                          onPressed: () {
                            setState(() {
                              _verifiedCount++;
                              _pendingCount--;
                              _pendingDrivers.removeAt(index);
                            });
                            ScaffoldMessenger.of(context).showSnackBar(
                              SnackBar(content: Text('Approved ${d['name']}!')),
                            );
                          },
                          style: ElevatedButton.styleFrom(
                            backgroundColor: const Color(0xFF2E8B57),
                            padding: const EdgeInsets.symmetric(horizontal: 10),
                          ),
                          child: const Text('Approve', style: TextStyle(color: Colors.white, fontSize: 11)),
                        ),
                        const SizedBox(width: 4),
                        OutlinedButton(
                          onPressed: () {
                            setState(() {
                              _blockedCount++;
                              _pendingCount--;
                              _pendingDrivers.removeAt(index);
                            });
                            ScaffoldMessenger.of(context).showSnackBar(
                              SnackBar(content: Text('Blocked ${d['name']} from network.')),
                            );
                          },
                          style: OutlinedButton.styleFrom(
                            foregroundColor: Colors.red,
                            side: const BorderSide(color: Colors.red),
                            padding: const EdgeInsets.symmetric(horizontal: 10),
                          ),
                          child: const Text('Block', style: TextStyle(fontSize: 11)),
                        ),
                      ],
                    ),
                  ),
                );
              },
            ),
          ],
        ),
      ),
    );
  }

  Widget _buildStatCard(String label, String value, IconData icon, Color color) {
    return Container(
      padding: const EdgeInsets.symmetric(vertical: 12, horizontal: 8),
      decoration: BoxDecoration(
        color: Colors.white,
        borderRadius: BorderRadius.circular(12),
        border: Border.all(color: Colors.black12),
      ),
      child: Column(
        children: [
          Icon(icon, color: color, size: 20),
          const SizedBox(height: 4),
          Text(value, style: TextStyle(fontWeight: FontWeight.bold, fontSize: 16, color: color)),
          Text(label, style: const TextStyle(fontSize: 9, color: Colors.grey)),
        ],
      ),
    );
  }
}

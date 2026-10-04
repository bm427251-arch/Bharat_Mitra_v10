import 'package:flutter/material.dart';

class EliteScreen extends StatelessWidget {
  const EliteScreen({super.key});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        backgroundColor: const Color(0xFF0D1B68),
        title: const Text(
          'Bharat Mitra Elite',
          style: TextStyle(color: Colors.white, fontWeight: FontWeight.bold),
        ),
      ),
      body: SingleChildScrollView(
        padding: const EdgeInsets.all(20),
        child: Column(
          children: [
            Container(
              padding: const EdgeInsets.all(20),
              decoration: BoxDecoration(
                gradient: const LinearGradient(
                  colors: [Color(0xFFFA8520), Color(0xFFD96E14)],
                  begin: Alignment.topLeft,
                  end: Alignment.bottomRight,
                ),
                borderRadius: BorderRadius.circular(16),
                boxShadow: [
                  BoxShadow(
                    color: Colors.black.withOpacity(0.1),
                    blurRadius: 10,
                    offset: const Offset(0, 4),
                  ),
                ],
              ),
              child: Column(
                children: const [
                  Icon(Icons.star, color: Colors.white, size: 48),
                  SizedBox(height: 12),
                  Text(
                    'Elite VIP Membership',
                    style: TextStyle(fontSize: 22, fontWeight: FontWeight.bold, color: Colors.white),
                  ),
                  SizedBox(height: 6),
                  Text(
                    'Active Safety & Verification Network',
                    style: TextStyle(fontSize: 16, color: Colors.white, fontWeight: FontWeight.w600),
                  ),
                  SizedBox(height: 12),
                  Text(
                    '✓ Zero Booking Surcharge\n✓ 24x7 Priority SOS Response\n✓ Direct Driver Line\n✓ Elite Profile Verification Badge',
                    textAlign: TextAlign.center,
                    style: TextStyle(color: Colors.white, height: 1.5),
                  ),
                ],
              ),
            ),
            const SizedBox(height: 24),
            ElevatedButton(
              onPressed: () {},
              style: ElevatedButton.styleFrom(
                backgroundColor: const Color(0xFF0D1B68),
                foregroundColor: Colors.white,
                minimumSize: const Size(double.infinity, 50),
                shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(10)),
              ),
              child: const Text('Upgrade to Elite Membership', style: TextStyle(fontSize: 16)),
            ),
          ],
        ),
      ),
    );
  }
}

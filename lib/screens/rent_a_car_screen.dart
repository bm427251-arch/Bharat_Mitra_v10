import 'package:flutter/material.dart';

class RentACarScreen extends StatelessWidget {
  const RentACarScreen({super.key});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        backgroundColor: const Color(0xFF0D1B68),
        title: const Text(
          'Rent A Car',
          style: TextStyle(color: Colors.white, fontWeight: FontWeight.bold),
        ),
      ),
      body: ListView(
        padding: const EdgeInsets.all(16),
        children: [
          _buildCarCard('Swift Dzire (Sedan)', '₹1,800/day • Unlimited Kms • AC', 'Available in Barasat / Kolkata'),
          _buildCarCard('Innova Crysta (7-Seater)', '₹3,200/day • Outstation • AC', 'Ideal for North Bengal / Digha Trips'),
          _buildCarCard('Mahindra Thar 4x4', '₹4,500/day • Adventure Special', 'Top Condition • Insured'),
        ],
      ),
    );
  }

  Widget _buildCarCard(String title, String specs, String desc) {
    return Card(
      margin: const EdgeInsets.only(bottom: 14),
      shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(12)),
      child: Padding(
        padding: const EdgeInsets.all(16),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Text(title, style: const TextStyle(fontSize: 18, fontWeight: FontWeight.bold, color: Color(0xFF0D1B68))),
            const SizedBox(height: 6),
            Text(specs, style: const TextStyle(fontWeight: FontWeight.w600, color: Color(0xFFFA8520))),
            const SizedBox(height: 6),
            Text(desc, style: const TextStyle(color: Colors.grey)),
            const SizedBox(height: 12),
            ElevatedButton(
              onPressed: () {},
              style: ElevatedButton.styleFrom(
                backgroundColor: const Color(0xFF0D1B68),
                foregroundColor: Colors.white,
                shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(8)),
              ),
              child: const Text('Book Vehicle Now'),
            )
          ],
        ),
      ),
    );
  }
}

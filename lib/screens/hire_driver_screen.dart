import 'package:flutter/material.dart';

class HireDriverScreen extends StatelessWidget {
  const HireDriverScreen({super.key});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        backgroundColor: const Color(0xFF0D1B68),
        title: const Text(
          'Hire Driver',
          style: TextStyle(color: Colors.white, fontWeight: FontWeight.bold),
        ),
      ),
      body: ListView(
        padding: const EdgeInsets.all(16),
        children: [
          _buildDriverCard('Subhasish Mondal', '12 yrs experience • Manual/Auto • Police Verified', 'Barasat / Salt Lake', '4.9 ★'),
          _buildDriverCard('Debashis Roy', '8 yrs experience • Night Specialist • Outstation Ready', 'New Town / Kolkata', '4.8 ★'),
          _buildDriverCard('Rajesh Das', '15 yrs experience • Luxury Cars / SUVs', 'Howrah / Airport Circle', '5.0 ★'),
        ],
      ),
    );
  }

  Widget _buildDriverCard(String name, String details, String zone, String rating) {
    return Card(
      margin: const EdgeInsets.only(bottom: 14),
      shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(12)),
      child: ListTile(
        leading: const CircleAvatar(
          backgroundColor: Color(0xFF0D1B68),
          child: Icon(Icons.person, color: Colors.white),
        ),
        title: Row(
          mainAxisAlignment: MainAxisAlignment.spaceBetween,
          children: [
            Text(name, style: const TextStyle(fontWeight: FontWeight.bold)),
            Text(rating, style: const TextStyle(color: Color(0xFFFA8520), fontWeight: FontWeight.bold)),
          ],
        ),
        subtitle: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            const SizedBox(height: 4),
            Text(details, style: const TextStyle(fontSize: 12)),
            const SizedBox(height: 2),
            Text('Zone: $zone', style: const TextStyle(fontSize: 12, color: Colors.grey)),
          ],
        ),
        trailing: ElevatedButton(
          onPressed: () {},
          style: ElevatedButton.styleFrom(
            backgroundColor: const Color(0xFFFA8520),
            foregroundColor: Colors.white,
            padding: const EdgeInsets.symmetric(horizontal: 12, vertical: 8),
          ),
          child: const Text('Hire'),
        ),
      ),
    );
  }
}

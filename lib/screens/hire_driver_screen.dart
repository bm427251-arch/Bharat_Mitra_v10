import 'package:flutter/material.dart';
import '../utils/logo_helper.dart';

class HireDriverScreen extends StatelessWidget {
  const HireDriverScreen({Key? key}) : super(key: key);

  final List<Map<String, dynamic>> _drivers = const [
    {'name': 'Rajesh Sharma', 'exp': '8 Years Exp', 'rate': '₹120/hr', 'types': 'Manual & Automatic', 'rating': '4.9 ★'},
    {'name': 'Bikash Das', 'exp': '5 Years Exp', 'rate': '₹100/hr', 'types': 'Manual Only', 'rating': '4.8 ★'},
    {'name': 'Amitav Ghosh', 'exp': '10 Years Exp', 'rate': '₹150/hr', 'types': 'Luxury & Outstation', 'rating': '5.0 ★'},
  ];

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: const Color(0xFFF8FAFC),
      appBar: AppBar(
        backgroundColor: const Color(0xFF0D1B68),
        title: Row(
          mainAxisAlignment: MainAxisAlignment.spaceBetween,
          children: [
            const Text('Hire Verified Drivers', style: TextStyle(fontWeight: FontWeight.bold, fontSize: 16, color: Colors.white)),
            const AppLogo(width: 90, height: 30, fit: BoxFit.contain),
          ],
        ),
      ),
      body: SingleChildScrollView(
        padding: const EdgeInsets.all(16.0),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            // Banner: Register as Driver (DL Only)
            Container(
              width: double.infinity,
              padding: const EdgeInsets.all(16),
              decoration: BoxDecoration(
                gradient: const LinearGradient(colors: [Color(0xFF2E8B57), Color(0xFF1B5E20)]),
                borderRadius: BorderRadius.circular(16),
              ),
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  const Text('Are You a Professional Driver?', style: TextStyle(color: Colors.white, fontWeight: FontWeight.bold, fontSize: 16)),
                  const SizedBox(height: 4),
                  const Text('No vehicle needed! Work with your Commercial Driving License.', style: TextStyle(color: Colors.white70, fontSize: 12)),
                  const SizedBox(height: 12),
                  ElevatedButton(
                    onPressed: () => Navigator.pushNamed(context, '/hire_driver_form'),
                    style: ElevatedButton.styleFrom(backgroundColor: const Color(0xFFFF8C00)),
                    child: const Text('Become Hire Driver - DL Only', style: TextStyle(color: Colors.white, fontWeight: FontWeight.bold)),
                  ),
                ],
              ),
            ),
            const SizedBox(height: 16),

            const Text('Available On-Demand Drivers', style: TextStyle(fontWeight: FontWeight.bold, fontSize: 16, color: Color(0xFF0D1B68))),
            const SizedBox(height: 10),

            ListView.separated(
              shrinkWrap: true,
              physics: const NeverScrollableScrollPhysics(),
              itemCount: _drivers.length,
              separatorBuilder: (_, __) => const SizedBox(height: 10),
              itemBuilder: (context, index) {
                final d = _drivers[index];
                return Card(
                  shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(14)),
                  elevation: 2,
                  child: Padding(
                    padding: const EdgeInsets.all(14.0),
                    child: Row(
                      children: [
                        const CircleAvatar(
                          radius: 24,
                          backgroundColor: Color(0xFF0D1B68),
                          child: Icon(Icons.person, color: Colors.white),
                        ),
                        const SizedBox(width: 12),
                        Expanded(
                          child: Column(
                            crossAxisAlignment: CrossAxisAlignment.start,
                            children: [
                              Text(d['name'] as String, style: const TextStyle(fontWeight: FontWeight.bold, fontSize: 14)),
                              Text('${d['exp']} • ${d['types']}', style: const TextStyle(fontSize: 11, color: Colors.grey)),
                              const SizedBox(height: 4),
                              Text('${d['rate']} • ${d['rating']}', style: const TextStyle(fontWeight: FontWeight.bold, color: Color(0xFF2E8B57))),
                            ],
                          ),
                        ),
                        ElevatedButton(
                          onPressed: () {
                            ScaffoldMessenger.of(context).showSnackBar(
                              SnackBar(content: Text('Hired ${d['name']} successfully!')),
                            );
                          },
                          style: ElevatedButton.styleFrom(backgroundColor: const Color(0xFF0D1B68)),
                          child: const Text('Hire', style: TextStyle(color: Colors.white, fontSize: 12)),
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
}

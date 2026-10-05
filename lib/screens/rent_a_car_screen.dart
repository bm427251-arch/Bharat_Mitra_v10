import 'package:flutter/material.dart';

class RentACarScreen extends StatelessWidget {
  const RentACarScreen({Key? key}) : super(key: key);

  final List<Map<String, dynamic>> _cars = const [
    {'name': 'Maruti Swift (Petrol/CNG)', 'cat': 'Hatchback', 'rate': '₹1,500/day', 'seats': '5 Seats', 'trans': 'Manual'},
    {'name': 'Hyundai Creta SX (Diesel)', 'cat': 'SUV', 'rate': '₹2,800/day', 'seats': '5 Seats', 'trans': 'Automatic'},
    {'name': 'Toyota Innova Crysta', 'cat': 'MUV / 7-Seater', 'rate': '₹3,500/day', 'seats': '7 Seats', 'trans': 'Manual'},
    {'name': 'Tata Nexon EV Prime', 'cat': 'Electric (EV)', 'rate': '₹2,200/day', 'seats': '5 Seats', 'trans': 'Automatic'},
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
            const Text('Rent A Car Catalog', style: TextStyle(fontWeight: FontWeight.bold, fontSize: 16, color: Colors.white)),
            Image.asset('assets/logo.png', width: 90, height: 30, fit: BoxFit.contain),
          ],
        ),
      ),
      body: SingleChildScrollView(
        padding: const EdgeInsets.all(16.0),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            // Banner: List Commercial Vehicle / Any Vehicle
            Container(
              width: double.infinity,
              padding: const EdgeInsets.all(16),
              decoration: BoxDecoration(
                gradient: const LinearGradient(colors: [Color(0xFF0D1B68), Color(0xFF1E3A8A)]),
                borderRadius: BorderRadius.circular(16),
              ),
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  const Text(
                    'Have Commercial Vehicles or Cars?',
                    style: TextStyle(color: Colors.white, fontWeight: FontWeight.bold, fontSize: 16),
                  ),
                  const SizedBox(height: 4),
                  const Text(
                    'Add Cars, Ambulances, Lorries, Trucks, Buses & Tempos for daily income.',
                    style: TextStyle(color: Colors.white70, fontSize: 12),
                  ),
                  const SizedBox(height: 12),
                  ElevatedButton(
                    onPressed: () => Navigator.pushNamed(context, '/rent_owner_form'),
                    style: ElevatedButton.styleFrom(backgroundColor: const Color(0xFFFF8C00)),
                    child: const Text('Become Rent Owner - Add Vehicle', style: TextStyle(color: Colors.white, fontWeight: FontWeight.bold)),
                  ),
                ],
              ),
            ),
            const SizedBox(height: 16),

            const Text('Available Rental Vehicles', style: TextStyle(fontWeight: FontWeight.bold, fontSize: 16, color: Color(0xFF0D1B68))),
            const SizedBox(height: 10),

            ListView.separated(
              shrinkWrap: true,
              physics: const NeverScrollableScrollPhysics(),
              itemCount: _cars.length,
              separatorBuilder: (_, __) => const SizedBox(height: 10),
              itemBuilder: (context, index) {
                final car = _cars[index];
                return Card(
                  shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(14)),
                  elevation: 2,
                  child: Padding(
                    padding: const EdgeInsets.all(14.0),
                    child: Row(
                      children: [
                        Container(
                          width: 50,
                          height: 50,
                          decoration: BoxDecoration(
                            color: const Color(0xFFFF8C00).withOpacity(0.12),
                            shape: BoxShape.circle,
                          ),
                          child: const Icon(Icons.directions_car, color: Color(0xFFFF8C00)),
                        ),
                        const SizedBox(width: 12),
                        Expanded(
                          child: Column(
                            crossAxisAlignment: CrossAxisAlignment.start,
                            children: [
                              Text(car['name'] as String, style: const TextStyle(fontWeight: FontWeight.bold, fontSize: 14)),
                              Text('${car['cat']} • ${car['seats']} • ${car['trans']}', style: const TextStyle(fontSize: 11, color: Colors.grey)),
                              const SizedBox(height: 4),
                              Text(car['rate'] as String, style: const TextStyle(fontWeight: FontWeight.bold, color: Color(0xFF2E8B57))),
                            ],
                          ),
                        ),
                        ElevatedButton(
                          onPressed: () {
                            ScaffoldMessenger.of(context).showSnackBar(
                              SnackBar(content: Text('Selected ${car['name']} for booking.')),
                            );
                          },
                          style: ElevatedButton.styleFrom(
                            backgroundColor: const Color(0xFF0D1B68),
                            padding: const EdgeInsets.symmetric(horizontal: 12),
                          ),
                          child: const Text('Book', style: TextStyle(color: Colors.white, fontSize: 12)),
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

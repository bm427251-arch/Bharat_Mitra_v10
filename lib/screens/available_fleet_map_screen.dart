import 'package:flutter/material.dart';

class AvailableFleetMapScreen extends StatefulWidget {
  const AvailableFleetMapScreen({Key? key}) : super(key: key);

  @override
  State<AvailableFleetMapScreen> createState() => _AvailableFleetMapScreenState();
}

class _AvailableFleetMapScreenState extends State<AvailableFleetMapScreen> {
  String _selectedFilter = 'All Services';
  int _selectedVehicleIndex = 4; // Defaults to Sedan

  final List<Map<String, dynamic>> _fleet = [
    {'name': 'Bike Taxi', 'cat': 'Fast Affordable', 'eta': 2, 'fare': 40, 'avail': 6, 'icon': Icons.two_wheeler},
    {'name': 'Toto E-Rickshaw', 'cat': 'Eco Shared', 'eta': 3, 'fare': 30, 'avail': 8, 'icon': Icons.electric_rickshaw},
    {'name': 'Auto Rickshaw', 'cat': 'Popular Quick', 'eta': 4, 'fare': 60, 'avail': 5, 'icon': Icons.directions_transit},
    {'name': 'Mini Cab', 'cat': 'Budget 4 seats', 'eta': 5, 'fare': 80, 'avail': 4, 'icon': Icons.local_taxi},
    {'name': 'Sedan', 'cat': 'Comfort 4 seats', 'eta': 3, 'fare': 100, 'avail': 5, 'icon': Icons.directions_car},
    {'name': 'SUV', 'cat': 'Spacious 6 seats', 'eta': 6, 'fare': 150, 'avail': 3, 'icon': Icons.airport_shuttle},
  ];

  @override
  Widget build(BuildContext context) {
    final selectedItem = _fleet[_selectedVehicleIndex];

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
                Text('Available Fleet Map', style: TextStyle(fontSize: 16, fontWeight: FontWeight.bold, color: Colors.white)),
                Text('6 Services • 12 nearby vehicles', style: TextStyle(fontSize: 11, color: Color(0xFFFFB366))),
              ],
            ),
            Image.asset('assets/logo.png', width: 100, height: 32, fit: BoxFit.contain),
          ],
        ),
      ),
      body: Column(
        children: [
          // Filter Chips
          SingleChildScrollView(
            scrollDirection: Axis.horizontal,
            padding: const EdgeInsets.symmetric(horizontal: 12, vertical: 8),
            child: Row(
              children: ['All Services', 'Bike', 'Toto', 'Auto', 'Mini', 'Sedan', 'SUV'].map((filter) {
                final isSel = _selectedFilter == filter;
                return Padding(
                  padding: const EdgeInsets.only(right: 8),
                  child: FilterChip(
                    label: Text(filter),
                    selected: isSel,
                    onSelected: (val) => setState(() => _selectedFilter = filter),
                    selectedColor: const Color(0xFF0D1B68),
                    labelStyle: TextStyle(
                      color: isSel ? Colors.white : const Color(0xFF0D1B68),
                      fontWeight: isSel ? FontWeight.bold : FontWeight.normal,
                    ),
                  ),
                );
              }).toList(),
          ),

          // Nearby Landmark Suggestions Bar
          SingleChildScrollView(
            scrollDirection: Axis.horizontal,
            padding: const EdgeInsets.symmetric(horizontal: 12, vertical: 4),
            child: Row(
              children: [
                const Text('Nearby: ', style: TextStyle(fontWeight: FontWeight.bold, fontSize: 11, color: Color(0xFF0D1B68))),
                ...['Dum Dum Metro (350m)', 'Airport T2 (1.8 km)', 'Sector V (3.2 km)', 'City Centre 1 (2.4 km)', 'Exide Crossing (4.5 km)'].map((lm) {
                  return Padding(
                    padding: const EdgeInsets.only(right: 6),
                    child: ActionChip(
                      label: Text(lm, style: const TextStyle(fontSize: 10, fontWeight: FontWeight.bold)),
                      backgroundColor: Colors.white,
                      onPressed: () {
                        ScaffoldMessenger.of(context).showSnackBar(
                          SnackBar(content: Text('Destination set to $lm')),
                        );
                      },
                    ),
                  );
                }).toList(),
              ],
            ),
          ),

          // Map Canvas Placeholder (with Kolkata landmarks)
          Expanded(
            child: Container(
              margin: const EdgeInsets.symmetric(horizontal: 12),
              decoration: BoxDecoration(
                color: const Color(0xFFE2E8F0),
                borderRadius: BorderRadius.circular(16),
                border: Border.all(color: Colors.black12),
              ),
              child: Stack(
                children: [
                  Center(
                    child: Column(
                      mainAxisAlignment: MainAxisAlignment.center,
                      children: [
                        const Icon(Icons.map, size: 50, color: Color(0xFF0D1B68)),
                        const SizedBox(height: 8),
                        const Text(
                          'Kolkata Transit Canvas',
                          style: TextStyle(fontWeight: FontWeight.bold, fontSize: 16, color: Color(0xFF0D1B68)),
                        ),
                        const SizedBox(height: 4),
                        Text(
                          'Park St • AJC Bose Rd • Exide Crossing • Salt Lake',
                          style: TextStyle(fontSize: 12, color: Colors.grey.shade700),
                        ),
                      ],
                    ),
                  ),
                  Positioned(
                    top: 16,
                    left: 16,
                    child: Container(
                      padding: const EdgeInsets.symmetric(horizontal: 10, vertical: 6),
                      decoration: BoxDecoration(
                        color: Colors.white,
                        borderRadius: BorderRadius.circular(20),
                        boxShadow: [BoxShadow(color: Colors.black12, blurRadius: 4)],
                      ),
                      child: const Row(
                        children: [
                          Icon(Icons.near_me, size: 14, color: Color(0xFF2E8B57)),
                          SizedBox(width: 4),
                          Text('12 Vehicles Live in 3km', style: TextStyle(fontSize: 11, fontWeight: FontWeight.bold)),
                        ],
                      ),
                    ),
                  ),
                ],
              ),
            ),
          ),

          // Bottom Sheet: Choose Your Ride
          Container(
            padding: const EdgeInsets.all(16),
            decoration: const BoxDecoration(
              color: Colors.white,
              borderRadius: BorderRadius.vertical(top: Radius.circular(24)),
              boxShadow: [BoxShadow(color: Colors.black12, blurRadius: 10, offset: Offset(0, -3))],
            ),
            child: Column(
              mainAxisSize: MainAxisSize.min,
              children: [
                Row(
                  mainAxisAlignment: MainAxisAlignment.spaceBetween,
                  children: const [
                    Text('Choose Your Ride', style: TextStyle(fontWeight: FontWeight.bold, fontSize: 16, color: Color(0xFF0D1B68))),
                    Text('Fair Price • No Surge', style: TextStyle(color: Color(0xFF2E8B57), fontWeight: FontWeight.bold, fontSize: 12)),
                  ],
                ),
                const SizedBox(height: 10),
                SizedBox(
                  height: 150,
                  child: ListView.separated(
                    itemCount: _fleet.length,
                    separatorBuilder: (_, __) => const SizedBox(height: 8),
                    itemBuilder: (context, index) {
                      final item = _fleet[index];
                      final isSel = _selectedVehicleIndex == index;
                      return GestureDetector(
                        onTap: () => setState(() => _selectedVehicleIndex = index),
                        child: Container(
                          padding: const EdgeInsets.symmetric(horizontal: 12, vertical: 8),
                          decoration: BoxDecoration(
                            color: isSel ? const Color(0xFFFF8C00).withOpacity(0.1) : const Color(0xFFF8FAFC),
                            borderRadius: BorderRadius.circular(12),
                            border: Border.all(
                              color: isSel ? const Color(0xFFFF8C00) : Colors.black12,
                              width: isSel ? 1.5 : 0.8,
                            ),
                          ),
                          child: Row(
                            children: [
                              CircleAvatar(
                                backgroundColor: isSel ? const Color(0xFFFF8C00) : const Color(0xFF0D1B68),
                                child: Icon(item['icon'] as IconData, color: Colors.white, size: 20),
                              ),
                              const SizedBox(width: 10),
                              Expanded(
                                child: Column(
                                  crossAxisAlignment: CrossAxisAlignment.start,
                                  children: [
                                    Row(
                                      children: [
                                        Text(item['name'] as String, style: const TextStyle(fontWeight: FontWeight.bold, fontSize: 14)),
                                        const SizedBox(width: 6),
                                        Container(
                                          padding: const EdgeInsets.symmetric(horizontal: 4, vertical: 1),
                                          decoration: BoxDecoration(
                                            color: const Color(0xFF2E8B57).withOpacity(0.15),
                                            borderRadius: BorderRadius.circular(4),
                                          ),
                                          child: Text('${item['avail']} near', style: const TextStyle(color: Color(0xFF2E8B57), fontSize: 9, fontWeight: FontWeight.bold)),
                                        ),
                                      ],
                                    ),
                                    Text('${item['cat']} • ETA ${item['eta']} mins', style: const TextStyle(fontSize: 11, color: Colors.grey)),
                                  ],
                                ),
                              ),
                              Text('₹${item['fare']}', style: const TextStyle(fontWeight: FontWeight.bold, fontSize: 16, color: Color(0xFF0D1B68))),
                            ],
                          ),
                        ),
                      );
                    },
                  ),
                ),
                const SizedBox(height: 12),
                SizedBox(
                  width: double.infinity,
                  height: 48,
                  child: ElevatedButton(
                    onPressed: () => Navigator.pushNamed(context, '/booking_confirmed'),
                    style: ElevatedButton.styleFrom(
                      backgroundColor: const Color(0xFFFF8C00),
                      shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(12)),
                    ),
                    child: Text(
                      'Confirm & Book ${selectedItem['name']} (₹${selectedItem['fare']})',
                      style: const TextStyle(fontWeight: FontWeight.bold, fontSize: 15, color: Colors.white),
                    ),
                  ),
                ),
              ],
            ),
          ),
        ],
      ),
    );
  }
}

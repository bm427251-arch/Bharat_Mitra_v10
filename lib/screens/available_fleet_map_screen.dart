import 'package:flutter/material.dart';
import '../utils/logo_helper.dart';

class AvailableFleetMapScreen extends StatefulWidget {
  const AvailableFleetMapScreen({Key? key}) : super(key: key);

  @override
  State<AvailableFleetMapScreen> createState() => _AvailableFleetMapScreenState();
}

class _AvailableFleetMapScreenState extends State<AvailableFleetMapScreen> with SingleTickerProviderStateMixin {
  late TabController _tabController;

  final List<Map<String, dynamic>> _activeBookings = [
    {
      'id': 'BM-ACT-8841',
      'service': 'Sedan (AC)',
      'status': 'In Transit • 75 km/h Live',
      'statusColor': Color(0xFF2E8B57),
      'driver': 'Subhash Mondal (4.9 ★)',
      'vehicleNumber': 'WB-02-AK-9102',
      'pickup': 'Talbanda, Badai, Kolkata',
      'drop': 'Airport Terminal 2, NSCB Airport',
      'fare': '₹100',
      'otp': '4821',
      'time': 'Started 12 mins ago',
      'route': '/ride_in_progress',
    },
  ];

  final List<Map<String, dynamic>> _pastRides = [
    {
      'id': 'BM-RD-7719',
      'service': 'Auto Rickshaw',
      'status': 'Completed',
      'statusColor': Colors.grey,
      'driver': 'Ratan Das (4.8 ★)',
      'vehicleNumber': 'WB-24-Auto-3901',
      'pickup': 'Dum Dum Metro Station',
      'drop': 'Nagerbazar Crossing',
      'fare': '₹60',
      'time': '04 Oct, 06:45 PM',
      'rating': '5.0 ★',
    },
    {
      'id': 'BM-RD-6642',
      'service': 'Toto (E-Rickshaw)',
      'status': 'Completed',
      'statusColor': Colors.grey,
      'driver': 'Kartik Paul (4.7 ★)',
      'vehicleNumber': 'WB-Toto-219',
      'pickup': 'Madhyamgram Chowmatha',
      'drop': 'Badu Road Junction',
      'fare': '₹30',
      'time': '03 Oct, 11:20 AM',
      'rating': '4.5 ★',
    },
    {
      'id': 'BM-RD-5510',
      'service': 'Bike Taxi',
      'status': 'Completed',
      'statusColor': Colors.grey,
      'driver': 'Bikash Ghosh (4.9 ★)',
      'vehicleNumber': 'WB-08-BK-4412',
      'pickup': 'Salt Lake Sector V',
      'drop': 'City Centre 1 Mall',
      'fare': '₹40',
      'time': '01 Oct, 09:15 AM',
      'rating': '5.0 ★',
    },
  ];

  final List<Map<String, dynamic>> _rentalBookings = [
    {
      'id': 'BM-RENT-201',
      'service': 'Mahindra Bolero Ambulance',
      'status': 'Active Rental',
      'statusColor': Color(0xFFFF8C00),
      'owner': 'Maa Tara Fleet & Logistics',
      'type': 'Emergency Life Support',
      'duration': '24 Hours Plan',
      'fare': '₹3,500',
      'date': '05 Oct, 08:00 AM',
    },
    {
      'id': 'BM-RENT-108',
      'service': 'Hyundai Creta (Self-Drive)',
      'status': 'Completed',
      'statusColor': Colors.grey,
      'owner': 'Kolkata Car Rentals',
      'type': 'Self Drive SUV',
      'duration': '2 Days Weekend',
      'fare': '₹5,200',
      'date': '28 Sep - 30 Sep',
    },
  ];

  final List<Map<String, dynamic>> _hireDriverBookings = [
    {
      'id': 'BM-HIRE-440',
      'driver': 'Suresh Mondal (Verified DL)',
      'status': 'Confirmed Scheduled',
      'statusColor': Color(0xFF0D1B68),
      'category': 'Hourly Outstation Tour',
      'hours': '8 Hours (Kolkata - Digha)',
      'fare': '₹1,200',
      'date': 'Tomorrow, 07:00 AM',
    },
  ];

  @override
  void initState() {
    super.initState();
    _tabController = TabController(length: 4, vsync: this);
  }

  @override
  void dispose() {
    _tabController.dispose();
    super.dispose();
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
                Text('My Bookings History', style: TextStyle(fontSize: 16, fontWeight: FontWeight.bold, color: Colors.white)),
                Text('Live Status & Past Trips', style: TextStyle(fontSize: 11, color: Color(0xFFFFB366))),
              ],
            ),
            GestureDetector(
              behavior: HitTestBehavior.opaque,
              onLongPress: () => Navigator.pushNamed(context, '/admin_login'),
              child: const AppLogo(width: 90, height: 30, fit: BoxFit.contain),
            ),
          ],
        ),
        bottom: TabBar(
          controller: _tabController,
          isScrollable: true,
          indicatorColor: const Color(0xFFFF8C00),
          indicatorWeight: 3,
          labelColor: Colors.white,
          unselectedLabelColor: Colors.white70,
          labelStyle: const TextStyle(fontWeight: FontWeight.bold, fontSize: 12),
          tabs: const [
            Tab(text: 'Active Rides (1)'),
            Tab(text: 'Past Rides (3)'),
            Tab(text: 'Rent A Car (2)'),
            Tab(text: 'Hire Driver (1)'),
          ],
        ),
      ),
      body: TabBarView(
        controller: _tabController,
        children: [
          _buildActiveRidesTab(),
          _buildPastRidesTab(),
          _buildRentalsTab(),
          _buildHireDriverTab(),
        ],
      ),
    );
  }

  Widget _buildActiveRidesTab() {
    return ListView.builder(
      padding: const EdgeInsets.all(14),
      itemCount: _activeBookings.length,
      itemBuilder: (context, index) {
        final b = _activeBookings[index];
        return Card(
          shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(16)),
          elevation: 4,
          margin: const EdgeInsets.only(bottom: 14),
          child: Padding(
            padding: const EdgeInsets.all(16),
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                Row(
                  mainAxisAlignment: MainAxisAlignment.spaceBetween,
                  children: [
                    Container(
                      padding: const EdgeInsets.symmetric(horizontal: 10, vertical: 4),
                      decoration: BoxDecoration(
                        color: (b['statusColor'] as Color).withOpacity(0.15),
                        borderRadius: BorderRadius.circular(20),
                        border: Border.all(color: b['statusColor'] as Color),
                      ),
                      child: Row(
                        children: [
                          Icon(Icons.fiber_manual_record, color: b['statusColor'] as Color, size: 10),
                          const SizedBox(width: 4),
                          Text(b['status'], style: TextStyle(color: b['statusColor'] as Color, fontWeight: FontWeight.bold, fontSize: 11)),
                        ],
                      ),
                    ),
                    Text(b['fare'], style: const TextStyle(fontWeight: FontWeight.w900, fontSize: 16, color: Color(0xFF0D1B68))),
                  ],
                ),
                const SizedBox(height: 12),
                Row(
                  children: [
                    const Icon(Icons.directions_car, color: Color(0xFF0D1B68), size: 20),
                    const SizedBox(width: 8),
                    Text(b['service'], style: const TextStyle(fontWeight: FontWeight.bold, fontSize: 14, color: Color(0xFF0D1B68))),
                    const Spacer(),
                    Container(
                      padding: const EdgeInsets.symmetric(horizontal: 8, vertical: 2),
                      decoration: BoxDecoration(color: const Color(0xFFFF8C00).withOpacity(0.2), borderRadius: BorderRadius.circular(6)),
                      child: Text('OTP: ${b['otp']}', style: const TextStyle(fontWeight: FontWeight.bold, color: Color(0xFF0D1B68), fontSize: 11)),
                    ),
                  ],
                ),
                const SizedBox(height: 6),
                Text('Driver: ${b['driver']} • ${b['vehicleNumber']}', style: TextStyle(fontSize: 12, color: Colors.grey.shade700)),
                const Divider(height: 20),
                Row(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    const Column(
                      children: [
                        Icon(Icons.trip_origin, size: 12, color: Color(0xFF2E8B57)),
                        SizedBox(height: 12),
                        Icon(Icons.location_on, size: 12, color: Color(0xFFFF8C00)),
                      ],
                    ),
                    const SizedBox(width: 8),
                    Expanded(
                      child: Column(
                        crossAxisAlignment: CrossAxisAlignment.start,
                        children: [
                          Text('From: ${b['pickup']}', style: const TextStyle(fontSize: 12, fontWeight: FontWeight.w500)),
                          const SizedBox(height: 8),
                          Text('To: ${b['drop']}', style: const TextStyle(fontSize: 12, fontWeight: FontWeight.bold, color: Color(0xFF0D1B68))),
                        ],
                      ),
                    ),
                  ],
                ),
                const SizedBox(height: 14),
                Row(
                  children: [
                    Expanded(
                      child: ElevatedButton.icon(
                        icon: const Icon(Icons.gps_fixed, size: 16, color: Colors.white),
                        label: const Text('Track Live Trip', style: TextStyle(color: Colors.white, fontSize: 12, fontWeight: FontWeight.bold)),
                        style: ElevatedButton.styleFrom(
                          backgroundColor: const Color(0xFF0D1B68),
                          shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(10)),
                        ),
                        onPressed: () => Navigator.pushNamed(context, b['route']),
                      ),
                    ),
                    const SizedBox(width: 8),
                    OutlinedButton.icon(
                      icon: const Icon(Icons.qr_code_2, size: 16, color: Color(0xFFFF8C00)),
                      label: const Text('Pay UPI', style: TextStyle(color: Color(0xFFFF8C00), fontSize: 12, fontWeight: FontWeight.bold)),
                      style: OutlinedButton.styleFrom(
                        side: const BorderSide(color: Color(0xFFFF8C00)),
                        shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(10)),
                      ),
                      onPressed: () => Navigator.pushNamed(context, '/auto_qr_payment'),
                    ),
                  ],
                ),
              ],
            ),
          ),
        );
      },
    );
  }

  Widget _buildPastRidesTab() {
    return ListView.builder(
      padding: const EdgeInsets.all(14),
      itemCount: _pastRides.length,
      itemBuilder: (context, index) {
        final r = _pastRides[index];
        return Card(
          shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(14)),
          elevation: 2,
          margin: const EdgeInsets.only(bottom: 12),
          child: Padding(
            padding: const EdgeInsets.all(14),
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                Row(
                  mainAxisAlignment: MainAxisAlignment.spaceBetween,
                  children: [
                    Text(r['service'], style: const TextStyle(fontWeight: FontWeight.bold, fontSize: 14, color: Color(0xFF0D1B68))),
                    Text(r['fare'], style: const TextStyle(fontWeight: FontWeight.w900, fontSize: 15, color: Color(0xFF0D1B68))),
                  ],
                ),
                const SizedBox(height: 4),
                Text('${r['time']} • ${r['driver']}', style: TextStyle(fontSize: 11, color: Colors.grey.shade600)),
                const SizedBox(height: 8),
                Text('📍 ${r['pickup']} ➔ ${r['drop']}', style: const TextStyle(fontSize: 12, color: Colors.black87)),
                const SizedBox(height: 10),
                Row(
                  mainAxisAlignment: MainAxisAlignment.spaceBetween,
                  children: [
                    Container(
                      padding: const EdgeInsets.symmetric(horizontal: 8, vertical: 3),
                      decoration: BoxDecoration(color: Colors.green.shade50, borderRadius: BorderRadius.circular(6)),
                      child: Text('Rated: ${r['rating']}', style: const TextStyle(color: Color(0xFF2E8B57), fontWeight: FontWeight.bold, fontSize: 11)),
                    ),
                    TextButton(
                      onPressed: () => Navigator.pushNamed(context, '/rating'),
                      child: const Text('View Receipt / Rating', style: TextStyle(fontSize: 11, color: Color(0xFF0D1B68), fontWeight: FontWeight.bold)),
                    ),
                  ],
                ),
              ],
            ),
          ),
        );
      },
    );
  }

  Widget _buildRentalsTab() {
    return ListView.builder(
      padding: const EdgeInsets.all(14),
      itemCount: _rentalBookings.length,
      itemBuilder: (context, index) {
        final rb = _rentalBookings[index];
        return Card(
          shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(14)),
          elevation: 2,
          margin: const EdgeInsets.only(bottom: 12),
          child: Padding(
            padding: const EdgeInsets.all(14),
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                Row(
                  mainAxisAlignment: MainAxisAlignment.spaceBetween,
                  children: [
                    Text(rb['service'], style: const TextStyle(fontWeight: FontWeight.bold, fontSize: 14, color: Color(0xFF0D1B68))),
                    Text(rb['fare'], style: const TextStyle(fontWeight: FontWeight.bold, fontSize: 15, color: Color(0xFFFF8C00))),
                  ],
                ),
                const SizedBox(height: 4),
                Text('Owner: ${rb['owner']} • ${rb['duration']}', style: TextStyle(fontSize: 11, color: Colors.grey.shade600)),
                const SizedBox(height: 6),
                Text('Status: ${rb['status']} • Date: ${rb['date']}', style: const TextStyle(fontSize: 12, fontWeight: FontWeight.w500)),
                const SizedBox(height: 10),
                SizedBox(
                  width: double.infinity,
                  child: OutlinedButton(
                    onPressed: () => Navigator.pushNamed(context, '/rent_a_car'),
                    child: const Text('View Rental Agreement & Details', style: TextStyle(fontSize: 11, color: Color(0xFF0D1B68))),
                  ),
                ),
              ],
            ),
          ),
        );
      },
    );
  }

  Widget _buildHireDriverTab() {
    return ListView.builder(
      padding: const EdgeInsets.all(14),
      itemCount: _hireDriverBookings.length,
      itemBuilder: (context, index) {
        final hd = _hireDriverBookings[index];
        return Card(
          shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(14)),
          elevation: 2,
          margin: const EdgeInsets.only(bottom: 12),
          child: Padding(
            padding: const EdgeInsets.all(14),
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                Row(
                  mainAxisAlignment: MainAxisAlignment.spaceBetween,
                  children: [
                    Text(hd['driver'], style: const TextStyle(fontWeight: FontWeight.bold, fontSize: 14, color: Color(0xFF0D1B68))),
                    Text(hd['fare'], style: const TextStyle(fontWeight: FontWeight.bold, fontSize: 15, color: Color(0xFF2E8B57))),
                  ],
                ),
                const SizedBox(height: 4),
                Text('${hd['category']} • ${hd['hours']}', style: TextStyle(fontSize: 11, color: Colors.grey.shade600)),
                const SizedBox(height: 6),
                Text('Schedule: ${hd['date']} • Status: ${hd['status']}', style: const TextStyle(fontSize: 12, fontWeight: FontWeight.w500)),
                const SizedBox(height: 10),
                SizedBox(
                  width: double.infinity,
                  child: OutlinedButton(
                    onPressed: () => Navigator.pushNamed(context, '/hire_driver'),
                    child: const Text('Driver Contact & Details', style: TextStyle(fontSize: 11, color: Color(0xFF0D1B68))),
                  ),
                ),
              ],
            ),
          ),
        );
      },
    );
  }
}

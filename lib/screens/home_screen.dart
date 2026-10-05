import 'package:flutter/material.dart';
import 'package:geolocator/geolocator.dart';
import 'package:geocoding/geocoding.dart';
import 'package:permission_handler/permission_handler.dart';

class HomeScreen extends StatefulWidget {
  const HomeScreen({Key? key}) : super(key: key);

  @override
  State<HomeScreen> createState() => _HomeScreenState();
}

class _HomeScreenState extends State<HomeScreen> with SingleTickerProviderStateMixin {
  String _currentAddress = 'Dum Dum, Kolkata';
  double _currentLat = 22.6534;
  double _currentLng = 88.4449;
  bool _isPermissionDenied = false;
  String _selectedService = 'Sedan';
  late AnimationController _pulseController;
  late Animation<double> _pulseAnimation;

  final List<Map<String, dynamic>> _nearbyLandmarks = const [
    {'name': 'Dum Dum Metro Station', 'cat': 'Metro', 'dist': '350m', 'sub': 'North-South Corridor Line', 'icon': Icons.subway},
    {'name': 'Airport Terminal 2 Gate 3', 'cat': 'Airport', 'dist': '1.8 km', 'sub': 'NSCB International Airport', 'icon': Icons.flight_takeoff},
    {'name': 'Salt Lake Sector V', 'cat': 'IT Hub', 'dist': '3.2 km', 'sub': 'College More / Karunamoyee', 'icon': Icons.business},
    {'name': 'City Centre 1 Mall', 'cat': 'Shopping', 'dist': '2.4 km', 'sub': 'DC Block, Salt Lake', 'icon': Icons.shopping_bag},
    {'name': 'RG Kar Medical College', 'cat': 'Hospital', 'dist': '2.1 km', 'sub': 'Belgachia / Shyambazar', 'icon': Icons.local_hospital},
    {'name': 'Exide Crossing', 'cat': 'Junction', 'dist': '4.5 km', 'sub': 'Rabindra Sadan / AJC Bose Rd', 'icon': Icons.place},
    {'name': 'Howrah Railway Station', 'cat': 'Rail Terminal', 'dist': '6.8 km', 'sub': 'Station Road, Howrah', 'icon': Icons.train},
    {'name': 'Eco Park Gate 2', 'cat': 'Park', 'dist': '4.1 km', 'sub': 'Major Arterial Road, New Town', 'icon': Icons.park},
  ];

  @override
  void initState() {
    super.initState();
    _pulseController = AnimationController(
      vsync: this,
      duration: const Duration(milliseconds: 900),
    )..repeat(reverse: true);
    _pulseAnimation = Tween<double>(begin: 0.4, end: 1.0).animate(_pulseController);

    _initLocation();
  }

  @override
  void dispose() {
    _pulseController.dispose();
    super.dispose();
  }

  Future<void> _initLocation() async {
    try {
      LocationPermission permission = await Geolocator.checkPermission();
      if (permission == LocationPermission.denied) {
        permission = await Geolocator.requestPermission();
        if (permission == LocationPermission.denied) {
          if (mounted) setState(() => _isPermissionDenied = true);
          return;
        }
      }
      if (permission == LocationPermission.deniedForever) {
        if (mounted) setState(() => _isPermissionDenied = true);
        return;
      }

      Position position = await Geolocator.getCurrentPosition(
        desiredAccuracy: LocationAccuracy.high,
      );

      List<Placemark> placemarks = await placemarkFromCoordinates(
        position.latitude,
        position.longitude,
      );

      if (placemarks.isNotEmpty && mounted) {
        Placemark place = placemarks.first;
        String sub = place.subLocality?.isNotEmpty == true
            ? place.subLocality!
            : (place.thoroughfare?.isNotEmpty == true ? place.thoroughfare! : 'Dum Dum');
        String loc = place.locality?.isNotEmpty == true
            ? place.locality!
            : (place.subAdministrativeArea?.isNotEmpty == true ? place.subAdministrativeArea! : 'Kolkata');
        setState(() {
          _currentLat = position.latitude;
          _currentLng = position.longitude;
          _currentAddress = '$sub, $loc';
          _isPermissionDenied = false;
        });
      }
    } catch (_) {
      // Default to Kolkata Dum Dum if sensors unavailable
      if (mounted) {
        setState(() {
          _currentAddress = 'Dum Dum, Kolkata';
          _isPermissionDenied = false;
        });
      }
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: const Color(0xFFF8FAFC),
      appBar: AppBar(
        backgroundColor: const Color(0xFF0D1B68),
        elevation: 0,
        title: Row(
          children: [
            Image.asset(
              'assets/logo.png',
              width: 120,
              height: 40,
              fit: BoxFit.contain,
            ),
            const SizedBox(width: 8),
            const Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              mainAxisSize: MainAxisSize.min,
              children: [
                Text(
                  'Hello, Aman!',
                  style: TextStyle(fontSize: 14, fontWeight: FontWeight.bold, color: Colors.white),
                ),
                Text(
                  'Good Morning',
                  style: TextStyle(fontSize: 10, color: Color(0xFFFFB366)),
                ),
              ],
            ),
          ],
        ),
        actions: [
          // Auto QR Shortcut
          IconButton(
            icon: const Icon(Icons.qr_code_scanner, color: Color(0xFFFFB366)),
            tooltip: 'Auto QR Payment',
            onPressed: () => Navigator.pushNamed(context, '/auto_qr_payment'),
          ),
          // Wallet Balance Chip
          GestureDetector(
            onTap: () => Navigator.pushNamed(context, '/wallet'),
            child: Container(
              margin: const EdgeInsets.symmetric(vertical: 12),
              padding: const EdgeInsets.symmetric(horizontal: 8, vertical: 4),
              decoration: BoxDecoration(
                color: const Color(0xFF2E8B57).withOpacity(0.25),
                borderRadius: BorderRadius.circular(8),
                border: Border.all(color: const Color(0xFF2E8B57)),
              ),
              child: const Row(
                children: [
                  Icon(Icons.account_balance_wallet, color: Colors.white, size: 14),
                  SizedBox(width: 4),
                  Text(
                    '₹1,200',
                    style: TextStyle(color: Colors.white, fontWeight: FontWeight.bold, fontSize: 11),
                  ),
                ],
              ),
            ),
          ),
          const SizedBox(width: 4),
          // Admin Dashboard Portal Button
          IconButton(
            icon: const Icon(Icons.admin_panel_settings, color: Colors.white),
            tooltip: 'Admin Dashboard',
            onPressed: () => Navigator.pushNamed(context, '/admin_dashboard'),
          ),
        ],
      ),
      body: SingleChildScrollView(
        child: Padding(
          padding: const EdgeInsets.all(16.0),
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              // Search Bar with currentAddress hint
              Container(
                decoration: BoxDecoration(
                  color: Colors.white,
                  borderRadius: BorderRadius.circular(16),
                  boxShadow: [
                    BoxShadow(color: Colors.black.withOpacity(0.06), blurRadius: 10, offset: const Offset(0, 4)),
                  ],
                ),
                child: TextField(
                  decoration: InputDecoration(
                    prefixIcon: const Icon(Icons.search, color: Color(0xFF0D1B68)),
                    hintText: 'Where to? (Pick landmark below)',
                    hintStyle: TextStyle(color: Colors.grey.shade600, fontSize: 14),
                    border: InputBorder.none,
                    contentPadding: const EdgeInsets.symmetric(horizontal: 16, vertical: 14),
                  ),
                ),
              ),
              const SizedBox(height: 10),

              // Auto-detected Current Location Row with Green Pulse
              Row(
                children: [
                  FadeTransition(
                    opacity: _pulseAnimation,
                    child: Container(
                      width: 10,
                      height: 10,
                      decoration: const BoxDecoration(
                        color: Color(0xFF2E8B57),
                        shape: BoxShape.circle,
                      ),
                    ),
                  ),
                  const SizedBox(width: 8),
                  const Icon(Icons.my_location, color: Color(0xFF2E8B57), size: 18),
                  const SizedBox(width: 6),
                  Expanded(
                    child: Text(
                      '📍 Current Live Location - $_currentAddress - Live Auto-detected',
                      style: const TextStyle(
                        fontSize: 12,
                        fontWeight: FontWeight.w600,
                        color: Color(0xFF2E8B57),
                      ),
                      overflow: TextOverflow.ellipsis,
                    ),
                  ),
                  if (_isPermissionDenied)
                    TextButton(
                      onPressed: () => openAppSettings(),
                      child: const Text('Enable Location', style: TextStyle(fontSize: 11, color: Color(0xFFFF8C00))),
                    ),
                ],
              ),
              const SizedBox(height: 14),

              // Suggestions for Similar Nearby Landmarks
              Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  Row(
                    mainAxisAlignment: MainAxisAlignment.spaceBetween,
                    children: [
                      Row(
                        children: const [
                          Icon(Icons.near_me, color: Color(0xFFFF8C00), size: 18),
                          SizedBox(width: 6),
                          Text(
                            'Nearby Similar Landmarks',
                            style: TextStyle(fontWeight: FontWeight.bold, fontSize: 13, color: Color(0xFF0D1B68)),
                          ),
                        ],
                      ),
                      Container(
                        padding: const EdgeInsets.symmetric(horizontal: 8, vertical: 2),
                        decoration: BoxDecoration(
                          color: const Color(0xFFFF8C00).withOpacity(0.15),
                          borderRadius: BorderRadius.circular(10),
                        ),
                        child: const Text(
                          'Near Live Location',
                          style: TextStyle(fontSize: 10, fontWeight: FontWeight.bold, color: Color(0xFFFF8C00)),
                        ),
                      ),
                    ],
                  ),
                  const SizedBox(height: 8),
                  SizedBox(
                    height: 90,
                    child: ListView.separated(
                      scrollDirection: Axis.horizontal,
                      itemCount: _nearbyLandmarks.length,
                      separatorBuilder: (_, __) => const SizedBox(width: 10),
                      itemBuilder: (context, index) {
                        final lm = _nearbyLandmarks[index];
                        return GestureDetector(
                          onTap: () {
                            ScaffoldMessenger.of(context).showSnackBar(
                              SnackBar(content: Text('Selected landmark: ${lm['name']}')),
                            );
                            Navigator.pushNamed(context, '/fleet_map');
                          },
                          child: Container(
                            width: 190,
                            padding: const EdgeInsets.all(10),
                            decoration: BoxDecoration(
                              color: Colors.white,
                              borderRadius: BorderRadius.circular(14),
                              border: Border.all(color: Colors.black12),
                              boxShadow: [
                                BoxShadow(color: Colors.black.withOpacity(0.04), blurRadius: 6, offset: const Offset(0, 2)),
                              ],
                            ),
                            child: Column(
                              crossAxisAlignment: CrossAxisAlignment.start,
                              children: [
                                Row(
                                  mainAxisAlignment: MainAxisAlignment.spaceBetween,
                                  children: [
                                    CircleAvatar(
                                      radius: 13,
                                      backgroundColor: const Color(0xFFFF8C00).withOpacity(0.15),
                                      child: Icon(lm['icon'] as IconData, size: 14, color: const Color(0xFFFF8C00)),
                                    ),
                                    Container(
                                      padding: const EdgeInsets.symmetric(horizontal: 6, vertical: 1),
                                      decoration: BoxDecoration(
                                        color: const Color(0xFF2E8B57).withOpacity(0.12),
                                        borderRadius: BorderRadius.circular(6),
                                      ),
                                      child: Text(
                                        lm['dist'] as String,
                                        style: const TextStyle(color: Color(0xFF2E8B57), fontWeight: FontWeight.bold, fontSize: 9),
                                      ),
                                    ),
                                  ],
                                ),
                                const Spacer(),
                                Text(
                                  lm['name'] as String,
                                  style: const TextStyle(fontWeight: FontWeight.bold, fontSize: 12, color: Color(0xFF0D1B68)),
                                  maxLines: 1,
                                  overflow: TextOverflow.ellipsis,
                                ),
                                Text(
                                  lm['sub'] as String,
                                  style: const TextStyle(fontSize: 10, color: Colors.grey),
                                  maxLines: 1,
                                  overflow: TextOverflow.ellipsis,
                                ),
                              ],
                            ),
                          ),
                        );
                      },
                    ),
                  ),
                ],
              ),
              const SizedBox(height: 16),

              // Top Tagline Banner
              Container(
                width: double.infinity,
                padding: const EdgeInsets.symmetric(horizontal: 14, vertical: 10),
                decoration: BoxDecoration(
                  gradient: const LinearGradient(
                    colors: [Color(0xFF0D1B68), Color(0xFF1E3A8A)],
                  ),
                  borderRadius: BorderRadius.circular(12),
                ),
                child: const Row(
                  children: [
                    Icon(Icons.bolt, color: Color(0xFFFF8C00), size: 20),
                    SizedBox(width: 8),
                    Expanded(
                      child: Text(
                        'Bike & Toto now available • Explore new rides',
                        style: TextStyle(color: Colors.white, fontWeight: FontWeight.bold, fontSize: 12),
                      ),
                    ),
                  ],
                ),
              ),
              const SizedBox(height: 18),

              // Choose Service Header
              const Text(
                'Where to? Choose Service',
                style: TextStyle(
                  fontSize: 17,
                  fontWeight: FontWeight.bold,
                  color: Color(0xFF0D1B68),
                ),
              ),
              const SizedBox(height: 12),

              // 6 SERVICES IN 2 ROWS (Row 1: Bike, Toto, Auto | Row 2: Mini, Sedan, SUV)
              // Row 1
              Row(
                children: [
                  Expanded(
                    child: _buildServiceCard(
                      title: 'Bike Taxi',
                      desc: 'Fast Affordable',
                      fare: '₹40',
                      icon: Icons.two_wheeler,
                      color: const Color(0xFFFF8C00),
                    ),
                  ),
                  const SizedBox(width: 8),
                  Expanded(
                    child: _buildServiceCard(
                      title: 'Toto E-Rickshaw',
                      desc: 'Eco Shared',
                      fare: '₹30',
                      icon: Icons.electric_rickshaw,
                      color: const Color(0xFF2E8B57),
                    ),
                  ),
                  const SizedBox(width: 8),
                  Expanded(
                    child: _buildServiceCard(
                      title: 'Auto Rickshaw',
                      desc: 'Popular Quick',
                      fare: '₹60',
                      icon: Icons.directions_transit,
                      color: const Color(0xFFFF8C00),
                    ),
                  ),
                ],
              ),
              const SizedBox(height: 8),

              // Row 2
              Row(
                children: [
                  Expanded(
                    child: _buildServiceCard(
                      title: 'Mini Cab',
                      desc: 'Budget 4 seats',
                      fare: '₹80',
                      icon: Icons.local_taxi,
                      color: const Color(0xFF0D1B68),
                    ),
                  ),
                  const SizedBox(width: 8),
                  Expanded(
                    child: _buildServiceCard(
                      title: 'Sedan',
                      desc: 'Comfort 4 seats',
                      fare: '₹100',
                      icon: Icons.directions_car,
                      color: const Color(0xFF0D1B68),
                    ),
                  ),
                  const SizedBox(width: 8),
                  Expanded(
                    child: _buildServiceCard(
                      title: 'SUV',
                      desc: 'Spacious 6 seats',
                      fare: '₹150',
                      icon: Icons.airport_shuttle,
                      color: const Color(0xFF0D1B68),
                    ),
                  ),
                ],
              ),
              const SizedBox(height: 20),

              // Auto QR Instant Payment Quick Banner
              Card(
                shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(16)),
                elevation: 4,
                color: Colors.white,
                child: InkWell(
                  onTap: () => Navigator.pushNamed(context, '/auto_qr_payment'),
                  borderRadius: BorderRadius.circular(16),
                  child: Padding(
                    padding: const EdgeInsets.all(14),
                    child: Row(
                      children: [
                        Container(
                          width: 44,
                          height: 44,
                          decoration: BoxDecoration(
                            color: const Color(0xFFFF8C00).withOpacity(0.15),
                            shape: BoxShape.circle,
                          ),
                          child: const Icon(Icons.qr_code_2, color: Color(0xFFFF8C00), size: 26),
                        ),
                        const SizedBox(width: 12),
                        const Expanded(
                          child: Column(
                            crossAxisAlignment: CrossAxisAlignment.start,
                            children: [
                              Text(
                                'Auto QR Instant Payment (₹100)',
                                style: TextStyle(fontWeight: FontWeight.bold, fontSize: 14, color: Color(0xFF0D1B68)),
                              ),
                              Text(
                                'Dynamic UPI QR • 05:00 Timer • GPay / PhonePe',
                                style: TextStyle(color: Colors.grey, fontSize: 11),
                              ),
                            ],
                          ),
                        ),
                        const Icon(Icons.arrow_forward_ios, size: 16, color: Color(0xFFFF8C00)),
                      ],
                    ),
                  ),
                ),
              ),
              const SizedBox(height: 16),

              // Core Ride & Rental Services
              const Text(
                'Core Ride & Rental Services',
                style: TextStyle(
                  fontSize: 16,
                  fontWeight: FontWeight.bold,
                  color: Color(0xFF0D1B68),
                ),
              ),
              const SizedBox(height: 10),

              // Rent A Car Card
              _buildCoreFeatureCard(
                title: 'Rent A Car Catalog',
                subtitle: 'Self-Drive & Commercial • Swift, Creta, Innova',
                icon: Icons.directions_car,
                color: const Color(0xFFFF8C00),
                onTap: () => Navigator.pushNamed(context, '/rent_a_car'),
              ),
              const SizedBox(height: 10),

              // Hire Driver Card
              _buildCoreFeatureCard(
                title: 'Hire Verified Driver',
                subtitle: 'Hourly & Daily • DL Only Verified Professionals',
                icon: Icons.person_pin,
                color: const Color(0xFF2E8B57),
                onTap: () => Navigator.pushNamed(context, '/hire_driver'),
              ),
              const SizedBox(height: 20),

              // Book City Ride CTA Button
              SizedBox(
                width: double.infinity,
                height: 50,
                child: ElevatedButton(
                  onPressed: () => Navigator.pushNamed(context, '/fleet_map'),
                  style: ElevatedButton.styleFrom(
                    backgroundColor: const Color(0xFF0D1B68),
                    shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(12)),
                  ),
                  child: const Row(
                    mainAxisAlignment: MainAxisAlignment.center,
                    children: [
                      Icon(Icons.navigation, color: Colors.white, size: 20),
                      SizedBox(width: 8),
                      Text(
                        'Book Ride Now (View 12 Nearby Vehicles)',
                        style: TextStyle(color: Colors.white, fontWeight: FontWeight.bold, fontSize: 14),
                      ),
                    ],
                  ),
                ),
              ),
              const SizedBox(height: 24),
            ],
          ),
        ),
      ),
    );
  }

  Widget _buildServiceCard({
    required String title,
    required String desc,
    required String fare,
    required IconData icon,
    required Color color,
  }) {
    final bool isSelected = _selectedService == title;
    return GestureDetector(
      onTap: () => setState(() => _selectedService = title),
      child: Container(
        padding: const EdgeInsets.symmetric(vertical: 12, horizontal: 6),
        decoration: BoxDecoration(
          color: isSelected ? const Color(0xFFFF8C00).withOpacity(0.12) : Colors.white,
          borderRadius: BorderRadius.circular(14),
          border: Border.all(
            color: isSelected ? const Color(0xFFFF8C00) : Colors.black12,
            width: isSelected ? 1.5 : 0.8,
          ),
          boxShadow: [
            BoxShadow(color: Colors.black.withOpacity(0.04), blurRadius: 6, offset: const Offset(0, 2)),
          ],
        ),
        child: Column(
          children: [
            Icon(icon, color: isSelected ? const Color(0xFFFF8C00) : color, size: 28),
            const SizedBox(height: 6),
            Text(
              title,
              textAlign: TextAlign.center,
              style: TextStyle(
                fontWeight: FontWeight.bold,
                fontSize: 11,
                color: isSelected ? const Color(0xFFFF8C00) : const Color(0xFF0D1B68),
              ),
            ),
            const SizedBox(height: 2),
            Text(
              desc,
              textAlign: TextAlign.center,
              style: const TextStyle(fontSize: 9, color: Colors.grey),
            ),
            const SizedBox(height: 4),
            Text(
              fare,
              style: const TextStyle(
                fontWeight: FontWeight.w900,
                fontSize: 12,
                color: Color(0xFF0D1B68),
              ),
            ),
          ],
        ),
      ),
    );
  }

  Widget _buildCoreFeatureCard({
    required String title,
    required String subtitle,
    required IconData icon,
    required Color color,
    required VoidCallback onTap,
  }) {
    return Card(
      shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(14)),
      elevation: 2,
      child: ListTile(
        onTap: onTap,
        leading: CircleAvatar(
          backgroundColor: color.withOpacity(0.15),
          child: Icon(icon, color: color),
        ),
        title: Text(title, style: const TextStyle(fontWeight: FontWeight.bold, fontSize: 14)),
        subtitle: Text(subtitle, style: const TextStyle(fontSize: 11)),
        trailing: const Icon(Icons.arrow_forward_ios, size: 16, color: Colors.grey),
      ),
    );
  }
}

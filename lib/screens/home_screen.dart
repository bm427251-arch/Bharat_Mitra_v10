import 'package:flutter/material.dart';
import 'package:geolocator/geolocator.dart';
import 'package:geocoding/geocoding.dart';
import 'package:permission_handler/permission_handler.dart';
import 'admin_dashboard_screen.dart';
import 'create_driver_profile_screen.dart';

class HomeScreen extends StatefulWidget {
  const HomeScreen({Key? key}) : super(key: key);

  @override
  State<HomeScreen> createState() => _HomeScreenState();
}

class _HomeScreenState extends State<HomeScreen> with SingleTickerProviderStateMixin {
  String _currentAddress = 'Talbanda, Badai, Kolkata';
  double _currentLat = 22.6841;
  double _currentLng = 88.4512;
  bool _isPermissionDenied = false;
  String _selectedService = 'Sedan';
  late AnimationController _pulseController;
  late Animation<double> _pulseAnimation;

  // Drop Location & Autocomplete State
  final TextEditingController _dropController = TextEditingController();
  final FocusNode _dropFocusNode = FocusNode();
  String? _selectedDropLocation;
  bool _isDropSelected = false;
  List<Map<String, dynamic>> _filteredSuggestions = [];

  final List<Map<String, dynamic>> _allLandmarks = const [
    {'name': 'Dum Dum Metro Station', 'cat': 'Metro', 'dist': '350m', 'sub': 'North-South Corridor Line', 'icon': Icons.subway},
    {'name': 'Airport Terminal 2 Gate 3', 'cat': 'Airport', 'dist': '1.8 km', 'sub': 'NSCB International Airport', 'icon': Icons.flight_takeoff},
    {'name': 'Salt Lake Sector V', 'cat': 'IT Hub', 'dist': '3.2 km', 'sub': 'College More / Karunamoyee', 'icon': Icons.business},
    {'name': 'City Centre 1 Mall', 'cat': 'Shopping', 'dist': '2.4 km', 'sub': 'DC Block, Salt Lake', 'icon': Icons.shopping_bag},
    {'name': 'RG Kar Medical College', 'cat': 'Hospital', 'dist': '2.1 km', 'sub': 'Belgachia / Shyambazar', 'icon': Icons.local_hospital},
    {'name': 'Exide Crossing', 'cat': 'Junction', 'dist': '4.5 km', 'sub': 'Rabindra Sadan / AJC Bose Rd', 'icon': Icons.place},
    {'name': 'Howrah Railway Station', 'cat': 'Rail Terminal', 'dist': '6.8 km', 'sub': 'Station Road, Howrah', 'icon': Icons.train},
    {'name': 'Eco Park Gate 2', 'cat': 'Park', 'dist': '4.1 km', 'sub': 'Major Arterial Road, New Town', 'icon': Icons.park},
    {'name': 'Barasat Chapadali More', 'cat': 'Bus Terminus', 'dist': '3.8 km', 'sub': 'Jessore Road, Barasat', 'icon': Icons.directions_bus},
    {'name': 'Madhyamgram Chowmatha', 'cat': 'Crossing', 'dist': '2.0 km', 'sub': 'BT Road / Badu Rd', 'icon': Icons.turn_sharp_right},
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

    _dropController.addListener(_onDropTextChanged);
  }

  @override
  void dispose() {
    _pulseController.dispose();
    _dropController.removeListener(_onDropTextChanged);
    _dropController.dispose();
    _dropFocusNode.dispose();
    super.dispose();
  }

  void _onDropTextChanged() {
    final query = _dropController.text.trim().toLowerCase();
    if (query.isEmpty) {
      setState(() {
        _filteredSuggestions = [];
        if (_selectedDropLocation == null) {
          _isDropSelected = false;
        }
      });
    } else {
      setState(() {
        _filteredSuggestions = _allLandmarks.where((lm) {
          final name = (lm['name'] as String).toLowerCase();
          final sub = (lm['sub'] as String).toLowerCase();
          final cat = (lm['cat'] as String).toLowerCase();
          return name.contains(query) || sub.contains(query) || cat.contains(query);
        }).toList();
      });
    }
  }

  void _selectDropLocation(String name) {
    setState(() {
      _selectedDropLocation = name;
      _dropController.text = name;
      _filteredSuggestions = [];
      _isDropSelected = true;
    });
    _dropFocusNode.unfocus();
    ScaffoldMessenger.of(context).showSnackBar(
      SnackBar(
        content: Text('Drop set to: $name. Select vehicle below.'),
        duration: const Duration(seconds: 2),
        backgroundColor: const Color(0xFF2E8B57),
      ),
    );
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
            : (place.thoroughfare?.isNotEmpty == true ? place.thoroughfare! : 'Talbanda');
        String loc = place.locality?.isNotEmpty == true
            ? place.locality!
            : (place.subAdministrativeArea?.isNotEmpty == true ? place.subAdministrativeArea! : 'Badai');
        setState(() {
          _currentLat = position.latitude;
          _currentLng = position.longitude;
          _currentAddress = '$sub, $loc';
          _isPermissionDenied = false;
        });
      }
    } catch (_) {
      if (mounted) {
        setState(() {
          _currentAddress = 'Talbanda, Badai, Kolkata';
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
            // SILENT ADMIN ENTRY: Logo Tap & LongPress -> Direct Silent Open Admin Login
            GestureDetector(
              behavior: HitTestBehavior.opaque,
              onTap: () => Navigator.pushNamed(context, '/admin_login'),
              onLongPress: () => Navigator.pushNamed(context, '/admin_login'),
              child: Container(
                padding: const EdgeInsets.symmetric(horizontal: 4, vertical: 4),
                child: Image.asset(
                  'assets/logo.png',
                  width: 120,
                  height: 40,
                  fit: BoxFit.contain,
                ),
              ),
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
          const SizedBox(width: 6),
          // Direct Admin Icon Button
          IconButton(
            icon: const Icon(Icons.admin_panel_settings, color: Colors.white),
            tooltip: 'Admin Portal',
            onPressed: () => Navigator.pushNamed(context, '/admin_login'),
          ),
        ],
      ),
      body: SingleChildScrollView(
        padding: const EdgeInsets.all(16.0),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            // LOCATION SECTION (Top)
            // a) Current Live Location
            Container(
              padding: const EdgeInsets.symmetric(horizontal: 14, vertical: 10),
              decoration: BoxDecoration(
                color: Colors.white,
                borderRadius: BorderRadius.circular(12),
                border: Border.all(color: const Color(0xFF2E8B57).withOpacity(0.3)),
                boxShadow: [
                  BoxShadow(color: Colors.black.withOpacity(0.04), blurRadius: 6, offset: const Offset(0, 2)),
                ],
              ),
              child: Row(
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
                    child: Column(
                      crossAxisAlignment: CrossAxisAlignment.start,
                      children: [
                        const Text(
                          'Current Live Location',
                          style: TextStyle(fontSize: 10, color: Colors.grey, fontWeight: FontWeight.w600),
                        ),
                        Text(
                          '📍 $_currentAddress',
                          style: const TextStyle(
                            fontSize: 13,
                            fontWeight: FontWeight.bold,
                            color: Color(0xFF2E8B57),
                          ),
                          overflow: TextOverflow.ellipsis,
                        ),
                      ],
                    ),
                  ),
                  if (_isPermissionDenied)
                    TextButton(
                      onPressed: () => openAppSettings(),
                      child: const Text('Enable GPS', style: TextStyle(fontSize: 11, color: Color(0xFFFF8C00))),
                    ),
                ],
              ),
            ),
            const SizedBox(height: 12),

            // b) Drop Location Box - Where to? (Pick landmark)
            Container(
              decoration: BoxDecoration(
                color: Colors.white,
                borderRadius: BorderRadius.circular(14),
                border: Border.all(
                  color: _isDropSelected ? const Color(0xFF0D1B68) : Colors.black12,
                  width: _isDropSelected ? 1.5 : 1.0,
                ),
                boxShadow: [
                  BoxShadow(color: Colors.black.withOpacity(0.05), blurRadius: 8, offset: const Offset(0, 3)),
                ],
              ),
              child: TextField(
                controller: _dropController,
                focusNode: _dropFocusNode,
                decoration: InputDecoration(
                  prefixIcon: const Icon(Icons.search, color: Color(0xFF0D1B68)),
                  hintText: 'Where to? (Pick landmark)',
                  hintStyle: TextStyle(color: Colors.grey.shade600, fontSize: 14),
                  suffixIcon: _dropController.text.isNotEmpty
                      ? IconButton(
                          icon: const Icon(Icons.clear, size: 18, color: Colors.grey),
                          onPressed: () {
                            _dropController.clear();
                            setState(() {
                              _selectedDropLocation = null;
                              _isDropSelected = false;
                              _filteredSuggestions = [];
                            });
                          },
                        )
                      : const Icon(Icons.location_on_outlined, color: Color(0xFFFF8C00)),
                  border: InputBorder.none,
                  contentPadding: const EdgeInsets.symmetric(horizontal: 16, vertical: 14),
                ),
              ),
            ),

            // Autocomplete Dropdown - Shown ONLY on Typing in Drop Box
            if (_filteredSuggestions.isNotEmpty)
              Container(
                margin: const EdgeInsets.only(top: 6),
                decoration: BoxDecoration(
                  color: Colors.white,
                  borderRadius: BorderRadius.circular(12),
                  border: Border.all(color: const Color(0xFF0D1B68).withOpacity(0.2)),
                  boxShadow: [
                    BoxShadow(color: Colors.black.withOpacity(0.08), blurRadius: 10, offset: const Offset(0, 4)),
                  ],
                ),
                constraints: const BoxConstraints(maxHeight: 200),
                child: ListView.separated(
                  shrinkWrap: true,
                  padding: EdgeInsets.zero,
                  itemCount: _filteredSuggestions.length,
                  separatorBuilder: (_, __) => const Divider(height: 1),
                  itemBuilder: (context, index) {
                    final lm = _filteredSuggestions[index];
                    return ListTile(
                      dense: true,
                      leading: CircleAvatar(
                        radius: 14,
                        backgroundColor: const Color(0xFFFF8C00).withOpacity(0.15),
                        child: Icon(lm['icon'] as IconData, size: 14, color: const Color(0xFFFF8C00)),
                      ),
                      title: Text(
                        lm['name'] as String,
                        style: const TextStyle(fontWeight: FontWeight.bold, fontSize: 13, color: Color(0xFF0D1B68)),
                      ),
                      subtitle: Text(
                        '${lm['sub']} • ${lm['dist']}',
                        style: const TextStyle(fontSize: 11, color: Colors.grey),
                      ),
                      trailing: const Icon(Icons.arrow_forward_ios, size: 12, color: Colors.grey),
                      onTap: () => _selectDropLocation(lm['name'] as String),
                    );
                  },
                ),
              ),

            const SizedBox(height: 16),

            // MIDDLE - LIVE MAP (NEW POSITION)
            // Show "12 Vehicles Live in 3km - Kolkata Transit Canvas" with Current Location Dot
            Container(
              height: 200,
              width: double.infinity,
              decoration: BoxDecoration(
                color: const Color(0xFFE2E8F0),
                borderRadius: BorderRadius.circular(16),
                border: Border.all(color: Colors.black12),
                boxShadow: [
                  BoxShadow(color: Colors.black.withOpacity(0.05), blurRadius: 8, offset: const Offset(0, 3)),
                ],
              ),
              child: ClipRRect(
                borderRadius: BorderRadius.circular(16),
                child: Stack(
                  children: [
                    // Grid background simulating map tiles
                    CustomPaint(
                      size: Size.infinite,
                      painter: _TransitMapGridPainter(),
                    ),
                    // Center content
                    Center(
                      child: Column(
                        mainAxisAlignment: MainAxisAlignment.center,
                        children: [
                          Container(
                            padding: const EdgeInsets.all(10),
                            decoration: BoxDecoration(
                              color: const Color(0xFF0D1B68).withOpacity(0.1),
                              shape: BoxShape.circle,
                            ),
                            child: const Icon(Icons.navigation, size: 36, color: Color(0xFF0D1B68)),
                          ),
                          const SizedBox(height: 6),
                          const Text(
                            'Kolkata Transit Canvas',
                            style: TextStyle(fontWeight: FontWeight.bold, fontSize: 15, color: Color(0xFF0D1B68)),
                          ),
                          const SizedBox(height: 2),
                          Text(
                            _isDropSelected
                                ? 'Route: Talbanda ➔ $_selectedDropLocation'
                                : 'Park St • AJC Bose Rd • Exide Crossing • Salt Lake',
                            style: TextStyle(fontSize: 11, color: Colors.grey.shade700, fontWeight: FontWeight.w500),
                            textAlign: TextAlign.center,
                          ),
                        ],
                      ),
                    ),
                    // Current Location Animated Radar Dot
                    Positioned(
                      left: 45,
                      bottom: 40,
                      child: Row(
                        children: [
                          FadeTransition(
                            opacity: _pulseAnimation,
                            child: Container(
                              width: 14,
                              height: 14,
                              decoration: BoxDecoration(
                                color: const Color(0xFF2E8B57),
                                shape: BoxShape.circle,
                                border: Border.all(color: Colors.white, width: 2),
                              ),
                            ),
                          ),
                          const SizedBox(width: 6),
                          Container(
                            padding: const EdgeInsets.symmetric(horizontal: 6, vertical: 2),
                            decoration: BoxDecoration(
                              color: Colors.white,
                              borderRadius: BorderRadius.circular(8),
                              boxShadow: const [BoxShadow(color: Colors.black12, blurRadius: 4)],
                            ),
                            child: const Text('You Are Here', style: TextStyle(fontSize: 9, fontWeight: FontWeight.bold, color: Color(0xFF2E8B57))),
                          ),
                        ],
                      ),
                    ),
                    // Vehicles Live Badge
                    Positioned(
                      top: 12,
                      left: 12,
                      child: Container(
                        padding: const EdgeInsets.symmetric(horizontal: 10, vertical: 6),
                        decoration: BoxDecoration(
                          color: Colors.white,
                          borderRadius: BorderRadius.circular(20),
                          boxShadow: [BoxShadow(color: Colors.black.withOpacity(0.1), blurRadius: 4)],
                        ),
                        child: const Row(
                          mainAxisSize: MainAxisSize.min,
                          children: [
                            Icon(Icons.near_me, size: 14, color: Color(0xFF2E8B57)),
                            SizedBox(width: 4),
                            Text('12 Vehicles Live in 3km', style: TextStyle(fontSize: 11, fontWeight: FontWeight.bold, color: Color(0xFF0D1B68))),
                          ],
                        ),
                      ),
                    ),
                    // Map Status or Route Indicator
                    if (_isDropSelected)
                      Positioned(
                        top: 12,
                        right: 12,
                        child: Container(
                          padding: const EdgeInsets.symmetric(horizontal: 10, vertical: 6),
                          decoration: BoxDecoration(
                            color: const Color(0xFF2E8B57),
                            borderRadius: BorderRadius.circular(20),
                            boxShadow: [BoxShadow(color: Colors.black.withOpacity(0.1), blurRadius: 4)],
                          ),
                          child: const Row(
                            mainAxisSize: MainAxisSize.min,
                            children: [
                              Icon(Icons.check_circle, size: 14, color: Colors.white),
                              SizedBox(width: 4),
                              Text('Drop Locked', style: TextStyle(fontSize: 11, fontWeight: FontWeight.bold, color: Colors.white)),
                            ],
                          ),
                        ),
                      ),
                  ],
                ),
              ),
            ),

            const SizedBox(height: 18),

            // VEHICLE LIST - SHOWN ONLY AFTER USER SELECTS DROP LOCATION
            if (_isDropSelected) ...[
              Row(
                mainAxisAlignment: MainAxisAlignment.spaceBetween,
                children: [
                  const Text(
                    'Where to? Choose Service',
                    style: TextStyle(
                      fontSize: 16,
                      fontWeight: FontWeight.bold,
                      color: Color(0xFF0D1B68),
                    ),
                  ),
                  Text(
                    'To: $_selectedDropLocation',
                    style: const TextStyle(fontSize: 11, color: Color(0xFFFF8C00), fontWeight: FontWeight.bold),
                  ),
                ],
              ),
              const SizedBox(height: 12),

              // 6 SERVICES IN 2 ROWS (Row 1: Bike, Toto, Auto | Row 2: Mini, Sedan, SUV)
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
              const SizedBox(height: 18),
            ],

            // BOTTOM SECTION - KEEP AS IS:
            // Core Ride & Rental Services (Rent A Car Catalog + Hire Verified Driver)
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
            const SizedBox(height: 16),

            // NEW - BECOME A RIDER OR DRIVER BIG CARD WITH 3 BUTTONS (0=Driver, 1=Rent Owner, 2=Hire Driver)
            Card(
              shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(16)),
              elevation: 3,
              color: const Color(0xFF0D1B68),
              child: Padding(
                padding: const EdgeInsets.all(14),
                child: Column(
                  children: [
                    InkWell(
                      borderRadius: BorderRadius.circular(12),
                      onTap: () {
                        Navigator.push(
                          context,
                          MaterialPageRoute(
                            builder: (_) => const CreateDriverProfileScreen(initialTabIndex: 0),
                          ),
                        );
                      },
                      child: Row(
                        children: [
                          Container(
                            padding: const EdgeInsets.all(10),
                            decoration: BoxDecoration(
                              color: const Color(0xFFFF8C00).withOpacity(0.25),
                              shape: BoxShape.circle,
                            ),
                            child: const Icon(Icons.assignment_ind, color: Color(0xFFFF8C00), size: 26),
                          ),
                          const SizedBox(width: 14),
                          const Expanded(
                            child: Column(
                              crossAxisAlignment: CrossAxisAlignment.start,
                              children: [
                                Text(
                                  'Become a Rider or Driver',
                                  style: TextStyle(fontSize: 15, fontWeight: FontWeight.bold, color: Colors.white),
                                ),
                                SizedBox(height: 2),
                                Text(
                                  'Create Profile • Partner Onboarding • Join Us',
                                  style: TextStyle(fontSize: 11, color: Color(0xFFFFB366)),
                                ),
                              ],
                            ),
                          ),
                          const Icon(Icons.arrow_forward_ios, size: 16, color: Colors.white),
                        ],
                      ),
                    ),
                    const SizedBox(height: 10),
                    const Divider(color: Colors.white24, height: 1),
                    const SizedBox(height: 10),
                    Row(
                      children: [
                        Expanded(
                          child: InkWell(
                            onTap: () {
                              Navigator.push(
                                context,
                                MaterialPageRoute(
                                  builder: (_) => const CreateDriverProfileScreen(initialTabIndex: 0),
                                ),
                              );
                            },
                            child: Container(
                              padding: const EdgeInsets.symmetric(vertical: 6),
                              decoration: BoxDecoration(
                                color: Colors.white.withOpacity(0.12),
                                borderRadius: BorderRadius.circular(8),
                              ),
                              child: const Column(
                                children: [
                                  Icon(Icons.drive_eta, color: Color(0xFFFF8C00), size: 18),
                                  SizedBox(height: 2),
                                  Text('Driver (0)', style: TextStyle(color: Colors.white, fontSize: 10, fontWeight: FontWeight.bold)),
                                ],
                              ),
                            ),
                          ),
                        ),
                        const SizedBox(width: 6),
                        Expanded(
                          child: InkWell(
                            onTap: () {
                              Navigator.push(
                                context,
                                MaterialPageRoute(
                                  builder: (_) => const CreateDriverProfileScreen(initialTabIndex: 1),
                                ),
                              );
                            },
                            child: Container(
                              padding: const EdgeInsets.symmetric(vertical: 6),
                              decoration: BoxDecoration(
                                color: Colors.white.withOpacity(0.12),
                                borderRadius: BorderRadius.circular(8),
                              ),
                              child: const Column(
                                children: [
                                  Icon(Icons.car_rental, color: Color(0xFFFF8C00), size: 18),
                                  SizedBox(height: 2),
                                  Text('Rent Owner (1)', style: TextStyle(color: Colors.white, fontSize: 10, fontWeight: FontWeight.bold)),
                                ],
                              ),
                            ),
                          ),
                        ),
                        const SizedBox(width: 6),
                        Expanded(
                          child: InkWell(
                            onTap: () {
                              Navigator.push(
                                context,
                                MaterialPageRoute(
                                  builder: (_) => const CreateDriverProfileScreen(initialTabIndex: 2),
                                ),
                              );
                            },
                            child: Container(
                              padding: const EdgeInsets.symmetric(vertical: 6),
                              decoration: BoxDecoration(
                                color: Colors.white.withOpacity(0.12),
                                borderRadius: BorderRadius.circular(8),
                              ),
                              child: const Column(
                                children: [
                                  Icon(Icons.badge, color: Color(0xFFFF8C00), size: 18),
                                  SizedBox(height: 2),
                                  Text('Hire Driver (2)', style: TextStyle(color: Colors.white, fontSize: 10, fontWeight: FontWeight.bold)),
                                ],
                              ),
                            ),
                          ),
                        ),
                      ],
                    ),
                  ],
                ),
              ),
            ),
            const SizedBox(height: 16),

            // Book City Ride CTA Button
            SizedBox(
              width: double.infinity,
              height: 50,
              child: ElevatedButton(
                onPressed: () {
                  if (!_isDropSelected) {
                    _dropFocusNode.requestFocus();
                    ScaffoldMessenger.of(context).showSnackBar(
                      const SnackBar(
                        content: Text('Please type and select a Drop Location first!'),
                        backgroundColor: Color(0xFFFF8C00),
                      ),
                    );
                  } else {
                    Navigator.pushNamed(context, '/booking_confirmed');
                  }
                },
                style: ElevatedButton.styleFrom(
                  backgroundColor: _isDropSelected ? const Color(0xFF0D1B68) : Colors.grey.shade400,
                  shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(12)),
                ),
                child: Row(
                  mainAxisAlignment: MainAxisAlignment.center,
                  children: [
                    const Icon(Icons.navigation, color: Colors.white, size: 20),
                    const SizedBox(width: 8),
                    Text(
                      _isDropSelected
                          ? 'Book $_selectedService Now ($selectedFare)'
                          : 'Enter Drop Location to Book Ride',
                      style: const TextStyle(color: Colors.white, fontWeight: FontWeight.bold, fontSize: 14),
                    ),
                  ],
                ),
              ),
            ),
            const SizedBox(height: 24),
          ],
        ),
      ),
    );
  }

  String get selectedFare {
    switch (_selectedService) {
      case 'Bike Taxi': return '₹40';
      case 'Toto E-Rickshaw': return '₹30';
      case 'Auto Rickshaw': return '₹60';
      case 'Mini Cab': return '₹80';
      case 'Sedan': return '₹100';
      case 'SUV': return '₹150';
      default: return '₹100';
    }
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

class _TransitMapGridPainter extends CustomPainter {
  @override
  void paint(Canvas canvas, Size size) {
    final paintLine = Paint()
      ..color = Colors.white.withOpacity(0.4)
      ..strokeWidth = 1.5;

    final paintRoad = Paint()
      ..color = const Color(0xFFCBD5E1)
      ..strokeWidth = 4.0;

    // Draw grid roads
    for (double i = 0; i < size.width; i += 40) {
      canvas.drawLine(Offset(i, 0), Offset(i, size.height), paintLine);
    }
    for (double j = 0; j < size.height; j += 40) {
      canvas.drawLine(Offset(0, j), Offset(size.width, j), paintLine);
    }

    // Draw main arterial roads
    canvas.drawLine(Offset(0, size.height * 0.4), Offset(size.width, size.height * 0.4), paintRoad);
    canvas.drawLine(Offset(size.width * 0.6, 0), Offset(size.width * 0.6, size.height), paintRoad);
  }

  @override
  bool shouldRepaint(covariant CustomPainter oldDelegate) => false;
}

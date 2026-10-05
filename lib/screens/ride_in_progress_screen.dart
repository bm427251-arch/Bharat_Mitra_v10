import 'package:flutter/material.dart';

class RideInProgressScreen extends StatefulWidget {
  const RideInProgressScreen({Key? key}) : super(key: key);

  @override
  State<RideInProgressScreen> createState() => _RideInProgressScreenState();
}

class _RideInProgressScreenState extends State<RideInProgressScreen> {
  double _currentSpeedKmh = 75.0; // Defaults to 75 km/h to demonstrate overspeed trigger
  final double _speedLimit = 60.0;
  bool _isNightMode = false;
  bool _harshBrakingDetected = false;

  @override
  void initState() {
    super.initState();
    // Night Mode check: hour >= 19
    final hour = DateTime.now().hour;
    if (hour >= 19) {
      _isNightMode = true;
    }
  }

  void _showSosDialog() {
    showDialog(
      context: context,
      builder: (ctx) => AlertDialog(
        title: const Row(
          children: [
            Icon(Icons.warning, color: Colors.red),
            SizedBox(width: 8),
            Text('Emergency SOS', style: TextStyle(color: Colors.red, fontWeight: FontWeight.bold)),
          ],
        ),
        content: const Text(
          'Instant connection to 24/7 Police Dispatch (112) and WhatsApp Live Location broadcast to trusted emergency contacts.',
        ),
        actions: [
          TextButton(
            onPressed: () => Navigator.pop(ctx),
            child: const Text('Cancel'),
          ),
          ElevatedButton.icon(
            onPressed: () {
              Navigator.pop(ctx);
              ScaffoldMessenger.of(context).showSnackBar(
                const SnackBar(content: Text('Dialing 112 & sharing live GPS coordinates on WhatsApp...')),
              );
            },
            icon: const Icon(Icons.call, color: Colors.white),
            label: const Text('Dial 112 Dispatch', style: TextStyle(color: Colors.white)),
            style: ElevatedButton.styleFrom(backgroundColor: Colors.red),
          ),
        ],
      ),
    );
  }

  void _showRouteDeviationDialog() {
    showDialog(
      context: context,
      builder: (ctx) => AlertDialog(
        title: const Text('Route Deviation Warning', style: TextStyle(fontWeight: FontWeight.bold, color: Color(0xFFFF8C00))),
        content: const Text(
          'Vehicle is 580m away from the recommended polyline route. Safety desk has been alerted.',
        ),
        actions: [
          TextButton(
            onPressed: () => Navigator.pop(ctx),
            child: const Text('Dismiss'),
          ),
          ElevatedButton(
            onPressed: () {
              Navigator.pop(ctx);
              ScaffoldMessenger.of(context).showSnackBar(
                const SnackBar(content: Text('Notified Bharat Mitra 24/7 Safety Desk.')),
              );
            },
            style: ElevatedButton.styleFrom(backgroundColor: const Color(0xFF0D1B68)),
            child: const Text('Notify Safety Desk', style: TextStyle(color: Colors.white)),
          ),
        ],
      ),
    );
  }

  @override
  Widget build(BuildContext context) {
    final bool isOverspeed = _currentSpeedKmh > _speedLimit;

    return Scaffold(
      body: Stack(
        children: [
          // 4. Center: Full Map Canvas (with Park St, AJC Bose Rd, Exide Crossing, Kolkata)
          Container(
            width: double.infinity,
            height: double.infinity,
            color: _isNightMode ? const Color(0xFF1E293B) : const Color(0xFFE2E8F0),
            child: Stack(
              children: [
                // Simulated Blue Polyline Route
                Center(
                  child: Container(
                    width: 280,
                    height: 8,
                    decoration: BoxDecoration(
                      color: const Color(0xFF1E70DC),
                      borderRadius: BorderRadius.circular(4),
                      boxShadow: [
                        BoxShadow(
                          color: const Color(0xFF1E70DC).withOpacity(0.5),
                          blurRadius: 8,
                        ),
                      ],
                    ),
                  ),
                ),
                // Driver Marker (Blue car)
                Center(
                  child: Transform.translate(
                    offset: const Offset(20, -2),
                    child: Container(
                      padding: const EdgeInsets.all(8),
                      decoration: const BoxDecoration(
                        color: Color(0xFF0D1B68),
                        shape: BoxShape.circle,
                        boxShadow: [BoxShadow(color: Colors.black26, blurRadius: 6)],
                      ),
                      child: const Icon(Icons.directions_car, color: Colors.white, size: 20),
                    ),
                  ),
                ),
                // Rider Dot Marker
                Center(
                  child: Transform.translate(
                    offset: const Offset(-130, -2),
                    child: Container(
                      width: 16,
                      height: 16,
                      decoration: BoxDecoration(
                        color: const Color(0xFF1E70DC),
                        shape: BoxShape.circle,
                        border: Border.all(color: Colors.white, width: 2),
                      ),
                    ),
                  ),
                ),
                // Map Labels
                Positioned(
                  top: 180,
                  left: 40,
                  child: Text(
                    'Park St',
                    style: TextStyle(
                      fontWeight: FontWeight.bold,
                      fontSize: 13,
                      color: _isNightMode ? Colors.white70 : Colors.black54,
                    ),
                  ),
                ),
                Positioned(
                  top: 240,
                  right: 50,
                  child: Text(
                    'AJC Bose Rd',
                    style: TextStyle(
                      fontWeight: FontWeight.bold,
                      fontSize: 13,
                      color: _isNightMode ? Colors.white70 : Colors.black54,
                    ),
                  ),
                ),
                Positioned(
                  bottom: 310,
                  left: 80,
                  child: Text(
                    'Exide Crossing',
                    style: TextStyle(
                      fontWeight: FontWeight.bold,
                      fontSize: 13,
                      color: _isNightMode ? Colors.white70 : Colors.black54,
                    ),
                  ),
                ),
                Positioned(
                  bottom: 270,
                  right: 40,
                  child: Text(
                    'Kolkata',
                    style: TextStyle(
                      fontWeight: FontWeight.bold,
                      fontSize: 18,
                      color: _isNightMode ? Colors.white30 : Colors.black26,
                    ),
                  ),
                ),
              ],
            ),
          ),

          // 1. Overspeed Banner (conditional) Orange #FF6B00 with speaker icon
          if (isOverspeed)
            Positioned(
              top: 0,
              left: 0,
              right: 0,
              child: SafeArea(
                bottom: false,
                child: Container(
                  color: const Color(0xFFFF6B00),
                  padding: const EdgeInsets.symmetric(horizontal: 16, vertical: 8),
                  child: Row(
                    children: [
                      const Icon(Icons.campaign, color: Colors.white, size: 22),
                      const SizedBox(width: 8),
                      Expanded(
                        child: Text(
                          '⚠️ Overspeed Alert - Driver is overspeeding ${_currentSpeedKmh.toInt()} km/h - Safe Driving Alert',
                          style: const TextStyle(color: Colors.white, fontWeight: FontWeight.bold, fontSize: 11),
                        ),
                      ),
                    ],
                  ),
                ),
              ),
            ),

          // 2. Top Bar: BHARAT MITRA logo left, SOS red button right
          Positioned(
            top: isOverspeed ? 46 : 0,
            left: 0,
            right: 0,
            child: SafeArea(
              bottom: false,
              child: Padding(
                padding: const EdgeInsets.symmetric(horizontal: 12, vertical: 8),
                child: Row(
                  mainAxisAlignment: MainAxisAlignment.spaceBetween,
                  children: [
                    Container(
                      padding: const EdgeInsets.symmetric(horizontal: 8, vertical: 4),
                      decoration: BoxDecoration(
                        color: const Color(0xFF0D1B68).withOpacity(0.9),
                        borderRadius: BorderRadius.circular(12),
                      ),
                      child: Row(
                        children: [
                          IconButton(
                            icon: const Icon(Icons.arrow_back, color: Colors.white, size: 20),
                            onPressed: () => Navigator.pop(context),
                          ),
                          Image.asset('assets/logo.png', width: 100, height: 32, fit: BoxFit.contain),
                        ],
                      ),
                    ),
                    Row(
                      children: [
                        // Night Mode toggle button
                        IconButton(
                          onPressed: () => setState(() => _isNightMode = !_isNightMode),
                          icon: Icon(
                            _isNightMode ? Icons.dark_mode : Icons.light_mode,
                            color: const Color(0xFF0D1B68),
                          ),
                        ),
                        // SOS Red Button
                        ElevatedButton.icon(
                          onPressed: _showSosDialog,
                          icon: const Icon(Icons.warning, color: Colors.white, size: 16),
                          label: const Text('SOS', style: TextStyle(color: Colors.white, fontWeight: FontWeight.bold)),
                          style: ElevatedButton.styleFrom(
                            backgroundColor: Colors.red,
                            shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(20)),
                          ),
                        ),
                      ],
                    ),
                  ],
                ),
              ),
            ),
          ),

          // 3. Left Middle: Speed Card
          Positioned(
            left: 16,
            top: 140,
            child: Container(
              padding: const EdgeInsets.all(12),
              decoration: BoxDecoration(
                color: Colors.white,
                borderRadius: BorderRadius.circular(14),
                border: Border.all(color: Colors.black12),
                boxShadow: [BoxShadow(color: Colors.black12, blurRadius: 6)],
              ),
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  Text(
                    '${_currentSpeedKmh.toInt()} km/h',
                    style: TextStyle(
                      fontSize: 22,
                      fontWeight: FontWeight.w900,
                      color: isOverspeed ? Colors.red : const Color(0xFF2E8B57),
                    ),
                  ),
                  const Text('Speed Limit 60 km/h', style: TextStyle(fontSize: 10, color: Colors.grey)),
                  const SizedBox(height: 8),
                  // Test Speed Chips
                  Row(
                    children: [45.0, 68.0, 75.0, 82.0].map((s) {
                      final isSel = _currentSpeedKmh == s;
                      return Padding(
                        padding: const EdgeInsets.only(right: 4),
                        child: GestureDetector(
                          onTap: () => setState(() => _currentSpeedKmh = s),
                          child: Container(
                            padding: const EdgeInsets.symmetric(horizontal: 5, vertical: 2),
                            decoration: BoxDecoration(
                              color: isSel ? const Color(0xFF0D1B68) : const Color(0xFFF1F5F9),
                              borderRadius: BorderRadius.circular(4),
                            ),
                            child: Text(
                              '${s.toInt()}',
                              style: TextStyle(fontSize: 9, fontWeight: FontWeight.bold, color: isSel ? Colors.white : Colors.black87),
                            ),
                          ),
                        ),
                      );
                    }).toList(),
                  ),
                  const SizedBox(height: 6),
                  // Deviation & Harsh Braking triggers
                  Row(
                    children: [
                      GestureDetector(
                        onTap: _showRouteDeviationDialog,
                        child: Container(
                          padding: const EdgeInsets.symmetric(horizontal: 4, vertical: 2),
                          decoration: BoxDecoration(
                            color: const Color(0xFFFF8C00).withOpacity(0.15),
                            borderRadius: BorderRadius.circular(4),
                          ),
                          child: const Text('Test >500m', style: TextStyle(fontSize: 9, color: Color(0xFFFF8C00), fontWeight: FontWeight.bold)),
                        ),
                      ),
                      const SizedBox(width: 4),
                      GestureDetector(
                        onTap: () {
                          setState(() => _harshBrakingDetected = true);
                          ScaffoldMessenger.of(context).showSnackBar(
                            const SnackBar(content: Text('⚠️ Harsh Braking Detected! Telemetry logged.')),
                          );
                        },
                        child: Container(
                          padding: const EdgeInsets.symmetric(horizontal: 4, vertical: 2),
                          decoration: BoxDecoration(
                            color: Colors.red.withOpacity(0.15),
                            borderRadius: BorderRadius.circular(4),
                          ),
                          child: const Text('Braking', style: TextStyle(fontSize: 9, color: Colors.red, fontWeight: FontWeight.bold)),
                        ),
                      ),
                    ],
                  ),
                ],
              ),
            ),
          ),

          // 5. Bottom: Driver Card & 6. Bottom-most: Voice Nav
          Positioned(
            left: 12,
            right: 12,
            bottom: 16,
            child: Column(
              mainAxisSize: MainAxisSize.min,
              children: [
                // 5. Driver Card
                Container(
                  padding: const EdgeInsets.all(14),
                  decoration: BoxDecoration(
                    color: Colors.white,
                    borderRadius: BorderRadius.circular(16),
                    boxShadow: [BoxShadow(color: Colors.black12, blurRadius: 8)],
                  ),
                  child: Column(
                    children: [
                      Row(
                        children: [
                          const CircleAvatar(
                            radius: 22,
                            backgroundColor: Color(0xFF0D1B68),
                            child: Icon(Icons.person, color: Colors.white),
                          ),
                          const SizedBox(width: 10),
                          Expanded(
                            child: Column(
                              crossAxisAlignment: CrossAxisAlignment.start,
                              children: const [
                                Text('Rohan Kumar (4.8 ★)', style: TextStyle(fontWeight: FontWeight.bold, fontSize: 14)),
                                Text('Toyota Sedan • DL 01 AB 1234', style: TextStyle(fontSize: 11, color: Colors.grey)),
                              ],
                            ),
                          ),
                          const Text('ETA 5 mins', style: TextStyle(fontWeight: FontWeight.bold, color: Color(0xFFFF8C00))),
                        ],
                      ),
                      const Divider(height: 20),
                      Row(
                        children: [
                          // Blue Share Live Trip Button
                          Expanded(
                            child: ElevatedButton.icon(
                              onPressed: () {
                                ScaffoldMessenger.of(context).showSnackBar(
                                  const SnackBar(content: Text('Live link: https://bharatmitra.app/trip/live?lat=22.6534&lng=88.4449 shared!')),
                                );
                              },
                              icon: const Icon(Icons.share, size: 16, color: Colors.white),
                              label: const Text('Share Live Trip', style: TextStyle(color: Colors.white, fontSize: 12, fontWeight: FontWeight.bold)),
                              style: ElevatedButton.styleFrom(
                                backgroundColor: const Color(0xFF0D1B68),
                                shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(8)),
                              ),
                            ),
                          ),
                          const SizedBox(width: 8),
                          // End Ride & Rate
                          Expanded(
                            child: ElevatedButton.icon(
                              onPressed: () => Navigator.pushNamed(context, '/rating'),
                              icon: const Icon(Icons.check_circle, size: 16, color: Colors.white),
                              label: const Text('End Ride & Rate', style: TextStyle(color: Colors.white, fontSize: 12, fontWeight: FontWeight.bold)),
                              style: ElevatedButton.styleFrom(
                                backgroundColor: const Color(0xFFFF8C00),
                                shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(8)),
                              ),
                            ),
                          ),
                        ],
                      ),
                    ],
                  ),
                ),
                const SizedBox(height: 8),

                // 6. Bottom-most: Voice Navigation Banner
                Container(
                  width: double.infinity,
                  padding: const EdgeInsets.symmetric(horizontal: 14, vertical: 10),
                  decoration: BoxDecoration(
                    color: const Color(0xFF0D1B68),
                    borderRadius: BorderRadius.circular(12),
                  ),
                  child: const Row(
                    children: [
                      Icon(Icons.volume_up, color: Color(0xFFFFB366), size: 18),
                      SizedBox(width: 8),
                      Expanded(
                        child: Text(
                          'In 300m, turn left onto AJC Bose Rd',
                          style: TextStyle(color: Colors.white, fontWeight: FontWeight.w600, fontSize: 12),
                          overflow: TextOverflow.ellipsis,
                        ),
                      ),
                    ],
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

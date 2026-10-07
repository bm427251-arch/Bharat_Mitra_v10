import 'package:flutter/material.dart';
import '../utils/logo_helper.dart';

class RatingScreen extends StatefulWidget {
  const RatingScreen({Key? key}) : super(key: key);

  @override
  State<RatingScreen> createState() => _RatingScreenState();
}

class _RatingScreenState extends State<RatingScreen> with SingleTickerProviderStateMixin {
  late TabController _tabController;

  // Screen 1: Rider Feedback State
  int _riderStars = 5;
  int _selectedTip = 50;
  bool _cleanVehicle = true;
  bool _politeDriver = true;
  bool _onTime = true;
  bool _safeDriving = true;
  bool _goodRoute = true;
  final _commentController = TextEditingController();

  // Screen 2: Driver to Customer State
  int _driverStars = 5;
  bool _punctualRespectful = true;

  @override
  void initState() {
    super.initState();
    _tabController = TabController(length: 2, vsync: this);
  }

  @override
  void dispose() {
    _tabController.dispose();
    _commentController.dispose();
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
            const Text('Trip Feedback & Rating', style: TextStyle(fontWeight: FontWeight.bold, fontSize: 16, color: Colors.white)),
            const AppLogo(width: 90, height: 30, fit: BoxFit.contain),
          ],
        ),
        bottom: TabBar(
          controller: _tabController,
          indicatorColor: const Color(0xFFFF8C00),
          labelColor: Colors.white,
          unselectedLabelColor: Colors.white60,
          labelStyle: const TextStyle(fontWeight: FontWeight.bold),
          tabs: const [
            Tab(text: 'Rider Feedback'),
            Tab(text: 'Driver to Customer'),
          ],
        ),
      ),
      body: TabBarView(
        controller: _tabController,
        children: [
          // SCREEN 1: RIDER FEEDBACK
          SingleChildScrollView(
            padding: const EdgeInsets.all(16.0),
            child: Column(
              children: [
                // Ride Completed Green Tick
                Container(
                  width: 60,
                  height: 60,
                  decoration: BoxDecoration(
                    color: const Color(0xFF2E8B57).withOpacity(0.15),
                    shape: BoxShape.circle,
                  ),
                  child: const Icon(Icons.check_circle, color: Color(0xFF2E8B57), size: 36),
                ),
                const SizedBox(height: 8),
                const Text('Ride Completed', style: TextStyle(fontWeight: FontWeight.w900, fontSize: 18, color: Color(0xFF0D1B68))),
                const Text('Driver Rohan • Toyota Sedan DL 01 AB 1234', style: TextStyle(fontSize: 12, color: Colors.grey)),
                const SizedBox(height: 16),

                // 5 Stars
                Row(
                  mainAxisAlignment: MainAxisAlignment.center,
                  children: List.generate(5, (index) {
                    final star = index + 1;
                    return IconButton(
                      icon: Icon(
                        star <= _riderStars ? Icons.star : Icons.star_border,
                        color: star <= _riderStars ? const Color(0xFFFF8C00) : Colors.grey,
                        size: 36,
                      ),
                      onPressed: () => setState(() => _riderStars = star),
                    );
                  }),
                ),
                const SizedBox(height: 16),

                // What went well checkboxes
                Container(
                  width: double.infinity,
                  padding: const EdgeInsets.all(14),
                  decoration: BoxDecoration(
                    color: Colors.white,
                    borderRadius: BorderRadius.circular(16),
                    border: Border.all(color: Colors.black12),
                  ),
                  child: Column(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      const Text('What went well?', style: TextStyle(fontWeight: FontWeight.bold, fontSize: 14, color: Color(0xFF0D1B68))),
                      const SizedBox(height: 8),
                      _buildCheckbox('Clean Vehicle', _cleanVehicle, (v) => setState(() => _cleanVehicle = v!)),
                      _buildCheckbox('Polite & Courteous Driver', _politeDriver, (v) => setState(() => _politeDriver = v!)),
                      _buildCheckbox('On Time Arrival', _onTime, (v) => setState(() => _onTime = v!)),
                      _buildCheckbox('Safe Driving & Speed Limit', _safeDriving, (v) => setState(() => _safeDriving = v!)),
                      _buildCheckbox('Good Route Taken', _goodRoute, (v) => setState(() => _goodRoute = v!)),
                    ],
                  ),
                ),
                const SizedBox(height: 16),

                // Tip Driver (₹20, ₹50, ₹100 orange chips)
                Container(
                  width: double.infinity,
                  padding: const EdgeInsets.all(14),
                  decoration: BoxDecoration(
                    color: Colors.white,
                    borderRadius: BorderRadius.circular(16),
                    border: Border.all(color: Colors.black12),
                  ),
                  child: Column(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      const Text('Tip Driver (100% goes to Rohan)', style: TextStyle(fontWeight: FontWeight.bold, fontSize: 14, color: Color(0xFF0D1B68))),
                      const SizedBox(height: 10),
                      Row(
                        mainAxisAlignment: MainAxisAlignment.spaceEvenly,
                        children: [20, 50, 100].map((tip) {
                          final isSel = _selectedTip == tip;
                          return ChoiceChip(
                            label: Text('₹$tip', style: TextStyle(color: isSel ? Colors.white : const Color(0xFFFF8C00), fontWeight: FontWeight.bold)),
                            selected: isSel,
                            selectedColor: const Color(0xFFFF8C00),
                            onSelected: (_) => setState(() => _selectedTip = isSel ? 0 : tip),
                          );
                        }).toList(),
                      ),
                    ],
                  ),
                ),
                const SizedBox(height: 16),

                // Comments Box
                TextField(
                  controller: _commentController,
                  maxLines: 2,
                  decoration: InputDecoration(
                    labelText: 'Comments (Optional)',
                    hintText: 'Share your trip experience...',
                    border: OutlineInputBorder(borderRadius: BorderRadius.circular(12)),
                    filled: true,
                    fillColor: Colors.white,
                  ),
                ),
                const SizedBox(height: 16),

                // Submit Orange Button
                SizedBox(
                  width: double.infinity,
                  height: 48,
                  child: ElevatedButton(
                    onPressed: () {
                      ScaffoldMessenger.of(context).showSnackBar(
                        const SnackBar(content: Text('Feedback submitted! Thank you for riding Bharat Mitra.')),
                      );
                      Navigator.pop(context);
                    },
                    style: ElevatedButton.styleFrom(
                      backgroundColor: const Color(0xFFFF8C00),
                      shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(12)),
                    ),
                    child: const Text('Submit Rating', style: TextStyle(color: Colors.white, fontWeight: FontWeight.bold, fontSize: 15)),
                  ),
                ),
                const SizedBox(height: 16),
              ],
            ),
          ),

          // SCREEN 2: DRIVER TO CUSTOMER
          SingleChildScrollView(
            padding: const EdgeInsets.all(16.0),
            child: Column(
              children: [
                const SizedBox(height: 10),
                const CircleAvatar(
                  radius: 30,
                  backgroundColor: Color(0xFF0D1B68),
                  child: Icon(Icons.person, color: Colors.white, size: 36),
                ),
                const SizedBox(height: 8),
                const Text('Rate Passenger Aman', style: TextStyle(fontWeight: FontWeight.bold, fontSize: 18, color: Color(0xFF0D1B68))),
                const Text('Help maintain high quality community standards', style: TextStyle(fontSize: 12, color: Colors.grey)),
                const SizedBox(height: 16),

                // 5 Stars
                Row(
                  mainAxisAlignment: MainAxisAlignment.center,
                  children: List.generate(5, (index) {
                    final star = index + 1;
                    return IconButton(
                      icon: Icon(
                        star <= _driverStars ? Icons.star : Icons.star_border,
                        color: star <= _driverStars ? const Color(0xFF2E8B57) : Colors.grey,
                        size: 36,
                      ),
                      onPressed: () => setState(() => _driverStars = star),
                    );
                  }),
                ),
                const SizedBox(height: 16),

                // Punctual & Respectful Checkbox
                Card(
                  shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(14)),
                  child: CheckboxListTile(
                    title: const Text('Passenger was punctual & respectful', style: TextStyle(fontWeight: FontWeight.bold, fontSize: 13)),
                    value: _punctualRespectful,
                    activeColor: const Color(0xFF2E8B57),
                    onChanged: (v) => setState(() => _punctualRespectful = v!),
                  ),
                ),
                const SizedBox(height: 16),

                // Block User & Report Actions
                Row(
                  children: [
                    Expanded(
                      child: ElevatedButton.icon(
                        onPressed: () {
                          ScaffoldMessenger.of(context).showSnackBar(
                            const SnackBar(content: Text('Passenger Aman blocked from future matchings.')),
                          );
                        },
                        icon: const Icon(Icons.block, size: 16, color: Colors.white),
                        label: const Text('Block User', style: TextStyle(color: Colors.white, fontWeight: FontWeight.bold)),
                        style: ElevatedButton.styleFrom(backgroundColor: const Color(0xFFFF8C00)),
                      ),
                    ),
                    const SizedBox(width: 10),
                    Expanded(
                      child: ElevatedButton.icon(
                        onPressed: () {
                          ScaffoldMessenger.of(context).showSnackBar(
                            const SnackBar(content: Text('Incident report logged to safety desk.')),
                          );
                        },
                        icon: const Icon(Icons.report, size: 16, color: Colors.white),
                        label: const Text('Report', style: TextStyle(color: Colors.white, fontWeight: FontWeight.bold)),
                        style: ElevatedButton.styleFrom(backgroundColor: Colors.red),
                      ),
                    ),
                  ],
                ),
                const SizedBox(height: 20),

                // Submit Green Button
                SizedBox(
                  width: double.infinity,
                  height: 48,
                  child: ElevatedButton(
                    onPressed: () {
                      ScaffoldMessenger.of(context).showSnackBar(
                        const SnackBar(content: Text('Driver feedback submitted!')),
                      );
                      Navigator.pop(context);
                    },
                    style: ElevatedButton.styleFrom(
                      backgroundColor: const Color(0xFF2E8B57),
                      shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(12)),
                    ),
                    child: const Text('Submit Rating', style: TextStyle(color: Colors.white, fontWeight: FontWeight.bold, fontSize: 15)),
                  ),
                ),
              ],
            ),
          ),
        ],
      ),
    );
  }

  Widget _buildCheckbox(String title, bool val, ValueChanged<bool?> onChanged) {
    return CheckboxListTile(
      dense: true,
      contentPadding: EdgeInsets.zero,
      title: Text(title, style: const TextStyle(fontSize: 13)),
      value: val,
      activeColor: const Color(0xFFFF8C00),
      onChanged: onChanged,
    );
  }
}

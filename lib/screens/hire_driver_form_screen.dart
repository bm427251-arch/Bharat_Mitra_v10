import 'package:flutter/material.dart';

class HireDriverFormScreen extends StatefulWidget {
  const HireDriverFormScreen({Key? key}) : super(key: key);

  @override
  State<HireDriverFormScreen> createState() => _HireDriverFormScreenState();
}

class _HireDriverFormScreenState extends State<HireDriverFormScreen> {
  final _nameController = TextEditingController(text: 'Subhashish Mondal');
  final _phoneController = TextEditingController(text: '9831092812');
  final _addressController = TextEditingController(text: 'Salt Lake Sector 5, Kolkata');
  final _dlNumberController = TextEditingController(text: 'DL-04-2018-009124');
  final _dlExpiryController = TextEditingController(text: '2033-05-18');
  final _refNameController = TextEditingController(text: 'Gourab Roy (Fleet Incharge)');
  final _refPhoneController = TextEditingController(text: '9830012984');

  String _selectedExperience = '3-5 Years';
  String _selectedSkill = 'Both (Manual & Automatic)';

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
                Text('Become Hire Driver', style: TextStyle(fontWeight: FontWeight.bold, fontSize: 16, color: Colors.white)),
                Text('DL Only • No Vehicle Needed', style: TextStyle(fontSize: 11, color: Color(0xFFFFB366))),
              ],
            ),
            Image.asset('assets/logo.png', width: 90, height: 30, fit: BoxFit.contain),
          ],
        ),
      ),
      body: SingleChildScrollView(
        padding: const EdgeInsets.all(16.0),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            // Notice: DL Only, No RC Needed
            Container(
              padding: const EdgeInsets.all(12),
              decoration: BoxDecoration(
                color: const Color(0xFF2E8B57).withOpacity(0.1),
                borderRadius: BorderRadius.circular(12),
                border: Border.all(color: const Color(0xFF2E8B57)),
              ),
              child: const Row(
                children: [
                  Icon(Icons.check_circle, color: Color(0xFF2E8B57), size: 20),
                  SizedBox(width: 8),
                  Expanded(
                    child: Text(
                      'No vehicle or RC required! You are registering as a freelance commercial driver.',
                      style: TextStyle(color: Color(0xFF2E8B57), fontWeight: FontWeight.bold, fontSize: 12),
                    ),
                  ),
                ],
              ),
            ),
            const SizedBox(height: 16),

            _buildField('Full Name', _nameController, Icons.person),
            const SizedBox(height: 10),
            _buildField('Phone Number', _phoneController, Icons.phone, suffix: 'OTP Verified ✓'),
            const SizedBox(height: 10),
            _buildField('Current Address', _addressController, Icons.home),
            const SizedBox(height: 10),
            _buildField('Commercial Driving License (DL) Number', _dlNumberController, Icons.badge),
            const SizedBox(height: 10),
            _buildField('DL Expiry Date (Mandatory)', _dlExpiryController, Icons.calendar_today),
            const SizedBox(height: 12),

            // Experience Dropdown
            const Text('Driving Experience (Years)', style: TextStyle(fontWeight: FontWeight.bold, fontSize: 12, color: Color(0xFF0D1B68))),
            const SizedBox(height: 6),
            Container(
              padding: const EdgeInsets.symmetric(horizontal: 12),
              decoration: BoxDecoration(color: Colors.white, borderRadius: BorderRadius.circular(12), border: Border.all(color: Colors.black26)),
              child: DropdownButtonHideUnderline(
                child: DropdownButton<String>(
                  value: _selectedExperience,
                  isExpanded: true,
                  items: ['1-2 Years', '3-5 Years', '6-8 Years', '8-10 Years', '10+ Years'].map((exp) {
                    return DropdownMenuItem(value: exp, child: Text(exp));
                  }).toList(),
                  onChanged: (val) {
                    if (val != null) setState(() => _selectedExperience = val);
                  },
                ),
              ),
            ),
            const SizedBox(height: 12),

            // Driving Skill (Manual, Automatic, Both)
            const Text('Transmission Skill', style: TextStyle(fontWeight: FontWeight.bold, fontSize: 12, color: Color(0xFF0D1B68))),
            const SizedBox(height: 6),
            Container(
              padding: const EdgeInsets.symmetric(horizontal: 12),
              decoration: BoxDecoration(color: Colors.white, borderRadius: BorderRadius.circular(12), border: Border.all(color: Colors.black26)),
              child: DropdownButtonHideUnderline(
                child: DropdownButton<String>(
                  value: _selectedSkill,
                  isExpanded: true,
                  items: ['Manual Only', 'Automatic Only', 'Both (Manual & Automatic)'].map((s) {
                    return DropdownMenuItem(value: s, child: Text(s));
                  }).toList(),
                  onChanged: (val) {
                    if (val != null) setState(() => _selectedSkill = val);
                  },
                ),
              ),
            ),
            const SizedBox(height: 12),

            _buildField('Reference Person Name', _refNameController, Icons.people),
            const SizedBox(height: 10),
            _buildField('Reference Phone Number', _refPhoneController, Icons.phone_android),
            const SizedBox(height: 16),

            // Uploads Checklist (DL & Selfie only, NO RC!)
            const Text('Uploaded Documents (No RC Needed)', style: TextStyle(fontWeight: FontWeight.bold, fontSize: 13, color: Color(0xFF0D1B68))),
            const SizedBox(height: 8),
            Wrap(
              spacing: 8,
              runSpacing: 8,
              children: [
                _buildBadge('Aadhaar Card Uploaded ✓'),
                _buildBadge('DL Photo Front & Back ✓'),
                _buildBadge('Driver Live Selfie Verified ✓'),
              ],
            ),
            const SizedBox(height: 16),

            // Rate Chart info
            Container(
              padding: const EdgeInsets.all(12),
              decoration: BoxDecoration(
                color: Colors.white,
                borderRadius: BorderRadius.circular(12),
                border: Border.all(color: Colors.black12),
              ),
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: const [
                  Text('Standard Payout Rates', style: TextStyle(fontWeight: FontWeight.bold, fontSize: 13, color: Color(0xFF0D1B68))),
                  SizedBox(height: 4),
                  Text('• Hourly City: ₹100 - ₹120/hr\n• Full Day (8 hrs): ₹800 - ₹1,000\n• Outstation: ₹1,400/day + allowance', style: TextStyle(fontSize: 11, color: Colors.grey)),
                ],
              ),
            ),
            const SizedBox(height: 20),

            // Submit Button
            SizedBox(
              width: double.infinity,
              height: 48,
              child: ElevatedButton(
                onPressed: () => Navigator.pushNamed(context, '/hire_driver_subscription'),
                style: ElevatedButton.styleFrom(
                  backgroundColor: const Color(0xFFFF8C00),
                  shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(12)),
                ),
                child: const Text('Proceed to Activate (₹199)', style: TextStyle(fontWeight: FontWeight.bold, color: Colors.white, fontSize: 14)),
              ),
            ),
            const SizedBox(height: 16),
          ],
        ),
      ),
    );
  }

  Widget _buildField(String label, TextEditingController controller, IconData icon, {String? suffix}) {
    return TextField(
      controller: controller,
      decoration: InputDecoration(
        labelText: label,
        prefixIcon: Icon(icon, color: const Color(0xFF0D1B68), size: 20),
        suffixText: suffix,
        suffixStyle: const TextStyle(color: Color(0xFF2E8B57), fontWeight: FontWeight.bold, fontSize: 11),
        border: OutlineInputBorder(borderRadius: BorderRadius.circular(12)),
        filled: true,
        fillColor: Colors.white,
      ),
    );
  }

  Widget _buildBadge(String text) {
    return Container(
      padding: const EdgeInsets.symmetric(horizontal: 10, vertical: 6),
      decoration: BoxDecoration(
        color: const Color(0xFF2E8B57).withOpacity(0.15),
        borderRadius: BorderRadius.circular(20),
      ),
      child: Text(text, style: const TextStyle(color: Color(0xFF2E8B57), fontWeight: FontWeight.bold, fontSize: 11)),
    );
  }
}

import 'package:flutter/material.dart';

class RentOwnerFormScreen extends StatefulWidget {
  const RentOwnerFormScreen({Key? key}) : super(key: key);

  @override
  State<RentOwnerFormScreen> createState() => _RentOwnerFormScreenState();
}

class _RentOwnerFormScreenState extends State<RentOwnerFormScreen> {
  final _ownerName = TextEditingController(text: 'Rajib Banerjee');
  final _companyName = TextEditingController(text: 'Maa Tara Fleet & Logistics');
  final _phone = TextEditingController(text: '9830129841');
  final _aadhaar = TextEditingController(text: '4891 0291 8841');
  final _pan = TextEditingController(text: 'ABCDE1234F');
  final _tradeLicence = TextEditingController(text: 'TL/WB/2023/8812');
  final _bankIfsc = TextEditingController(text: 'SBI • A/C: 38810291048 • IFSC: SBIN0000024');
  final _vehicleModel = TextEditingController(text: 'Bolero Life Support Ambulance');

  final List<String> _allVehicleCategories = [
    'Ambulance',
    'Lorry',
    'Truck',
    'Pickup Van',
    'Bus',
    'Traveller',
    'Tempo',
    'JCB',
    'Crane',
    'Sedan',
    'SUV',
    'Hatchback',
    'MUV',
    'Luxury Car',
    'Electric Vehicle (EV)',
    'Bike Taxi',
    'Auto Rickshaw',
    'Toto (E-Rickshaw)',
    'Other Commercial Vehicle',
  ];

  String _selectedCategory = 'Ambulance';

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
                Text('Become Rent Owner', style: TextStyle(fontWeight: FontWeight.bold, fontSize: 16, color: Colors.white)),
                Text('Any Vehicle Can Be Added', style: TextStyle(fontSize: 11, color: Color(0xFFFFB366))),
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
            // Title card
            Container(
              width: double.infinity,
              padding: const EdgeInsets.all(14),
              decoration: BoxDecoration(
                color: const Color(0xFF0D1B68).withOpacity(0.08),
                borderRadius: BorderRadius.circular(12),
                border: Border.all(color: const Color(0xFF0D1B68).withOpacity(0.2)),
              ),
              child: const Text(
                'Add any commercial or private vehicle: Ambulance, Lorry, Truck, Bus, Tempo, or Car to Bharat Mitra.',
                style: TextStyle(fontWeight: FontWeight.bold, fontSize: 12, color: Color(0xFF0D1B68)),
              ),
            ),
            const SizedBox(height: 16),

            // Vehicle Category Dropdown (All Categories)
            const Text('Vehicle Category (All Types Supported)', style: TextStyle(fontWeight: FontWeight.bold, fontSize: 13, color: Color(0xFF0D1B68))),
            const SizedBox(height: 6),
            Container(
              padding: const EdgeInsets.symmetric(horizontal: 12),
              decoration: BoxDecoration(
                color: Colors.white,
                borderRadius: BorderRadius.circular(12),
                border: Border.all(color: Colors.black26),
              ),
              child: DropdownButtonHideUnderline(
                child: DropdownButton<String>(
                  value: _selectedCategory,
                  isExpanded: true,
                  items: _allVehicleCategories.map((cat) {
                    return DropdownMenuItem(value: cat, child: Text(cat, style: const TextStyle(fontWeight: FontWeight.bold)));
                  }).toList(),
                  onChanged: (val) {
                    if (val != null) setState(() => _selectedCategory = val);
                  },
                ),
              ),
            ),
            const SizedBox(height: 12),

            // Vehicle Model Free Text
            _buildField('Vehicle Model (e.g. Bolero Ambulance, Tata 407 Lorry)', _vehicleModel, Icons.local_shipping),
            const SizedBox(height: 10),

            _buildField('Owner Full Name', _ownerName, Icons.person),
            const SizedBox(height: 10),
            _buildField('Company / Agency Name (Optional)', _companyName, Icons.business),
            const SizedBox(height: 10),
            _buildField('Phone Number', _phone, Icons.phone, suffix: 'OTP Verified ✓'),
            const SizedBox(height: 10),
            _buildField('Aadhaar Number (Front & Back)', _aadhaar, Icons.badge),
            const SizedBox(height: 10),
            _buildField('PAN Number', _pan, Icons.credit_card),
            const SizedBox(height: 10),
            _buildField('Trade Licence / Registration', _tradeLicence, Icons.article),
            const SizedBox(height: 10),
            _buildField('Bank Account & IFSC', _bankIfsc, Icons.account_balance),
            const SizedBox(height: 16),

            // Uploaded Documents
            const Text('Required Vehicle Documents', style: TextStyle(fontWeight: FontWeight.bold, fontSize: 13, color: Color(0xFF0D1B68))),
            const SizedBox(height: 8),
            Wrap(
              spacing: 8,
              runSpacing: 8,
              children: [
                _buildDocBadge('RC Book Photo Uploaded ✓'),
                _buildDocBadge('Insurance Policy Photo ✓'),
                _buildDocBadge('PUC Certificate Uploaded ✓'),
                _buildDocBadge('Vehicle 4-Sides Photos ✓'),
              ],
            ),
            const SizedBox(height: 20),

            // Subscribe Button ₹299
            SizedBox(
              width: double.infinity,
              height: 48,
              child: ElevatedButton(
                onPressed: () => Navigator.pushNamed(context, '/rent_subscription'),
                style: ElevatedButton.styleFrom(
                  backgroundColor: const Color(0xFFFF8C00),
                  shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(12)),
                ),
                child: const Text('Proceed to Subscribe (₹299/mo)', style: TextStyle(fontWeight: FontWeight.bold, fontSize: 14, color: Colors.white)),
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

  Widget _buildDocBadge(String text) {
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

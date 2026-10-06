import 'package:flutter/material.dart';

class CreateDriverProfileScreen extends StatelessWidget {
  final int initialTabIndex;
  const CreateDriverProfileScreen({Key? key, this.initialTabIndex = 0}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return DefaultTabController(
      key: ValueKey(initialTabIndex),
      length: 3,
      initialIndex: initialTabIndex.clamp(0, 2),
      child: Scaffold(
        backgroundColor: const Color(0xFFF8FAFC),
        appBar: AppBar(
          backgroundColor: const Color(0xFF0D1B68),
          elevation: 0,
          title: Row(
            mainAxisAlignment: MainAxisAlignment.spaceBetween,
            children: [
              const Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                mainAxisSize: MainAxisSize.min,
                children: [
                  Text('Create Partner Profile', style: TextStyle(fontWeight: FontWeight.bold, fontSize: 16, color: Colors.white)),
                  Text('Join Bharat Mitra Mobility', style: TextStyle(fontSize: 11, color: Color(0xFFFFB366))),
                ],
              ),
              Image.asset('assets/logo.png', width: 90, height: 30, fit: BoxFit.contain),
            ],
          ),
          bottom: const TabBar(
            isScrollable: false,
            indicatorColor: Color(0xFFFF8C00),
            indicatorWeight: 3.5,
            labelColor: Colors.white,
            unselectedLabelColor: Colors.white70,
            labelStyle: TextStyle(fontWeight: FontWeight.bold, fontSize: 12),
            tabs: [
              Tab(icon: Icon(Icons.drive_eta, size: 18), text: 'Driver Profile'),
              Tab(icon: Icon(Icons.car_rental, size: 18), text: 'Rent A Car Owner'),
              Tab(icon: Icon(Icons.badge, size: 18), text: 'Hire Driver (DL)'),
            ],
          ),
        ),
        body: const TabBarView(
          children: [
            _DriverProfileForm(),
            _RentCarOwnerForm(),
            _HireDriverForm(),
          ],
        ),
      ),
    );
  }
}

// =============================================================
// TAB 0: DRIVER PROFILE (Vehicle Type, DL, RC, Insurance, PUC)
// =============================================================
class _DriverProfileForm extends StatefulWidget {
  const _DriverProfileForm({Key? key}) : super(key: key);

  @override
  State<_DriverProfileForm> createState() => _DriverProfileFormState();
}

class _DriverProfileFormState extends State<_DriverProfileForm> {
  String _selectedVehicle = 'Sedan';
  final List<String> _vehicleTypes = ['Toto', 'Auto', 'Bike', 'Mini', 'Sedan', 'SUV'];

  final _nameController = TextEditingController(text: 'Suresh Mondal');
  final _phoneController = TextEditingController(text: '9876543210');
  final _addressController = TextEditingController(text: 'Barasat North 24 Parganas');
  final _dlController = TextEditingController(text: 'DL-WB-2021-99214');
  final _dlExpiryController = TextEditingController(text: '2031-10-14');
  final _rcController = TextEditingController(text: 'WB-25-AB-1290');
  final _insuranceController = TextEditingController(text: 'INS-99214081');
  final _pucController = TextEditingController(text: 'PUC-771249');

  @override
  void dispose() {
    _nameController.dispose();
    _phoneController.dispose();
    _addressController.dispose();
    _dlController.dispose();
    _dlExpiryController.dispose();
    _rcController.dispose();
    _insuranceController.dispose();
    _pucController.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return SingleChildScrollView(
      padding: const EdgeInsets.all(16.0),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          // Vehicle Type Chips: Toto, Auto, Bike, Mini, Sedan, SUV
          const Text('Select Vehicle Type', style: TextStyle(fontWeight: FontWeight.bold, fontSize: 14, color: Color(0xFF0D1B68))),
          const SizedBox(height: 8),
          Wrap(
            spacing: 8,
            children: _vehicleTypes.map((type) {
              final isSel = _selectedVehicle == type;
              return ChoiceChip(
                label: Text(type),
                selected: isSel,
                selectedColor: const Color(0xFFFF8C00),
                labelStyle: TextStyle(color: isSel ? Colors.white : const Color(0xFF0D1B68), fontWeight: FontWeight.bold),
                onSelected: (_) => setState(() => _selectedVehicle = type),
              );
            }).toList(),
          ),
          const SizedBox(height: 16),

          // Required Fields
          _buildField('Full Name', _nameController, Icons.person),
          const SizedBox(height: 10),
          _buildField('Phone Number', _phoneController, Icons.phone, suffixText: 'OTP Verified ✓'),
          const SizedBox(height: 10),
          _buildField('Residential Address', _addressController, Icons.home),
          const SizedBox(height: 10),
          _buildField('DL Number', _dlController, Icons.badge),
          const SizedBox(height: 10),
          _buildField('DL Expiry Date (Mandatory)', _dlExpiryController, Icons.calendar_today),
          const SizedBox(height: 10),
          _buildField('RC Book Number (Mandatory)', _rcController, Icons.description),
          const SizedBox(height: 10),
          _buildField('Vehicle Insurance Policy Number', _insuranceController, Icons.shield),
          const SizedBox(height: 10),
          _buildField('PUC Certificate Number', _pucController, Icons.eco),
          const SizedBox(height: 16),

          // Uploaded Verification Documents
          const Text('Uploaded Verification Documents', style: TextStyle(fontWeight: FontWeight.bold, fontSize: 13, color: Color(0xFF0D1B68))),
          const SizedBox(height: 8),
          Wrap(
            spacing: 8,
            runSpacing: 8,
            children: [
              _buildDocChip('Driver Photo Attached ✓'),
              _buildDocChip('DL Front & Back Photo ✓'),
              _buildDocChip('RC Book Copy Attached ✓'),
              _buildDocChip('Vehicle 4 Photos Attached ✓'),
            ],
          ),
          const SizedBox(height: 16),

          // Notice
          Container(
            padding: const EdgeInsets.all(12),
            decoration: BoxDecoration(
              color: const Color(0xFF0D1B68).withOpacity(0.08),
              borderRadius: BorderRadius.circular(10),
              border: Border.all(color: const Color(0xFF0D1B68).withOpacity(0.2)),
            ),
            child: const Row(
              children: [
                Icon(Icons.security, color: Color(0xFF0D1B68), size: 20),
                SizedBox(width: 8),
                Expanded(
                  child: Text(
                    'Profile submitted for Bharat Mitra Admin & Police Verification. No Auto Approval.',
                    style: TextStyle(fontSize: 11, color: Color(0xFF0D1B68), fontWeight: FontWeight.bold),
                  ),
                ),
              ],
            ),
          ),
          const SizedBox(height: 20),

          // Submit Button
          SizedBox(
            width: double.infinity,
            height: 48,
            child: ElevatedButton(
              onPressed: () {
                ScaffoldMessenger.of(context).showSnackBar(
                  SnackBar(
                    content: Text('$_selectedVehicle Driver Profile submitted for Verification!'),
                    backgroundColor: const Color(0xFF2E8B57),
                  ),
                );
              },
              style: ElevatedButton.styleFrom(
                backgroundColor: const Color(0xFF0D1B68),
                shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(10)),
              ),
              child: const Text('Submit Driver Profile for Verification', style: TextStyle(color: Colors.white, fontWeight: FontWeight.bold, fontSize: 13)),
            ),
          ),
          const SizedBox(height: 24),
        ],
      ),
    );
  }
}

// =============================================================
// TAB 1: RENT A CAR OWNER (Fleet/Self-Drive/Commercial)
// =============================================================
class _RentCarOwnerForm extends StatefulWidget {
  const _RentCarOwnerForm({Key? key}) : super(key: key);

  @override
  State<_RentCarOwnerForm> createState() => _RentCarOwnerFormState();
}

class _RentCarOwnerFormState extends State<_RentCarOwnerForm> {
  final _fullNameController = TextEditingController(text: 'Rajib Banerjee');
  final _phoneController = TextEditingController(text: '9830129841');
  final _companyController = TextEditingController(text: 'Maa Tara Fleet & Logistics');
  final _aadhaarController = TextEditingController(text: '4891 0291 8841');
  final _panController = TextEditingController(text: 'ABCDE1234F');
  final _tradeLicenseController = TextEditingController(text: 'TL/WB/2023/8812');
  final _addressController = TextEditingController(text: 'VIP Road, Kaikhali, Kolkata - 700052');
  final _bankController = TextEditingController(text: 'SBI • A/C: 38810291048 • IFSC: SBIN0000024');
  final _rcController = TextEditingController(text: 'WB-19-TR-4910');
  final _insuranceController = TextEditingController(text: 'INS-77218401');
  final _pucController = TextEditingController(text: 'PUC-441092');
  final _rateController = TextEditingController(text: '₹2,500/day • ₹45,000/month');

  String _selectedCategory = 'Ambulance';
  final List<String> _categories = [
    'Ambulance', 'Lorry', 'Truck', 'Bus', 'Mini', 'Sedan', 'SUV',
    'Pickup Van', 'Traveller', 'Tempo', 'JCB', 'Crane', 'Luxury Car', 'EV',
  ];

  String _rentType = 'Commercial';

  @override
  void dispose() {
    _fullNameController.dispose();
    _phoneController.dispose();
    _companyController.dispose();
    _aadhaarController.dispose();
    _panController.dispose();
    _tradeLicenseController.dispose();
    _addressController.dispose();
    _bankController.dispose();
    _rcController.dispose();
    _insuranceController.dispose();
    _pucController.dispose();
    _rateController.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return SingleChildScrollView(
      padding: const EdgeInsets.all(16.0),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          Container(
            padding: const EdgeInsets.all(12),
            decoration: BoxDecoration(
              color: const Color(0xFFFF8C00).withOpacity(0.12),
              borderRadius: BorderRadius.circular(10),
              border: Border.all(color: const Color(0xFFFF8C00)),
            ),
            child: const Row(
              children: [
                Icon(Icons.directions_car, color: Color(0xFFFF8C00), size: 20),
                SizedBox(width: 8),
                Expanded(
                  child: Text(
                    'Rent Out Any Vehicle (Self-Drive & Commercial) • Earn Daily/Monthly',
                    style: TextStyle(fontSize: 11, fontWeight: FontWeight.bold, color: Color(0xFF0D1B68)),
                  ),
                ),
              ],
            ),
          ),
          const SizedBox(height: 16),

          // Vehicle Category Dropdown (Ambulance, Lorry, Truck, Bus, Mini, Sedan, SUV etc)
          const Text('Vehicle Category', style: TextStyle(fontWeight: FontWeight.bold, fontSize: 13, color: Color(0xFF0D1B68))),
          const SizedBox(height: 6),
          Container(
            padding: const EdgeInsets.symmetric(horizontal: 12),
            decoration: BoxDecoration(
              color: Colors.white,
              borderRadius: BorderRadius.circular(10),
              border: Border.all(color: Colors.black12),
            ),
            child: DropdownButtonHideUnderline(
              child: DropdownButton<String>(
                value: _selectedCategory,
                isExpanded: true,
                items: _categories.map((c) => DropdownMenuItem(value: c, child: Text(c))).toList(),
                onChanged: (val) => setState(() => _selectedCategory = val!),
              ),
            ),
          ),
          const SizedBox(height: 12),

          // Rent Type: Self-Drive / Commercial
          const Text('Rent Type', style: TextStyle(fontWeight: FontWeight.bold, fontSize: 13, color: Color(0xFF0D1B68))),
          const SizedBox(height: 6),
          Row(
            children: ['Commercial', 'Self-Drive', 'Both'].map((t) {
              final isSel = _rentType == t;
              return Padding(
                padding: const EdgeInsets.only(right: 8),
                child: ChoiceChip(
                  label: Text(t),
                  selected: isSel,
                  selectedColor: const Color(0xFFFF8C00),
                  labelStyle: TextStyle(color: isSel ? Colors.white : const Color(0xFF0D1B68), fontWeight: FontWeight.bold),
                  onSelected: (_) => setState(() => _rentType = t),
                ),
              );
            }).toList(),
          ),
          const SizedBox(height: 12),

          // Fields
          _buildField('Full Name', _fullNameController, Icons.person),
          const SizedBox(height: 10),
          _buildField('Phone Number', _phoneController, Icons.phone, suffixText: 'OTP Verified ✓'),
          const SizedBox(height: 10),
          _buildField('Company / Fleet Name', _companyController, Icons.business),
          const SizedBox(height: 10),
          _buildField('Aadhaar Card Number', _aadhaarController, Icons.badge),
          const SizedBox(height: 10),
          _buildField('PAN Card Number', _panController, Icons.credit_card),
          const SizedBox(height: 10),
          _buildField('Trade License / GSTIN', _tradeLicenseController, Icons.article),
          const SizedBox(height: 10),
          _buildField('Address', _addressController, Icons.location_on),
          const SizedBox(height: 10),
          _buildField('Bank Account + IFSC', _bankController, Icons.account_balance),
          const SizedBox(height: 10),
          _buildField('RC Book Number', _rcController, Icons.description),
          const SizedBox(height: 10),
          _buildField('Insurance Policy Number', _insuranceController, Icons.shield),
          const SizedBox(height: 10),
          _buildField('PUC Certificate Number', _pucController, Icons.eco),
          const SizedBox(height: 10),
          _buildField('Daily / Monthly Rate', _rateController, Icons.currency_rupee),
          const SizedBox(height: 20),

          // Submit Button
          SizedBox(
            width: double.infinity,
            height: 48,
            child: ElevatedButton(
              onPressed: () {
                ScaffoldMessenger.of(context).showSnackBar(
                  const SnackBar(
                    content: Text('Vehicle Registered in Rent A Car Catalog!'),
                    backgroundColor: Color(0xFF2E8B57),
                  ),
                );
              },
              style: ElevatedButton.styleFrom(
                backgroundColor: const Color(0xFF0D1B68),
                shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(10)),
              ),
              child: const Text('Submit Rent A Car Owner Profile', style: TextStyle(color: Colors.white, fontWeight: FontWeight.bold, fontSize: 13)),
            ),
          ),
          const SizedBox(height: 24),
        ],
      ),
    );
  }
}

// =============================================================
// TAB 2: HIRE DRIVER (DL ONLY)
// =============================================================
class _HireDriverForm extends StatefulWidget {
  const _HireDriverForm({Key? key}) : super(key: key);

  @override
  State<_HireDriverForm> createState() => _HireDriverFormState();
}

class _HireDriverFormState extends State<_HireDriverForm> {
  final _nameController = TextEditingController(text: 'Subhashish Mondal');
  final _phoneController = TextEditingController(text: '9831092812');
  final _addressController = TextEditingController(text: 'Salt Lake Sector 5, Kolkata');
  final _dlController = TextEditingController(text: 'DL-04-2018-009124');
  final _dlExpiryController = TextEditingController(text: '2033-05-18');
  final _languagesController = TextEditingController(text: 'Bengali, Hindi, English');
  final _referenceController = TextEditingController(text: 'Gourab Roy • 9830012984 (Fleet Incharge)');

  String _selectedExp = '3-5 Years';
  String _selectedTransmission = 'Both';

  @override
  void dispose() {
    _nameController.dispose();
    _phoneController.dispose();
    _addressController.dispose();
    _dlController.dispose();
    _dlExpiryController.dispose();
    _languagesController.dispose();
    _referenceController.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return SingleChildScrollView(
      padding: const EdgeInsets.all(16.0),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          Container(
            padding: const EdgeInsets.all(12),
            decoration: BoxDecoration(
              color: const Color(0xFF2E8B57).withOpacity(0.12),
              borderRadius: BorderRadius.circular(10),
              border: Border.all(color: const Color(0xFF2E8B57)),
            ),
            child: const Row(
              children: [
                Icon(Icons.verified, color: Color(0xFF2E8B57), size: 20),
                SizedBox(width: 8),
                Expanded(
                  child: Text(
                    'DL Only • No Vehicle Needed • Hourly & Daily Driver For Hire',
                    style: TextStyle(fontSize: 11, fontWeight: FontWeight.bold, color: Color(0xFF0D1B68)),
                  ),
                ),
              ],
            ),
          ),
          const SizedBox(height: 16),

          _buildField('Full Name', _nameController, Icons.person),
          const SizedBox(height: 10),
          _buildField('Phone Number', _phoneController, Icons.phone, suffixText: 'OTP Verified ✓'),
          const SizedBox(height: 10),
          _buildField('Residential Address', _addressController, Icons.home),
          const SizedBox(height: 10),
          _buildField('DL Number', _dlController, Icons.badge),
          const SizedBox(height: 10),
          _buildField('DL Expiry Date', _dlExpiryController, Icons.calendar_today),
          const SizedBox(height: 12),

          // Driving Experience Chips (1-2Y, 3-5Y, 5Y+)
          const Text('Driving Experience', style: TextStyle(fontWeight: FontWeight.bold, fontSize: 13, color: Color(0xFF0D1B68))),
          const SizedBox(height: 6),
          Wrap(
            spacing: 8,
            children: ['1-2 Years', '3-5 Years', '5+ Years'].map((exp) {
              final isSel = _selectedExp == exp;
              return ChoiceChip(
                label: Text(exp),
                selected: isSel,
                selectedColor: const Color(0xFF2E8B57),
                labelStyle: TextStyle(color: isSel ? Colors.white : const Color(0xFF0D1B68), fontWeight: FontWeight.bold),
                onSelected: (_) => setState(() => _selectedExp = exp),
              );
            }).toList(),
          ),
          const SizedBox(height: 12),

          // Transmission Chips (Manual/Auto/Both)
          const Text('Transmission', style: TextStyle(fontWeight: FontWeight.bold, fontSize: 13, color: Color(0xFF0D1B68))),
          const SizedBox(height: 6),
          Wrap(
            spacing: 8,
            children: ['Manual', 'Automatic', 'Both'].map((tr) {
              final isSel = _selectedTransmission == tr;
              return ChoiceChip(
                label: Text(tr),
                selected: isSel,
                selectedColor: const Color(0xFF0D1B68),
                labelStyle: TextStyle(color: isSel ? Colors.white : const Color(0xFF0D1B68), fontWeight: FontWeight.bold),
                onSelected: (_) => setState(() => _selectedTransmission = tr),
              );
            }).toList(),
          ),
          const SizedBox(height: 12),

          _buildField('Languages', _languagesController, Icons.translate),
          const SizedBox(height: 10),
          _buildField('Reference', _referenceController, Icons.people),
          const SizedBox(height: 20),

          // Submit Button
          SizedBox(
            width: double.infinity,
            height: 48,
            child: ElevatedButton(
              onPressed: () {
                ScaffoldMessenger.of(context).showSnackBar(
                  const SnackBar(
                    content: Text('Professional Driver Profile Submitted!'),
                    backgroundColor: Color(0xFF2E8B57),
                  ),
                );
              },
              style: ElevatedButton.styleFrom(
                backgroundColor: const Color(0xFF0D1B68),
                shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(10)),
              ),
              child: const Text('Submit Hire Driver Profile (DL Only)', style: TextStyle(color: Colors.white, fontWeight: FontWeight.bold, fontSize: 13)),
            ),
          ),
          const SizedBox(height: 24),
        ],
      ),
    );
  }
}

// =============================================================
// REUSABLE FIELD & CHIP HELPERS
// =============================================================
Widget _buildField(String label, TextEditingController controller, IconData icon, {String? suffixText}) {
  return Container(
    decoration: BoxDecoration(
      color: Colors.white,
      borderRadius: BorderRadius.circular(10),
      border: Border.all(color: Colors.black12),
    ),
    child: TextField(
      controller: controller,
      decoration: InputDecoration(
        labelText: label,
        labelStyle: TextStyle(fontSize: 12, color: Colors.grey.shade700),
        prefixIcon: Icon(icon, color: const Color(0xFF0D1B68), size: 18),
        suffixText: suffixText,
        suffixStyle: const TextStyle(color: Color(0xFF2E8B57), fontWeight: FontWeight.bold, fontSize: 11),
        border: InputBorder.none,
        contentPadding: const EdgeInsets.symmetric(horizontal: 14, vertical: 12),
      ),
    ),
  );
}

Widget _buildDocChip(String label) {
  return Container(
    padding: const EdgeInsets.symmetric(horizontal: 10, vertical: 6),
    decoration: BoxDecoration(
      color: const Color(0xFF2E8B57).withOpacity(0.12),
      borderRadius: BorderRadius.circular(8),
      border: Border.all(color: const Color(0xFF2E8B57)),
    ),
    child: Text(label, style: const TextStyle(color: Color(0xFF2E8B57), fontWeight: FontWeight.bold, fontSize: 11)),
  );
}

import 'package:flutter/material.dart';

class CreateDriverProfileScreen extends StatefulWidget {
  const CreateDriverProfileScreen({Key? key}) : super(key: key);

  @override
  State<CreateDriverProfileScreen> createState() => _CreateDriverProfileScreenState();
}

class _CreateDriverProfileScreenState extends State<CreateDriverProfileScreen> {
  String _selectedVehicle = 'Sedan';
  final _nameController = TextEditingController(text: 'Suresh Mondal');
  final _phoneController = TextEditingController(text: '9876543210');
  final _addressController = TextEditingController(text: 'Barasat, North 24 Parganas');
  final _dlController = TextEditingController(text: 'DL-WB-2021-99214');
  final _dlExpiryController = TextEditingController(text: '2031-10-14');
  final _rcController = TextEditingController(text: 'WB-25-AB-1290');
  final _insuranceController = TextEditingController(text: 'INS-99214081');
  final _pucController = TextEditingController(text: 'PUC-771249');

  final List<String> _vehicleTypes = ['Toto', 'Auto', 'Bike', 'Mini', 'Sedan', 'SUV'];

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: const Color(0xFFF8FAFC),
      appBar: AppBar(
        backgroundColor: const Color(0xFF0D1B68),
        title: Row(
          mainAxisAlignment: MainAxisAlignment.spaceBetween,
          children: [
            const Text('Create Driver Profile', style: TextStyle(fontWeight: FontWeight.bold, fontSize: 16, color: Colors.white)),
            Image.asset('assets/logo.png', width: 90, height: 30, fit: BoxFit.contain),
          ],
        ),
      ),
      body: SingleChildScrollView(
        padding: const EdgeInsets.all(16.0),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            // Vehicle Type Selection (Toto, Auto, Bike, Mini, Sedan, SUV)
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

            // Form Fields
            _buildTextField('Full Name', _nameController, Icons.person),
            const SizedBox(height: 10),
            _buildTextField('Phone Number', _phoneController, Icons.phone, suffixText: 'OTP Verified ✓'),
            const SizedBox(height: 10),
            _buildTextField('Residential Address', _addressController, Icons.home),
            const SizedBox(height: 10),
            _buildTextField('Driving License Number (DL)', _dlController, Icons.badge),
            const SizedBox(height: 10),
            _buildTextField('DL Expiry Date (Mandatory)', _dlExpiryController, Icons.calendar_today),
            const SizedBox(height: 10),
            _buildTextField('RC Book Number (Mandatory)', _rcController, Icons.description),
            const SizedBox(height: 10),
            _buildTextField('Vehicle Insurance Policy Number', _insuranceController, Icons.shield),
            const SizedBox(height: 10),
            _buildTextField('PUC Certificate Number', _pucController, Icons.eco),
            const SizedBox(height: 16),

            // Document Upload Verification Badges
            const Text('Uploaded Verification Documents', style: TextStyle(fontWeight: FontWeight.bold, fontSize: 13, color: Color(0xFF0D1B68))),
            const SizedBox(height: 8),
            Wrap(
              spacing: 8,
              runSpacing: 8,
              children: [
                _buildUploadChip('Driver Photo Attached ✓'),
                _buildUploadChip('DL Photo Front & Back ✓'),
                _buildUploadChip('RC Book Copy Attached ✓'),
                _buildUploadChip('Vehicle 4-Sides Photos ✓'),
              ],
            ),
            const SizedBox(height: 16),

            // Notice: Submitted for Admin & Police Verification (NO AUTO APPROVAL TEXT)
            Container(
              padding: const EdgeInsets.all(12),
              decoration: BoxDecoration(
                color: const Color(0xFF0D1B68).withOpacity(0.08),
                borderRadius: BorderRadius.circular(10),
                border: Border.all(color: const Color(0xFF0D1B68).withOpacity(0.2)),
              ),
              child: const Row(
                children: [
                  Icon(Icons.info_outline, color: Color(0xFF0D1B68), size: 20),
                  SizedBox(width: 8),
                  Expanded(
                    child: Text(
                      'Applications undergo standard background and document verification by the transport desk. Profile activates upon review.',
                      style: TextStyle(fontSize: 11, color: Color(0xFF0D1B68)),
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
                  showDialog(
                    context: context,
                    builder: (ctx) => AlertDialog(
                      title: const Text('Profile Submitted', style: TextStyle(fontWeight: FontWeight.bold, color: Color(0xFF0D1B68))),
                      content: Text('Your $_selectedVehicle partner application has been submitted for review. Admin silent push dispatched.'),
                      actions: [
                        ElevatedButton(
                          onPressed: () {
                            Navigator.pop(ctx);
                            Navigator.pop(context);
                          },
                          style: ElevatedButton.styleFrom(backgroundColor: const Color(0xFFFF8C00)),
                          child: const Text('Done', style: TextStyle(color: Colors.white)),
                        ),
                      ],
                    ),
                  );
                },
                style: ElevatedButton.styleFrom(
                  backgroundColor: const Color(0xFFFF8C00),
                  shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(12)),
                ),
                child: const Text('Submit Driver Profile for Verification', style: TextStyle(fontWeight: FontWeight.bold, color: Colors.white, fontSize: 14)),
              ),
            ),
            const SizedBox(height: 16),
          ],
        ),
      ),
    );
  }

  Widget _buildTextField(String label, TextEditingController controller, IconData icon, {String? suffixText}) {
    return TextField(
      controller: controller,
      decoration: InputDecoration(
        labelText: label,
        prefixIcon: Icon(icon, color: const Color(0xFF0D1B68), size: 20),
        suffixText: suffixText,
        suffixStyle: const TextStyle(color: Color(0xFF2E8B57), fontWeight: FontWeight.bold, fontSize: 11),
        border: OutlineInputBorder(borderRadius: BorderRadius.circular(12)),
        filled: true,
        fillColor: Colors.white,
      ),
    );
  }

  Widget _buildUploadChip(String text) {
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

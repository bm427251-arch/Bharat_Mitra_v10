import 'package:flutter/material.dart';
import '../utils/logo_helper.dart';

class BookingConfirmedScreen extends StatelessWidget {
  const BookingConfirmedScreen({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: const Color(0xFFF8FAFC),
      appBar: AppBar(
        backgroundColor: const Color(0xFF0D1B68),
        title: const Text('Booking Status', style: TextStyle(fontWeight: FontWeight.bold, color: Colors.white)),
        actions: [
          Padding(
            padding: const EdgeInsets.only(right: 12),
            child: GestureDetector(
              behavior: HitTestBehavior.opaque,
              onLongPress: () => Navigator.pushNamed(context, '/admin_login'),
              child: const AppLogo(width: 90, height: 30, fit: BoxFit.contain),
            ),
          ),
        ],
      ),
      body: Padding(
        padding: const EdgeInsets.all(20.0),
        child: Column(
          children: [
            Expanded(
              child: SingleChildScrollView(
                child: Column(
                  children: [
                    const SizedBox(height: 12),
                    // Green Tick
                    Container(
                      width: 72,
                      height: 72,
                      decoration: BoxDecoration(
                        color: const Color(0xFF2E8B57).withOpacity(0.15),
                        shape: BoxShape.circle,
                      ),
                      child: const Center(
                        child: CircleAvatar(
                          radius: 26,
                          backgroundColor: Color(0xFF2E8B57),
                          child: Icon(Icons.check, color: Colors.white, size: 32),
                        ),
                      ),
                    ),
                    const SizedBox(height: 12),
                    const Text(
                      'Booking Confirmed!',
                      style: TextStyle(fontSize: 22, fontWeight: FontWeight.bold, color: Color(0xFF0D1B68)),
                    ),
                    const SizedBox(height: 4),
                    const Text('Driver is heading toward your pickup location', style: TextStyle(color: Colors.grey, fontSize: 13)),
                    const SizedBox(height: 16),

                    // Fair Price Card (No commission text)
                    Container(
                      width: double.infinity,
                      padding: const EdgeInsets.all(14),
                      decoration: BoxDecoration(
                        color: Colors.white,
                        borderRadius: BorderRadius.circular(16),
                        border: Border.all(color: Colors.black12),
                        boxShadow: [BoxShadow(color: Colors.black.withOpacity(0.04), blurRadius: 8)],
                      ),
                      child: Row(
                        mainAxisAlignment: MainAxisAlignment.spaceBetween,
                        children: const [
                          Column(
                            crossAxisAlignment: CrossAxisAlignment.start,
                            children: [
                              Text('Total Fare', style: TextStyle(fontSize: 12, color: Colors.grey)),
                              Text('₹100', style: TextStyle(fontSize: 24, fontWeight: FontWeight.w900, color: Color(0xFF0D1B68))),
                            ],
                          ),
                          Chip(
                            label: Text('Fair Price • All-inclusive', style: TextStyle(color: Color(0xFF2E8B57), fontWeight: FontWeight.bold, fontSize: 11)),
                            backgroundColor: Color(0xFFE8F5E9),
                          ),
                        ],
                      ),
                    ),
                    const SizedBox(height: 16),

                    // Driver & Vehicle Card
                    Container(
                      width: double.infinity,
                      padding: const EdgeInsets.all(16),
                      decoration: BoxDecoration(
                        color: Colors.white,
                        borderRadius: BorderRadius.circular(16),
                        border: Border.all(color: Colors.black12),
                      ),
                      child: Column(
                        children: [
                          Row(
                            children: [
                              const CircleAvatar(
                                radius: 24,
                                backgroundColor: Color(0xFF0D1B68),
                                child: Icon(Icons.person, color: Colors.white, size: 28),
                              ),
                              const SizedBox(width: 12),
                              Expanded(
                                child: Column(
                                  crossAxisAlignment: CrossAxisAlignment.start,
                                  children: const [
                                    Text('Rohan Kumar', style: TextStyle(fontWeight: FontWeight.bold, fontSize: 16)),
                                    Text('Toyota Sedan • DL 01 AB 1234', style: TextStyle(fontSize: 12, color: Colors.grey)),
                                  ],
                                ),
                              ),
                              Container(
                                padding: const EdgeInsets.symmetric(horizontal: 8, vertical: 4),
                                decoration: BoxDecoration(
                                  color: const Color(0xFFFF8C00).withOpacity(0.15),
                                  borderRadius: BorderRadius.circular(8),
                                ),
                                child: const Text('4.8 ★', style: TextStyle(color: Color(0xFFFF8C00), fontWeight: FontWeight.bold, fontSize: 12)),
                              ),
                            ],
                          ),
                          const Divider(height: 24),
                          Row(
                            mainAxisAlignment: MainAxisAlignment.spaceBetween,
                            children: const [
                              Row(
                                children: [
                                  Icon(Icons.timer, size: 18, color: Color(0xFFFF8C00)),
                                  SizedBox(width: 4),
                                  Text('Arriving in 3 mins', style: TextStyle(fontWeight: FontWeight.bold, fontSize: 13)),
                                ],
                              ),
                              Text('OTP: 4829', style: TextStyle(fontWeight: FontWeight.w900, fontSize: 15, color: Color(0xFF0D1B68))),
                            ],
                          ),
                        ],
                      ),
                    ),
                  ],
                ),
              ),
            ),

            // Action Buttons
            Column(
              children: [
                SizedBox(
                  width: double.infinity,
                  height: 50,
                  child: ElevatedButton.icon(
                    onPressed: () => Navigator.pushNamed(context, '/ride_in_progress'),
                    icon: const Icon(Icons.navigation, color: Colors.white),
                    label: const Text('Track Ride Live', style: TextStyle(fontWeight: FontWeight.bold, color: Colors.white, fontSize: 15)),
                    style: ElevatedButton.styleFrom(
                      backgroundColor: const Color(0xFFFF8C00),
                      shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(12)),
                    ),
                  ),
                ),
                const SizedBox(height: 10),
                SizedBox(
                  width: double.infinity,
                  height: 48,
                  child: OutlinedButton.icon(
                    onPressed: () => Navigator.pushNamed(context, '/auto_qr_payment'),
                    icon: const Icon(Icons.qr_code, color: Color(0xFF0D1B68)),
                    label: const Text('Pay Now via UPI (₹100)', style: TextStyle(fontWeight: FontWeight.bold, color: Color(0xFF0D1B68), fontSize: 14)),
                    style: OutlinedButton.styleFrom(
                      side: const BorderSide(color: Color(0xFF0D1B68), width: 1.5),
                      shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(12)),
                    ),
                  ),
                ),
              ],
            ),
          ],
        ),
      ),
    );
  }
}

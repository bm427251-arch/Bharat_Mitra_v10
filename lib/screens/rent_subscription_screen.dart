import 'package:flutter/material.dart';

class RentSubscriptionScreen extends StatelessWidget {
  const RentSubscriptionScreen({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: const Color(0xFFF8FAFC),
      appBar: AppBar(
        backgroundColor: const Color(0xFF0D1B68),
        title: Row(
          mainAxisAlignment: MainAxisAlignment.spaceBetween,
          children: [
            const Text('Rent Owner Subscription', style: TextStyle(fontWeight: FontWeight.bold, fontSize: 16, color: Colors.white)),
            Image.asset(
              'assets/logo.png',
              width: 90,
              height: 30,
              fit: BoxFit.contain,
              errorBuilder: (c, e, s) => const Icon(Icons.car_rental, color: Colors.white, size: 24),
            ),
          ],
        ),
      ),
      body: Padding(
        padding: const EdgeInsets.all(20.0),
        child: Column(
          children: [
            Expanded(
              child: SingleChildScrollView(
                child: Column(
                  children: [
                    const Icon(Icons.verified, color: Color(0xFFFF8C00), size: 64),
                    const SizedBox(height: 12),
                    const Text('Rent Vehicle Pro Listing', style: TextStyle(fontWeight: FontWeight.bold, fontSize: 22, color: Color(0xFF0D1B68))),
                    const SizedBox(height: 4),
                    const Text('List your vehicle on Bharat Mitra for 365 days', style: TextStyle(color: Colors.grey, fontSize: 13)),
                    const SizedBox(height: 24),

                    // Price Card
                    Container(
                      width: double.infinity,
                      padding: const EdgeInsets.all(20),
                      decoration: BoxDecoration(
                        color: Colors.white,
                        borderRadius: BorderRadius.circular(20),
                        border: Border.all(color: const Color(0xFFFF8C00), width: 1.5),
                        boxShadow: [BoxShadow(color: Colors.black.withOpacity(0.04), blurRadius: 10)],
                      ),
                      child: Column(
                        children: const [
                          Text('Annual Fleet Partner Membership', style: TextStyle(fontWeight: FontWeight.bold, fontSize: 14)),
                          SizedBox(height: 10),
                          Text('₹299', style: TextStyle(fontSize: 36, fontWeight: FontWeight.w900, color: Color(0xFF0D1B68))),
                          Text('Full Year Listing • Zero Commission Direct Bookings', style: TextStyle(fontSize: 12, color: Color(0xFF2E8B57), fontWeight: FontWeight.bold)),
                          Divider(height: 24),
                          Text('✓ Unlimited Renter Inquiries\n✓ Direct Customer Contact\n✓ Insurance & Safety Assistance\n✓ Priority Placement on Bharat Mitra Wall', style: TextStyle(fontSize: 12, height: 1.6)),
                        ],
                      ),
                    ),
                  ],
                ),
              ),
            ),
            SizedBox(
              width: double.infinity,
              height: 50,
              child: ElevatedButton(
                onPressed: () {
                  showDialog(
                    context: context,
                    builder: (ctx) => AlertDialog(
                      title: const Text('Subscription Activated!'),
                      content: const Text('Your ₹299 vehicle listing subscription has been activated via Razorpay. Your vehicle is now live!'),
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
                child: const Text('Pay ₹299 via Razorpay', style: TextStyle(fontWeight: FontWeight.bold, color: Colors.white, fontSize: 15)),
              ),
            ),
          ],
        ),
      ),
    );
  }
}

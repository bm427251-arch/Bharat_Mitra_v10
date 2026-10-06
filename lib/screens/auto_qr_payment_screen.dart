import 'dart:async';
import 'package:flutter/material.dart';

class AutoQrPaymentScreen extends StatefulWidget {
  const AutoQrPaymentScreen({Key? key}) : super(key: key);

  @override
  State<AutoQrPaymentScreen> createState() => _AutoQrPaymentScreenState();
}

class _AutoQrPaymentScreenState extends State<AutoQrPaymentScreen> {
  int _secondsRemaining = 300; // 05:00
  Timer? _timer;

  @override
  void initState() {
    super.initState();
    _startTimer();
  }

  void _startTimer() {
    _timer = Timer.periodic(const Duration(seconds: 1), (timer) {
      if (_secondsRemaining > 0) {
        setState(() => _secondsRemaining--);
      } else {
        _timer?.cancel();
      }
    });
  }

  @override
  void dispose() {
    _timer?.cancel();
    super.dispose();
  }

  String get _formattedTime {
    int min = _secondsRemaining ~/ 60;
    int sec = _secondsRemaining % 60;
    return '${min.toString().padLeft(2, '0')}:${sec.toString().padLeft(2, '0')}';
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
            const Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              mainAxisSize: MainAxisSize.min,
              children: [
                Text('Instant UPI Payment', style: TextStyle(fontWeight: FontWeight.bold, fontSize: 16, color: Colors.white)),
                Text('Scan to Pay Securely', style: TextStyle(fontSize: 11, color: Color(0xFFFFB366))),
              ],
            ),
            Image.asset('assets/logo.png', width: 90, height: 30, fit: BoxFit.contain),
          ],
        ),
      ),
      body: SingleChildScrollView(
        padding: const EdgeInsets.all(20.0),
        child: Column(
          children: [
            // Amount Card
            Container(
              width: double.infinity,
              padding: const EdgeInsets.all(16),
              decoration: BoxDecoration(
                color: Colors.white,
                borderRadius: BorderRadius.circular(16),
                border: Border.all(color: Colors.black12),
              ),
              child: Row(
                mainAxisAlignment: MainAxisAlignment.spaceBetween,
                children: [
                  const Column(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      Text('Payable Amount', style: TextStyle(color: Colors.grey, fontSize: 12)),
                      Text('₹100', style: TextStyle(fontSize: 26, fontWeight: FontWeight.w900, color: Color(0xFF0D1B68))),
                    ],
                  ),
                  Container(
                    padding: const EdgeInsets.symmetric(horizontal: 10, vertical: 6),
                    decoration: BoxDecoration(
                      color: const Color(0xFFFF8C00).withOpacity(0.15),
                      borderRadius: BorderRadius.circular(8),
                    ),
                    child: Row(
                      children: [
                        const Icon(Icons.timer, size: 14, color: Color(0xFFFF8C00)),
                        const SizedBox(width: 4),
                        Text(_formattedTime, style: const TextStyle(fontWeight: FontWeight.bold, color: Color(0xFFFF8C00))),
                      ],
                    ),
                  ),
                ],
              ),
            ),
            const SizedBox(height: 20),

            // Center QR Code Box
            Container(
              padding: const EdgeInsets.all(20),
              decoration: BoxDecoration(
                color: Colors.white,
                borderRadius: BorderRadius.circular(20),
                border: Border.all(color: const Color(0xFF0D1B68), width: 1.5),
                boxShadow: [BoxShadow(color: Colors.black.withOpacity(0.06), blurRadius: 12)],
              ),
              child: Column(
                children: [
                  // Stylized QR Box
                  Container(
                    width: 200,
                    height: 200,
                    decoration: BoxDecoration(
                      color: const Color(0xFFF1F5F9),
                      borderRadius: BorderRadius.circular(12),
                      border: Border.all(color: Colors.black12),
                    ),
                    child: Center(
                      child: Stack(
                        alignment: Alignment.center,
                        children: [
                          const Icon(Icons.qr_code_2, size: 180, color: Color(0xFF0D1B68)),
                          Container(
                            padding: const EdgeInsets.all(4),
                            decoration: BoxDecoration(
                              color: Colors.white,
                              shape: BoxShape.circle,
                              boxShadow: [BoxShadow(color: Colors.black26, blurRadius: 4)],
                            ),
                            child: const Icon(Icons.flash_on, color: Color(0xFFFF8C00), size: 24),
                          ),
                        ],
                      ),
                    ),
                  ),
                  const SizedBox(height: 12),
                  const Text(
                    'Scan with any UPI App',
                    style: TextStyle(fontWeight: FontWeight.bold, fontSize: 14, color: Color(0xFF0D1B68)),
                  ),
                  const Text('GPay • PhonePe • Paytm • BHIM', style: TextStyle(color: Colors.grey, fontSize: 11)),
                ],
              ),
            ),
            const SizedBox(height: 20),

            // Available Wallet Balance option
            Container(
              padding: const EdgeInsets.all(14),
              decoration: BoxDecoration(
                color: const Color(0xFF2E8B57).withOpacity(0.1),
                borderRadius: BorderRadius.circular(14),
                border: Border.all(color: const Color(0xFF2E8B57)),
              ),
              child: Row(
                children: const [
                  Icon(Icons.account_balance_wallet, color: Color(0xFF2E8B57)),
                  SizedBox(width: 10),
                  Expanded(
                    child: Column(
                      crossAxisAlignment: CrossAxisAlignment.start,
                      children: [
                        Text('Available Wallet Balance', style: TextStyle(fontSize: 11, color: Color(0xFF2E8B57))),
                        Text('₹1,200', style: TextStyle(fontSize: 16, fontWeight: FontWeight.bold, color: Color(0xFF2E8B57))),
                      ],
                    ),
                  ),
                  Text('Active', style: TextStyle(fontWeight: FontWeight.bold, color: Color(0xFF2E8B57))),
                ],
              ),
            ),
            const SizedBox(height: 24),

            // Pay Now CTA Button
            SizedBox(
              width: double.infinity,
              height: 50,
              child: ElevatedButton(
                onPressed: () {
                  ScaffoldMessenger.of(context).showSnackBar(
                    const SnackBar(content: Text('Payment of ₹100 Successful! Booking confirmed.')),
                  );
                  Navigator.pop(context);
                },
                style: ElevatedButton.styleFrom(
                  backgroundColor: const Color(0xFFFF8C00),
                  shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(12)),
                ),
                child: const Text('Pay Now ₹100 via UPI / Wallet', style: TextStyle(fontWeight: FontWeight.bold, fontSize: 15, color: Colors.white)),
              ),
            ),
            const SizedBox(height: 10),
            const Text(
              'Instant Dynamic UPI • 100% Secure',
              style: TextStyle(fontSize: 11, color: Colors.grey),
            ),
          ],
        ),
      ),
    );
  }
}

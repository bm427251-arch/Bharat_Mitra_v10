import 'package:flutter/material.dart';
import '../screens/home_screen.dart';
import '../screens/rent_a_car_screen.dart';
import '../screens/hire_driver_screen.dart';
import '../screens/payment_history_wallet_screen.dart';

class MainBottomNavFixed extends StatefulWidget {
  const MainBottomNavFixed({super.key});
  @override
  State<MainBottomNavFixed> createState() => _MainBottomNavFixedState();
}

class _MainBottomNavFixedState extends State<MainBottomNavFixed> {
  int _index = 0;
  final _pages = const [
    HomeScreen(),
    RentACarScreen(),
    HireDriverScreen(),
    PaymentHistoryWalletScreen(),
  ];

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: _pages[_index],
      bottomNavigationBar: Container(
        decoration: const BoxDecoration(
          border: Border(top: BorderSide(color: Colors.black12)),
        ),
        child: BottomNavigationBar(
          currentIndex: _index,
          onTap: (i) => setState(() => _index = i),
          type: BottomNavigationBarType.fixed,
          backgroundColor: Colors.white,
          selectedItemColor: const Color(0xFF0D1B68),
          unselectedItemColor: Colors.grey,
          items: const [
            BottomNavigationBarItem(icon: Icon(Icons.home), label: 'Home'),
            BottomNavigationBarItem(icon: Icon(Icons.car_rental), label: 'Rent A Car'),
            BottomNavigationBarItem(icon: Icon(Icons.person), label: 'Hire Driver'),
            BottomNavigationBarItem(icon: Icon(Icons.account_balance_wallet), label: 'Wallet'),
          ],
        ),
      ),
    );
  }
}

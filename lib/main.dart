import 'package:flutter/material.dart';
import 'screens/home_screen.dart';
import 'screens/available_fleet_map_screen.dart';
import 'screens/booking_confirmed_screen.dart';
import 'screens/auto_qr_payment_screen.dart';
import 'screens/ride_in_progress_screen.dart';
import 'screens/payment_history_wallet_screen.dart';
import 'screens/create_driver_profile_screen.dart';
import 'screens/rent_a_car_screen.dart';
import 'screens/rent_owner_form_screen.dart';
import 'screens/hire_driver_screen.dart';
import 'screens/hire_driver_form_screen.dart';
import 'screens/rent_subscription_screen.dart';
import 'screens/hire_driver_subscription_screen.dart';
import 'screens/admin_dashboard_screen.dart';
import 'screens/admin_login_screen.dart';
import 'screens/rating_screen.dart';
import 'utils/logo_helper.dart';

void main() async {
  WidgetsFlutterBinding.ensureInitialized();
  await LogoHelper.init();
  runApp(const BharatMitraApp());
}

class BharatMitraApp extends StatelessWidget {
  const BharatMitraApp({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Bharat Mitra Official',
      debugShowCheckedModeBanner: false,
      theme: ThemeData(
        useMaterial3: true,
        primaryColor: const Color(0xFF0D1B68),
        scaffoldBackgroundColor: const Color(0xFFF8FAFC),
        colorScheme: const ColorScheme.light(
          primary: Color(0xFF0D1B68),
          secondary: Color(0xFFFF8C00),
          tertiary: Color(0xFF2E8B57),
          surface: Colors.white,
          background: Color(0xFFF8FAFC),
        ),
        appBarTheme: const AppBarTheme(
          backgroundColor: Color(0xFF0D1B68),
          foregroundColor: Colors.white,
          elevation: 0,
        ),
      ),
      initialRoute: '/',
      routes: {
        '/': (context) => const MainNavigationHolder(),
        '/fleet_map': (context) => const AvailableFleetMapScreen(),
        '/booking_confirmed': (context) => const BookingConfirmedScreen(),
        '/auto_qr_payment': (context) => const AutoQrPaymentScreen(),
        '/ride_in_progress': (context) => const RideInProgressScreen(),
        '/wallet': (context) => const PaymentHistoryWalletScreen(),
        '/create_driver': (context) => const CreateDriverProfileScreen(),
        '/rent_a_car': (context) => const RentACarScreen(),
        '/rent_owner_form': (context) => const RentOwnerFormScreen(),
        '/rent_subscription': (context) => const RentSubscriptionScreen(),
        '/hire_driver': (context) => const HireDriverScreen(),
        '/hire_driver_form': (context) => const HireDriverFormScreen(),
        '/hire_driver_subscription': (context) => const HireDriverSubscriptionScreen(),
        '/admin': (context) => const AdminLoginScreen(),
        '/admin_login': (context) => const AdminLoginScreen(),
        '/admin_dashboard': (context) => const AdminDashboardScreen(),
        '/rating': (context) => const RatingScreen(),
      },
    );
  }
}

class MainNavigationHolder extends StatefulWidget {
  const MainNavigationHolder({Key? key}) : super(key: key);

  @override
  State<MainNavigationHolder> createState() => _MainNavigationHolderState();
}

class _MainNavigationHolderState extends State<MainNavigationHolder> {
  int _currentIndex = 0;

  final List<Widget> _screens = const [
    HomeScreen(showBottomNav: false),
    RentACarScreen(),
    HireDriverScreen(),
    RentACarScreen(),
  ];

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: _screens[_currentIndex],
      bottomNavigationBar: Container(
        decoration: BoxDecoration(
          color: Colors.white,
          border: const Border(top: BorderSide(color: Colors.black12, width: 0.8)),
          boxShadow: [
            BoxShadow(
              color: Colors.black.withOpacity(0.08),
              blurRadius: 16,
              offset: const Offset(0, -4),
            ),
          ],
        ),
        child: BottomNavigationBar(
          currentIndex: _currentIndex,
          onTap: (index) => setState(() => _currentIndex = index),
          type: BottomNavigationBarType.fixed,
          backgroundColor: Colors.white,
          selectedItemColor: const Color(0xFF0D1B68),
          unselectedItemColor: Colors.grey.shade600,
          selectedLabelStyle: const TextStyle(fontWeight: FontWeight.bold, fontSize: 11),
          unselectedLabelStyle: const TextStyle(fontWeight: FontWeight.normal, fontSize: 11),
          items: const [
            BottomNavigationBarItem(
              icon: Icon(Icons.home),
              label: 'Home',
            ),
            BottomNavigationBarItem(
              icon: Icon(Icons.directions_car),
              label: 'Rent Car',
            ),
            BottomNavigationBarItem(
              icon: Icon(Icons.person),
              label: 'Hire Driver',
            ),
            BottomNavigationBarItem(
              icon: Icon(Icons.shield),
              label: 'Elite',
            ),
          ],
        ),
      ),
    );
  }
}

import 'package:flutter/material.dart';

class PaymentHistoryWalletScreen extends StatefulWidget {
  const PaymentHistoryWalletScreen({Key? key}) : super(key: key);

  @override
  State<PaymentHistoryWalletScreen> createState() => _PaymentHistoryWalletScreenState();
}

class _PaymentHistoryWalletScreenState extends State<PaymentHistoryWalletScreen> {
  int _walletBalance = 1200;
  String _selectedFilter = 'All';

  final List<Map<String, dynamic>> _transactions = [
    {'title': 'Ride Payment - Sedan', 'amt': '₹100', 'type': 'Paid', 'mode': 'UPI / Auto QR', 'time': 'Today, 10:30 AM'},
    {'title': 'Wallet Top-up via Razorpay', 'amt': '+₹500', 'type': 'Added', 'mode': 'Razorpay / GPay', 'time': 'Yesterday, 04:15 PM'},
    {'title': 'Ride Payment - Auto Rickshaw', 'amt': '₹60', 'type': 'Paid', 'mode': 'Wallet', 'time': '02 Oct, 08:45 AM'},
    {'title': 'Wallet Top-up via UPI', 'amt': '+₹1000', 'type': 'Added', 'mode': 'UPI', 'time': '28 Sep, 02:10 PM'},
    {'title': 'Rental Car Security Deposit', 'amt': '₹500', 'type': 'Paid', 'mode': 'Cash / Wallet', 'time': '25 Sep, 11:00 AM'},
  ];

  void _showAddMoneyDialog() {
    final textController = TextEditingController(text: '500');
    showDialog(
      context: context,
      builder: (ctx) => AlertDialog(
        title: const Text('Add Money to Wallet', style: TextStyle(fontWeight: FontWeight.bold, color: Color(0xFF0D1B68))),
        content: Column(
          mainAxisSize: MainAxisSize.min,
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            const Text('Enter amount to add via Razorpay / UPI:', style: TextStyle(fontSize: 12)),
            const SizedBox(height: 8),
            TextField(
              controller: textController,
              keyboardType: TextInputType.number,
              decoration: const InputDecoration(
                prefixText: '₹ ',
                border: OutlineInputBorder(),
              ),
            ),
            const SizedBox(height: 10),
            Row(
              mainAxisAlignment: MainAxisAlignment.spaceEvenly,
              children: [100, 200, 500].map((amt) {
                return OutlinedButton(
                  onPressed: () => textController.text = '$amt',
                  child: Text('₹$amt'),
                );
              }).toList(),
            ),
          ],
        ),
        actions: [
          TextButton(onPressed: () => Navigator.pop(ctx), child: const Text('Cancel')),
          ElevatedButton(
            onPressed: () {
              final added = int.tryParse(textController.text) ?? 500;
              setState(() {
                _walletBalance += added;
                _transactions.insert(0, {
                  'title': 'Wallet Top-up via Razorpay',
                  'amt': '+₹$added',
                  'type': 'Added',
                  'mode': 'Razorpay',
                  'time': 'Just now',
                });
              });
              Navigator.pop(ctx);
              ScaffoldMessenger.of(context).showSnackBar(
                SnackBar(content: Text('Added ₹$added to wallet via Razorpay!')),
              );
            },
            style: ElevatedButton.styleFrom(backgroundColor: const Color(0xFFFF8C00)),
            child: const Text('Proceed with Razorpay', style: TextStyle(color: Colors.white)),
          ),
        ],
      ),
    );
  }

  @override
  Widget build(BuildContext context) {
    final filtered = _selectedFilter == 'All'
        ? _transactions
        : _transactions.where((t) => t['type'] == _selectedFilter).toList();

    return Scaffold(
      backgroundColor: const Color(0xFFF8FAFC),
      appBar: AppBar(
        backgroundColor: const Color(0xFF0D1B68),
        title: Row(
          mainAxisAlignment: MainAxisAlignment.spaceBetween,
          children: [
            const Text('Payment History & Wallet', style: TextStyle(fontWeight: FontWeight.bold, fontSize: 16, color: Colors.white)),
            Image.asset('assets/logo.png', width: 90, height: 30, fit: BoxFit.contain),
          ],
        ),
      ),
      body: SingleChildScrollView(
        padding: const EdgeInsets.all(16.0),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            // Prominent Green Box Top: Wallet Balance ₹1,200
            Container(
              width: double.infinity,
              padding: const EdgeInsets.all(20),
              decoration: BoxDecoration(
                color: const Color(0xFF2E8B57),
                borderRadius: BorderRadius.circular(20),
                boxShadow: [
                  BoxShadow(color: const Color(0xFF2E8B57).withOpacity(0.3), blurRadius: 10, offset: const Offset(0, 4)),
                ],
              ),
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  Row(
                    mainAxisAlignment: MainAxisAlignment.spaceBetween,
                    children: const [
                      Text('Available Wallet Balance', style: TextStyle(color: Colors.white70, fontSize: 13)),
                      Icon(Icons.account_balance_wallet, color: Colors.white),
                    ],
                  ),
                  const SizedBox(height: 8),
                  Text(
                    '₹$_walletBalance',
                    style: const TextStyle(color: Colors.white, fontSize: 32, fontWeight: FontWeight.w900),
                  ),
                  const SizedBox(height: 14),
                  // Orange Add Money Button
                  SizedBox(
                    width: double.infinity,
                    height: 44,
                    child: ElevatedButton.icon(
                      onPressed: _showAddMoneyDialog,
                      icon: const Icon(Icons.add, color: Colors.white),
                      label: const Text('Add Money (Razorpay)', style: TextStyle(color: Colors.white, fontWeight: FontWeight.bold)),
                      style: ElevatedButton.styleFrom(
                        backgroundColor: const Color(0xFFFF8C00),
                        shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(10)),
                      ),
                    ),
                  ),
                ],
              ),
            ),
            const SizedBox(height: 16),

            // Quick Top-up Chips
            Row(
              children: [
                const Text('Quick Add: ', style: TextStyle(fontWeight: FontWeight.bold, fontSize: 13, color: Color(0xFF0D1B68))),
                const SizedBox(width: 8),
                ...[100, 200, 500].map((amt) {
                  return Padding(
                    padding: const EdgeInsets.only(right: 6),
                    child: ActionChip(
                      label: Text('+₹$amt', style: const TextStyle(fontWeight: FontWeight.bold, fontSize: 12)),
                      onPressed: () {
                        setState(() => _walletBalance += amt);
                        ScaffoldMessenger.of(context).showSnackBar(
                          SnackBar(content: Text('Added ₹$amt to wallet!')),
                        );
                      },
                    ),
                  );
                }).toList(),
              ],
            ),
            const SizedBox(height: 20),

            // Transactions Header & Filters
            Row(
              mainAxisAlignment: MainAxisAlignment.spaceBetween,
              children: [
                const Text('Transactions', style: TextStyle(fontWeight: FontWeight.bold, fontSize: 16, color: Color(0xFF0D1B68))),
                Row(
                  children: ['All', 'Paid', 'Added'].map((f) {
                    final isSel = _selectedFilter == f;
                    return Padding(
                      padding: const EdgeInsets.only(left: 4),
                      child: ChoiceChip(
                        label: Text(f, style: TextStyle(fontSize: 11, color: isSel ? Colors.white : Colors.black87)),
                        selected: isSel,
                        selectedColor: const Color(0xFF0D1B68),
                        onSelected: (_) => setState(() => _selectedFilter = f),
                      ),
                    );
                  }).toList(),
                ),
              ],
            ),
            const SizedBox(height: 10),

            // Transactions List
            ListView.separated(
              shrinkWrap: true,
              physics: const NeverScrollableScrollPhysics(),
              itemCount: filtered.length,
              separatorBuilder: (_, __) => const SizedBox(height: 8),
              itemBuilder: (context, index) {
                final item = filtered[index];
                final isAdded = item['type'] == 'Added';
                return Card(
                  elevation: 2,
                  shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(12)),
                  child: ListTile(
                    leading: CircleAvatar(
                      backgroundColor: (isAdded ? const Color(0xFF2E8B57) : const Color(0xFFFF8C00)).withOpacity(0.15),
                      child: Icon(
                        isAdded ? Icons.arrow_downward : Icons.arrow_upward,
                        color: isAdded ? const Color(0xFF2E8B57) : const Color(0xFFFF8C00),
                        size: 18,
                      ),
                    ),
                    title: Text(item['title'] as String, style: const TextStyle(fontWeight: FontWeight.bold, fontSize: 13)),
                    subtitle: Text('${item['mode']} • ${item['time']}', style: const TextStyle(fontSize: 11, color: Colors.grey)),
                    trailing: Text(
                      item['amt'] as String,
                      style: TextStyle(
                        fontWeight: FontWeight.bold,
                        fontSize: 15,
                        color: isAdded ? const Color(0xFF2E8B57) : const Color(0xFF0D1B68),
                      ),
                    ),
                  ),
                );
              },
            ),
          ],
        ),
      ),
    );
  }
}

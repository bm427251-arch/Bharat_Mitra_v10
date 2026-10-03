import 'dart:math' as math;
import 'package:flutter/material.dart';

void main() {
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
        colorScheme: ColorScheme.fromSeed(
          seedColor: const Color(0xFFFA8520),
          primary: const Color(0xFFFA8520),
          secondary: const Color(0xFF0D1B68),
        ),
        useMaterial3: true,
      ),
      home: const SplashScreen(),
    );
  }
}

class SplashScreen extends StatefulWidget {
  const SplashScreen({Key? key}) : super(key: key);

  @override
  State<SplashScreen> createState() => _SplashScreenState();
}

class _SplashScreenState extends State<SplashScreen> {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: const Color(0xFF0D1B68),
      body: Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            const BharatMitraLogoWidget(width: 280, height: 350),
            const SizedBox(height: 24),
            const Text(
              'Together We Protect, Serve & Travel',
              style: TextStyle(
                color: Color(0xFF94A3B8),
                fontSize: 14,
                fontWeight: FontWeight.w500,
                letterSpacing: 0.5,
              ),
            ),
          ],
        ),
      ),
    );
  }
}

class BharatMitraLogoWidget extends StatelessWidget {
  final double width;
  final double height;

  const BharatMitraLogoWidget({
    Key? key,
    this.width = 300,
    this.height = 350,
  }) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Container(
      width: width,
      height: height,
      decoration: BoxDecoration(
        color: const Color(0xFF0D1B68), // গাঢ় নীল ব্যাকগ্রাউন্ড
        borderRadius: BorderRadius.circular(16),
      ),
      child: CustomPaint(
        painter: BharatMitraLogoPainter(),
      ),
    );
  }
}

class BharatMitraLogoPainter extends CustomPainter {
  @override
  void paint(Canvas canvas, Size size) {
    final double w = size.width;
    final double h = size.height;

    final double scale = math.min(w / 300.0, h / 350.0);
    final double ox = (w - (300.0 * scale)) / 2;
    final double oy = (h - (350.0 * scale)) / 2;

    // ১. ভারতের মানচিত্রের বর্ডার এবং কালারিং লজিক (উত্তর অংশ কমলা, দক্ষিণ অংশ সবুজ)
    final paintOrange = Paint()
      ..color = const Color(0xFFFA8520)
      ..style = PaintingStyle.fill;

    final paintGreen = Paint()
      ..color = const Color(0xFF0FA338)
      ..style = PaintingStyle.fill;

    // ১.১ উত্তর ও পশ্চিম ভারত (কমলা অংশ)
    final Path northWestMapPath = Path()
      ..moveTo(ox + 125 * scale, oy + 42 * scale)
      ..lineTo(ox + 130 * scale, oy + 36 * scale)
      ..lineTo(ox + 135 * scale, oy + 38 * scale)
      ..lineTo(ox + 143 * scale, oy + 44 * scale)
      ..lineTo(ox + 148 * scale, oy + 52 * scale)
      ..lineTo(ox + 156 * scale, oy + 46 * scale)
      ..lineTo(ox + 155 * scale, oy + 58 * scale)
      ..lineTo(ox + 150 * scale, oy + 66 * scale)
      ..lineTo(ox + 153 * scale, oy + 76 * scale)
      ..lineTo(ox + 148 * scale, oy + 84 * scale)
      ..lineTo(ox + 160 * scale, oy + 92 * scale)
      ..lineTo(ox + 150 * scale, oy + 104 * scale)
      ..lineTo(ox + 135 * scale, oy + 115 * scale)
      ..lineTo(ox + 124 * scale, oy + 128 * scale)
      ..lineTo(ox + 110 * scale, oy + 142 * scale)
      ..lineTo(ox + 106 * scale, oy + 152 * scale)
      ..lineTo(ox + 104 * scale, oy + 165 * scale)
      ..lineTo(ox + 98 * scale, oy + 160 * scale)
      ..lineTo(ox + 88 * scale, oy + 155 * scale)
      ..lineTo(ox + 80 * scale, oy + 148 * scale)
      ..lineTo(ox + 84 * scale, oy + 140 * scale)
      ..lineTo(ox + 74 * scale, oy + 138 * scale)
      ..lineTo(ox + 76 * scale, oy + 130 * scale)
      ..lineTo(ox + 86 * scale, oy + 128 * scale)
      ..lineTo(ox + 90 * scale, oy + 114 * scale)
      ..lineTo(ox + 96 * scale, oy + 110 * scale)
      ..lineTo(ox + 93 * scale, oy + 98 * scale)
      ..lineTo(ox + 100 * scale, oy + 94 * scale)
      ..lineTo(ox + 116 * scale, oy + 90 * scale)
      ..lineTo(ox + 120 * scale, oy + 78 * scale)
      ..lineTo(ox + 118 * scale, oy + 68 * scale)
      ..lineTo(ox + 122 * scale, oy + 54 * scale)
      ..close();
    canvas.drawPath(northWestMapPath, paintOrange);

    // ১.২ পূর্ব ও দক্ষিণ ভারত (সবুজ অংশ)
    final Path eastSouthMapPath = Path()
      ..moveTo(ox + 150 * scale, oy + 104 * scale)
      ..lineTo(ox + 164 * scale, oy + 110 * scale)
      ..lineTo(ox + 180 * scale, oy + 116 * scale)
      ..lineTo(ox + 196 * scale, oy + 114 * scale)
      ..lineTo(ox + 204 * scale, oy + 106 * scale)
      ..lineTo(ox + 218 * scale, oy + 95 * scale)
      ..lineTo(ox + 226 * scale, oy + 98 * scale)
      ..lineTo(ox + 242 * scale, oy + 100 * scale)
      ..lineTo(ox + 252 * scale, oy + 112 * scale)
      ..lineTo(ox + 242 * scale, oy + 120 * scale)
      ..lineTo(ox + 236 * scale, oy + 130 * scale)
      ..lineTo(ox + 238 * scale, oy + 144 * scale)
      ..lineTo(ox + 232 * scale, oy + 150 * scale)
      ..lineTo(ox + 222 * scale, oy + 140 * scale)
      ..lineTo(ox + 214 * scale, oy + 145 * scale)
      ..lineTo(ox + 208 * scale, oy + 134 * scale)
      ..lineTo(ox + 204 * scale, oy + 146 * scale)
      ..lineTo(ox + 205 * scale, oy + 160 * scale)
      ..lineTo(ox + 198 * scale, oy + 172 * scale)
      ..lineTo(ox + 186 * scale, oy + 190 * scale)
      ..lineTo(ox + 170 * scale, oy + 210 * scale)
      ..lineTo(ox + 154 * scale, oy + 230 * scale)
      ..lineTo(ox + 148 * scale, oy + 245 * scale)
      ..lineTo(ox + 140 * scale, oy + 256 * scale)
      ..lineTo(ox + 132 * scale, oy + 266 * scale)
      ..lineTo(ox + 126 * scale, oy + 256 * scale)
      ..lineTo(ox + 120 * scale, oy + 240 * scale)
      ..lineTo(ox + 115 * scale, oy + 215 * scale)
      ..lineTo(ox + 110 * scale, oy + 185 * scale)
      ..lineTo(ox + 104 * scale, oy + 165 * scale)
      ..lineTo(ox + 106 * scale, oy + 152 * scale)
      ..lineTo(ox + 110 * scale, oy + 142 * scale)
      ..lineTo(ox + 124 * scale, oy + 128 * scale)
      ..lineTo(ox + 135 * scale, oy + 115 * scale)
      ..close();
    canvas.drawPath(eastSouthMapPath, paintGreen);

    // ২. মাঝখানের হ্যান্ডশেক ড্র করার লজিক
    final paintHandshake = Paint()
      ..color = const Color(0xFFFA8520)
      ..style = PaintingStyle.fill;

    final paintStrokeNavy = Paint()
      ..color = const Color(0xFF0D1B68)
      ..style = PaintingStyle.stroke
      ..strokeWidth = 2.2 * scale
      ..strokeCap = StrokeCap.round
      ..strokeJoin = StrokeJoin.round;

    final Path leftArm = Path()
      ..moveTo(ox + 110 * scale, oy + 135 * scale)
      ..lineTo(ox + 126 * scale, oy + 124 * scale)
      ..lineTo(ox + 142 * scale, oy + 130 * scale)
      ..lineTo(ox + 134 * scale, oy + 148 * scale)
      ..lineTo(ox + 116 * scale, oy + 150 * scale)
      ..close();
    canvas.drawPath(leftArm, paintHandshake);
    canvas.drawPath(leftArm, paintStrokeNavy);

    final Path rightHand = Path()
      ..moveTo(ox + 132 * scale, oy + 132 * scale)
      ..lineTo(ox + 150 * scale, oy + 130 * scale)
      ..lineTo(ox + 166 * scale, oy + 144 * scale)
      ..lineTo(ox + 184 * scale, oy + 165 * scale)
      ..lineTo(ox + 172 * scale, oy + 175 * scale)
      ..lineTo(ox + 144 * scale, oy + 158 * scale)
      ..lineTo(ox + 130 * scale, oy + 146 * scale)
      ..close();
    canvas.drawPath(rightHand, paintHandshake);
    canvas.drawPath(rightHand, paintStrokeNavy);

    // ৩. ৩টি নীল ডট
    final paintBlueDot = Paint()
      ..color = const Color(0xFF1E70DC)
      ..style = PaintingStyle.fill;

    final double dotRadius = 4.0 * scale;
    final double px = ox + 152 * scale;
    final double py = oy + 152 * scale;

    canvas.drawCircle(Offset(px, py - (6 * scale)), dotRadius, paintBlueDot);
    canvas.drawCircle(Offset(px - (5.5 * scale), py + (4 * scale)), dotRadius, paintBlueDot);
    canvas.drawCircle(Offset(px + (5.5 * scale), py + (4 * scale)), dotRadius, paintBlueDot);

    // ৪. টেক্সট
    final double textY = oy + (300 * scale);
    final double fontSize = 30 * scale;

    final outlineSpan = TextSpan(
      children: [
        TextSpan(
          text: 'BHARAT ',
          style: TextStyle(
            fontSize: fontSize,
            fontWeight: FontWeight.w900,
            letterSpacing: 2,
            foreground: Paint()
              ..style = PaintingStyle.stroke
              ..strokeWidth = 5.0 * scale
              ..strokeJoin = StrokeJoin.round
              ..strokeCap = StrokeCap.round
              ..color = Colors.white,
          ),
        ),
        TextSpan(
          text: 'MITRA',
          style: TextStyle(
            fontSize: fontSize,
            fontWeight: FontWeight.w900,
            letterSpacing: 2,
            foreground: Paint()
              ..style = PaintingStyle.stroke
              ..strokeWidth = 5.0 * scale
              ..strokeJoin = StrokeJoin.round
              ..strokeCap = StrokeCap.round
              ..color = Colors.white,
          ),
        ),
      ],
    );

    final TextPainter outlinePainter = TextPainter(
      text: outlineSpan,
      textDirection: TextDirection.ltr,
    )..layout();

    final double textStartX = ox + (150 * scale) - (outlinePainter.width / 2);
    outlinePainter.paint(canvas, Offset(textStartX, textY));

    final fillSpan = TextSpan(
      children: [
        TextSpan(
          text: 'BHARAT ',
          style: TextStyle(
            fontSize: fontSize,
            fontWeight: FontWeight.w900,
            letterSpacing: 2,
            color: const Color(0xFFFA8520),
          ),
        ),
        TextSpan(
          text: 'MITRA',
          style: TextStyle(
            fontSize: fontSize,
            fontWeight: FontWeight.w900,
            letterSpacing: 2,
            color: const Color(0xFF0FA338),
          ),
        ),
      ],
    );

    final TextPainter fillPainter = TextPainter(
      text: fillSpan,
      textDirection: TextDirection.ltr,
    )..layout();

    fillPainter.paint(canvas, Offset(textStartX, textY));
  }

  @override
  bool shouldRepaint(covariant CustomPainter oldDelegate) => false;
}

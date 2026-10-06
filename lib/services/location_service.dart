import 'package:geolocator/geolocator.dart';
import 'package:geocoding/geocoding.dart';

class LocationService {
  static Future<Position?> getCurrentPosition() async {
    bool serviceEnabled = await Geolocator.isLocationServiceEnabled();
    if (!serviceEnabled) {
      return null;
    }

    LocationPermission permission = await Geolocator.checkPermission();
    if (permission == LocationPermission.denied) {
      permission = await Geolocator.requestPermission();
      if (permission == LocationPermission.denied) {
        return null;
      }
    }

    if (permission == LocationPermission.deniedForever) {
      return null;
    }

    return await Geolocator.getCurrentPosition(
      desiredAccuracy: LocationAccuracy.high,
    );
  }

  static Future<String> getAddressFromCoordinates(double lat, double lng) async {
    try {
      List<Placemark> placemarks = await placemarkFromCoordinates(lat, lng);
      if (placemarks.isNotEmpty) {
        Placemark place = placemarks.first;
        String sub = place.subLocality?.isNotEmpty == true
            ? place.subLocality!
            : (place.thoroughfare?.isNotEmpty == true ? place.thoroughfare! : 'Talbanda');
        String loc = place.locality?.isNotEmpty == true
            ? place.locality!
            : (place.subAdministrativeArea?.isNotEmpty == true ? place.subAdministrativeArea! : 'Badai');
        String state = place.administrativeArea?.isNotEmpty == true
            ? place.administrativeArea!
            : (place.subAdministrativeArea?.isNotEmpty == true ? place.subAdministrativeArea! : 'Kolkata');
        return '$sub, $loc, $state';
      }
    } catch (_) {}
    return 'Talbanda, Badai, Kolkata';
  }
}

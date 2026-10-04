# Firebase Setup Instructions for Bharat Mitra Official

To link your live Firebase Project and connect Firestore, Authentication, and Cloud Storage, follow these steps:

---

### Step 1: Open the Firebase Console
1. Go to [https://console.firebase.google.com/](https://console.firebase.google.com/).
2. Select or create your project: **`Bharat Mitra`** (or your existing project).

---

### Step 2: Register the Android Application
1. In your Firebase Project Overview, click the **Android** icon (`+ Add app`).
2. Enter the exact Android package name:
   ```
   com.aistudio.bharatmitra.ofc
   ```
3. Enter App nickname (Optional): `Bharat Mitra Official`.
4. Click **Register app**.

---

### Step 3: Download `google-services.json`
1. Click **Download google-services.json**.
2. Save the file to your computer.

---

### Step 4: Place `google-services.json` in the Project
Move the downloaded `google-services.json` file into the `app/` folder of this project:
```
Bharat_Mitra_v10/
├── app/
│   ├── google-services.json   <--- PLACE FILE HERE
│   ├── build.gradle.kts
│   └── src/
```

---

### Step 5: Enable Firebase Firestore & Authentication in Console
1. In Firebase Console, go to **Build > Firestore Database** and click **Create database**.
   - Choose production mode or test mode.
   - Region: Select **asia-south1 (Mumbai)** or your preferred region.
2. Go to **Build > Authentication**:
   - Click **Get Started**.
   - Enable **Google** provider and/or **Email/Password**.

---

### Step 6: Firestore Collections & Schema
The project is configured with the following collections:

1. **`users`**:
   - `name`: String
   - `phone`: String
   - `email`: String
   - `aadhaarMasked`: String
   - `role`: "rider" | "driver" | "elite" | "admin"
   - `createdAt`: String / Timestamp

2. **`elite_applications`**:
   - `regName`: String
   - `regPhone`: String
   - `regEmail`: String
   - `rawAadhaarDigits`: String (masked e.g. `XXXX-XXXX-4819`)
   - `orgName`: String
   - `isOrgIdUploaded`: Boolean
   - `status`: "PENDING" | "APPROVED" | "REJECTED"
   - `appliedAt`: String / Timestamp

3. **`complaints`**:
   - `userId`: String
   - `description`: String
   - `status`: "PENDING" | "INVESTIGATING" | "RESOLVED"
   - `adminReply`: String
   - `createdAt`: String / Timestamp

4. **`drivers`**:
   - `name`: String
   - `photoUrl`: String
   - `rcNumber`: String
   - `insuranceValidDate`: String
   - `rating`: Double
   - `totalTrips`: Int
   - `isVerified`: Boolean
   - `shiftFee`: Int (800)
   - `overtimeRate`: Int (100)

5. **`rides`**:
   - `pickupLat`, `pickupLng`: Double
   - `dropLat`, `dropLng`: Double
   - `fareBase`: Int
   - `fareWith15PercentCommission`: Int
   - `commissionAmount`: Int
   - `status`: "CONFIRMED" | "DRIVER_ASSIGNED" | "COMPLETED"
   - `riderId`: String
   - `driverId`: String
   - `otp`: String
   - `pickupAddress`, `dropAddress`: String
   - `createdAt`: String / Timestamp

6. **`tariffs`** (`standard_rates` doc):
   - `bikeBase`: 30
   - `totoBase`: 50
   - `autoBase`: 80
   - `fourSeaterNonAc`: 180
   - `fourSeaterAc`: 240
   - `sevenSeaterNonAc`: 320
   - `sevenSeaterAc`: 390
   - `commissionPercent`: 15

---

### Step 7: Build & Run
Run `./gradlew assembleDebug` to compile the app with live Firebase integration!

package com.example.data

import com.example.model.*

object SampleData {

    const val RAZORPAY_PAYMENT_URL = "https://razorpay.me/@bharatmitrainfotech"

    val rideOptions = listOf(
        RideOption(
            id = "bike",
            name = "Bike",
            category = "Two-Wheeler",
            iconType = "two_wheeler",
            hasAcOption = false,
            baseFareNonAc = 30,
            baseFareAc = 30,
            perKmRate = 8.5,
            capacity = "1 Person",
            etaMinutes = 4
        ),
        RideOption(
            id = "toto",
            name = "Toto",
            category = "E-Rickshaw",
            iconType = "electric_rickshaw",
            hasAcOption = false,
            baseFareNonAc = 50,
            baseFareAc = 50,
            perKmRate = 12.0,
            capacity = "4 Persons",
            etaMinutes = 6
        ),
        RideOption(
            id = "auto",
            name = "Auto",
            category = "Auto Rickshaw",
            iconType = "electric_rickshaw",
            hasAcOption = false,
            baseFareNonAc = 80,
            baseFareAc = 80,
            perKmRate = 16.0,
            capacity = "3-4 Persons",
            etaMinutes = 5
        ),
        RideOption(
            id = "four_seater",
            name = "4-Seater Cab",
            category = "Compact / Sedan",
            iconType = "car",
            hasAcOption = true,
            baseFareNonAc = 180,
            baseFareAc = 240,
            perKmRate = 22.0,
            capacity = "4 Persons",
            etaMinutes = 8
        ),
        RideOption(
            id = "seven_seater",
            name = "7-Seater Cab",
            category = "SUV / MPV",
            iconType = "suv",
            hasAcOption = true,
            baseFareNonAc = 320,
            baseFareAc = 390,
            perKmRate = 32.0,
            capacity = "6-7 Persons",
            etaMinutes = 11
        )
    )

    val locationSuggestions = listOf(
        LocationSuggestion(
            title = "Barasat Court",
            subtitle = "Kachhari Road, Barasat, North 24 Parganas",
            lat = 22.7222,
            lng = 88.4812,
            distanceKmFromCenter = 1.2
        ),
        LocationSuggestion(
            title = "Colony More",
            subtitle = "Jessore Road Crossing, Barasat",
            lat = 22.7235,
            lng = 88.4825,
            distanceKmFromCenter = 0.5
        ),
        LocationSuggestion(
            title = "Barasat Station",
            subtitle = "Station Road, Barasat, Sealdah North Section",
            lat = 22.7198,
            lng = 88.4841,
            distanceKmFromCenter = 1.6
        ),
        LocationSuggestion(
            title = "Madhyamgram Chowmatha",
            subtitle = "Sodepur-Barasat Road Junction, Madhyamgram",
            lat = 22.6980,
            lng = 88.4550,
            distanceKmFromCenter = 4.8
        ),
        LocationSuggestion(
            title = "Champadali More Bus Terminus",
            subtitle = "Taki Road, Barasat Main Hub",
            lat = 22.7250,
            lng = 88.4880,
            distanceKmFromCenter = 2.9
        ),
        LocationSuggestion(
            title = "Dakshinpara More",
            subtitle = "Barasat-Barrackpore Road",
            lat = 22.7120,
            lng = 88.4680,
            distanceKmFromCenter = 3.6
        ),
        LocationSuggestion(
            title = "Netaji Subhash Chandra Bose International Airport",
            subtitle = "Dum Dum, Gate 1 Departure, Kolkata",
            lat = 22.6540,
            lng = 88.4467,
            distanceKmFromCenter = 9.8
        ),
        LocationSuggestion(
            title = "Salt Lake Sector V",
            subtitle = "IT Hub, College More, Bidhannagar",
            lat = 22.5800,
            lng = 88.4330,
            distanceKmFromCenter = 18.5
        )
    )

    val verifiedDrivers = listOf(
        DriverProfile(
            id = "drv_1",
            name = "Subhashish Banerjee",
            avatarInitials = "SB",
            phone = "+91 98301 24510",
            rating = 4.9,
            totalTrips = 1420,
            experienceYears = 8,
            vehicleModel = "Maruti Suzuki Dzire VXi",
            vehicleCategory = VehicleCategory.SEDAN,
            rcNumber = "WB-25-AB-4921",
            insuranceValidity = "28 Dec 2027 (Digit Comprehensive)",
            fixed8HrFee = 800,
            overtimePerHourRate = 100,
            bio = "Expert in North 24 Parganas, Kolkata Airport, and Highway journeys. Polite, punctual, verified non-smoker.",
            badges = listOf("Police Verified", "Commercial DL", "COVID Vaccinated")
        ),
        DriverProfile(
            id = "drv_2",
            name = "Raju Mondal",
            avatarInitials = "RM",
            phone = "+91 97482 11984",
            rating = 4.85,
            totalTrips = 980,
            experienceYears = 6,
            vehicleModel = "Mayuri Deluxe Electric Toto",
            vehicleCategory = VehicleCategory.TOTO,
            rcNumber = "WB-26-E-1842",
            insuranceValidity = "15 Aug 2026 (Bajaj Allianz)",
            fixed8HrFee = 800,
            overtimePerHourRate = 100,
            bio = "Local Barasat & Madhyamgram route specialist. Smooth driving with eco-friendly battery vehicle.",
            badges = listOf("Local Guide", "Top Rated Toto", "Police Verified")
        ),
        DriverProfile(
            id = "drv_3",
            name = "Tapas Sengupta",
            avatarInitials = "TS",
            phone = "+91 94330 89234",
            rating = 4.95,
            totalTrips = 2150,
            experienceYears = 12,
            vehicleModel = "Toyota Innova Crysta ZX",
            vehicleCategory = VehicleCategory.SUV,
            rcNumber = "WB-02-AK-7719",
            insuranceValidity = "10 Nov 2027 (ICICI Lombard)",
            fixed8HrFee = 800,
            overtimePerHourRate = 100,
            bio = "VIP protocol experience, English speaking, automatic and manual transmission master. Perfect for outstation.",
            badges = listOf("Elite Chauffeur", "First-Aid Certified", "Commercial DL")
        ),
        DriverProfile(
            id = "drv_4",
            name = "Amitava Roy",
            avatarInitials = "AR",
            phone = "+91 89104 33219",
            rating = 4.8,
            totalTrips = 840,
            experienceYears = 5,
            vehicleModel = "Bajaj Compact 4S Auto",
            vehicleCategory = VehicleCategory.AUTO,
            rcNumber = "WB-24-AT-3312",
            insuranceValidity = "04 Jan 2027 (HDFC ERGO)",
            fixed8HrFee = 800,
            overtimePerHourRate = 100,
            bio = "Quick transit across Colony More, Jessore Road, and Barasat Court. Safe family-friendly driver.",
            badges = listOf("Police Verified", "Aadhaar Verified")
        ),
        DriverProfile(
            id = "drv_5",
            name = "Debashis Chakraborty",
            avatarInitials = "DC",
            phone = "+91 70034 51928",
            rating = 4.9,
            totalTrips = 1680,
            experienceYears = 9,
            vehicleModel = "Tata Tiago XZ+",
            vehicleCategory = VehicleCategory.HATCHBACK,
            rcNumber = "WB-25-CD-9014",
            insuranceValidity = "19 Sep 2027 (Tata AIG)",
            fixed8HrFee = 800,
            overtimePerHourRate = 100,
            bio = "Smooth city commuter. Excellent knowledge of Kolkata bypass, Sector V, and Newtown alleys.",
            badges = listOf("Commercial DL", "Zero Accidents", "Background Verified")
        ),
        DriverProfile(
            id = "drv_6",
            name = "Prabir Ghosh",
            avatarInitials = "PG",
            phone = "+91 91632 77410",
            rating = 4.88,
            totalTrips = 1120,
            experienceYears = 7,
            vehicleModel = "Mahindra Scorpio-N 4x4",
            vehicleCategory = VehicleCategory.SUV,
            rcNumber = "WB-25-SC-0811",
            insuranceValidity = "02 Jun 2028 (Oriental Insurance)",
            fixed8HrFee = 800,
            overtimePerHourRate = 100,
            bio = "Trained in defensive driving and night highway travel. Rugged vehicle for events, delegations, and long routes.",
            badges = listOf("Night Patrol Qualified", "Commercial DL", "Police Verified")
        )
    )

    val rentalVehicles = listOf(
        // Toto
        RentalVehicle("rent_t1", "Mayuri Standard Eco", VehicleCategory.TOTO, VehicleVariant.NON_AC, 90, 650, 4, "Electric", verifiedDrivers[1]),
        RentalVehicle("rent_t2", "Saarthi Deluxe E-Rickshaw", VehicleCategory.TOTO, VehicleVariant.AC, 120, 850, 4, "Electric (Fan & Curtains)", verifiedDrivers[1]),
        RentalVehicle("rent_t3", "Jezza Royal Club Executive", VehicleCategory.TOTO, VehicleVariant.PREMIUM, 160, 1100, 4, "Electric (High Torque)", verifiedDrivers[1]),

        // Auto
        RentalVehicle("rent_a1", "Bajaj RE 4-Stroke", VehicleCategory.AUTO, VehicleVariant.NON_AC, 110, 800, 3, "LPG / Petrol", verifiedDrivers[3]),
        RentalVehicle("rent_a2", "TVS King Deluxe Cool Edition", VehicleCategory.AUTO, VehicleVariant.AC, 140, 950, 3, "CNG / Blower", verifiedDrivers[3]),
        RentalVehicle("rent_a3", "Piaggio Ape City Gold Executive", VehicleCategory.AUTO, VehicleVariant.PREMIUM, 180, 1250, 3, "Petrol (Leather Seats)", verifiedDrivers[3]),

        // Hatchback
        RentalVehicle("rent_h1", "Maruti WagonR Tour", VehicleCategory.HATCHBACK, VehicleVariant.NON_AC, 200, 1400, 4, "Petrol", verifiedDrivers[4]),
        RentalVehicle("rent_h2", "Tata Tiago XZ Cool AC", VehicleCategory.HATCHBACK, VehicleVariant.AC, 240, 1700, 4, "Petrol", verifiedDrivers[4]),
        RentalVehicle("rent_h3", "Hyundai i20 Asta Luxury Edition", VehicleCategory.HATCHBACK, VehicleVariant.PREMIUM, 320, 2300, 4, "Turbo Petrol", verifiedDrivers[4]),

        // Sedan
        RentalVehicle("rent_s1", "Swift Dzire Tour Economy", VehicleCategory.SEDAN, VehicleVariant.NON_AC, 250, 1800, 4, "CNG / Petrol", verifiedDrivers[0]),
        RentalVehicle("rent_s2", "Maruti Dzire ZXi Climate Control", VehicleCategory.SEDAN, VehicleVariant.AC, 310, 2200, 4, "Petrol", verifiedDrivers[0]),
        RentalVehicle("rent_s3", "Honda City ZX Royal Edition", VehicleCategory.SEDAN, VehicleVariant.PREMIUM, 450, 3400, 4, "i-VTEC Petrol", verifiedDrivers[0]),

        // SUV
        RentalVehicle("rent_suv1", "Mahindra Bolero Neo", VehicleCategory.SUV, VehicleVariant.NON_AC, 320, 2300, 7, "Diesel", verifiedDrivers[5]),
        RentalVehicle("rent_suv2", "Mahindra Scorpio-N Climate Plus", VehicleCategory.SUV, VehicleVariant.AC, 420, 3100, 7, "mStallion Petrol", verifiedDrivers[5]),
        RentalVehicle("rent_suv3", "Toyota Innova Crysta Captain Lounge", VehicleCategory.SUV, VehicleVariant.PREMIUM, 600, 4500, 7, "2.4L Diesel Luxury", verifiedDrivers[2])
    )

    val sampleEliteRegistrations = listOf(
        EliteRegistration(
            id = "reg_101",
            fullName = "Bikramjit Majumdar",
            phone = "+91 98310 99421",
            email = "bikram.majumdar@bharatmitra.in",
            maskedAadhaar = "XXXX-XXXX-4819",
            organizationName = "All Bengal Transport Welfare Association",
            status = EliteApprovalStatus.APPROVED,
            appliedDate = "Yesterday, 04:15 PM",
            notes = "State Secretary credentials verified. Authorized for Public Emergency triggers."
        ),
        EliteRegistration(
            id = "reg_102",
            fullName = "Ananya Mukherjee",
            phone = "+91 91234 56780",
            email = "ananya.mukh@gmail.com",
            maskedAadhaar = "XXXX-XXXX-7320",
            organizationName = "North 24 Parganas Citizens Safety Council",
            status = EliteApprovalStatus.PENDING,
            appliedDate = "Today, 09:20 AM",
            notes = "Political/Org ID card submitted for scrutiny. Awaiting admin review."
        ),
        EliteRegistration(
            id = "reg_103",
            fullName = "Kaushik Dutta",
            phone = "+91 80173 44521",
            email = "kaushik.dutta@kolkatacivic.org",
            maskedAadhaar = "XXXX-XXXX-1994",
            organizationName = "Barasat Civic Volunteer Forum",
            status = EliteApprovalStatus.PENDING,
            appliedDate = "Today, 11:45 AM",
            notes = "Volunteer coordinator badge attached. Fast-track requested."
        )
    )

    val mapDots = listOf(
        // Orange Dots: Nearby Elite Members (Within 1/3/5 km)
        MapDot("dot_o1", "Sanjay Ghosh (Elite #108)", "+91 98300 11223", DotType.ORANGE_NEARBY_ELITE, -0.15f, 0.22f, 0.8, "Colony More, Barasat"),
        MapDot("dot_o2", "Dipankar Roy (Elite #142)", "+91 98305 66778", DotType.ORANGE_NEARBY_ELITE, 0.28f, -0.18f, 1.4, "Barasat Court Gate"),
        MapDot("dot_o3", "Moumita Sen (Elite #095)", "+91 97480 33445", DotType.ORANGE_NEARBY_ELITE, -0.38f, -0.25f, 2.7, "Madhyamgram Chowmatha"),
        MapDot("dot_o4", "Tanmoy Pal (Elite #210)", "+91 89100 55441", DotType.ORANGE_NEARBY_ELITE, 0.52f, 0.40f, 4.3, "Airport Gate 1 Area"),
        MapDot("dot_o5", "Pradyut Das (Elite #188)", "+91 94331 99880", DotType.ORANGE_NEARBY_ELITE, 0.12f, 0.45f, 3.1, "Champadali Bus Stand"),

        // Green Dots: User's Own Created Group Members (Worldwide / Global)
        MapDot("dot_g1", "Sourav Mukherjee (My Brother)", "+91 98309 88123", DotType.GREEN_USER_GROUP, -0.08f, -0.10f, 0.4, "Dakshinpara Barasat"),
        MapDot("dot_g2", "Anirban Mitra (Family Circle)", "+91 98311 22334", DotType.GREEN_USER_GROUP, 0.35f, 0.15f, 2.0, "Kachhari Road"),
        MapDot("dot_g3", "Debolina Roy (Group Co-Admin)", "+91 98322 44556", DotType.GREEN_USER_GROUP, 0.65f, -0.55f, 8.5, "Salt Lake Sector V, Kolkata"),
        MapDot("dot_g4", "Rahul Sharma (Overseas Node)", "+44 7911 123456", DotType.GREEN_USER_GROUP, 0.90f, 0.80f, 7600.0, "London, UK (Global Group)")
    )

    val sampleComplaints = listOf(
        UserComplaint(
            id = "cmp_1",
            userName = "Priya Sengupta",
            userPhone = "+91 98305 44120",
            issueType = "Driver Overcharge Issue",
            details = "Driver requested extra ₹50 for AC on a 4-seater ride near Colony More.",
            filedAt = "Today, 11:20 AM",
            status = ComplaintStatus.PENDING,
            adminReply = ""
        ),
        UserComplaint(
            id = "cmp_2",
            userName = "Arunangshu Mitra",
            userPhone = "+91 91238 99014",
            issueType = "GPS Drift Near Railway Station",
            details = "Pickup location drifted by 200m towards Station Road flyover.",
            filedAt = "Yesterday, 06:45 PM",
            status = ComplaintStatus.RESOLVED,
            adminReply = "Calibrated station geofence node with local antenna correction."
        ),
        UserComplaint(
            id = "cmp_3",
            userName = "Debabrata Pal",
            userPhone = "+91 80170 33219",
            issueType = "Elite Verification Delay",
            details = "Uploaded organization ID card 2 days ago; pending review.",
            filedAt = "2 days ago",
            status = ComplaintStatus.INVESTIGATING,
            adminReply = "Verification team is reviewing party association credentials."
        )
    )

    val sampleRatings = listOf(
        UserRating("rat_1", "Suman Roy", "+91 98311 22345", 5, "Subhashish da was very punctual and smooth driver on highway trip.", "Hire A Driver", "Today"),
        UserRating("rat_2", "Rina Ganguly", "+91 97480 11980", 5, "Elite SOS response simulation connected in 3 seconds. Very reassuring!", "Elite Service", "Yesterday"),
        UserRating("rat_3", "Tanmay Sen", "+91 89104 55670", 4, "Toto ride from Colony More to Barasat Court was prompt and fair priced.", "Ride Booking", "Yesterday"),
        UserRating("rat_4", "Amitava Ghosh", "+91 94330 77812", 5, "Dzire rental was spotless. Clean AC and verified driver Tapas.", "Rent A Car", "3 days ago")
    )

    val sampleAdminMessages = listOf(
        AdminMessage(
            id = "msg_1",
            title = "Welcome to Bharat Mitra Official Network",
            body = "Your trusted companion for safe rides, car rental, verified drivers, and mutual Elite emergency protection across West Bengal.",
            sentAt = "Today, 09:00 AM",
            recipientType = "All Users",
            isUrgent = false
        ),
        AdminMessage(
            id = "msg_2",
            title = "Barasat Monsoon Travel Advisory",
            body = "Expect slight delays on Jessore Road near Madhyamgram due to road resurfacing. Our verified drivers are taking bypass routes.",
            sentAt = "Yesterday, 04:30 PM",
            recipientType = "All Users",
            isUrgent = true
        )
    )

    val sampleRiders = listOf(
        RiderProfile(
            id = "rdr_1",
            name = "Raju Mondal",
            phone = "+91 97482 11984",
            vehicleType = "TOTO",
            vehicleNumber = "WB-26-E-1842",
            address = "Colony More, Jessore Road, Barasat",
            aadhaarMasked = "XXXX-XXXX-4819",
            drivingLicence = "Exempt / Verified e-Vehicle",
            rating = 4.9,
            isVerified = true
        ),
        RiderProfile(
            id = "rdr_2",
            name = "Arup Karmakar",
            phone = "+91 98305 66712",
            vehicleType = "BIKE",
            vehicleNumber = "WB-25-BK-9180",
            address = "Kachhari Road, Barasat Court",
            aadhaarMasked = "XXXX-XXXX-7721",
            drivingLicence = "WB-25-2019-00918",
            rating = 4.85,
            isVerified = true
        ),
        RiderProfile(
            id = "rdr_3",
            name = "Prabir Das",
            phone = "+91 91238 44091",
            vehicleType = "TOTO",
            vehicleNumber = "WB-26-E-4421",
            address = "Station Road, Barasat Junction",
            aadhaarMasked = "XXXX-XXXX-3310",
            drivingLicence = "Exempt / Verified e-Vehicle",
            rating = 4.88,
            isVerified = true
        )
    )

    val sampleRentOwners = listOf(
        RentACarOwnerProfile(
            id = "rent_own_1",
            ownerName = "Bikash Mukherjee",
            companyName = "Maa Tara Car Travels",
            phone = "+91 98310 99421",
            address = "Champadali More, Barasat, North 24 Parganas",
            aadhaarMasked = "XXXX-XXXX-1928",
            panNumber = "BMUKP4412K",
            businessLicence = "TRD/2025/BST/7781",
            bankDetails = "State Bank of India • A/C: 38210944120 • IFSC: SBIN0000024",
            cars = listOf(
                RentCarItem("car_1", "Toyota Innova Crysta Luxury", "WB-02-AK-7719", "", "RC-991204", "National Ins #8812"),
                RentCarItem("car_2", "Maruti Suzuki Dzire Tour", "WB-25-AB-4921", "", "RC-441290", "Digit Comprehensive")
            ),
            isVerified = true
        ),
        RentACarOwnerProfile(
            id = "rent_own_2",
            ownerName = "Debabrata Ghosh",
            companyName = "Bengal Highway Fleet Co.",
            phone = "+91 98301 55678",
            address = "Jessore Road, Madhyamgram Chowmatha",
            aadhaarMasked = "XXXX-XXXX-8821",
            panNumber = "DGHO18892L",
            businessLicence = "TRD/2024/MDG/1102",
            bankDetails = "HDFC Bank • A/C: 50100492188 • IFSC: HDFC0000142",
            cars = listOf(
                RentCarItem("car_3", "Mahindra Scorpio-N 4x4", "WB-25-SC-0811", "", "RC-778219", "Oriental Insurance")
            ),
            isVerified = true
        )
    )
}

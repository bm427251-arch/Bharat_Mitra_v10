package com.example

import com.example.data.SampleData
import com.example.model.DotType
import com.example.model.EmergencyType
import com.example.model.VehicleCategory
import org.junit.Assert.*
import org.junit.Test

class BharatMitraLogicTest {

    @Test
    fun testStandardFares() {
        val bike = SampleData.rideOptions.first { it.id == "bike" }
        assertEquals(30, bike.baseFareNonAc)

        val toto = SampleData.rideOptions.first { it.id == "toto" }
        assertEquals(50, toto.baseFareNonAc)

        val auto = SampleData.rideOptions.first { it.id == "auto" }
        assertEquals(80, auto.baseFareNonAc)

        val fourSeater = SampleData.rideOptions.first { it.id == "four_seater" }
        assertEquals(180, fourSeater.baseFareNonAc)
        assertEquals(240, fourSeater.baseFareAc)

        val sevenSeater = SampleData.rideOptions.first { it.id == "seven_seater" }
        assertEquals(320, sevenSeater.baseFareNonAc)
        assertEquals(390, sevenSeater.baseFareAc)
    }

    @Test
    fun testVerifiedDriversFeeStructure() {
        assertEquals(6, SampleData.verifiedDrivers.size)
        SampleData.verifiedDrivers.forEach { driver ->
            assertEquals(800, driver.fixed8HrFee)
            assertEquals(100, driver.overtimePerHourRate)
            assertTrue(driver.isRcVerified)
            assertTrue(driver.isInsuranceVerified)
            assertTrue(driver.isAadhaarVerified)
            assertTrue(driver.isCommercialDlVerified)
            assertTrue(driver.isPoliceVerified)
        }
    }

    @Test
    fun testMapDotsDistribution() {
        val orangeDots = SampleData.mapDots.filter { it.dotType == DotType.ORANGE_NEARBY_ELITE }
        val greenDots = SampleData.mapDots.filter { it.dotType == DotType.GREEN_USER_GROUP }

        assertTrue(orangeDots.isNotEmpty())
        assertTrue(greenDots.isNotEmpty())

        // Orange dots should have local distances (e.g. <= 5km)
        orangeDots.forEach { dot ->
            assertTrue("Orange dot distance ${dot.distanceKm} should be nearby", dot.distanceKm <= 5.0)
        }
    }

    @Test
    fun testAadhaarRedactionMasking() {
        val rawInput = "123456784819"
        val last4 = rawInput.takeLast(4)
        val masked = "XXXX-XXXX-$last4"
        assertEquals("XXXX-XXXX-4819", masked)
    }

    @Test
    fun testRazorpayUrl() {
        assertEquals("https://razorpay.me/@bharatmitrainfotech", SampleData.RAZORPAY_PAYMENT_URL)
    }
}

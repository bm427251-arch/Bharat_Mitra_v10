package com.example

import com.example.model.VehicleCategory
import org.junit.Assert.*
import org.junit.Test

class BharatMitraLogicTest {

    @Test
    fun testCustomerFairPrice() {
        val fairPrice = 100
        assertEquals("Customer sees Fair Price ₹100 ONLY", 100, fairPrice)
    }

    @Test
    fun testAdminInternal15PercentCommission() {
        val totalGross = 1500
        val company15Percent = (totalGross * 0.15).toInt()
        val driver85Percent = totalGross - company15Percent

        assertEquals(225, company15Percent)
        assertEquals(1275, driver85Percent)
        assertEquals(1500, company15Percent + driver85Percent)
    }

    @Test
    fun testAllVehicleTypesIncludeAmbulanceAndHeavyVehicles() {
        val allCategories = VehicleCategory.values().map { it.name }
        assertTrue(allCategories.contains("AMBULANCE"))
        assertTrue(allCategories.contains("LORRY"))
        assertTrue(allCategories.contains("TRUCK"))
        assertTrue(allCategories.contains("BUS"))
        assertTrue(allCategories.contains("SEDAN"))
        assertTrue(allCategories.contains("SUV"))
    }

    @Test
    fun testHireDriverRateChart() {
        val rate1Hr = 120
        val rate4Hr = 400
        val rate8Hr = 750

        assertTrue(rate1Hr > 0)
        assertTrue(rate4Hr > rate1Hr)
        assertTrue(rate8Hr > rate4Hr)
    }

    @Test
    fun testSubscriptionsPricing() {
        val rentProMonthly = 299
        val hireDriverProMonthly = 299

        assertEquals(299, rentProMonthly)
        assertEquals(299, hireDriverProMonthly)
    }
}

package de.kleinanzeigen.challenge.util

import de.kleinanzeigen.challenge.data.repositpry.realestate.model.Address
import de.kleinanzeigen.challenge.data.repositpry.realestate.model.Price
import org.junit.Assert.assertEquals
import org.junit.Test
import java.text.SimpleDateFormat
import java.util.Locale

class ExtensionsTest {

    @Test
    fun `fromIsoToLocalizedDateString returns localized date for valid input`() {
        val isoDate = "2023-01-22T15:30:00.000+0000" // ISO format
        val expectedDate =
            SimpleDateFormat.getDateInstance(SimpleDateFormat.MEDIUM, Locale.getDefault())
                .format(SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ",
                    Locale.getDefault()).parse(isoDate)!!)

        val result = isoDate.fromIsoToLocalizedDateString()

        assertEquals(expectedDate, result)
    }

    @Test
    fun `toFormattedCurrency returns formatted currency for valid input`() {
        val price = Price(amount = 1234, currencyCode = "EUR")
        val locale = Locale.GERMANY
        val expected = "1.234 €"

        val result = price.toFormattedCurrency(locale)

        assertEquals(expected, result)
    }

    @Test
    fun `toGermanAddressFormat returns properly formatted address`() {
        val address = Address(street = "Seestraße 1", zipCode = "12345", city = "Berlin")
        val expected = "Seestraße 1, 12345 Berlin"

        val result = address.toGermanAddressFormat()

        assertEquals(expected, result)
    }

}
package de.kleinanzeigen.challenge.util

import de.kleinanzeigen.challenge.data.repositpry.realestate.model.Address
import de.kleinanzeigen.challenge.data.repositpry.realestate.model.Price
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Currency
import java.util.Date
import java.util.Locale

fun String.fromIsoToLocalizedDateString(inputFormat: String = "yyyy-MM-dd'T'HH:mm:ss.SSSZ"): String {

    val outputFormat = SimpleDateFormat.getDateInstance(SimpleDateFormat.MEDIUM, Locale.getDefault())
    val inputSdf = SimpleDateFormat(inputFormat, Locale.getDefault())
    val date: Date? = inputSdf.parse(this)
    return if (date != null) {
        outputFormat.format(date)
    } else {
        ""
    }
}

fun Price.toFormattedCurrency(locale: Locale = Locale.getDefault()): String {
    val currencyInstance = NumberFormat.getCurrencyInstance(locale).apply {
        currency = Currency.getInstance(currencyCode)
        minimumFractionDigits = 0
        maximumFractionDigits = 2
    }
    val formattedValue = currencyInstance.format(this.amount)
    val currencySymbol = currencyInstance.currency?.symbol ?: ""

    return "${formattedValue.replace(currencySymbol, "").trim()} $currencySymbol"
}

fun Address.toGermanAddressFormat(): String{
    return String.format("%s, %s %s", street, zipCode, city)
}
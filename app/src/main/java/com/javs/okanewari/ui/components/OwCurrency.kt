package com.javs.okanewari.ui.components

data class OwCurrency (
    val symbol: String,
    val description: String
)

object CurrencySymbols{
    val dropdownCurrencyMenu = listOf(
        OwCurrency("$", "Dollar"),
        OwCurrency("£", "Pound"),
        OwCurrency("€", "Euro"),
        OwCurrency("¥", "Yen/Yuan")
    )

    val supportedCurrency = listOf("$", "£", "€", "¥")
}
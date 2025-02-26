package com.example.okanewari.data

import com.example.okanewari.Party

fun DummyPartyData(): List<Party> {
    return listOf(
        Party(name = "Sapporo 2024"),
        Party(name = "Shimanamikaido 2024"),
        Party(name = "Osaka 2023"),
        Party(name = "Fukuoka 2023"),
        Party(name = "Koreaaaaaaaaaaaaaaaaaaaaa 2024"),
        Party(name = "2022 Tokyo"),
        Party(name = "Korea"),
        Party(name = "Kyoto"),
        Party(name = "Kobe")
    )
}

fun GetDummyExpenses(): Party{
    val myParty = Party(name = "Dummy Party")
    for (i in 1..5){
        myParty.addExpense()
    }
    return myParty
}
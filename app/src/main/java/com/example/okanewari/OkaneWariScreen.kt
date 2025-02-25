package com.example.okanewari

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun OkaniWariApp(){
    // TODO Get info
    val myPartys = DummyPartyData()

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        LazyColumn(
            modifier = Modifier.padding(innerPadding)
        ) {
            items(myPartys) {
                DisplayPartyCard(it)
            }
        }
    }

}

@Composable
fun DisplayPartyCard(party: Party) {
    Log.d("Main Act.", "Displaying party card.")
    Card (
        modifier = Modifier.fillMaxSize().padding(12.dp).clickable {
            Log.d("Main Act.", "Party Selected")
        }
    ){
        Row(
            verticalAlignment = Alignment.CenterVertically
        ){
            Image(
                painter = painterResource(id = R.drawable.baseline_groups_24),
                contentDescription = "Photo of a person",
                modifier = Modifier.width(100.dp).height(100.dp).padding(all = 12.dp)
            )
            Column{
                Text(
                    // TODO align text in the column
                    text = party.name,
                    fontSize = 32.sp,
                    textAlign = TextAlign.Center,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}



// TODO move to separate dummy data file
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
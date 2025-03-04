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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.okanewari.data.DummyPartyData

@Composable
fun ListPartysScreen(
    onPartyCardClicked: () -> Unit,
    modifier: Modifier = Modifier
){
    // TODO Get info passed by click
    val myPartys = DummyPartyData()

    LazyColumn(
        modifier = modifier
    ) {
        items(myPartys) {
            DisplayPartyCard(
                party = it,
                myClick = onPartyCardClicked
            )
        }
    }
}


@Composable
fun DisplayPartyCard(party: Party, myClick: () -> Unit,) {
    Card (
        // Making each card a clickable to take to the party screen
        modifier = Modifier
            .fillMaxSize()
            .padding(dimensionResource(R.dimen.card_padding))
            .clickable(onClick = myClick)
    ){
        Row(
            verticalAlignment = Alignment.CenterVertically
        ){
            Image(
                painter = painterResource(id = R.drawable.baseline_groups_24),
                contentDescription = "Photo of a person",
                // TODO move constants to resource folder
                modifier = Modifier
                    .width(dimensionResource(R.dimen.thumbnail_width))
                    .height(dimensionResource(R.dimen.thumbnail_height))
                    .padding(all = dimensionResource(R.dimen.thumbnail_padding))
            )
            Column{
                Text(
                    // TODO align text in the column
                    // TODO move constants to resource folder
                    text = party.name,
                    fontSize = 32.sp,
                    textAlign = TextAlign.Center,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

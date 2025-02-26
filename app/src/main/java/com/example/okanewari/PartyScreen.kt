package com.example.okanewari

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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.okanewari.data.GetDummyExpenses

@Composable
fun PartyScreen(
    onNextButtonClicked: () -> Unit,
    modifier: Modifier = Modifier
){
    val myParty = GetDummyExpenses()
    LazyColumn (
        modifier = modifier
    ){
        items(myParty.expenseList){
            Card (
                // Making each card a clickable to take to the party screen
                modifier = Modifier.fillMaxSize().padding(12.dp)
            ){
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Text(
                        // TODO align text in the column
                        text = it.expenseName,
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        // TODO align text in the column
                        text = myParty.moneySymbol + it.total,
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

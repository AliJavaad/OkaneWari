package com.example.okanewari.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.okanewari.R
import com.example.okanewari.data.GetDummyExpenses
import com.example.okanewari.navigation.NavigationDestination
import com.example.okanewari.ui.components.DisplayFab

object ListExpensesDestination : NavigationDestination {
    override val route = "list_expenses"
    override val titleRes = R.string.list_expenses
}

@Composable
fun ListExpensesScreen(
    onAddExpenseButtonClicked: () -> Unit,
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
    DisplayFab(
        myClick = onAddExpenseButtonClicked
    )
}

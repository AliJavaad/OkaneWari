package com.example.okanewari.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.okanewari.OkaneWareTopAppBar
import com.example.okanewari.R
import com.example.okanewari.data.GetDummyExpenses
import com.example.okanewari.navigation.NavigationDestination
import com.example.okanewari.ui.components.DisplayFab
import com.example.okanewari.ui.components.FabSize

object ListExpensesDestination : NavigationDestination {
    override val route = "list_expenses"
    override val titleRes = R.string.list_expenses
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListExpensesScreen(
    onAddExpenseButtonClicked: () -> Unit,
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier
){
    // TODO get from database
    val myParty = GetDummyExpenses()
    Scaffold (
        topBar = {
            OkaneWareTopAppBar(
                // TODO title should be the current party name
                title = stringResource(ListExpensesDestination.titleRes),
                canNavigateBack = true,
                navigateUp = navigateBack
            )
        },
        floatingActionButton = {
            DisplayFab(
                myClick = onAddExpenseButtonClicked
            )
        }
    ) { innerPadding ->
        LazyColumn (
            modifier = modifier.padding(innerPadding)
        ){
            items(myParty.expenseList){
                Card (
                    // TODO make each card clickable
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
}

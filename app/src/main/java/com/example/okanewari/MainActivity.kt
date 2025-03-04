package com.example.okanewari

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.okanewari.ui.theme.OkaneWariTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent{
            OkaneWariTheme {
                OkaneWariApp()
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    OkaneWariTheme {
        Greeting("Android")
    }
}

/*
NOTES:

Party List table reqs.
    PartyName: Parties should be able to have the same name
    EntryTableKey: Key to the entry table

Entry list table
    Entry number:
    Name of Expense:
    Total Amount:
    Date: WAL
    Member_1: (Amount paid by member x)
    Member_2:
    Member_3: (Creating a new member would create a new column in the table)

    max member number = 15?
    max entries?
 */
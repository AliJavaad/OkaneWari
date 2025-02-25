package com.example.okanewari

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp

@Composable
fun PartyScreen(){
    Text(
        // TODO align text in the column
        text = "The Party Screen",
        fontSize = 32.sp,
        textAlign = TextAlign.Center
    )
}
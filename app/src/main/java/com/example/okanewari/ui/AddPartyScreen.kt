package com.example.okanewari.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import com.example.okanewari.R
import com.example.okanewari.ui.components.DoneAndCancelButtons


@Composable
fun AddPartyScreen(
    onDoneButtonClicked: () -> Unit,
    onCancelButtonClicked: () -> Unit,
    modifier: Modifier = Modifier
){
    Column(
        modifier = Modifier.padding(dimensionResource(R.dimen.medium_padding))
    ){
        TextField(
            value = "Fix me",
            onValueChange = { },
            label = { Text(stringResource(R.string.party_name)) },
            modifier = Modifier.fillMaxWidth()
        )
        DoneAndCancelButtons(
            doneButtonClick = onDoneButtonClicked,
            cancelButtonClick = onCancelButtonClicked
        )
    }

}
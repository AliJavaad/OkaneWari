package com.example.okanewari.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import com.example.okanewari.R
import com.example.okanewari.navigation.NavigationDestination
import com.example.okanewari.ui.components.DoneAndCancelButtons


object AddPartyDestination: NavigationDestination{
    override val route = "add_new_party"
    override val titleRes = R.string.add_new_party
}

@Composable
fun AddPartyScreen(
    onDoneButtonClicked: () -> Unit,
    onCancelButtonClicked: () -> Unit
){
    var toName by remember { mutableStateOf("") }
    Column(
        modifier = Modifier.padding(dimensionResource(R.dimen.medium_padding))
    ){
        TextField(
            value = toName,
            onValueChange = { toName = it },
            label = { Text(stringResource(R.string.party_name)) },
            placeholder = { Text(stringResource(R.string.my_party)) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        DoneAndCancelButtons(
            doneButtonClick = onDoneButtonClicked,
            cancelButtonClick = onCancelButtonClicked
        )
    }
}
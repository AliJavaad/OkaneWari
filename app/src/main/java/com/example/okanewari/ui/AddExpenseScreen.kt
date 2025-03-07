package com.example.okanewari.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import com.example.okanewari.R

@Composable
fun AddExpenseScreen(
    onDoneButtonClicked: () -> Unit,
    onCancelButtonClicked: () -> Unit,
    modifier: Modifier = Modifier
){
    DoneAndCancelButtons(
        doneButtonClick = onDoneButtonClicked,
        cancelButtonClick = onCancelButtonClicked
    )
}

@Composable
private fun DoneAndCancelButtons(
    doneButtonClick: () -> Unit,
    cancelButtonClick: () -> Unit
) {
    val mediumPadding = dimensionResource(R.dimen.medium_padding)
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(mediumPadding),
        verticalArrangement = Arrangement.spacedBy(mediumPadding),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = doneButtonClick
        ) {
            Text(
                text = stringResource(R.string.done),
                fontSize = 16.sp
            )
        }

        OutlinedButton(
            onClick = cancelButtonClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = stringResource(R.string.cancel),
                fontSize = 16.sp
            )
        }
    }
}
package com.example.okanewari.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import com.example.okanewari.R

@Composable
fun DoneAndCancelButtons(
    doneButtonClick: () -> Unit,
    cancelButtonClick: () -> Unit,
    enableDone: Boolean = true
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
            enabled = enableDone,
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

@Composable
fun DoneCancelDeleteButtons(
    doneButtonClick: () -> Unit,
    cancelButtonClick: () -> Unit,
    deleteButtonClicked: () -> Unit,
    enableDone: Boolean = true,
    enableDelete: Boolean = true,
    dialogueTitle: String = stringResource(R.string.attention),
    dialogueText: String = stringResource(R.string.delete_question)
) {
    DoneAndCancelButtons(
        doneButtonClick = doneButtonClick,
        cancelButtonClick = cancelButtonClick,
        enableDone = enableDone
    )

    var deleteConfirmationRequired by rememberSaveable { mutableStateOf(false) }
    val mediumPadding = dimensionResource(R.dimen.medium_padding)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(mediumPadding),
        verticalArrangement = Arrangement.spacedBy(mediumPadding),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        FilledTonalButton(
            onClick = { deleteConfirmationRequired = true },
            enabled = enableDelete,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
            ){
                Icon(
                    imageVector = Icons.Filled.Delete,
                    contentDescription = stringResource(R.string.delete)
                )
                Text(
                    text = stringResource(R.string.delete),
                    fontSize = 16.sp
                )
            }
        }
        if (deleteConfirmationRequired){
            DeleteConfirmationDialog(
                onConfirm = {
                    deleteConfirmationRequired = false
                    deleteButtonClicked() },
                onCancel = { deleteConfirmationRequired = false },
                title = dialogueTitle,
                text = dialogueText
            )
        }
    }
}

@Composable
fun DeleteConfirmationDialog(
    onConfirm: () -> Unit,
    onCancel: () -> Unit,
    title: String,
    text: String
) {
    AlertDialog(
        onDismissRequest = onCancel,
        title = { Text(title) },
        text = { Text(text) },
        dismissButton = {
            TextButton(onClick = onCancel) {
                Text(text = stringResource(R.string.no))
            }
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(text = stringResource(R.string.yes))
            }
        }
    )
}
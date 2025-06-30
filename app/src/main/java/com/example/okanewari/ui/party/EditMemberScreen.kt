package com.example.okanewari.ui.party

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.okanewari.OkaneWareTopAppBar
import com.example.okanewari.R
import com.example.okanewari.navigation.NavigationDestination
import com.example.okanewari.ui.OwViewModelProvider
import com.example.okanewari.ui.components.DeleteConfirmationDialog
import com.example.okanewari.ui.components.DoneAndCancelButtons
import com.example.okanewari.ui.components.DoneCancelDeleteButtons
import kotlinx.coroutines.launch


object EditMemberDestination: NavigationDestination {
    override val route = "edit_member"
    override val titleRes = R.string.edit_member_screen
    const val partyIdArg = "partyIdArg"
    const val memberIdArg = "expenseIdArg"
    val routeWithArg = "${route}/{$partyIdArg}/{$memberIdArg}"
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditMemberScreen(
    navigateBack: () -> Unit,
    navigateUp: () -> Unit,
    canNavigateBackBool: Boolean = true,
    viewModel: EditMemberViewModel = viewModel(factory = OwViewModelProvider.Factory)
){
    val coroutineScope = rememberCoroutineScope()
    Scaffold(
        topBar = {
            OkaneWareTopAppBar(
                title = stringResource(R.string.editing_member) + ": " +
                        viewModel.editMemberUiState.topBarPartyName,
                canNavigateBack = canNavigateBackBool,
                navigateUp = navigateUp
            )
        }
    ){ innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {
            MemberInputDialogue(
                memberDetails = viewModel.editMemberUiState.memberUiState.memberDetails,
                partyDetails = viewModel.editMemberUiState.partyUiState.partyDetails,
                onValueChange = viewModel::updateUiState
            )
            DoneCancelDeleteButtons(
                doneButtonClick = {
                    coroutineScope.launch {
                        viewModel.updateParty()
                        viewModel.updateMember()
                        navigateBack()
                    }
                },
                cancelButtonClick = navigateBack,
                deleteButtonClicked = {},
                enableDelete = !viewModel.editMemberUiState.memberUiState.memberDetails.owner,
                enableDone = viewModel.editMemberUiState.memberUiState.isEntryValid,
                dialogueText = stringResource(R.string.delete_member_dialogue_text_simple)
            )
            if (viewModel.editMemberUiState.memberUiState.memberDetails.owner){
                Text(
                    text = "The owner of the party cannot be deleted.",
                    color = Color.Red,
                    modifier = Modifier.padding(horizontal = dimensionResource(R.dimen.medium_padding))
                )
            }
        }
    }
}


// TODO Select to distribute expense evenly or to a member
//@Composable
//fun DeleteMemberButton(
//    deleteButtonClicked: () -> Unit,
//    enableDelete: Boolean = true
//){
//    var deleteConfirmationRequired by rememberSaveable { mutableStateOf(false) }
//    val mediumPadding = dimensionResource(R.dimen.medium_padding)
//
//    Column(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(mediumPadding),
//        verticalArrangement = Arrangement.spacedBy(mediumPadding),
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        FilledTonalButton(
//            onClick = { deleteConfirmationRequired = true },
//            enabled = enableDelete,
//            modifier = Modifier.fillMaxWidth()
//        ) {
//            Row(
//                modifier = Modifier
//            ){
//                Icon(
//                    imageVector = Icons.Filled.Delete,
//                    contentDescription = stringResource(R.string.delete)
//                )
//                Text(
//                    text = stringResource(R.string.delete),
//                    fontSize = 16.sp
//                )
//            }
//        }
//        if (deleteConfirmationRequired){
//            DeleteMemberDialogue(
//                onConfirm = {
//                    deleteConfirmationRequired = false
//                    deleteButtonClicked() },
//                onCancel = { deleteConfirmationRequired = false },
//            )
//        }
//    }
//}
//
//@Composable
//fun DeleteMemberDialogue(
//    onConfirm: () -> Unit,
//    onCancel: () -> Unit,
//    title: String = stringResource(R.string.attention),
//    text: String = stringResource(R.string.delete_question)
//) {
//    Dialog(
//        onDismissRequest = onCancel
//    ) {
//        Card(
//            modifier = Modifier
//                .fillMaxWidth()
//                .height(200.dp)
//                .padding(16.dp),
//            shape = RoundedCornerShape(16.dp),
//        ) {
//            Column(
//                modifier = Modifier.fillMaxWidth(),
//                verticalArrangement = Arrangement.Center,
//                horizontalAlignment = Alignment.CenterHorizontally,
//            ) {
//                Text(
//                    text = "Attention!",
//                    fontSize = 24.sp,
//                    fontWeight = FontWeight.Bold,
//                    modifier = Modifier.padding(8.dp),
//                    textAlign = TextAlign.Left,
//                )
//                Text(
//                    text = "To delete a member, please select how to handle their expenses.",
//                    fontSize = 16.sp,
//                    modifier = Modifier.padding(8.dp),
//                    textAlign = TextAlign.Left,
//                )
//            }
//        }
//    }
//}
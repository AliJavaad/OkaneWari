package com.example.okanewari.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeFloatingActionButton
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import com.example.okanewari.R


enum class FabSize(){
    LARGE,
    NORMAL,
    SMALL
}

@Composable
fun DisplayFab(
    myClick: () -> Unit,
    icon: ImageVector = Icons.Filled.Add,
    fabSize: FabSize = FabSize.NORMAL
){
    when (fabSize) {
        FabSize.LARGE -> {
            LargeFloatingActionButton(
                onClick = myClick,
                shape = CircleShape,
                modifier = Modifier
                    .padding(all = dimensionResource(R.dimen.medium_padding))
            ) {
                Icon(icon, stringResource( R.string.add_new_party))
            }
        }
        FabSize.SMALL -> {
            SmallFloatingActionButton(
                onClick = myClick,
                shape = CircleShape,
                modifier = Modifier
                    .padding(all = dimensionResource(R.dimen.medium_padding))
            ) {
                Icon(icon, stringResource( R.string.add_new_party))
            }
        }
        FabSize.NORMAL -> {
            FloatingActionButton(
                onClick = myClick,
                shape = CircleShape,
                modifier = Modifier
                    .padding(all = dimensionResource(R.dimen.medium_padding))
            ) {
                Icon(icon, stringResource(R.string.add_new_party))
            }
        }
    }
}
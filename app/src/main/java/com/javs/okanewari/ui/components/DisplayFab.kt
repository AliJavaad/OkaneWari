package com.javs.okanewari.ui.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeFloatingActionButton
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import com.javs.okanewari.R


enum class FabSize(){
    LARGE,
    NORMAL,
    SMALL
}

@Composable
fun DisplayFab(
    myClick: () -> Unit,
    icon: ImageVector = Icons.Filled.Add,
    fabSize: FabSize = FabSize.NORMAL,
    contentDescription: String = ""
){
    when (fabSize) {
        FabSize.LARGE -> {
            LargeFloatingActionButton(
                onClick = myClick,
                shape = CircleShape,
                modifier = Modifier
                    .navigationBarsPadding()
            ) {
                Icon(icon, contentDescription)
            }
        }
        FabSize.SMALL -> {
            SmallFloatingActionButton(
                onClick = myClick,
                shape = CircleShape,
                modifier = Modifier
                    .navigationBarsPadding()
            ) {
                Icon(icon, contentDescription)
            }
        }
        FabSize.NORMAL -> {
            FloatingActionButton(
                onClick = myClick,
                shape = CircleShape,
                modifier = Modifier
                    .navigationBarsPadding()
            ) {
                Icon(icon, contentDescription)
            }
        }
    }
}
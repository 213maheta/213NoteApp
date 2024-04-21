package com.twoonethree.noteapp.composeutils

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.twoonethree.noteapp.utils.ColorProvider

@Composable
fun ColorList(onBackColorSelect: (Int) -> Unit) {
    LazyRow(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
    ) {
        items(8) {
            ColorItem(it, onBackColorSelect)
        }
    }
}

@Composable
fun ColorItem(posColor: Int, onBackColorSelect: (Int) -> Unit) {
    Box(
        modifier = Modifier
            .size(80.dp)
            .padding(horizontal = 2.dp)
            .background(color = colorResource(id = ColorProvider.colorList[posColor]), shape = CircleShape)
            .clip(shape = CircleShape)
            .border(width = 2.dp, color = Color.Black, shape = CircleShape)
            .clickable {
                onBackColorSelect(posColor)
            }
    )
    {

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ColorBottomSheet(onBackColorSelect: (Int) -> Unit, onDismissColorSheet: (Boolean) -> Unit) {
    val sheetState = rememberModalBottomSheetState()

    ModalBottomSheet(
        onDismissRequest = {
            onDismissColorSheet(false)
        },
        sheetState = sheetState
    ) {
        ColorList(onBackColorSelect)
    }
}
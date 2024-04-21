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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ColorList(onBackColorSelect: (Color) -> Unit)
{
    val colorList = listOf(Color.Black, Color.White, Color.Red, Color.Blue, Color.Green, Color.Yellow, Color.Magenta)
    LazyRow(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
    ) {
        items(colorList){
            ColorItem(it, onBackColorSelect)
        }
    }
}

@Composable
fun ColorItem(color: Color, onBackColorSelect: (Color) -> Unit)
{
    Box(
        modifier = Modifier
            .size(80.dp)
            .padding(horizontal = 2.dp)
            .background(color = color, shape = CircleShape)
            .clip(shape = CircleShape)
            .border(width = 2.dp, color = Color.Black, shape = CircleShape)
            .clickable {
                onBackColorSelect(color)
            }
    )
    {

    }
}
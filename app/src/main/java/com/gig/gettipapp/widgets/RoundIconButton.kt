package com.gig.gettipapp.widgets

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

val IconButtonSizeModifier = Modifier.size(40.dp)


@Composable
fun RoundIconButton(
    modifier: Modifier = Modifier,
    image: ImageVector,
    colors: CardColors = CardDefaults.cardColors(
        containerColor = MaterialTheme.colorScheme.background,
        contentColor = Color.Black.copy(alpha = 0.8f)
    ),
    elevation: CardElevation = CardDefaults.cardElevation(
        defaultElevation = 3.dp,
        pressedElevation = 5.dp
    ),
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .padding(4.dp)
            .clickable { onClick.invoke() }
            .then(IconButtonSizeModifier),
        shape = CircleShape,
        colors = colors,
        elevation = elevation,
    ) {
        Icon (
            modifier = Modifier.fillMaxSize(),
            imageVector = image,
            contentDescription = "Plus or minus"
        )
    }
}
package dev.idkwuu.allesandroid.ui.components

import androidx.compose.foundation.Box
import androidx.compose.foundation.Image
import androidx.compose.foundation.Text
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.ColumnScope.gravity
import androidx.compose.foundation.layout.RowScope.gravity
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import java.util.*

@Composable
fun ListItem(title: String, subtitle: String, vectorIcon: Int? = null, imageIcon: Int? = null, onClick: () -> Unit) {
    Surface(
        modifier = Modifier.clickable(onClick = onClick)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
                .gravity(Alignment.CenterVertically)
                .padding(16.dp),
        ) {
            Column(
                Modifier.gravity(Alignment.CenterVertically)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.body1)
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.body2)
            }
            Box(
                modifier = Modifier.gravity(Alignment.CenterVertically)
            ) {
                if (vectorIcon != null) {
                    Image(vectorResource(vectorIcon), modifier = Modifier.size(30.dp))
                } else if (imageIcon != null){
                    Image(imageResource(imageIcon), modifier = Modifier.size(30.dp))
                }
            }
        }
    }
}

@Composable
fun Subtitle(text: String) {
    Text(
        text = text.toUpperCase(Locale.ROOT),
        style = MaterialTheme.typography.body1,
        modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 8.dp))
}
package com.chrrissoft.permissions.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign

@Composable
fun SectionTitle(
    title: String,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.fillMaxWidth(),
    ) {
        HorizontalDivider(color = MaterialTheme.colorScheme.primary, modifier = Modifier.weight(1f))
        Text(
            text = title,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.labelMedium,
            textAlign = TextAlign.Center
        )
        HorizontalDivider(color = MaterialTheme.colorScheme.primary, modifier = Modifier.weight(1f))
    }
}

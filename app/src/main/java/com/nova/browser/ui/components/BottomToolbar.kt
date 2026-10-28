package com.nova.browser.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nova.browser.ui.theme.BorderGlow
import com.nova.browser.ui.theme.DeepPurpleBar
import com.nova.browser.ui.theme.DeepPurpleCard
import com.nova.browser.ui.theme.NeonCyan
import com.nova.browser.ui.theme.TextMuted

@Composable
fun BottomToolbar(
    canGoBack: Boolean,
    canGoForward: Boolean,
    tabCount: Int,
    onBack: () -> Unit,
    onForward: () -> Unit,
    onRefresh: () -> Unit,
    onHome: () -> Unit,
    onOpenTabs: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(DeepPurpleBar)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Back Button
        IconButton(
            onClick = onBack,
            enabled = canGoBack
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                tint = if (canGoBack) NeonCyan else TextMuted,
                modifier = Modifier.size(24.dp)
            )
        }

        // Forward Button
        IconButton(
            onClick = onForward,
            enabled = canGoForward
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = "Forward",
                tint = if (canGoForward) NeonCyan else TextMuted,
                modifier = Modifier.size(24.dp)
            )
        }

        // Refresh Button
        IconButton(onClick = onRefresh) {
            Icon(
                imageVector = Icons.Default.Refresh,
                contentDescription = "Refresh",
                tint = NeonCyan,
                modifier = Modifier.size(24.dp)
            )
        }

        // Home Button
        IconButton(onClick = onHome) {
            Icon(
                imageVector = Icons.Default.Home,
                contentDescription = "Home",
                tint = NeonCyan,
                modifier = Modifier.size(24.dp)
            )
        }

        // Tabs Counter Button
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(DeepPurpleCard)
                .border(1.dp, BorderGlow, RoundedCornerShape(8.dp))
                .clickable { onOpenTabs() }
                .padding(horizontal = 10.dp, vertical = 6.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = tabCount.toString(),
                color = NeonCyan,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
        }
    }
}

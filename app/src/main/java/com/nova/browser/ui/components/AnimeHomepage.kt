package com.nova.browser.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nova.browser.model.SearchEngineRegistry
import com.nova.browser.model.SearchProvider
import com.nova.browser.ui.theme.*

@Composable
fun AnimeHomepage(
    currentProvider: SearchProvider,
    onSearchProviderClick: () -> Unit,
    onShortcutClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(CyberDarkBackground)
    ) {
        // Futuristic Cyberpunk Canvas Background (Anime Aesthetic elements)
        Canvas(modifier = Modifier.fillMaxSize()) {
            val width = size.width
            val height = size.height

            // Neon glowing gradient orb
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(NeonPurple.copy(alpha = 0.25f), Color.Transparent),
                    center = androidx.compose.ui.geometry.Offset(width * 0.8f, height * 0.2f),
                    radius = width * 0.7f
                )
            )

            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(NeonCyan.copy(alpha = 0.2f), Color.Transparent),
                    center = androidx.compose.ui.geometry.Offset(width * 0.2f, height * 0.7f),
                    radius = width * 0.8f
                )
            )

            // Sci-fi tech grid line
            val path = Path().apply {
                moveTo(0f, height * 0.35f)
                lineTo(width * 0.4f, height * 0.35f)
                lineTo(width * 0.5f, height * 0.38f)
                lineTo(width, height * 0.38f)
            }
            drawPath(
                path = path,
                color = NeonCyan.copy(alpha = 0.3f),
                style = androidx.compose.ui.graphics.drawscope.Stroke(width = 2f)
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Cyber/Anime Brand Badge
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(
                        Brush.horizontalGradient(
                            listOf(NeonCyan.copy(alpha = 0.15f), NeonPurple.copy(alpha = 0.15f))
                        )
                    )
                    .border(
                        1.dp,
                        Brush.horizontalGradient(listOf(NeonCyan, NeonPurple)),
                        RoundedCornerShape(12.dp)
                    )
                    .padding(horizontal = 20.dp, vertical = 8.dp)
            ) {
                Text(
                    text = "SYSTEM :: NOVA v1.0",
                    color = NeonCyan,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 2.sp
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Main Logo Title
            Text(
                text = "NOVA BROWSER",
                color = TextPrimary,
                fontSize = 32.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 3.sp
            )

            Text(
                text = "NEXT GEN ANIME WEB EXPERIENCE",
                color = TextSecondary,
                fontSize = 11.sp,
                letterSpacing = 1.5.sp
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Search Engine Selector Chip
            Surface(
                onClick = onSearchProviderClick,
                shape = RoundedCornerShape(20.dp),
                color = CyberSurfaceDark,
                border = androidx.compose.foundation.BorderStroke(1.dp, NeonCyan.copy(alpha = 0.5f)),
                modifier = Modifier.padding(bottom = 24.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null,
                        tint = NeonCyan,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Engine: ${currentProvider.name}",
                        color = TextPrimary,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = null,
                        tint = TextSecondary,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            // Quick Website Shortcuts Grid
            Text(
                text = "QUICK ACCESS",
                color = NeonCyan,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp,
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(bottom = 12.dp)
            )

            val shortcuts = listOf(
                ShortcutItem("Google", "https://www.google.com", Icons.Default.Search, NeonCyan),
                ShortcutItem("YouTube", "https://www.youtube.com", Icons.Default.PlayArrow, NeonPink),
                ShortcutItem("GitHub", "https://github.com", Icons.Default.Code, TextPrimary),
                ShortcutItem("Wikipedia", "https://en.wikipedia.org", Icons.Default.MenuBook, NeonPurple),
                ShortcutItem("DuckDuckGo", "https://duckduckgo.com", Icons.Default.Shield, NeonCyan),
                ShortcutItem("Brave", "https://search.brave.com", Icons.Default.RocketLaunch, NeonPink)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                shortcuts.take(3).forEach { item ->
                    ShortcutCard(item = item, onClick = { onShortcutClick(item.url) })
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                shortcuts.drop(3).take(3).forEach { item ->
                    ShortcutCard(item = item, onClick = { onShortcutClick(item.url) })
                }
            }
        }
    }
}

private data class ShortcutItem(
    val name: String,
    val url: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
    val accentColor: Color
)

@Composable
private fun ShortcutCard(
    item: ShortcutItem,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(100.dp)
            .height(85.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CyberCardDark),
        border = androidx.compose.foundation.BorderStroke(1.dp, item.accentColor.copy(alpha = 0.3f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(item.accentColor.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = item.icon,
                    contentDescription = item.name,
                    tint = item.accentColor,
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = item.name,
                color = TextPrimary,
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

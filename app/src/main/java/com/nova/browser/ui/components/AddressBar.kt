package com.nova.browser.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nova.browser.search.SearchEngine
import com.nova.browser.search.SearchEngineRegistry
import com.nova.browser.ui.theme.BorderGlow
import com.nova.browser.ui.theme.DeepPurpleBar
import com.nova.browser.ui.theme.DeepPurpleCard
import com.nova.browser.ui.theme.ElectricPurple
import com.nova.browser.ui.theme.NeonCyan
import com.nova.browser.ui.theme.TextMuted
import com.nova.browser.ui.theme.TextPrimary
import com.nova.browser.ui.theme.TextSecondary
import com.nova.browser.utils.UrlUtils

@Composable
fun AddressBar(
    currentUrl: String,
    isLoading: Boolean,
    progress: Int,
    currentSearchEngine: SearchEngine,
    onSearchEngineSelected: (SearchEngine) -> Unit,
    onNavigate: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var textValue by remember(currentUrl) { mutableStateOf(currentUrl) }
    var isEditing by remember { mutableStateOf(false) }
    var engineMenuExpanded by remember { mutableStateOf(false) }

    val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }

    val displayUrl = remember(currentUrl) {
        if (currentUrl.isEmpty()) "" else UrlUtils.extractDisplayDomain(currentUrl)
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(DeepPurpleBar)
            .padding(horizontal = 8.dp, vertical = 6.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(DeepPurpleCard)
                .border(1.dp, BorderGlow, RoundedCornerShape(20.dp))
                .padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            // Search Provider Selector / Security Icon
            Box {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .clickable { engineMenuExpanded = true }
                        .padding(horizontal = 6.dp, vertical = 4.dp)
                ) {
                    Icon(
                        imageVector = if (currentUrl.startsWith("https://")) Icons.Default.Lock else Icons.Default.Search,
                        contentDescription = "Search Provider",
                        tint = NeonCyan,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = currentSearchEngine.name.take(3).uppercase(),
                        color = NeonCyan,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                DropdownMenu(
                    expanded = engineMenuExpanded,
                    onDismissRequest = { engineMenuExpanded = false },
                    modifier = Modifier.background(DeepPurpleCard)
                ) {
                    Text(
                        text = "General Search",
                        color = TextMuted,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                    )
                    SearchEngineRegistry.getGeneralEngines().forEach { engine ->
                        DropdownMenuItem(
                            text = { Text(engine.name, color = if (engine == currentSearchEngine) NeonCyan else TextPrimary) },
                            onClick = {
                                onSearchEngineSelected(engine)
                                engineMenuExpanded = false
                            }
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Specialized Search",
                        color = TextMuted,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                    )
                    SearchEngineRegistry.getSpecializedEngines().forEach { engine ->
                        DropdownMenuItem(
                            text = { Text(engine.name, color = if (engine == currentSearchEngine) NeonCyan else TextPrimary) },
                            onClick = {
                                onSearchEngineSelected(engine)
                                engineMenuExpanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.width(6.dp))

            // URL/Query Input Field
            OutlinedTextField(
                value = if (isEditing) textValue else displayUrl,
                onValueChange = {
                    textValue = it
                    isEditing = true
                },
                modifier = Modifier
                    .weight(1f)
                    .focusRequester(focusRequester),
                placeholder = {
                    Text(
                        text = "Search or enter address",
                        color = TextMuted,
                        fontSize = 14.sp
                    )
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Go),
                keyboardActions = KeyboardActions(
                    onGo = {
                        isEditing = false
                        focusManager.clearFocus()
                        onNavigate(textValue)
                    }
                ),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                    focusedTextColor = TextPrimary,
                    unfocusedTextColor = TextPrimary,
                    cursorColor = NeonCyan
                )
            )

            if (textValue.isNotEmpty()) {
                IconButton(
                    onClick = {
                        textValue = ""
                        isEditing = true
                    },
                    modifier = Modifier.size(28.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Clear",
                        tint = TextSecondary,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }

        if (isLoading) {
            Spacer(modifier = Modifier.height(2.dp))
            LinearProgressIndicator(
                progress = { progress / 100f },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(2.dp),
                color = NeonCyan,
                trackColor = ElectricPurple.copy(alpha = 0.3f)
            )
        }
    }
}

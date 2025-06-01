package com.example.analyticssdk

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.analyticssdk.integrations.DevIntegration
import com.example.analyticssdk.integrations.FirebaseIntegration
import com.example.analyticssdk.integrations.MixpanelIntegration
import com.example.mcanalytics.AnalyticsManager

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AnalyticsDemo()
        }
    }
}

@Preview(showSystemUi = true)
@Composable
private fun AnalyticsDemoPreview() {
    AnalyticsDemo()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnalyticsDemo() {
    var newKey by remember { mutableStateOf("") }
    var newValue by remember { mutableStateOf("") }
    var globalProperties by remember { mutableStateOf(mapOf<String, String>()) }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Analytics Demo") }) },
        content = { padding ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .padding(16.dp)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                EventButtonsSection()

                Divider()

                GlobalPropertiesSection(
                    properties = globalProperties,
                    onAddProperty = { key, value ->
                        if (key.isNotBlank() && value.isNotBlank()) {
                            globalProperties = globalProperties.toMutableMap().apply {
                                put(key, value)
                            }
                            AnalyticsManager.setGlobalProperties(mapOf(key to value))
                            newKey = ""
                            newValue = ""
                        }
                    },
                    onUpdateProperty = { key, newValue ->
                        globalProperties = globalProperties.toMutableMap().apply {
                            put(key, newValue)
                        }
                        AnalyticsManager.setGlobalProperties(mapOf(key to newValue))
                    },
                    onRemoveProperty = { key ->
                        globalProperties = globalProperties - key
                        AnalyticsManager.unsetGlobalProperty(listOf(key))
                    },
                    onRemoveAll = {
                        globalProperties = emptyMap()
                        AnalyticsManager.unsetAllGlobalProperties()
                    },
                    newKey = newKey,
                    newValue = newValue,
                    onNewKeyChange = { newKey = it },
                    onNewValueChange = { newValue = it }
                )
            }
        }
    )
}


@Composable
private fun EventButtonsSection() {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(
            text = "Send Events",
            style = MaterialTheme.typography.headlineSmall
        )

        AnalyticsButton("Send Firebase Event") {
            AnalyticsManager.sendEvent(
                "firebase_button_click",
                mapOf("button_type" to "firebase", "timestamp" to System.currentTimeMillis()),
                FirebaseIntegration.integrationName
            )
        }

        AnalyticsButton("Send Mixpanel Event") {
            AnalyticsManager.sendEvent(
                "mixpanel_button_click",
                mapOf("button_type" to "mixpanel", "timestamp" to System.currentTimeMillis()),
                MixpanelIntegration.integrationName
            )
        }

        AnalyticsButton("Send Dev Event") {
            AnalyticsManager.sendEvent(
                "dev_button_click",
                mapOf("button_type" to "dev", "timestamp" to System.currentTimeMillis()),
                DevIntegration.integrationName
            )
        }
    }
}

@Composable
fun GlobalPropertiesSection(
    properties: Map<String, String>,
    onAddProperty: (String, String) -> Unit,
    onUpdateProperty: (String, String) -> Unit,
    onRemoveProperty: (String) -> Unit,
    onRemoveAll: () -> Unit,
    newKey: String,
    newValue: String,
    onNewKeyChange: (String) -> Unit,
    onNewValueChange: (String) -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Global Properties", style = MaterialTheme.typography.headlineSmall)

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedTextField(
                value = newKey,
                onValueChange = onNewKeyChange,
                label = { Text("Key") },
                modifier = Modifier.weight(1f)
            )
            OutlinedTextField(
                value = newValue,
                onValueChange = onNewValueChange,
                label = { Text("Value") },
                modifier = Modifier.weight(1f)
            )
        }

        AnalyticsButton("Add Property") {
            onAddProperty(newKey, newValue)
        }

        Spacer(Modifier.height(8.dp))

        if (properties.isEmpty()) {
            Text("No global properties set.", style = MaterialTheme.typography.bodyMedium)
        } else {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                properties.forEach { (key, value) ->
                    var editableValue by remember(key) { mutableStateOf(value) }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = key,
                            modifier = Modifier.weight(0.5f)
                        )
                        OutlinedTextField(
                            value = editableValue,
                            onValueChange = {
                                editableValue = it
                                onUpdateProperty(key, it)
                            },
                            modifier = Modifier.weight(1f),
                            label = { Text("Value") }
                        )
                        IconButton(onClick = { onRemoveProperty(key) }) {
                            Icon(Icons.Default.Delete, contentDescription = "Remove")
                        }
                    }
                }

                AnalyticsButton("Remove All Properties", onClick = onRemoveAll)
            }
        }
    }
}


@Composable
private fun AnalyticsButton(
    text: String,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(text)
    }
}


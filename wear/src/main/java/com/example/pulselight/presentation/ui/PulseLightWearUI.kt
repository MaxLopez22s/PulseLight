package com.example.pulselight.presentation.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun PulseLightWearUI(
    isServiceRunning: Boolean,
    onToggleService: () -> Unit
) {
    var bpm by remember { mutableStateOf("--") }
    var lux by remember { mutableStateOf("--") }
    var ambiente by remember { mutableStateOf("--") }

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "PulseLight",
                style = MaterialTheme.typography.titleLarge
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("BPM: $bpm", style = MaterialTheme.typography.bodyLarge)
                    Text("Luz: $lux lx", style = MaterialTheme.typography.bodyLarge)
                    Text("Ambiente: $ambiente", style = MaterialTheme.typography.bodyMedium)
                }
            }

            Button(onClick = onToggleService) {
                Text(text = if (isServiceRunning) "Detener" else "Iniciar")
            }
        }
    }
}

package com.example.pulselight.presentation

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import com.example.pulselight.service.SensorService
import com.example.pulselight.presentation.ui.PulseLightWearUI  // ← importa tu nueva interfaz

class MainActivity : ComponentActivity() {

    private var isServiceRunning by mutableStateOf(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            PulseLightWearUI(
                isServiceRunning = isServiceRunning,
                onToggleService = { toggleSensorService() }
            )
        }
    }

    private fun toggleSensorService() {
        if (isServiceRunning) {
            stopService(Intent(this, SensorService::class.java))
        } else {
            startService(Intent(this, SensorService::class.java))
        }
        isServiceRunning = !isServiceRunning
    }
}

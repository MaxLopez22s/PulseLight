package com.example.mobile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainActivity : ComponentActivity() {

    private val viewModel by viewModels<DataViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val dato by viewModel.datos.observeAsState("Esperando datos del reloj...")

            MaterialTheme {
                MobileDataScreen(dato = dato)
            }
        }
    }
}

class DataViewModel : ViewModel() {
    private val _datos = MutableLiveData("Esperando datos del reloj...")
    val datos: LiveData<String> get() = _datos

    fun actualizar(datos: String) {
        _datos.value = datos
    }
}

@Composable
fun MobileDataScreen(dato: String) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = dato,
                style = MaterialTheme.typography.headlineSmall
            )
        }
    }
}

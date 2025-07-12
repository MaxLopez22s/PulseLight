package com.example.pulselight.presentation.service

import android.app.Service
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.IBinder
import android.util.Log
import com.google.android.gms.wearable.PutDataMapRequest
import com.google.android.gms.wearable.Wearable

class SensorService : Service(), SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private var heartRateSensor: Sensor? = null
    private var lightSensor: Sensor? = null

    private var currentBPM: Int? = null
    private var currentLux: Float? = null

    override fun onCreate() {
        super.onCreate()
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        heartRateSensor = sensorManager.getDefaultSensor(Sensor.TYPE_HEART_RATE)
        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)

        heartRateSensor?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL)
        } ?: Log.w("PulseLight", "Sensor de ritmo cardíaco no disponible")

        lightSensor?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL)
        } ?: Log.w("PulseLight", "Sensor de luz no disponible")
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event == null) return

        when (event.sensor.type) {
            Sensor.TYPE_HEART_RATE -> {
                val bpm = event.values.firstOrNull()?.toInt()
                if (bpm != null && bpm > 0) {
                    currentBPM = bpm
                }
            }
            Sensor.TYPE_LIGHT -> {
                val lux = event.values.firstOrNull()
                if (lux != null && lux >= 0f) {
                    currentLux = lux
                }
            }
        }

        if (currentBPM != null && currentLux != null) {
            enviarDatosAlTelefono(currentBPM!!, currentLux!!)
            currentBPM = null
            currentLux = null
        }
    }

    private fun enviarDatosAlTelefono(bpm: Int, lux: Float) {
        val ambiente = if (lux < 10f) "Ambiente oscuro" else "Ambiente iluminado"

        val putDataRequest = PutDataMapRequest.create("/pulselight/data").apply {
            dataMap.putLong("timestamp", System.currentTimeMillis())
            dataMap.putInt("bpm", bpm)
            dataMap.putFloat("lux", lux)
            dataMap.putString("ambiente", ambiente)
        }.asPutDataRequest()

        Wearable.getDataClient(this).putDataItem(putDataRequest)
            .addOnSuccessListener {
                Log.d("PulseLight", "Datos enviados: BPM=$bpm, Lux=$lux lx")
            }
            .addOnFailureListener { e ->
                Log.e("PulseLight", "Error al enviar datos", e)
            }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // Puedes usar esto si necesitas reaccionar a cambios de precisión del sensor
    }

    override fun onDestroy() {
        super.onDestroy()
        sensorManager.unregisterListener(this)
    }

    override fun onBind(intent: Intent?): IBinder? = null
}

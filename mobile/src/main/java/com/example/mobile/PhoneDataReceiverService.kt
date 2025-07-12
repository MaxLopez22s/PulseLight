package com.example.mobile

import android.util.Log
import com.google.android.gms.wearable.*
import org.json.JSONObject
import okhttp3.*
import java.io.IOException

class PhoneDataReceiverService : WearableListenerService() {

    override fun onDataChanged(dataEvents: DataEventBuffer) {
        for (event in dataEvents) {
            if (event.type == DataEvent.TYPE_CHANGED) {
                val item = event.dataItem
                if (item.uri.path == "/pulselight/data") {
                    val dataMap = DataMapItem.fromDataItem(item).dataMap
                    val bpm = dataMap.getInt("bpm")
                    val lux = dataMap.getFloat("lux")
                    val ambiente = dataMap.getString("ambiente")
                    val timestamp = dataMap.getLong("timestamp")

                    val json = JSONObject().apply {
                        put("bpm", bpm)
                        put("lux", lux)
                        put("ambiente", ambiente)
                        put("timestamp", timestamp)
                    }

                    // 📍Después de construir el JSON:
                    Log.d("PhoneReceiver", "Datos recibidos del reloj: $json")
                    enviarDatosAServidor(json) // ← aquí se llama la función para enviar a tu API
                }
            }
        }
    }

    private fun enviarDatosAServidor(json: JSONObject) {
        val url = "https://TU_API/render/api/datos" // Reemplaza con tu URL real

        val body = json.toString()
        val client = OkHttpClient()
        val requestBody = RequestBody.create(
            MediaType.parse("application/json"), body
        )

        val request = Request.Builder()
            .url(url)
            .post(requestBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("API", "Fallo al enviar datos: ${e.message}")
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    Log.d("API", "Datos enviados correctamente al servidor")
                } else {
                    Log.e("API", "Error del servidor: ${response.code()}")
                }
            }
        })
    }
}

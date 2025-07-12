package com.example.pulselight.presentation.data

import android.content.Context
import android.util.Log
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

object EventoStorage {

    private const val FILE_NAME = "pulselight_registros.json"

    fun guardarRegistro(context: Context, bpm: Int, lux: Float, ambiente: String) {
        val timestamp = System.currentTimeMillis()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val fecha = dateFormat.format(Date(timestamp))

        val nuevoRegistro = JSONObject().apply {
            put("fecha", fecha)
            put("timestamp", timestamp)
            put("bpm", bpm)
            put("lux", lux)
            put("ambiente", ambiente)
        }

        try {
            val archivo = File(context.filesDir, FILE_NAME)
            val registros = if (archivo.exists()) {
                JSONArray(archivo.readText())
            } else {
                JSONArray()
            }

            registros.put(nuevoRegistro)
            archivo.writeText(registros.toString())
            Log.d("EventoStorage", "Registro guardado correctamente")
        } catch (e: Exception) {
            Log.e("EventoStorage", "Error al guardar registro: ${e.message}")
        }
    }

    fun obtenerRegistros(context: Context): JSONArray? {
        return try {
            val archivo = File(context.filesDir, FILE_NAME)
            if (archivo.exists()) {
                JSONArray(archivo.readText())
            } else null
        } catch (e: Exception) {
            Log.e("EventoStorage", "Error al leer registros: ${e.message}")
            null
        }
    }
}

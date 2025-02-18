package com.example.trainingroutine_pablocavaz.data.workers

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.trainingroutine_pablocavaz.R

class SessionNotificationWorker(context: Context, params: WorkerParameters) : Worker(context, params) {

    override fun doWork(): Result {
        val sessionName = inputData.getString("session_name") ?: "Nueva Sesión"
        val jugadorIds = inputData.getString("jugadores") ?: ""

        Log.d("WorkManager", "Ejecutando Worker para sesión: $sessionName - Jugadores: $jugadorIds") // 🟢 Verificar

        // Enviar la notificación
        sendNotification(sessionName, jugadorIds)

        return Result.success()
    }

    private fun sendNotification(sessionName: String, jugadorIds: String) {
        val channelId = "session_notifications"
        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Notificaciones de Sesiones",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }

        val jugadoresList = jugadorIds.split(",")

        for (jugadorId in jugadoresList) {
            val notificationId = jugadorId.hashCode()

            val notification = NotificationCompat.Builder(applicationContext, channelId)
                .setSmallIcon(R.mipmap.ic_launcher) //Icono cambiado aquí al logo de la app
                .setContentTitle("Nueva Sesión Asignada")
                .setContentText("Te han asignado a la sesión: $sessionName")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .build()

            notificationManager.notify(notificationId, notification)
        }
    }

}

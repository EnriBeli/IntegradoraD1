package com.example.integradorad

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class NotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val medicationName = intent?.getStringExtra("medication_name")

        val notification = NotificationCompat.Builder(context!!, "medication_channel")
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle("Recordatorio de Medicación")
            .setContentText("Es hora de tomar tu medicamento: $medicationName")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()

        val notificationManager = NotificationManagerCompat.from(context)
        notificationManager.notify(1, notification)
    }
}

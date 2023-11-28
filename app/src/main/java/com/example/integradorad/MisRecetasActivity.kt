package com.example.integradorad

import android.app.AlertDialog
import android.app.DownloadManager
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference


import java.io.File
import java.io.IOException
import java.util.concurrent.TimeUnit


class MisRecetasActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mis_recetas)

        auth = FirebaseAuth.getInstance()

        val openPdfButton = findViewById<Button>(R.id.openPdfButton)
        openPdfButton.setOnClickListener {
            openPdf()
        }
    }

    private fun openPdf() {
        val user: FirebaseUser? = auth.currentUser

        if (user != null) {
            val userId = user.uid

            // Construir la URL del PDF utilizando el UID del usuario
            val pdfUrl = "https://firebasestorage.googleapis.com/v0/b/integradora10-bb03f.appspot.com/o/receta_medica%2F$userId.pdf?alt=media&token=faa5efbd-2506-49a3-9779-03e554e0c03f"

            val intent = Intent(Intent.ACTION_VIEW)
            intent.setDataAndType(Uri.parse(pdfUrl), "application/pdf")
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP

            try {
                startActivity(intent)
                // Muestra un AlertDialog indicando al usuario que descargue el PDF
                val alertDialog = AlertDialog.Builder(this)
                    .setTitle("Descargar Receta")
                    .setMessage("Estimado usuario Por favor, descargue su receta.")
                    .setPositiveButton("Aceptar") { dialog, _ ->
                        // Descarga el PDF cuando se presiona "Aceptar"
                        downloadPdf(pdfUrl)
                        dialog.dismiss()
                    }
                    .create()

                alertDialog.show()
            } catch (e: ActivityNotFoundException) {
                // Manejar caso en el que no hay aplicaciones de visor de PDF instaladas
                e.printStackTrace()
                // Puedes mostrar un mensaje al usuario indicando que no hay aplicaciones de visor de PDF instaladas.
            }
        } else {
            // El usuario no está autenticado, puedes redirigirlo a la pantalla de inicio de sesión o mostrar un mensaje.
        }
    }
    private fun downloadPdf(pdfUrl: String) {
        val request = DownloadManager.Request(Uri.parse(pdfUrl))
            .setTitle("Descargando Receta")
            .setDescription("Su receta se está descargando...")
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            .setDestinationInExternalPublicDir(
                Environment.DIRECTORY_DOWNLOADS,
                "Mi_receta_medica.pdf"
            )

        val downloadManager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        downloadManager.enqueue(request)
    }
}
package com.example.integradorad


import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity

class ListaPersonasActivity : AppCompatActivity() {
    private val listaPersonas: MutableList<Persona> = ArrayList()
    private var etNombre: EditText? = null
    private var etUbicacion: EditText? = null
    private var btnAgregar: Button? = null
    private var listView: ListView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista_personas)

        listView = findViewById(R.id.listView) as? ListView

        listaPersonas.add(Persona("Consultorio Médico Dr. Pérez", "https://maps.app.goo.gl/YM94hkSxAuCDBakq9"))
        listaPersonas.add(Persona("Consultorio Médico Dr. Hernández", "https://maps.app.goo.gl/gLzSRPptrQgAPn3P9"))
        listaPersonas.add(Persona("Consultorio Médico Dr. Gómez", "geo:17.202965,-93.010821"))

        if (listView != null) {
            val adapter = PersonaAdapter(this, listaPersonas)
            listView?.adapter = adapter
        }

        btnAgregar?.setOnClickListener(View.OnClickListener {
            val nombre = etNombre?.text.toString()
            val ubicacion = etUbicacion?.text.toString()
            listaPersonas.add(Persona(nombre, ubicacion))
            (listView?.adapter as? PersonaAdapter)?.notifyDataSetChanged()
        })

        listView?.setOnItemClickListener { parent, view, position, id ->
            // Redirigir al navegador web con el enlace de ubicación
            val ubicacion = listaPersonas[position].ubicacion
            val gmmIntentUri = Uri.parse(ubicacion)
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
            startActivity(mapIntent)
        }

    }
}

package com.example.integradorad

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*

class ListaMedicamentosActivity : AppCompatActivity() {

    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val medicationRef = database.getReference("medicamentos")
    private lateinit var medicamentoAdapter: MedicamentoAdapter
    private lateinit var medicamentoList: MutableList<Medicamento>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista_medicamentos)

        // Configura RecyclerView y Adapter
        val recyclerView: RecyclerView = findViewById(R.id.listaMedicamentosRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        medicamentoList = mutableListOf()
        medicamentoAdapter = MedicamentoAdapter(medicamentoList)
        recyclerView.adapter = medicamentoAdapter

        // Configura el listener para obtener los datos de Firebase
        val valueEventListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                medicamentoList.clear()
                for (dataSnapshot in snapshot.children) {
                    val medicamento = dataSnapshot.getValue(Medicamento::class.java)
                    if (medicamento != null) {
                        medicamentoList.add(medicamento)
                    }
                }
                medicamentoAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                // Manejar errores, si es necesario
            }
        }

        // Agregar el listener a la referencia de Firebase
        medicationRef.addValueEventListener(valueEventListener)
    }

    fun volverAMisMedicamentos(view: View) {
        finish()  // Esto cierra la actividad actual y vuelve a la actividad anterior
    }
}

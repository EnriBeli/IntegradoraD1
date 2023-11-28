package com.example.integradorad

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class ListaMedicamentosActivity : AppCompatActivity() {

    private lateinit var userMedicationRef: DatabaseReference
    private lateinit var medicamentoAdapter: MedicamentoAdapter
    private lateinit var medicamentoList: MutableList<Medicamento>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista_medicamentos)

        val vaciarListaButton: Button = findViewById(R.id.vaciarListaButton)
        vaciarListaButton.setOnClickListener { vaciarLista(it) }


        // Obtener el usuario actual
        val currentUser = FirebaseAuth.getInstance().currentUser

        // Verificar si el usuario está autenticado
        if (currentUser != null) {
            // Establecer la referencia de Firebase para los medicamentos del usuario actual
            userMedicationRef = FirebaseDatabase.getInstance()
                .getReference("usuarios_medicamentos/${currentUser.uid}/medicamentos")

            // Configurar RecyclerView y Adapter
            val recyclerView: RecyclerView = findViewById(R.id.listaMedicamentosRecyclerView)
            recyclerView.layoutManager = LinearLayoutManager(this)
            medicamentoList = mutableListOf()
            medicamentoAdapter = MedicamentoAdapter(medicamentoList)
            recyclerView.adapter = medicamentoAdapter

            // Configurar el listener para obtener los datos de Firebase
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
            userMedicationRef.addValueEventListener(valueEventListener)
        }
    }
    fun vaciarLista(view: View) {
        // Crear un cuadro de diálogo de confirmación
        val builder = AlertDialog.Builder(this)
        builder.setTitle("¿Está seguro?")
            .setMessage("¿Está seguro de que quiere vaciar la lista de medicamentos?")
            .setPositiveButton("Sí") { dialog, which ->
                // Si el usuario confirma, eliminar todos los medicamentos de la base de datos
                userMedicationRef.removeValue()
                Toast.makeText(this, "Lista de medicamentos vaciada", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("No", null) // Si el usuario cancela, no hacer nada

        // Mostrar el cuadro de diálogo
        builder.show()
    }


    fun volverAMisMedicamentos(view: View) {
        finish()  // Esto cierra la actividad actual y vuelve a la actividad anterior
    }
}
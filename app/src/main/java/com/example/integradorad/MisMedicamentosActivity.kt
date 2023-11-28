package com.example.integradorad

import android.Manifest
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TimePicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.AlarmManagerCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import java.util.*

class MisMedicamentosActivity : AppCompatActivity() {

    private lateinit var medicationNameEditText: EditText
    private lateinit var timePicker: TimePicker
    private lateinit var addMedicationButton: Button
    private val REQUEST_SCHEDULE_EXACT_ALARM_PERMISSION = 123

    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private lateinit var currentUser: FirebaseUser
    private lateinit var userMedicationRef: DatabaseReference
    private lateinit var medicamentoAdapter: MedicamentoAdapter
    private lateinit var medicamentoList: MutableList<Medicamento>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mis_medicamentos)

        medicationNameEditText = findViewById(R.id.medicationNameEditText)
        timePicker = findViewById(R.id.timePicker)
        addMedicationButton = findViewById(R.id.addMedicationButton)
        medicamentoList = mutableListOf()
        medicamentoAdapter = MedicamentoAdapter(medicamentoList)

        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager

        // Obtener el usuario actual
        currentUser = auth.currentUser!!

        // Configurar la referencia de Firebase para los medicamentos del usuario actual
        userMedicationRef = database.getReference("usuarios_medicamentos/${currentUser.uid}/medicamentos")

        val recyclerView: RecyclerView = findViewById(R.id.medicamentoRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = medicamentoAdapter

        addMedicationButton.setOnClickListener {
            val medicationName = medicationNameEditText.text.toString().trim()
            val selectedHour = timePicker.hour
            val selectedMinute = timePicker.minute

            // Verificar si el nombre del medicamento es válido
            if (medicationName.isNotEmpty()) {
                // Programar notificación y guardar medicamento en Firebase
                scheduleNotification(alarmManager, medicationName, selectedHour, selectedMinute)
            } else {
                Toast.makeText(this, "Ingresa un nombre de medicamento válido", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun verListaMedicamentos(view: View) {
        val intent = Intent(this, ListaMedicamentosActivity::class.java)
        startActivity(intent)
    }

    private fun scheduleNotification(
        alarmManager: AlarmManager,
        medicationName: String,
        selectedHour: Int,
        selectedMinute: Int
    ) {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, selectedHour)
        calendar.set(Calendar.MINUTE, selectedMinute)
        calendar.set(Calendar.SECOND, 0)

        val intent = Intent(this, NotificationReceiver::class.java)
        intent.putExtra("medication_name", medicationName)

        val pendingIntent = PendingIntent.getBroadcast(
            this,
            System.currentTimeMillis().toInt(),
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )

        // Configura la notificación para que se repita diariamente
        val triggerTime = calendar.timeInMillis
        AlarmManagerCompat.setExactAndAllowWhileIdle(
            alarmManager,
            AlarmManager.RTC_WAKEUP,
            triggerTime,
            pendingIntent
        )

        // Guardar el medicamento en Firebase
        val newMedicamento = Medicamento(medicationName, "$selectedHour:$selectedMinute")
        userMedicationRef.push().setValue(newMedicamento)
        medicamentoAdapter.notifyDataSetChanged()

        Toast.makeText(this, "Notificación programada", Toast.LENGTH_SHORT).show()
    }
}

class Medicamento(val nombre: String = "", val hora: String = "") {

}

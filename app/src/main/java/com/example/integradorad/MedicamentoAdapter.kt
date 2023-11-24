package com.example.integradorad

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MedicamentoAdapter(private val medicamentos: List<Medicamento>) :
    RecyclerView.Adapter<MedicamentoAdapter.MedicamentoViewHolder>() {

    inner class MedicamentoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val medicationNameTextView: TextView = itemView.findViewById(R.id.medicationNameTextView)
        val medicationTimeTextView: TextView = itemView.findViewById(R.id.medicationTimeTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MedicamentoViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.medication_item, parent, false)
        return MedicamentoViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MedicamentoViewHolder, position: Int) {
        val medicamento = medicamentos[position]
        holder.medicationNameTextView.text = medicamento.nombre
        holder.medicationTimeTextView.text = medicamento.hora
    }

    override fun getItemCount(): Int {
        return medicamentos.size
    }
}

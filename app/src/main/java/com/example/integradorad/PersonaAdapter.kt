package com.example.integradorad
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

class PersonaAdapter(private val context: Context, private val personas: List<Persona>) : BaseAdapter() {

    override fun getCount(): Int {
        return personas.size
    }

    override fun getItem(position: Int): Any {
        return personas[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: View
        val viewHolder: ViewHolder

        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_persona, parent, false)
            viewHolder = ViewHolder(view)
            view.tag = viewHolder
        } else {
            view = convertView
            viewHolder = view.tag as ViewHolder
        }

        val persona = getItem(position) as Persona

        viewHolder.nombreTextView.text = persona.nombre
        viewHolder.ubicacionTextView.text = persona.ubicacion

        return view
    }

    private inner class ViewHolder(view: View) {
        val nombreTextView: TextView = view.findViewById(R.id.nombreTextView)
        val ubicacionTextView: TextView = view.findViewById(R.id.ubicacionTextView)
    }
}

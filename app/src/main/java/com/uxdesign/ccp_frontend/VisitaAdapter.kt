package com.uxdesign.ccp_frontend

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.uxdesign.cpp.R

class VisitaAdapter(private val visitas: List<VisitaRequest>) : RecyclerView.Adapter<VisitaAdapter.VisitaViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VisitaViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_visita, parent, false)
        return VisitaViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: VisitaViewHolder, position: Int) {
        val visita = visitas[position]
        holder.bind(visita)
        val context = holder.itemView.context

        holder.itemView.setOnClickListener {
            val intent = Intent(context, CambiarEstadoVisitaActivity::class.java).apply {
                putExtra("visita", visita)
            }
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return visitas.size
    }

    class VisitaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nombreCliente: TextView = itemView.findViewById(R.id.clienteVisita)
        private val direccionCliente: TextView = itemView.findViewById(R.id.direccionCliente)
        private val telefonoCliente: TextView = itemView.findViewById(R.id.telefonoCliente)
        private val estadoVisita: TextView = itemView.findViewById(R.id.estadoVisita)

        fun bind(visita: VisitaRequest) {
            nombreCliente.text = visita.cliente.nombre
            direccionCliente.text = visita.cliente.direccion
            telefonoCliente.text = visita.cliente.telefono
            estadoVisita.text = visita.estado
        }
    }
}
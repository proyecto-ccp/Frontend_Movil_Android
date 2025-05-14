package com.uxdesign.ccp_frontend

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.uxdesign.cpp.R

class CiudadVisitaAdapter(private val ciudadVisita: MutableList<CiudadVisita>,
                          private val visitas: List<VisitaRequest>) : RecyclerView.Adapter<CiudadVisitaAdapter.CiudadVisitaViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CiudadVisitaViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_ciudad_visita, parent, false)
        return CiudadVisitaViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CiudadVisitaViewHolder, position: Int) {
        val ciudadItem = ciudadVisita[position]
        holder.bind(ciudadItem)
        val context = holder.itemView.context

        holder.itemView.setOnClickListener {
            val visitasFiltradas = visitas.filter {
                it.cliente.ciudad == ciudadItem.ciudad && it.fechaVisita?.startsWith(ciudadItem.fecha) == true
            }
            val intent = Intent(context, DetalleVisitaActivity::class.java).apply {
                putExtra("visitas_filtradas", ArrayList(visitasFiltradas))

            }
            context.startActivity(intent)

        }
    }
    override fun getItemCount(): Int {
        return ciudadVisita.size
    }

    class CiudadVisitaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val ciudad: TextView = itemView.findViewById(R.id.ciudadVisita)
        private val cantidad: TextView = itemView.findViewById(R.id.cantidadVisita)
        private val fecha: TextView = itemView.findViewById(R.id.fechaVisita)

        fun bind(visita: CiudadVisita) {
            ciudad.text = visita.ciudad
            cantidad.text = "${visita.cantidad} visita(s)"
            fecha.text = visita.fecha
        }
    }
}
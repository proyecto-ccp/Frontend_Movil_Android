package com.uxdesign.ccp_frontend

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.uxdesign.cpp.R

class ClienteAdapter (private val clientes: List<Cliente>) : RecyclerView.Adapter<ClienteAdapter.ClienteViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClienteViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_cliente, parent, false)
        return ClienteViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ClienteViewHolder, position: Int) {
        val cliente = clientes[position]
        holder.bind(cliente)
        val context = holder.itemView.context

        holder.itemView.setOnClickListener {
            val intent = Intent(context, DetalleClienteActivity::class.java).apply {
                putExtra("cliente_id", cliente.id)
                putExtra("cliente_nombre", cliente.nombre)
                putExtra("cliente_tipodoc", cliente.tipoDocumento)
                putExtra("cliete_documento", cliente.documento)
                putExtra("cliente_direccion", cliente.direccion)
            }
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return clientes.size
    }

    class ClienteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nombreCliente: TextView = itemView.findViewById(R.id.nombreProducto)
        private val ciudadCliente: TextView = itemView.findViewById(R.id.descripcionProducto)

        fun bind(cliente: Cliente) {
            nombreCliente.text = cliente.nombre
            ciudadCliente.text = cliente.idCiudad
        }
    }
}
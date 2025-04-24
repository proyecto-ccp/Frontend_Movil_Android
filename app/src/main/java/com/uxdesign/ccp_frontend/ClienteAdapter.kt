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

        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, DetalleClienteActivity::class.java).apply {
                putExtra("cliente_id", cliente.id)
                putExtra("cliente_nombre", cliente.nombre)
                putExtra("cliente_apellido", cliente.apellido)
                putExtra("cliente_tipodoc", cliente.tipoDocumento)
                putExtra("cliente_documento", cliente.documento)
                putExtra("cliente_telefono", cliente.telefono)
                putExtra("cliente_direccion", cliente.direccion)
                putExtra("cliente_ciudad", cliente.idCiudad)
                putExtra("cliente_zona", cliente.idZona)
                putExtra("cliente_correo", cliente.email)
            }
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return clientes.size
    }

    class ClienteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nombreCliente: TextView = itemView.findViewById(R.id.nombreCliente)
        private val ciudadCliente: TextView = itemView.findViewById(R.id.ciudadCliente)

        fun bind(cliente: Cliente) {
            nombreCliente.text = "${cliente.nombre} ${cliente.apellido}"
            ciudadCliente.text = cliente.idCiudad
        }
    }
}
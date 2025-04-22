package com.uxdesign.ccp_frontend

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.uxdesign.cpp.R

class PedidoAdapter(private val pedidos: List<Pedido>) : RecyclerView.Adapter<PedidoAdapter.PedidoViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PedidoViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_cliente, parent, false)
        return PedidoViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: PedidoViewHolder, position: Int) {
        val pedido = pedidos[position]
        holder.bind(pedido)

        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, DetalleClienteActivity::class.java).apply {
                putExtra("pedido_id", pedido.id)
                putExtra("pedido_fecha", pedido.fecha)
                putExtra("pedido_estado", pedido.estado)
                putExtra("pedido_valor", pedido.valor)
            }
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return pedidos.size
    }

    class PedidoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val pedidoFecha: TextView = itemView.findViewById(R.id.fechaPedido)
        private val pedidoEstado: TextView = itemView.findViewById(R.id.estadoPedido)
        private val pedidoValor: TextView = itemView.findViewById(R.id.valorPedido)

        fun bind(pedido: Pedido) {
            pedidoFecha.text = pedido.fecha
            pedidoEstado.text = pedido.estado
            pedidoValor.text = pedido.valor.toString()
        }
    }
}
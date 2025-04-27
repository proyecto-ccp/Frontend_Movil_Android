package com.uxdesign.ccp_frontend

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.uxdesign.cpp.R
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class PedidoAdapter(private val pedidos: List<PedidoProcesado>) : RecyclerView.Adapter<PedidoAdapter.PedidoViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PedidoViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_estado_pedido, parent, false)
        return PedidoViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: PedidoViewHolder, position: Int) {
        val pedido = pedidos[position]
        holder.bind(pedido)

        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, DetallePedidoActivity::class.java).apply {
                putExtra("pedido_id", pedido.id)
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

        fun bind(pedido: PedidoProcesado) {
            val formato = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val fechaFormateada = try {
                val fecha = formato.parse(pedido.fechaEntrega)
                formato.format(fecha ?: Date())
            } catch (e: Exception) {
                pedido.fechaEntrega
            }

            pedidoFecha.text = fechaFormateada
            pedidoEstado.text = pedido.estadoPedido
            pedidoValor.text = String.format(Locale.getDefault(), "$%.2f", pedido.valorTotal)
        }
    }
}
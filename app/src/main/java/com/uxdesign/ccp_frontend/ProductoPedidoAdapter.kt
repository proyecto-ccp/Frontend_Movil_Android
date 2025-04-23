package com.uxdesign.ccp_frontend

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.uxdesign.cpp.R

class ProductoPedidoAdapter(private val productos: List<Producto>) : RecyclerView.Adapter<ProductoPedidoAdapter.ProductoPedidoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductoPedidoViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_producto, parent, false)
        return ProductoPedidoViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ProductoPedidoViewHolder, position: Int) {
        val producto = productos[position]
        holder.bind(producto)
        val context = holder.itemView.context
    }

    override fun getItemCount(): Int {
        return productos.size
    }

    class ProductoPedidoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nombreProducto: TextView = itemView.findViewById(R.id.nombreProducto)
        private val descripcionProducto: TextView = itemView.findViewById(R.id.descripcionProducto)
        private val precioProducto: TextView = itemView.findViewById(R.id.precioProducto)
        private val cantidadProducto: TextView = itemView.findViewById(R.id.cantidadProducto)
        private val valorProducto: TextView = itemView.findViewById(R.id.valorTotalProducto)

        fun bind(producto: Producto) {
            nombreProducto.text = producto.nombre
            descripcionProducto.text = producto.descripcion
            precioProducto.text = "Valor Unitario: $${producto.precioUnitario}"
            cantidadProducto.text = "Cantidad: $${producto.cantidad}"
            valorProducto.text = "Valor Total: $${(producto.precioUnitario*producto.cantidad)}"
        }
    }
}
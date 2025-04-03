package com.uxdesign.ccp_frontend

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.uxdesign.cpp.R

class ProductoAdapter(private val productos: List<Producto>) : RecyclerView.Adapter<ProductoAdapter.ProductoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductoViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_producto, parent, false)
        return ProductoViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ProductoViewHolder, position: Int) {
        val producto = productos[position]
        holder.bind(producto)
        val context = holder.itemView.context

        holder.itemView.setOnClickListener {
            val intent = Intent(context, DetalleProductoActivity::class.java).apply {
                putExtra("producto_id", producto.id)
                putExtra("producto_nombre", producto.nombre)
                putExtra("producto_precio", producto.precio)
                putExtra("producto_descripcion", producto.descripcion)
                putExtra("producto_imagen", producto.imagen)
            }
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return productos.size
    }

    class ProductoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nombreProducto: TextView = itemView.findViewById(R.id.nombreProducto)
        private val descripcionProducto: TextView = itemView.findViewById(R.id.descripcionProducto)
        private val precioProducto: TextView = itemView.findViewById(R.id.precioProducto)

        fun bind(producto: Producto) {
            nombreProducto.text = producto.nombre
            descripcionProducto.text = producto.descripcion
            precioProducto.text = producto.precio
        }
    }
}

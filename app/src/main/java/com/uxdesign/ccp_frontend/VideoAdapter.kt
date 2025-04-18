package com.uxdesign.ccp_frontend

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.uxdesign.cpp.R

class VideoAdapter(private val videos: List<Video>) : RecyclerView.Adapter<VideoAdapter.VideoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_video, parent, false)
        return VideoViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        val video = videos[position]
        holder.bind(video)
        val context = holder.itemView.context

        holder.itemView.setOnClickListener {
           val intent = Intent(context, GenerarRecomendacionActivity::class.java).apply {
                putExtra("video_imagen", video.imagen)
                putExtra("video_minuto", video.minuto)
           }
            context.startActivity(intent)

        }
    }
    override fun getItemCount(): Int {
        return videos.size
    }

    class VideoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nombreVideo: TextView = itemView.findViewById(R.id.nombreVideo)
        private val clienteVideo: TextView = itemView.findViewById(R.id.cliente)
        private val productoVideo: TextView = itemView.findViewById(R.id.nombreProducto)
        private val estadoVideo: TextView = itemView.findViewById(R.id.estado)

        fun bind(video: Video) {
            nombreVideo.text = video.nombre
            clienteVideo.text = video.cliente
            productoVideo.text = video.producto
            estadoVideo.text = video.estado
        }
    }

}
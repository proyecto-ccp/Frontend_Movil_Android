package com.uxdesign.ccp_frontend

import org.junit.Assert.*
import org.junit.Test

class VideoTest {

    @Test
    fun `deberia crear una instancia valida de Video`() {
        // Crear una instancia del objeto Video
        val video = Video(
            id = "video123",
            idCliente = "cliente456",
            idProducto = "producto789",
            nombre = "Video de Producto",
            urlVideo = "http://example.com/video.mp4",
            urlImagen = "http://example.com/imagen.jpg",  // Se puede pasar una url de imagen
            estadoCarga = "Cargado"
        )

        // Verificar que los valores sean correctos
        assertEquals("video123", video.id)
        assertEquals("cliente456", video.idCliente)
        assertEquals("producto789", video.idProducto)
        assertEquals("Video de Producto", video.nombre)
        assertEquals("http://example.com/video.mp4", video.urlVideo)
        assertEquals("http://example.com/imagen.jpg", video.urlImagen)
        assertEquals("Cargado", video.estadoCarga)
    }

    @Test
    fun `deberia crear una instancia de Video sin urlImagen`() {
        // Crear una instancia del objeto Video sin la urlImagen
        val video = Video(
            id = "video124",
            idCliente = "cliente457",
            idProducto = "producto790",
            nombre = "Otro Video de Producto",
            urlVideo = "http://example.com/video2.mp4",
            urlImagen = null,  // urlImagen es null
            estadoCarga = "Cargado"
        )

        // Verificar que los valores sean correctos
        assertEquals("video124", video.id)
        assertEquals("cliente457", video.idCliente)
        assertEquals("producto790", video.idProducto)
        assertEquals("Otro Video de Producto", video.nombre)
        assertEquals("http://example.com/video2.mp4", video.urlVideo)
        assertNull(video.urlImagen)  // Verificar que la urlImagen es null
        assertEquals("Cargado", video.estadoCarga)
    }
}

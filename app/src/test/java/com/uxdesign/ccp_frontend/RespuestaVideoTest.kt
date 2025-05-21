package com.uxdesign.ccp_frontend

import org.junit.Assert.*
import org.junit.Test

class RespuestaVideoTest {

    @Test
    fun `deberia crear una instancia valida de RespuestaVideo`() {
        // Crear una lista de videos de ejemplo
        val videos = listOf(
            Video(
                id = "1",
                idCliente = "cliente1",
                idProducto = "producto1",
                nombre = "Video A",
                urlVideo = "http://urlvideo1.com",
                urlImagen = "http://urlimagen1.com",
                estadoCarga = "Cargado"
            ),
            Video(
                id = "2",
                idCliente = "cliente2",
                idProducto = "producto2",
                nombre = "Video B",
                urlVideo = "http://urlvideo2.com",
                urlImagen = "http://urlimagen2.com",
                estadoCarga = "En progreso"
            )
        )

        // Crear una instancia de RespuestaVideo
        val respuestaVideo = RespuestaVideo(
            resultado = 1,
            mensaje = "Videos obtenidos correctamente",
            status = 200,
            videos = videos
        )

        // Verificar que los valores sean correctos
        assertEquals(1, respuestaVideo.resultado)
        assertEquals("Videos obtenidos correctamente", respuestaVideo.mensaje)
        assertEquals(200, respuestaVideo.status)
        assertEquals(2, respuestaVideo.videos.size)  // Verificar que haya dos videos
        assertEquals("Video A", respuestaVideo.videos[0].nombre)
        assertEquals("Video B", respuestaVideo.videos[1].nombre)
        assertEquals("http://urlvideo1.com", respuestaVideo.videos[0].urlVideo)
        assertEquals("http://urlvideo2.com", respuestaVideo.videos[1].urlVideo)
    }

    @Test
    fun `deberia manejar respuesta con lista vacia de videos`() {
        // Crear una lista vacía de videos
        val videosVacios = emptyList<Video>()

        // Crear una instancia de RespuestaVideo con videos vacíos
        val respuestaVideo = RespuestaVideo(
            resultado = 0,
            mensaje = "No se encontraron videos",
            status = 404,
            videos = videosVacios
        )

        // Verificar que los valores sean correctos
        assertEquals(0, respuestaVideo.resultado)
        assertEquals("No se encontraron videos", respuestaVideo.mensaje)
        assertEquals(404, respuestaVideo.status)
        assertTrue(respuestaVideo.videos.isEmpty())  // Verificar que la lista de videos esté vacía
    }
}

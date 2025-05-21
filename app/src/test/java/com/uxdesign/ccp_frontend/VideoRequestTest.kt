package com.uxdesign.ccp_frontend

import org.junit.Assert.*
import org.junit.Test

class VideoRequestTest {

    @Test
    fun `deberia crear una instancia valida de VideoRequest`() {
        // Crear una instancia de VideoRequest
        val videoRequest = VideoRequest(
            idCliente = "cliente123",
            idProducto = 101,
            nombre = "Video Producto A",
            video = "http://video.com/url"
        )

        // Verificar que los valores sean correctos
        assertEquals("cliente123", videoRequest.idCliente)
        assertEquals(101, videoRequest.idProducto)
        assertEquals("Video Producto A", videoRequest.nombre)
        assertEquals("http://video.com/url", videoRequest.video)
    }

    @Test
    fun `deberia crear una instancia con valores vacíos correctamente`() {
        // Crear una instancia de VideoRequest con valores vacíos
        val videoRequest = VideoRequest(
            idCliente = "",
            idProducto = 0,
            nombre = "",
            video = ""
        )

        // Verificar que los valores sean correctos (vacíos)
        assertEquals("", videoRequest.idCliente)
        assertEquals(0, videoRequest.idProducto)
        assertEquals("", videoRequest.nombre)
        assertEquals("", videoRequest.video)
    }

    @Test
    fun `deberia tener valores correctos despues de la creacion`() {
        val videoRequest = VideoRequest(
            idCliente = "cliente456",
            idProducto = 202,
            nombre = "Video Producto B",
            video = "http://video.com/another_url"
        )

        // Verificar que los valores sean correctos después de la creación
        assertNotNull(videoRequest)
        assertEquals("cliente456", videoRequest.idCliente)
        assertEquals(202, videoRequest.idProducto)
        assertEquals("Video Producto B", videoRequest.nombre)
        assertEquals("http://video.com/another_url", videoRequest.video)
    }
}

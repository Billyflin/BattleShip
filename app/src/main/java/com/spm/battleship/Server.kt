package com.spm.battleship


import java.io.OutputStream
import java.net.ServerSocket
import java.net.Socket
import java.nio.charset.Charset
import java.util.*
import kotlin.concurrent.thread


fun main() {
    val server = ServerSocket(9999)
    println("Server is running on port ${server.localPort}")

    while (true) {
        val client = server.accept()
        println("Client connected: ${client.inetAddress.hostAddress}")

        // Run client in it's own thread.
        thread { ClientHandler(client).run() }
    }

}

class ClientHandler(client: Socket) {
    private val client: Socket = client
    private val reader: Scanner = Scanner(client.getInputStream())
    private val writer: OutputStream = client.getOutputStream()
    private val calculator: Calculator = Calculator()
    private var running: Boolean = false

    fun run() {
        running = true
        // Welcome message
        while (running) {
            try {
                val text = reader.nextLine()
                if (text == "EXIT") {
                    shutdown()
                    continue
                }
                val values = text.split(' ')
                if (text == "host") {
                    val query = roomDeliver(values[0], values[1], values[2])
                    write(query.toString())
                    continue
                }

                val result = calculator.calculate(values[0].toInt(), values[1].toInt(), values[2])
                write(result)
            } catch (ex: Exception) {

            } finally {

            }

        }
    }


    private fun write(message: String) {
        writer.write((message + '\n').toByteArray(Charset.defaultCharset()))
    }

    private fun shutdown() {
        running = false
        client.close()
        println("${client.inetAddress.hostAddress} closed the connection")
    }

    fun roomDeliver(a: String, b: String, c: String): String {
        when (c) {
            "host" -> return listOf(a, b, client.inetAddress.hostAddress).toString()
            else -> {
                return "Something whent wrong"
            }
        }
    }
}

    //id ,sala,

class Calculator {

    fun calculate(a: Int, b: Int, operation: String): String {
        when (operation) {
            "add" -> return calc(a, b, ::add).toString()
            "sub" -> return calc(a, b, ::sub).toString()
            "div" -> return calc(a.toDouble(), b.toDouble(), ::div).toString()
            "multi" -> return calc(a, b, ::multi).toString()
            else -> {
                return "Something whent wrong"
            }
        }
    }

        // A Calculator (functional programming)
        private fun <T> calc(a: T, b: T, operation: (T, T) -> T): T {
            return operation(a, b)
        }

        private fun add(a: Int, b: Int): Int = a + b
        private fun sub(a: Int, b: Int): Int = a - b
        private fun div(a: Double, b: Double): Double = a / b
        private fun multi(a: Int, b: Int): Int = a * b

    }


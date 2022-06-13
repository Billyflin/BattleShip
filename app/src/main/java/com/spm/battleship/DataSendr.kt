package com.spm.battleship

import java.io.OutputStream
import java.net.Socket
import java.nio.charset.Charset
import java.util.*
import kotlin.concurrent.thread

class DataSendr {
    fun main() {
        val address = "192.168.1.89"
        val port = 9999

        val client = Client2(address, port)
        client.run()
    }

    class Client2(address: String, port: Int) {
        private val connection: Socket = Socket(address, port)
        private var connected: Boolean = true

        init {
            println("Connected to server at $address on port $port")
        }

        private val reader: Scanner = Scanner(connection.getInputStream())
        private val writer: OutputStream = connection.getOutputStream()

        fun run() {
            thread { read() }
            while (connected) {
                val input = readLine() ?: ""
                if ("exit" in input) {
                    connected = false
                    reader.close()
                    connection.close()
                } else {
                    write(input)
                }
            }

        }

        private fun write(message: String) {
            writer.write((message + '\n').toByteArray(Charset.defaultCharset()))
        }

        private fun read() {
            while (connected)
                println(reader.nextLine())
        }
    }

}
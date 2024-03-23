package ru.alexp0111.flexypixel.bluetooth

import com.google.gson.Gson

interface BluetoothMessage {
    fun asJson(): String
}

data class MessagePixel(
    private val panelPosition: Int,
    private val pixelPosition: String,
    private val pixelColor: String,
) : BluetoothMessage {
    override fun asJson(): String {
        return Gson().toJson(this)
    }

}

data class MessageType(
    val type: String,
) : BluetoothMessage {

    override fun asJson(): String {
        return Gson().toJson(this)
    }

    companion object {
        val CONFIG = MessageType("CONF")
        val MODE = MessageType("MODE")
        val DATA = MessageType("DATA")
    }
}

data class TransactionMode(
    val mode: String,
) : BluetoothMessage {

    override fun asJson(): String {
        return Gson().toJson(this)
    }

    companion object {
        val PIXEL = TransactionMode("PIX")
        val SEQUENCE = TransactionMode("SEQ")
    }
}

class PanelConfiguration(
    private val configuration: Array<String>
) : BluetoothMessage {
    override fun asJson(): String {
        return Gson().toJson(this)
    }

    override fun toString(): String {
        return configuration.toList().toString()
    }
}
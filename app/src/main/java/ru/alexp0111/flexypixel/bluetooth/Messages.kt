package ru.alexp0111.flexypixel.bluetooth

import com.google.gson.Gson
import ru.alexp0111.flexypixel.data.model.PanelConfiguration

interface BluetoothMessage {
    fun asJson(): String
}

/**
 * Type of message, that will be transferred
 *
 * ==========================
 * @Example: {"type": "MODE"}
 * ==========================
 * */
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


/**
 * Type of transaction mode
 * 1. PIXEL - sending only one pixel message
 * 2. SEQUENCE - sending frames
 *
 * =========================
 * @Example: {"mode": "SEQ"}
 * =========================
 * */
data class MessageTransactionMode(
    val mode: String,
) : BluetoothMessage {

    override fun asJson(): String {
        return Gson().toJson(this)
    }

    companion object {
        val PIXEL = MessageTransactionMode("PIX")
        val SEQUENCE = MessageTransactionMode("SEQ")
    }
}


/**
 * Information about specific pixel pixel.
 *
 * ======================================================================
 * @Example: {"panelPosition":9,"pixelColor":"967","pixelPosition":"000"}
 * ======================================================================
 * */
data class MessagePixel(
    private val panelPosition: Int,
    private val pixelPosition: String,
    private val pixelColor: String,
) : BluetoothMessage {
    override fun asJson(): String {
        return Gson().toJson(this)
    }

}


/**
 * Panel configuration: string with size MAX_SIZE * 3,
 * representing type of panel in chain.
 *
 * ========================================
 * @Example: {"configuration": "000...000"}
 * ========================================
 * */
data class MessagePanelConfiguration(
    private val configuration: String = "000".repeat(PanelConfiguration.MAX_SIZE),
) : BluetoothMessage {
    override fun asJson(): String {
        return Gson().toJson(this)
    }

    override fun toString(): String {
        return configuration.toList().toString()
    }
}


/**
 * Information obout how much frames we sending & delay between frames (milliseconds)
 * Max(interframeDelay) = 86'400'000
 *
 * =================================
 * @Example: {"framesAmount":"030","interframeDelay":"00000042"}
 * =================================
 * */
data class MessageFramesMetaData(
    private val framesAmount: String,
    private val interfameDelay: String,
) : BluetoothMessage {
    override fun asJson(): String {
        return Gson().toJson(this)
    }

    companion object {
        const val INTERFRAME_SIZE = 8
    }
}


/**
 * Hole frame with number of panels set in configuration.
 *
 * =========================================================
 * @Example: {"frame":"000000000000000000..000000000000000"}
 * =========================================================
 * */
data class MessageFrame(
    private val frame: String,
) : BluetoothMessage {
    override fun asJson(): String {
        return Gson().toJson(this)
    }
}
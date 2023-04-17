package com.sixiaolong.androidwol

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val button: Button = findViewById(R.id.WoLButton)

        button.setOnClickListener {
            val mMacAddress = findViewById<EditText>(R.id.MacAddress)
            val iIPAddress = findViewById<EditText>(R.id.IPAddress)
            val iIPPort = findViewById<EditText>(R.id.IPPort)

            val macAddress = mMacAddress.text.toString()
            val ipAddress = iIPAddress.text.toString()
            val ipPort = iIPPort.text.toString().toInt()
            wakeOnLan(macAddress, ipAddress, ipPort)
        }
    }
}

fun wakeOnLan(macAddress: String, ipAddress: String, ipPort: Int) {
    GlobalScope.launch(Dispatchers.IO) {
        val macBytes = macAddress.split(":").map { it.toInt(16).toByte() }.toByteArray()
        val macString = macBytes.joinToString(":") { "%02X".format(it) }
        Log.v("macBytes内容", macString)
        val bytes = ByteArray(6 + 16 * 6)
        for (i in 0 until 6) {
            bytes[i] = 0xFF.toByte()
        }

        for (i in 1..16) {
            for (j in macBytes.indices) {
                bytes[i * 6 + j] = macBytes[j]
            }
        }

        val magicPacketString = bytes.joinToString(" ") { "%02X".format(it) }
        Log.v("Magic Packet内容", magicPacketString)

        val address = InetAddress.getByName(ipAddress)
        val packet = DatagramPacket(bytes, bytes.size, address, ipPort)
        val socket = DatagramSocket()

        for (i in 0 until 10) {
            socket.send(packet)
        }
        socket.close()
    }
}

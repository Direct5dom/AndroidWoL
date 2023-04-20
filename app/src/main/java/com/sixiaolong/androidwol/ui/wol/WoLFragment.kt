package com.sixiaolong.androidwol.ui.wol

import android.annotation.SuppressLint
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import com.sixiaolong.androidwol.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress

class WoLFragment : Fragment() {
//    private lateinit var button: Button
//
//    // 引入SharedPreferences进行轻量化存储
//    private lateinit var mSharedPreferences: SharedPreferences
//    private lateinit var mEditor: SharedPreferences.Editor
//
//    @SuppressLint("MissingInflatedId")
//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        // 初始化三个SharedPreferences
//        mSharedPreferences = requireActivity().getSharedPreferences("MacAddress", MODE_PRIVATE)
//        mSharedPreferences = requireActivity().getSharedPreferences("IPAddress", MODE_PRIVATE)
//        mSharedPreferences = requireActivity().getSharedPreferences("IPPort", MODE_PRIVATE)
//        mEditor = mSharedPreferences.edit()
//
//        val view =
//            inflater.inflate(com.sixiaolong.androidwol.R.layout.fragment_wol, container, false)
//
//        val mMacAddress = view.findViewById<EditText>(R.id.MacAddress)
//        val iIPAddress = view.findViewById<EditText>(R.id.IPAddress)
//        val iIPPort = view.findViewById<EditText>(R.id.IPPort)
//
//        val mMacAddressString = mSharedPreferences.getString("MacAddress", "")
//        val iIpAddressString = mSharedPreferences.getString("IPAddress", "")
//        val iIPPortIntInt = mSharedPreferences.getInt("IPAddress", 0)
//        // 如果有 读取上一次的配置
//        if (mMacAddressString != null)
//            mMacAddress.setText(mMacAddressString)
//        if (mMacAddressString != null)
//            iIPAddress.setText(iIpAddressString)
//        if (mMacAddressString != null)
//            iIPPort.setText(iIPPortIntInt.toString())
//
//        button = view.findViewById(com.sixiaolong.androidwol.R.id.WoLButton)
//        button.setOnClickListener {
//
//            val macAddress = mMacAddress.text.toString()
//            val ipAddress = iIPAddress.text.toString()
//            val ipPort = iIPPort.text.toString().toInt()
//
//            // 保存本次配置
//            mEditor.putString("MacAddress", macAddress)
//            mEditor.putString("IPAddress", ipAddress)
//            mEditor.putInt("IPPort", ipPort)
//            mEditor.apply()
//
//            wakeOnLan(macAddress, ipAddress, ipPort)
//        }
//        return view
//    }

    private fun wakeOnLan(macAddress: String, ipAddress: String, ipPort: Int) {
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
}
package com.sixiaolong.androidwol.ui.wol

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import com.sixiaolong.androidwol.R

class WoLConfigFragment : Fragment() {
    private lateinit var button: Button

    // 引入SharedPreferences进行轻量化存储
    private lateinit var mSharedPreferences: SharedPreferences
    private lateinit var mEditor: SharedPreferences.Editor

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // 初始化三个SharedPreferences
        mSharedPreferences = requireActivity().getSharedPreferences(
            "MacAddress",
            Context.MODE_PRIVATE
        )
        mSharedPreferences = requireActivity().getSharedPreferences(
            "IPAddress",
            Context.MODE_PRIVATE
        )
        mSharedPreferences = requireActivity().getSharedPreferences("IPPort", Context.MODE_PRIVATE)
        mEditor = mSharedPreferences.edit()

        val view =
            inflater.inflate(
                com.sixiaolong.androidwol.R.layout.fragment_wol_config,
                container,
                false
            )

        val nName = view.findViewById<EditText>(R.id.ConfigName)
        val mMacAddress = view.findViewById<EditText>(R.id.ConfigMacAddress)
        val iIPAddress = view.findViewById<EditText>(R.id.ConfigIPAddress)
        val iIPPort = view.findViewById<EditText>(R.id.ConfigIPPort)

        val nNameString = mSharedPreferences.getString("Name", "")
        val mMacAddressString = mSharedPreferences.getString("MacAddress", "")
        val iIpAddressString = mSharedPreferences.getString("IPAddress", "")
        val iIPPortString = mSharedPreferences.getString("IPAddress", "")
        // 如果有 读取上一次的配置
        if (nNameString != null)
            nName.setText(nNameString)
        if (mMacAddressString != null)
            mMacAddress.setText(mMacAddressString)
        if (mMacAddressString != null)
            iIPAddress.setText(iIpAddressString)
        if (mMacAddressString != null)
            iIPPort.setText(iIPPortString.toString())

        button = view.findViewById(com.sixiaolong.androidwol.R.id.ConfigSaveButton)
        button.setOnClickListener {

            val name = nName.text.toString()
            val macAddress = mMacAddress.text.toString()
            val ipAddress = iIPAddress.text.toString()
            val ipPort = iIPPort.text.toString().toInt()

            // 保存本次配置
            mEditor.putString("Name",name)
            mEditor.putString("MacAddress", macAddress)
            mEditor.putString("IPAddress", ipAddress)
            mEditor.putInt("IPPort", ipPort)
            mEditor.apply()
        }
        return view
    }
}
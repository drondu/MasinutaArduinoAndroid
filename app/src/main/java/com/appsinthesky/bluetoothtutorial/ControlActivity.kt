package com.appsinthesky.bluetoothtutorial

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.ListView
import com.appsinthesky.bluetoothtutorial.MainMenu.Companion.m_bluetoothSocket
import com.appsinthesky.bluetoothtutorial.MainMenu.Companion.m_isConnected
import kotlinx.android.synthetic.main.control_layout.*
import java.io.IOException


class ControlActivity : AppCompatActivity() {
    companion object {
        lateinit var m_address: String
        var listItems: ArrayList<String> = ArrayList<String>()
        lateinit var adapter: ArrayAdapter<String>
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.control_layout)

        try {
            m_address = intent.getStringExtra("test")
        } catch (e: Exception) {
        }

        //clear when the app is closed
        listItems.clear();

        val lv = findViewById<ListView>(R.id.lista_adapter)
        adapter = ArrayAdapter(this, R.layout.mytextview_layout, listItems)
        lv.adapter = adapter

        sendCommand("maze#");
        Log.d("test branch: ", "123")
        control_led_on.setOnClickListener { sendCommand("a") }
        control_led_off.setOnClickListener { sendCommand("b") }
        control_led_disconnect.setOnClickListener { disconnect() }

    }

    private fun sendCommand(input: String) {
        if (m_bluetoothSocket != null) {
            try {
                m_bluetoothSocket!!.outputStream.write(input.toByteArray())
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }

        val buffer = ByteArray(1024)
        var bytes: Int

        try {
            bytes = m_bluetoothSocket!!.inputStream.read(buffer)
            val readMessage = String(buffer, 0, bytes)
            Log.d("123: ", readMessage)
            listItems.add(readMessage)
            adapter.notifyDataSetChanged()

        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun disconnect() {
        if (m_bluetoothSocket != null) {
            try {
                m_bluetoothSocket!!.outputStream.write("exit".toByteArray())
                m_bluetoothSocket!!.close()
                m_bluetoothSocket = null
                m_isConnected = false
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        finish()
    }
}
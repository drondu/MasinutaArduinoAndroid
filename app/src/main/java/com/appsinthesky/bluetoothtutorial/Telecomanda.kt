package com.appsinthesky.bluetoothtutorial

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.ArrayAdapter
import android.widget.ListView
import com.appsinthesky.bluetoothtutorial.MainMenu.Companion.m_bluetoothSocket
import com.appsinthesky.bluetoothtutorial.MainMenu.Companion.m_isConnected
import kotlinx.android.synthetic.main.activity_telecomanda.*
import java.io.IOException
import java.util.*


class Telecomanda : AppCompatActivity() {
    companion object {
        lateinit var m_address: String
        var listItems: ArrayList<String> = ArrayList<String>()
        lateinit var adapter: ArrayAdapter<String>
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_telecomanda)

        try {
            m_address = intent.getStringExtra("test2")
        } catch (e: Exception) {
        }

        listItems.clear();

        val lv = findViewById<ListView>(R.id.lista_tele)
        adapter = ArrayAdapter(this, R.layout.mytextview_layout, listItems)
        lv.adapter = adapter

        sendCommand("telecomanda#");

        button_sus.setOnClickListener { sendCommand("w") }
        button_jos.setOnClickListener { sendCommand("s") }
        button_dreapta.setOnClickListener { sendCommand("d")}
        button_stanga.setOnClickListener { sendCommand("a")}
        button_stop.setOnClickListener { sendCommand("stop")}
        button_rotate.setOnClickListener { sendCommand("r")}
        disconnect_telecomanda.setOnClickListener { disconnect() }
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
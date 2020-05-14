package com.appsinthesky.bluetoothtutorial

import android.app.ProgressDialog
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.Context
import android.content.Intent
import android.graphics.ColorSpace.connect
import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.system.Os.connect
import android.util.Log
import android.widget.Button
import android.widget.Toast
import java.io.IOException
import java.lang.Exception
import java.util.*

class MainMenu : AppCompatActivity() {

    companion object {
        var m_myUUID: UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")
        var m_bluetoothSocket: BluetoothSocket? = null
        lateinit var m_progress: ProgressDialog
        lateinit var m_bluetoothAdapter: BluetoothAdapter
        var m_isConnected: Boolean = false
        var m_address_copy: String = ""
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_menu)

        try {
            m_address_copy = intent.getStringExtra("test")

            if (m_address_copy.isEmpty()) {
                Log.d("ceva ceva", m_address_copy)
            }
            ConnectToDevice(this).execute()
        } catch (e: Exception) {
        }

        val buttonConnect = findViewById<Button>(R.id.buttonConectare)
        buttonConnect.setOnClickListener {
            val intent = Intent(this, SelectDeviceActivity::class.java)
            startActivity(intent)
        }

        val buttonMaze = findViewById<Button>(R.id.button_MAZE)
        buttonMaze.setOnClickListener {
            if (m_address_copy.isEmpty()) {
                Toast.makeText(this, "Selecteaza un device!", Toast.LENGTH_LONG).show()
            } else {
                val intent = Intent(this, ControlActivity::class.java)
                intent.putExtra("test", m_address_copy)
                startActivity(intent)
            }
        }


        val buttonDeplasare = findViewById<Button>(R.id.button_TELE)
        buttonDeplasare.setOnClickListener {
            if (m_address_copy.isEmpty()) {
                Toast.makeText(this, "Selecteaza un device!", Toast.LENGTH_LONG).show()
            } else {
                val intent2 = Intent(this, Telecomanda::class.java)
                intent2.putExtra("test2", m_address_copy)
                startActivity(intent2)
            }
        }



    }
    private class ConnectToDevice(c: Context) : AsyncTask<Void, Void, String>() {
        private var connectSuccess: Boolean = true
        private val context: Context

        init {
            this.context = c
        }

        override fun onPreExecute() {
            super.onPreExecute()
            m_progress = ProgressDialog.show(context, "Connecting...", "please wait")
        }

        override fun doInBackground(vararg p0: Void?): String? {
            try {
                if (m_bluetoothSocket == null || !m_isConnected) {
                    m_bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
                    val device: BluetoothDevice = m_bluetoothAdapter.getRemoteDevice(MainMenu.m_address_copy)
                    m_bluetoothSocket = device.createInsecureRfcommSocketToServiceRecord(m_myUUID)
                    BluetoothAdapter.getDefaultAdapter().cancelDiscovery()
                    m_bluetoothSocket!!.connect()
                }
            } catch (e: IOException) {
                connectSuccess = false
                e.printStackTrace()
            }
            return null
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            if (!connectSuccess) {
                Log.i("data", "couldn't connect")
            } else {
                m_isConnected = true
            }
            m_progress.dismiss()
        }
    }
}

package com.example.restrequestandroid

import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONArray
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

class SendRequestActivity : AppCompatActivity() {
    var lat: Double? = 0.0
    var lon: Double? = 0.0
    val TAG = "SendRequestActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_send_request)
        val receiveData = findViewById<TextView>(R.id.tv_receiveData)
        val btn_send = findViewById<TextView>(R.id.btn_send)
        val bundle = intent.extras
        lat = bundle?.getDouble("Lat")
        lon = bundle?.getDouble("Lon")
        receiveData.text = "Latitide:" + lat + "\n Longitude:" + lon
        btn_send.setOnClickListener() {
            sendJson()
        }
    }

    fun sendJson() {
        var list = JSONArray()
        var json = JSONObject() as JSONObject

        json.put("longitude", lat)
        json.put("latitude", lon)
        json.put("altitude", "33.5")
        json.put("gpstime", "2020-05-24 11:24:00")

        list.put(json)
        Log.d("JSONArray", list.toString())
        val jsonString = list.toString()
        HttpPost().execute(jsonString)


    }

    fun deleteJson() {

        var list = JSONArray()

        var json = JSONObject() as JSONObject

        json.put("id", 18)
        list.put(json)
        Log.d("DeleteJSONArray", list.toString())
        val jsonString = list.toString()
        HttpDelete().execute(jsonString)
    }

    fun putJson() {

        var list = JSONArray()

        var json = JSONObject() as JSONObject

        json.put("id", 18)
        json.put("longitude", lat)
        json.put("latitude", lon)
        json.put("altitude", "44.5")
        json.put("gpstime", "2044-05-24 11:24:00")
        list.put(json)
        Log.d("PutJSONArray", list.toString())
        val jsonString = list.toString()
        HttpPut().execute(jsonString)


    }

    class HttpPost : AsyncTask<String, String, String>() {
        override fun doInBackground(vararg p0: String?): String {
            val data = p0[0] as String
            Log.i("method-data", data)
            val urlstr = "http://10.0.2.2:8080/logger"
            val url = URL(urlstr)
            val httpClient = url.openConnection() as HttpURLConnection
            httpClient.apply {
                readTimeout = 10000
                connectTimeout = 5000
                requestMethod = "POST"
                instanceFollowRedirects = true
                doOutput = true
                doInput = false
                useCaches = false
                setRequestProperty("Content-Type", "application/json; charset=UTF-8")
            }

            try {
                httpClient.connect()
                val os = httpClient.outputStream
                val bw = os.bufferedWriter(Charsets.UTF_8)
                bw.write(data)
                bw.flush()
                bw.close()
                val code = httpClient.responseCode

                Log.i("HttpPost", "レスポンスコード:" + code)


            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                httpClient.disconnect()
            }

            return ""
        }
    }

    class HttpDelete : AsyncTask<String, String, String>() {
        override fun doInBackground(vararg p0: String?): String {
            val data = p0[0] as String
            val urlstr = "http://10.0.2.2:8080/logger"
            val url = URL(urlstr)
            val httpClient = url.openConnection() as HttpURLConnection
            httpClient.apply {
                readTimeout = 10000
                connectTimeout = 5000
                requestMethod = "DELETE"
                instanceFollowRedirects = false
                doOutput = true
                doInput = true
                useCaches = false
                setRequestProperty("Content-Type", "application/x-www-form-urlencoded")
            }

            try {
                httpClient.connect()
                val os = httpClient.outputStream
                val bw = os.bufferedWriter(Charsets.UTF_8)
                bw.write(data)
                bw.flush()
                bw.close()
                val code = httpClient.responseCode

                Log.i("HttpPost", "レスポンスコード:" + code)


            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                httpClient.disconnect()
            }

            return ""
        }
    }

    class HttpPut : AsyncTask<String, String, String>() {
        override fun doInBackground(vararg p0: String?): String {
            val data = p0[0] as String
            val urlstr = "http://10.0.2.2:8080/logger"
            val url = URL(urlstr)
            val httpClient = url.openConnection() as HttpURLConnection
            httpClient.apply {
                readTimeout = 10000
                connectTimeout = 5000
                requestMethod = "PUT"
                instanceFollowRedirects = true
                doOutput = true
                doInput = true
                useCaches = false
                setRequestProperty("Content-Type", "application/json; charset=UTF-8")
            }

            try {
                httpClient.connect()
                val os = httpClient.outputStream
                val bw = os.bufferedWriter(Charsets.UTF_8)
                bw.write(data)
                bw.flush()
                bw.close()
                val code = httpClient.responseCode

                Log.i("HttpPut", "レスポンスコード:" + code)


            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                httpClient.disconnect()
            }

            return ""
        }
    }
}
package com.example.restrequestandroid

import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONArray
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

class SendRequestActivity : AppCompatActivity() {
    var lat: Double? = 0.0
    var lon: Double? = 0.0
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
        var code = HttpPost().execute(jsonString).get()
        if (code == 200) {
            Toast.makeText(applicationContext, "Success", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(applicationContext, "Update fail" + code, Toast.LENGTH_SHORT).show()
        }

    }

    fun deleteJson() {

        var list = JSONArray()

        var json = JSONObject() as JSONObject


        json.put("id", 19)
        list.put(json)
        Log.d("DeleteJSONArray", list.toString())
        val jsonString = list.toString()
        var code = HttpDelete().execute(jsonString).get()
        if (code == 200) {
            Toast.makeText(applicationContext, "Success", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(applicationContext, "Update fail" + code, Toast.LENGTH_SHORT).show()
        }
    }

    fun putJson() {

        var list = JSONArray()

        var json = JSONObject() as JSONObject

        json.put("id", 19)
        json.put("longitude", 11.1234)
        json.put("latitude", 22.5678)
        json.put("altitude", "11.5")
        json.put("gpstime", "2011-05-24 11:24:00")
        list.put(json)
        Log.d("PutJSONArray", list.toString())
        val jsonString = list.toString()
        var code = HttpPut().execute(jsonString).get()
        if (code == 200) {
            Toast.makeText(applicationContext, "Success", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(applicationContext, "Update fail" + code, Toast.LENGTH_SHORT).show()
        }

    }

    fun getJson() {

        var list = JSONArray()

        var json = JSONObject() as JSONObject

        json.put("id", 18)
        list.put(json)
        Log.d("PutJSONArray", list.toString())
        val jsonString = list.toString()
        var code = HttpGet().execute(jsonString).get()
        if (code != null) {
            Toast.makeText(applicationContext, "Success" + code, Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(applicationContext, "Update fail" + code, Toast.LENGTH_SHORT).show()
        }

    }

    class HttpPost : AsyncTask<String, String, Int>() {
        override fun doInBackground(vararg p0: String?): Int {
            val data = p0[0] as String
            var code = 0
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
                code = httpClient.responseCode

                Log.i("HttpPost", "レスポンスコード:" + code)


            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                httpClient.disconnect()
            }

            return code
        }
    }

    class HttpGet : AsyncTask<String, String, String>() {
        override fun doInBackground(vararg p0: String?): String {
            val data = p0[0] as String
            var code = 0
            var result = ""
            var line = ""
            var read = ""
            Log.i("method-data", data)
            val urlstr = "http://10.0.2.2:8080/logger"
            val url = URL(urlstr)
            val httpClient = url.openConnection() as HttpURLConnection
            httpClient.apply {
                readTimeout = 10000
                connectTimeout = 5000
                requestMethod = "GET"
                instanceFollowRedirects = false
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
                code = httpClient.responseCode
                if (code == 200) {
                    val it = httpClient.inputStream
                    read = it.bufferedReader(Charsets.UTF_8).use { it.readText() }

                }
                Log.i("HttpGet", "レスポンスコード:" + read)


            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                httpClient.disconnect()
            }

            return read
        }
    }

    class HttpDelete : AsyncTask<String, String, Int>() {
        override fun doInBackground(vararg p0: String?): Int {
            val data = p0[0] as String
            var code = 0
            val urlstr = "http://10.0.2.2:8080/logger"
            val url = URL(urlstr)
            val httpClient = url.openConnection() as HttpURLConnection
            httpClient.apply {
                readTimeout = 10000
                connectTimeout = 5000
                requestMethod = "DELETE"
                instanceFollowRedirects = false
                useCaches = false

                setRequestProperty("Content-Type", "application/json; charset=UTF-8")
//                setRequestProperty("Content-Type", "application/x-www-form-urlencoded")
//                setRequestProperty("X-HTTP-Method-Override", "DELETE")
            }

            try {
                httpClient.connect()
                val os = httpClient.outputStream
                val bw = os.bufferedWriter(Charsets.UTF_8)
                bw.write(data)
                bw.flush()
                bw.close()
                code = httpClient.responseCode

                Log.i("HttpDelete", "レスポンスコード:" + code)


            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                httpClient.disconnect()
            }

            return code
        }
    }

    class HttpPut : AsyncTask<String, String, Int>() {
        override fun doInBackground(vararg p0: String?): Int {
            val data = p0[0] as String
            var code = 0
            val urlstr = "http://10.0.2.2:8080/logger"
            val url = URL(urlstr)
            val httpClient = url.openConnection() as HttpURLConnection
            httpClient.apply {
                readTimeout = 10000
                connectTimeout = 5000
                requestMethod = "POST"
                instanceFollowRedirects = false
                doOutput = true
                doInput = true
                useCaches = false
                setRequestProperty("Content-Type", "application/json; charset=UTF-8")
//                setRequestProperty("Content-Type", "application/x-www-form-urlencoded")
            }

            try {
                httpClient.connect()
                val os = httpClient.outputStream
                os.write(data.toByteArray())
                os.close()
                code = httpClient.responseCode

                Log.i("HttpPut", "レスポンスコード:" + code)


            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                httpClient.disconnect()
            }

            return code
        }
    }
}
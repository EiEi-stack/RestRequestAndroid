package com.example.restrequestandroid

import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONArray
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

class SendRequestActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_send_request)
    }
    fun sendJson(items: List<Map<String, String>>) {

        var list = JSONArray()

        for (item in items) {
            var json = JSONObject() as JSONObject

            json.put("longitude", "111.812")
            json.put("latitude", "22.7104")
            json.put("altitude", "33.5")
            json.put("gpstime", "2020-05-24 11:24:00")

            list.put(json)
        }

        Log.d("JSONArray", list.toString())

        val jsonString = list.toString()

        HttpPost().execute(jsonString)

    }
}
class HttpPost : AsyncTask<String, String, String>() {
    override fun doInBackground(vararg p0: String?): String {
        val data = p0[0] as String
        val urlstr = "http://192.168.1.1:8080/logger"
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
package com.developndesign.telehealthpatient.network

import android.os.AsyncTask
import android.util.Log
import com.google.gson.JsonObject
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody

/*
 * @params strings[0] = Url to hit
 * @params strings[1] = User auth token
 */
class SendNotificationToDoctor: AsyncTask<String, Void, String>() {
    override fun doInBackground(vararg strings: String?): String {
        try {
            val client = OkHttpClient()
            val request: Request = Request.Builder()
                    .url(strings[0])
                    .post(
                            RequestBody.create(MediaType.parse("application/json;charset=utf-8"), JsonObject().toString())
                    )
                    .addHeader("Content-Type", "application/json;charset=utf-8")
                    .addHeader("token", strings[1])
                    .build()
            var response  = client.newCall(request).execute()
            var jsonData = response.body()?.string()
            Log.i("fcmNotification", jsonData);
            return jsonData!!
        } catch (e: Exception) {
            Log.e("TAG", "doInBackground: $e")
            e.printStackTrace()
        }
        return "err"
    }
}
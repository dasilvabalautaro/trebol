package com.hiddenodds.trebol.tools

import android.content.Context
import android.net.ConnectivityManager
import kotlinx.coroutines.experimental.launch
import java.net.ConnectException
import java.net.HttpURLConnection
import java.net.URL


class ConnectionNetwork(val context: Context) {
    private var isConnect = false

    init {
        //verifyConnect()
    }

    fun isOnline(): Boolean {
        val connectivityManager = context.getSystemService(
                Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val netInfo = connectivityManager.activeNetworkInfo
        return netInfo != null && netInfo.isConnected
    }

    private fun checkUrl(): Boolean{
        return try {
            val url = URL(Constants.URL_SERVER)
            val connection: HttpURLConnection = url.openConnection()
                    as HttpURLConnection
            val statusCode = connection.responseCode
            statusCode == 200
        }catch (ce: ConnectException){
            println(ce.message)
            false
        }

    }

    private fun verifyConnect(){
        launch{
            isConnect = if (isOnline()){
                checkUrl()
            }else{
                false
            }
        }
    }

    fun checkConnect(): Boolean = this.isConnect
}
package com.hiddenodds.trebol.tools

import android.content.Context
import android.net.ConnectivityManager
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.net.ConnectException
import java.net.HttpURLConnection
import java.net.URL


class ConnectionNetwork(val context: Context) {
    private var isConnect = false

    fun isOnline(): Boolean {
        val connectivityManager = context.getSystemService(
                Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val netInfo = connectivityManager.activeNetwork
        return netInfo != null
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
        GlobalScope.launch{
            isConnect = if (isOnline()){
                checkUrl()
            }else{
                false
            }
        }
    }

    fun checkConnect(): Boolean = this.isConnect
}
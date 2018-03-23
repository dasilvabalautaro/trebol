package com.hiddenodds.trebolv2.model.persistent.network

import android.os.StrictMode
import com.hiddenodds.trebolv2.model.exception.DatabaseOperationException
import com.hiddenodds.trebolv2.tools.Constants
import io.reactivex.Observable
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.sql.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ServiceRemote @Inject constructor() {
    var ip: String = Constants.SQLSERVER
    var database: String = Constants.DATABASE
    var user: String = Constants.USER
    var password: String = Constants.PASSWORD

    private var connect: java.sql.Connection? = null
    private var urlConnect: String = ""
    private var error: DatabaseOperationException? = null

    @Throws(Exception::class, SQLException::class)
    private fun connection(){
        val policy = StrictMode.ThreadPolicy.Builder()
                .permitAll()
                .build()
        StrictMode.setThreadPolicy(policy)


        Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance()
        this.urlConnect = "jdbc:jtds:sqlserver://" + ip +
                ";useLOBs=false;databaseName=" + database +
                ";user=" + user + ";password=" + password + ";"
        DriverManager.setLoginTimeout(15)
        this.connect = DriverManager.getConnection(this.urlConnect)
    }

    fun getDataObservable(sql: String): Observable<JSONArray>{
        return Observable.create { subscriber ->
            val jsonArray = this.getData(sql)
            if (jsonArray != null){
                subscriber.onNext(jsonArray)
                subscriber.onComplete()
            }else{
                subscriber.onError(Throwable(this.error))
            }
        }
    }

    private fun getData(sql: String): JSONArray?{
        var statement: Statement? = null
        var resultSet: ResultSet? = null
        var jsonArray: JSONArray? = null

        try {
            connection()
            if (connect != null){
                statement = connect!!.createStatement()
                resultSet = statement.executeQuery(sql)
                val resultSetMetaData: ResultSetMetaData = resultSet.metaData
                jsonArray = JSONArray()

                while (resultSet.next()){
                    val columns: Int = resultSetMetaData.columnCount
                    val jsonObject = JSONObject()
                    for (i in (1..columns)){
                        jsonObject.put(resultSetMetaData.getColumnName(i),
                                if (resultSet.getObject(i) != null)
                                    resultSet.getObject(i) else JSONObject.NULL)

                    }

                    jsonArray.put(jsonObject)
                }

            }

        }catch (sx: SQLException) {

            println(sx.message)
            if (!sx.message.isNullOrEmpty()) {
                this.error = DatabaseOperationException(sx.message!!)

            }

        }catch (je: JSONException){
            println(je.message)
            if (!je.message.isNullOrEmpty()) {
                this.error = DatabaseOperationException(je.message!!)

            }
        }catch (ex: Exception){
            println(ex.message)
            if (!ex.message.isNullOrEmpty()) {
                this.error = DatabaseOperationException(ex.message!!)

            }
        }finally {
            if (statement != null){
                statement.close()
            }
            if (resultSet != null){
                resultSet.close()
            }
            if (connect != null){
                connect!!.close()
            }

        }
        return jsonArray
    }


    fun closeConnection(){
        try {
            if (connect != null){
                connect!!.close()
                connect = null
            }
        }catch (ex: Exception){
            println(ex.message)
        }
    }


}
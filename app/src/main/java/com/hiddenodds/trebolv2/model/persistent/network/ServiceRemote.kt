package com.hiddenodds.trebolv2.model.persistent.network

import android.os.StrictMode
import com.hiddenodds.trebolv2.model.exception.DatabaseOperationException
import com.hiddenodds.trebolv2.tools.Constants
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import org.json.JSONArray
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
    var observableException:
            Subject<DatabaseOperationException> = PublishSubject.create()
    var messageSuccess: String = ""
    var observableMessageSuccess: Subject<String> = PublishSubject.create()

    init {
        observableException
                .subscribe { error }
        observableMessageSuccess
                .subscribe { messageSuccess }
    }

    fun connection(){
        val policy = StrictMode.ThreadPolicy.Builder()
                .permitAll()
                .build()
        StrictMode.setThreadPolicy(policy)

        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance()
            this.urlConnect = "jdbc:jtds:sqlserver://" + ip +
                    ";useLOBs=false;databaseName=" + database +
                    ";user=" + user + ";password=" + password + ";"

            this.connect = DriverManager.getConnection(this.urlConnect)
            this.messageSuccess = Constants.SUCCESS
            this.observableMessageSuccess.onNext(this.messageSuccess)

        }catch (sx: SQLException) {
            println(sx.message)
            if (!sx.message.isNullOrEmpty()) {
                this.error = DatabaseOperationException(sx.message!!)
                this.observableException.onNext(this.error!!)
            }

        }catch (ex: Exception){
            println(ex.message)
            if (!ex.message.isNullOrEmpty()){
                this.error = DatabaseOperationException(ex.message!!)
                this.observableException.onNext(this.error!!)
            }
        }
    }

    fun getDataObservable(sql: String): Observable<JSONArray>{
        return Observable.create { subscriber ->
            val jsonArray = this.getData(sql)
            if (jsonArray.length() != 0){
                subscriber.onNext(jsonArray)
                subscriber.onComplete()
            }else{
                subscriber.onError(Throwable())
            }
        }
    }

    private fun getData(sql: String): JSONArray{
        var statement: Statement? = null
        val jsonArray = JSONArray()

        try {
            statement = connect!!.createStatement()
            val resultSet: ResultSet = statement.executeQuery(sql)
            val resultSetMetaData: ResultSetMetaData = resultSet.metaData
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
        }catch (sx: SQLException) {
            println(sx.message)
            if (!sx.message.isNullOrEmpty()) {
                this.error = DatabaseOperationException(sx.message!!)
                this.observableException.onNext(this.error!!)
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
package com.hiddenodds.trebolv2.model.persistent.network

import android.os.StrictMode
import com.hiddenodds.trebolv2.model.exception.DatabaseOperationException
import com.hiddenodds.trebolv2.presentation.model.AssignedMaterialModel
import com.hiddenodds.trebolv2.presentation.model.NotificationModel
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

    fun setDataObservable(sql: String,
                          listValues: ArrayList<String>):
            Observable<Boolean>{
        return Observable.create { subscriber ->
            val result = this.setData(sql, listValues)
            if (result){
                subscriber.onNext(result)
                subscriber.onComplete()
            }else{
                subscriber.onError(Throwable(this.error))
            }
        }

    }

    private fun listParamNotification(notify: NotificationModel):
            ArrayList<String>{
        val list: ArrayList<String> = ArrayList()
        var value = notify.dateEnd
        list.add(value)
        list.add(value)
        value = notify.reportTechnical
        list.add(value)
        value = notify.observations
        list.add(value)
        value = notify.workHours
        list.add(value)
        value = notify.satd
        list.add(value)
        value = notify.diet
        list.add(value)
        value = notify.code
        list.add(value)

        return list
    }

    private fun listParamMachine(notify: NotificationModel):
            ArrayList<String>{
        val list: ArrayList<String> = ArrayList()
        var value = notify.vSoft1 + " " +
                notify.vSoft2 + " " +
                notify.vSoft3
        list.add(value)
        value = notify.hours
        list.add(value)
        value = notify.totalTeam
        list.add(value)
        value = notify.lastAmount
        list.add(value)
        value = notify.machine
        list.add(value)
        return list
    }

    private fun listParamsPieces(notify: NotificationModel,
                              assigned: AssignedMaterialModel):
            ArrayList<String>{
        val list: ArrayList<String> = ArrayList()
        var value = assigned.material!!.code
        list.add(value)
        value = assigned.quantity.toString()
        list.add(value)
        value = notify.machine
        list.add(value)
        value = notify.reportTechnical
        list.add(value)
        value = notify.code
        list.add(value)
        value = notify.idTech
        list.add(value)
        return list
    }

    fun setDataNotificationObservable(notification: NotificationModel):
            Observable<Boolean>{
        return Observable.create { subscriber ->
            val result = this.setDataNotification(notification)
            if (result){
                subscriber.onNext(result)
                subscriber.onComplete()
            }else{
                subscriber.onError(Throwable(this.error))
            }
        }

    }

    private fun setDataNotification(notification: NotificationModel): Boolean{
        var result = false
        var preparedStatement: PreparedStatement? = null
        val paramsNotification = listParamNotification(notification)
        val sqlNotification = StatementSQL.updateNotification()
        val paramsMachine = listParamMachine(notification)
        val sqlMachine = StatementSQL.updateMachine()
        val sqlMaxRow = StatementSQL.getMaxRowPieces()
        val sqlAddPieces = StatementSQL.addPieces()
        val sqlNameStore = StatementSQL.getNameStore()
        val sqlQuantityStore = StatementSQL.getQuantityInStore()
        val sqlUpdateQuantity = StatementSQL.updateQuantityInStore()

        try {
            connection()
            if (connect != null){
                //Notification
                preparedStatement = connect!!.prepareStatement(sqlNotification)
                for (i in paramsNotification.indices){
                    preparedStatement.setString(i + 1,
                            paramsNotification[i])
                }
                preparedStatement.execute()

                println("Execute Update Notification")
                //Machine
                preparedStatement = connect!!.prepareStatement(sqlMachine)
                for (i in paramsMachine.indices){
                    preparedStatement.setString(i + 1,
                            paramsMachine[i])
                }
                preparedStatement.execute()
                println("Execute Update Machine")
                // Add Pieces
                for (material:AssignedMaterialModel in notification.materialUse){
                    preparedStatement = connect!!.prepareStatement(sqlMaxRow)
                    val res: ResultSet = preparedStatement.executeQuery()
                    if (res.next()){
                        var max = res.getLong("BIG")
                        max += 1
                        println("Get Max" + max.toString())
                        preparedStatement = connect!!.prepareStatement(sqlAddPieces)
                        val paramsPieces = listParamsPieces(notification, material)
                        for (i in paramsPieces.indices){
                            preparedStatement.setString(i + 1,
                                    paramsPieces[i])
                        }
                        preparedStatement.setLong(7, max)
                        preparedStatement.execute()
                        println("Execute Add Pieces")
                        preparedStatement = connect!!.prepareStatement(sqlNameStore)
                        preparedStatement.setString(1, notification.idTech)
                        val resStore = preparedStatement.executeQuery()
                        if (resStore.next()){
                            val nameStore = resStore.getString("ALMACEN")
                            println("Name store: $nameStore")

                            if (nameStore.isNotEmpty()){
                                preparedStatement = connect!!.prepareStatement(sqlQuantityStore)
                                preparedStatement.setString(1,
                                        material.material!!.code)
                                preparedStatement.setString(2,
                                        nameStore)
                                val resExistence = preparedStatement.executeQuery()
                                if (resExistence.next()){
                                    val existence = resExistence
                                            .getLong("EXISTENCIA")
                                    val quantityUpdate = existence - material.quantity
                                    println("Quantity existence update: " + quantityUpdate.toString())
                                    preparedStatement = connect!!
                                            .prepareStatement(sqlUpdateQuantity)
                                    preparedStatement.setLong(1, quantityUpdate)
                                    preparedStatement.setString(2,
                                            material.material!!.code)
                                    preparedStatement.setString(3,
                                            nameStore)
                                    preparedStatement.execute()
                                    println("Execute Update Quantity")

                                }

                            }
                        }

                    }
                }
                result = true
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
            if (preparedStatement != null){
                preparedStatement.close()
            }

            if (connect != null){
                connect!!.close()
            }

        }

        return result
    }

    private fun setData(sql: String, listValues: ArrayList<String>): Boolean{
        var result = false
        var preparedStatement: PreparedStatement? = null
        try {
            connection()
            if (connect != null){
                preparedStatement = connect!!.prepareStatement(sql)
                for (i in listValues.indices){
                    preparedStatement.setString(i + 1, listValues[i])
                }
                val res = preparedStatement.execute()
                if (res){
                    result = true
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
            if (preparedStatement != null){
                preparedStatement.close()
            }

            if (connect != null){
                connect!!.close()
            }

        }

        return result
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
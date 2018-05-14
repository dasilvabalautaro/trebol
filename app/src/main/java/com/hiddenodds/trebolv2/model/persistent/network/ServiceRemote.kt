package com.hiddenodds.trebolv2.model.persistent.network

import android.os.StrictMode
import com.hiddenodds.trebolv2.model.exception.DatabaseOperationException
import com.hiddenodds.trebolv2.presentation.model.AssignedMaterialModel
import com.hiddenodds.trebolv2.presentation.model.NotificationModel
import com.hiddenodds.trebolv2.tools.ChangeFormat
import com.hiddenodds.trebolv2.tools.Variables
import io.reactivex.Observable
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.sql.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ServiceRemote @Inject constructor() {
    var ip: String? = null
    var database: String? = null
    var user: String? = null
    var password: String? = null
    var ipAux: String? = null
    var databaseAux: String? = null
    var userAux: String? = null
    var passwordAux: String? = null

    private var connect: java.sql.Connection? = null
    private var urlConnect: String = ""
    private var error: DatabaseOperationException? = null

    init {
        setVariablesConfiguration()
        setVariablesConfigurationAuxiliar()
    }

    private fun setVariablesConfiguration(){
        this.ip = Variables.sqlServer
        this.database = Variables.database
        this.user = Variables.user
        this.password = Variables.password
    }

    private fun setVariablesConfigurationAuxiliar(){
        this.ipAux = Variables.sqlServer
        this.databaseAux = Variables.database
        this.userAux = Variables.user
        this.passwordAux = Variables.password
    }

    private fun restartVariablesConfiguration(){
        this.ip = this.ipAux
        this.database = this.databaseAux
        this.user = this.userAux
        this.password = this.passwordAux
    }


    fun verifyConnectObservable():
            Observable<Boolean>{
        setVariablesConfiguration()
        return Observable.create { subscriber ->
            val result = this.verifyConnect()
            if (result){
                subscriber.onNext(result)
                subscriber.onComplete()
            }else{
                restartVariablesConfiguration()
                subscriber.onError(Throwable(this.error))
            }
        }

    }

    private fun verifyConnect(): Boolean{
        var result = false
        try {
            connection()
            if (connect != null){
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

            if (connect != null){
                connect!!.close()
            }

        }
        return result
    }

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
        value = if (notify.workHours.isEmpty()){
            "0"
        }else{
            notify.workHours
        }
        list.add(value)
        value = if (notify.satd.isEmpty()){
            "0"
        }else{
            notify.satd
        }
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
        value = if (notify.hours.isEmpty()){
            "0"
        }else{
            notify.hours
        }
        list.add(value)
        value = if (notify.totalTeam.isEmpty()){
            "0"
        }else{
            notify.totalTeam
        }
        list.add(value)
        value = if (notify.lastAmount.isEmpty()){
            "0"
        }else{
            notify.lastAmount
        }
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

    @Throws(Exception::class, SQLException::class)
    private fun updateNotification(notification: NotificationModel){
        var preparedStatement: PreparedStatement?
        println("Init Update Notification ${notification.code}")
        val sqlNotification = StatementSQL.updateNotification()
        val paramsNotification = listParamNotification(notification)
        preparedStatement = connect!!.prepareStatement(sqlNotification)
        for (i in paramsNotification.indices){
            when(i){
                0, 1 ->{
                    val dateIn = ChangeFormat
                            .convertStringToDate(paramsNotification[i])
                    preparedStatement.setDate(i + 1,
                            dateIn)
                }
                4 -> {
                    val num = paramsNotification[i].toDouble()
                    preparedStatement.setDouble(i + 1,
                            num)
                }
                5 ->{
                    val num = paramsNotification[i].toDouble()
                    preparedStatement.setDouble(i + 1,
                            num)
                }
                else -> {
                    preparedStatement.setString(i + 1,
                            paramsNotification[i])
                }
            }

        }
        preparedStatement.execute()

        println("Finish Update Notification")
        if (preparedStatement != null){
            preparedStatement.close()
        }
    }

    @Throws(Exception::class, SQLException::class)
    private fun updateMachine(notification: NotificationModel){
        var preparedStatement: PreparedStatement?
        println("Init Update Machine")
        val sqlMachine = StatementSQL.updateMachine()
        val paramsMachine = listParamMachine(notification)

        preparedStatement = connect!!.prepareStatement(sqlMachine)
        for (i in paramsMachine.indices){
            when(i){
                1, 2, 3 ->{
                    val num = paramsMachine[i].toLong()
                    preparedStatement.setLong(i + 1,
                            num)
                }
                else ->{
                    preparedStatement.setString(i + 1,
                            paramsMachine[i])
                }
            }

        }

        preparedStatement.execute()
        println("Finish Update Machine")
        if (preparedStatement != null){
            preparedStatement.close()
        }
    }

    @Throws(Exception::class, SQLException::class)
    private fun changePieces(notification: NotificationModel){
        var preparedStatement: PreparedStatement? = null
        val sqlMaxRow = StatementSQL.getMaxRowPieces()
        val sqlAddPieces = StatementSQL.addPieces()
        val sqlNameStore = StatementSQL.getNameStore()
        val sqlQuantityStore = StatementSQL.getQuantityInStore()
        val sqlUpdateQuantity = StatementSQL.updateQuantityInStore()

        println("Init change Pieces: " + notification.materialUse.size.toString() +
        " pieces out: " + notification.materialOut.size.toString())

        for (material:AssignedMaterialModel in notification.materialUse){
            println("Material asignado: " + material.material!!.code)
            preparedStatement = connect!!.prepareStatement(sqlMaxRow)
            val res: ResultSet = preparedStatement.executeQuery()
            if (res.next()){
                var max = res.getLong("BIG")
                max += 1
                println("Get Max: " + max.toString())
                preparedStatement = connect!!.prepareStatement(sqlAddPieces)
                val paramsPieces = listParamsPieces(notification,
                        material)
                for (i in paramsPieces.indices){
                    if (i == 1){
                        val unity = paramsPieces[i].toLong()
                        preparedStatement.setLong(i + 1,
                                unity)
                    }else{
                        preparedStatement.setString(i + 1,
                                paramsPieces[i])
                    }

                }
                println("Init Add Pieces")
                preparedStatement.setLong(7, max)
                preparedStatement.execute()
                println("Finish Add Pieces")
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
                            println("Finish existence update: " + quantityUpdate.toString())
                            preparedStatement = connect!!
                                    .prepareStatement(sqlUpdateQuantity)
                            preparedStatement.setLong(1, quantityUpdate)
                            preparedStatement.setString(2,
                                    material.material!!.code)
                            preparedStatement.setString(3,
                                    nameStore)
                            preparedStatement.execute()
                            println("Finish Update Quantity")

                        }

                    }
                }

            }
        }
        if (preparedStatement != null){
            preparedStatement.close()
        }
    }

    private fun setDataNotification(notification: NotificationModel): Boolean{
        var result = false

        try {
            connection()
            if (connect != null){

                updateNotification(notification)
                updateMachine(notification)
                changePieces(notification)
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
            /*if (preparedStatement != null){
                preparedStatement.close()
            }*/

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
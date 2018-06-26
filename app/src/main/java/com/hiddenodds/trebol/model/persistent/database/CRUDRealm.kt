package com.hiddenodds.trebol.model.persistent.database

import android.os.Parcel
import com.hiddenodds.trebol.model.data.*
import com.hiddenodds.trebol.model.interfaces.IDataContent
import com.hiddenodds.trebol.model.interfaces.IRepository
import com.hiddenodds.trebol.model.interfaces.ITaskCompleteListener
import com.hiddenodds.trebol.presentation.model.AssignedMaterialModel
import io.realm.Realm
import io.realm.RealmList
import io.realm.RealmObject
import io.realm.RealmResults
import java.util.*






abstract class CRUDRealm: IRepository {

    override fun <E : RealmObject> save(clazz: Class<E>,
                                        parcel: Parcel,
                                        listener: ITaskCompleteListener):E? {
        val realm: Realm = Realm.getDefaultInstance()

        var e: E? = null
        try {
            realm.executeTransaction {
                e = it.createObject(clazz, UUID.randomUUID().toString())
                if (e is IDataContent){
                    (e as IDataContent).setContent(parcel)
                }

                listener.onSaveSucceeded()

            }
        }catch (e: Throwable){
            listener.onSaveFailed(e.message!!)
        }
        return e

    }

    override fun <E : RealmObject> save(clazz: Class<E>,
                                        listener: ITaskCompleteListener):String? {
        val realm: Realm = Realm.getDefaultInstance()
        var id: String? = null
        var e: E?
        try {
            realm.executeTransaction {
                e = it.createObject(clazz, UUID.randomUUID().toString())
                if (e != null && e is Maintenance){
                    id = (e as Maintenance).id
                }

                listener.onSaveSucceeded()

            }
        }catch (e: Throwable){
            listener.onSaveFailed(e.message!!)
        }finally {
            realm.close()
        }
        return id

    }

    fun saveAssignedMat(assignedMaterialModel: AssignedMaterialModel,
                             listener: ITaskCompleteListener): AssignedMaterial?{
        val realm: Realm = Realm.getDefaultInstance()
        var assignedMaterial: AssignedMaterial? = null
        try {
            realm.executeTransaction {
                val material = realm.where(Material::class.java)
                        .equalTo("id", assignedMaterialModel.material!!.id)
                        .findFirst()

                assignedMaterial = it.createObject(AssignedMaterial::class.java,
                        UUID.randomUUID().toString())
                assignedMaterial!!.quantity = assignedMaterialModel.quantity
                assignedMaterial!!.material = material

                listener.onSaveSucceeded()
            }


        }catch (e: Throwable){
            listener.onSaveFailed(e.message!!)
        }finally {

        }
        return assignedMaterial
    }

    fun saveAssignedMaterial(assignedMaterialModel: AssignedMaterialModel,
                             listener: ITaskCompleteListener): String?{
        val realm: Realm = Realm.getDefaultInstance()

        var id: String? = null
        var assignedMaterial: AssignedMaterial?
        try {
            realm.executeTransaction {
                val material = realm.where(Material::class.java)
                        .equalTo("id", assignedMaterialModel.material!!.id)
                        .findFirst()

                assignedMaterial = it.createObject(AssignedMaterial::class.java,
                        UUID.randomUUID().toString())
                assignedMaterial!!.quantity = assignedMaterialModel.quantity
                assignedMaterial!!.material = material
                id = assignedMaterial!!.id
                realm.insertOrUpdate(assignedMaterial!!)
                println("Id Assigned: " + assignedMaterial!!.id)
                listener.onSaveSucceeded()
            }


        }catch (e: Throwable){
            listener.onSaveFailed(e.message!!)
        }finally {
            realm.close()
        }
        return id
    }

    override fun <E : RealmObject> getAllData(clazz: Class<E>): RealmResults<E>? {
        val realm: Realm = Realm.getDefaultInstance()
        return realm.where(clazz).findAll()
    }

    override fun <E : RealmObject> getDataByField(clazz: Class<E>,
                                                  fieldName: String,
                                                  value: String): RealmResults<E>? {
        val realm: Realm = Realm.getDefaultInstance()

        return realm.where(clazz).equalTo(fieldName, value).findAll()
    }

    override fun <E : RealmObject> deleteByField(clazz: Class<E>,
                                                 fieldName: String,
                                                 value: String,
                                                 listener: ITaskCompleteListener): Boolean {
        val realm: Realm = Realm.getDefaultInstance()
        return try {
            realm.executeTransaction {
                it.where(clazz)
                        .equalTo(fieldName, value)
                        .findAll()?.deleteAllFromRealm()
                listener.onSaveSucceeded()
            }
            true
        }catch (e: Throwable){
            listener.onSaveFailed(e.message!!)
            false
        }finally {
            realm.close()
        }
    }
    override fun <E : RealmObject> deleteAll(clazz: Class<E>,
                                             listener: ITaskCompleteListener): Boolean {

        val realm: Realm = Realm.getDefaultInstance()
        return try {
            realm.executeTransaction {
                val list: RealmResults<E> = it.where(clazz).findAll()
                if (list.isValid){
                    list.deleteAllFromRealm()
                }
    /*
                    it.where(clazz)
                            .findAll()?.deleteAllFromRealm()
    */
                listener.onSaveSucceeded()
            }

            true
        }catch (e: Throwable){
            listener.onSaveFailed(e.message!!)
            false
        }finally {
            realm.close()
        }

    }

    fun nameFileSignature(name: String,
                      listener: ITaskCompleteListener): String{
        val realm: Realm = Realm.getDefaultInstance()
        var nameFile = ""
        var signature: Signature? = null
        try {
            realm.executeTransaction {
                val e = realm.where(Signature::class.java)
                        .equalTo("name", name).findFirst()
                if (e != null){
                    nameFile = e.id
                }else{
                    signature = it.createObject(Signature::class.java,
                            UUID.randomUUID().toString())
                    signature!!.name = name
                    nameFile = signature!!.id
                }
                listener.onSaveSucceeded()
            }

        }catch (e: Throwable){
            listener.onSaveFailed(e.message!!)
        }finally {
            realm.close()
        }

        return nameFile
    }

    fun updateToDownload(code: String, fieldName: String,
                         value: String,
                         listener: ITaskCompleteListener): Boolean{

        val realm: Realm = Realm.getDefaultInstance()
        try {
            realm.executeTransaction {
                val e = realm.where(Download::class.java)
                        .equalTo("code", code).findFirst()

                if (e != null && fieldName == "customer"){
                    e.customer = value

                }
                if (e != null && fieldName == "notification"){
                    e.notification = value
                }
                if (e != null && e.notification.isNotEmpty()
                        && e.customer.isNotEmpty()){
                    e.state = 1
                }

                listener.onSaveSucceeded()

            }
            return true

        }catch (e: Throwable){
            listener.onSaveFailed(e.message!!)
            return false
        }finally {
            realm.close()
        }

    }

    fun updateAssignedMaterial(id: String, quantity: Int,
                               listener: ITaskCompleteListener): Boolean{
        val realm: Realm = Realm.getDefaultInstance()
        try {
            realm.executeTransaction {
                val assigned = realm.where(AssignedMaterial::class.java)
                        .equalTo("id", id).findFirst()
                if (assigned != null){
                    assigned.quantity = quantity
                    realm.insertOrUpdate(assigned)
                }
                listener.onSaveSucceeded()
            }
            realm.isAutoRefresh = true
        }catch (e: Throwable){
            listener.onSaveFailed(e.message!!)
            return false
        }finally {
            realm.close()
        }
        return true
    }

    fun updateFieldNotification(id: String, nameFieldUpdate: String,
                                newValue: String,
                                listener: ITaskCompleteListener): Boolean{
        val realm: Realm = Realm.getDefaultInstance()
        try {

            realm.executeTransaction {
                val notification = realm.where(Notification::class.java)
                        .equalTo("id", id).findFirst()

                if (notification != null){
                    if (nameFieldUpdate == "diet"){
                        notification.diet = newValue
                    }
                    if (nameFieldUpdate == "observations"){
                        notification.observations = newValue
                    }
                    if (nameFieldUpdate == "reportTechnical"){
                        notification.reportTechnical = newValue
                    }
                    if (nameFieldUpdate == "lastAmount"){
                        notification.lastAmount = newValue
                    }
                    if (nameFieldUpdate == "totalTeam"){
                        notification.totalTeam = newValue
                    }
                    if (nameFieldUpdate == "hours"){
                        notification.hours = newValue
                    }
                    if (nameFieldUpdate == "inside"){
                        notification.inside = newValue
                    }
                    if (nameFieldUpdate == "vSoft1"){
                        notification.vSoft1 = newValue
                    }
                    if (nameFieldUpdate == "vSoft2"){
                        notification.vSoft2 = newValue
                    }
                    if (nameFieldUpdate == "vSoft3"){
                        notification.vSoft3 = newValue
                    }
                    if (nameFieldUpdate == "outside"){
                        notification.outside = newValue
                    }
                    if (nameFieldUpdate == "workHours"){
                        notification.workHours = newValue
                    }
                    if (nameFieldUpdate == "state"){
                        notification.state = newValue
                    }
                    if (nameFieldUpdate == "dateEnd"){
                        notification.dateEnd = newValue
                    }
                    realm.insertOrUpdate(notification)

                }
                listener.onSaveSucceeded()
            }
            //realm.refresh()
        }catch (e: Throwable){
            listener.onSaveFailed(e.message!!)
            return false
        }finally {
            realm.close()
        }
        return true
    }

    fun updateFieldMaintenance(id: String, nameFieldUpdate: String,
                                newValue: String,
                                listener: ITaskCompleteListener): Boolean{
        val realm: Realm = Realm.getDefaultInstance()
        try {

            realm.executeTransaction {
                val maintenance = realm.where(Maintenance::class.java)
                        .equalTo("id", id).findFirst()

                if (maintenance != null){
                    if (nameFieldUpdate == "codeNotify"){
                        maintenance.codeNotify = newValue
                    }
                    if (nameFieldUpdate == "verification1"){
                        maintenance.verification1 = newValue
                    }
                    if (nameFieldUpdate == "verification2"){
                        maintenance.verification2 = newValue
                    }
                    if (nameFieldUpdate == "verification3"){
                        maintenance.verification3 = newValue
                    }
                    if (nameFieldUpdate == "verification4"){
                        maintenance.verification4 = newValue
                    }
                    if (nameFieldUpdate == "verification5"){
                        maintenance.verification5 = newValue
                    }
                    if (nameFieldUpdate == "verification6"){
                        maintenance.verification6 = newValue
                    }
                    if (nameFieldUpdate == "verification7"){
                        maintenance.verification7 = newValue
                    }
                    if (nameFieldUpdate == "verification8"){
                        maintenance.verification8 = newValue
                    }
                    if (nameFieldUpdate == "verification9"){
                        maintenance.verification9 = newValue
                    }
                    if (nameFieldUpdate == "verification10"){
                        maintenance.verification10 = newValue
                    }
                    if (nameFieldUpdate == "verification11"){
                        maintenance.verification11 = newValue
                    }
                    if (nameFieldUpdate == "verification12"){
                        maintenance.verification12 = newValue
                    }
                    if (nameFieldUpdate == "verification13"){
                        maintenance.verification13 = newValue
                    }
                    if (nameFieldUpdate == "verification14"){
                        maintenance.verification14 = newValue
                    }
                    
                    if (nameFieldUpdate == "maintenance1"){
                        maintenance.maintenance1 = newValue
                    }
                    if (nameFieldUpdate == "maintenance2"){
                        maintenance.maintenance2 = newValue
                    }
                    if (nameFieldUpdate == "maintenance3"){
                        maintenance.maintenance3 = newValue
                    }
                    if (nameFieldUpdate == "maintenance4"){
                        maintenance.maintenance4 = newValue
                    }
                    if (nameFieldUpdate == "maintenance5"){
                        maintenance.maintenance5 = newValue
                    }
                    if (nameFieldUpdate == "maintenance6"){
                        maintenance.maintenance6 = newValue
                    }
                    if (nameFieldUpdate == "maintenance7"){
                        maintenance.maintenance7 = newValue
                    }
                    if (nameFieldUpdate == "maintenance8"){
                        maintenance.maintenance8 = newValue
                    }
                    if (nameFieldUpdate == "maintenance9"){
                        maintenance.maintenance9 = newValue
                    }
                    if (nameFieldUpdate == "maintenance10"){
                        maintenance.maintenance10 = newValue
                    }
                    if (nameFieldUpdate == "maintenance11"){
                        maintenance.maintenance11 = newValue
                    }
                    if (nameFieldUpdate == "maintenance12"){
                        maintenance.maintenance12 = newValue
                    }
                    if (nameFieldUpdate == "maintenance13"){
                        maintenance.maintenance13 = newValue
                    }
                    if (nameFieldUpdate == "maintenance14"){
                        maintenance.maintenance14 = newValue
                    }
                    if (nameFieldUpdate == "test1"){
                        maintenance.test1 = newValue
                    }
                    if (nameFieldUpdate == "test2"){
                        maintenance.test2 = newValue
                    }
                    if (nameFieldUpdate == "test3"){
                        maintenance.test3 = newValue
                    }
                    if (nameFieldUpdate == "test4"){
                        maintenance.test4 = newValue
                    }
                    if (nameFieldUpdate == "test5"){
                        maintenance.test5 = newValue
                    }
                    if (nameFieldUpdate == "test6"){
                        maintenance.test6 = newValue
                    }
                    if (nameFieldUpdate == "test7"){
                        maintenance.test7 = newValue
                    }
                    if (nameFieldUpdate == "test8"){
                        maintenance.test8 = newValue
                    }
                    if (nameFieldUpdate == "test9"){
                        maintenance.test9 = newValue
                    }
                    if (nameFieldUpdate == "test10"){
                        maintenance.test10 = newValue
                    }
                    if (nameFieldUpdate == "test11"){
                        maintenance.test11 = newValue
                    }
                    if (nameFieldUpdate == "test12"){
                        maintenance.test12 = newValue
                    }
                    if (nameFieldUpdate == "test13"){
                        maintenance.test13 = newValue
                    }
                    if (nameFieldUpdate == "test14"){
                        maintenance.test14 = newValue
                    }
                    if (nameFieldUpdate == "test15"){
                        maintenance.test15 = newValue
                    }
                    if (nameFieldUpdate == "test16"){
                        maintenance.test16 = newValue
                    }
                    if (nameFieldUpdate == "security"){
                        maintenance.security = newValue
                    }
                    if (nameFieldUpdate == "documentation"){
                        maintenance.documentation = newValue
                    }
                    if (nameFieldUpdate == "knowPrint"){
                        maintenance.knowPrint = newValue
                    }
                    if (nameFieldUpdate == "nextHours"){
                        maintenance.nextHours = newValue
                    }
                    if (nameFieldUpdate == "reportTechnical"){
                        maintenance.reportTechnical = newValue
                    }

                    realm.insertOrUpdate(maintenance)

                }
                listener.onSaveSucceeded()
            }

        }catch (e: Throwable){
            listener.onSaveFailed(e.message!!)
            return false
        }finally {
            realm.close()
        }
        return true
    }



    override fun <E : RealmObject> updateField(nameField: String,
                                               valueSearch: String,
                                               nameFieldUpdate: String,
                                               newValue: String,
                                               clazz: Class<E>,
                                               listener: ITaskCompleteListener): Boolean {

        val realm: Realm = Realm.getDefaultInstance()
        try {
            realm.executeTransaction {
                val e: E? = realm.where(clazz).equalTo(nameField, valueSearch).findFirst()

                if (e != null && e is Notification){
                    if (nameFieldUpdate == "type"){

                    }
                }

                listener.onSaveSucceeded()

            }
            return true

        }catch (e: Throwable){
            listener.onSaveFailed(e.message!!)
            return false
        }finally {
            realm.close()
        }

    }

    fun getTechnicalMaster(code: String, password: String): RealmResults<Technical>?{
        val realm: Realm = Realm.getDefaultInstance()
        return realm.where(Technical::class.java)
                .equalTo("code", code)
                .equalTo("password", password)
                .findAll()
    }

    fun saveListTRD(code:String, list: ArrayList<String>,
                    listener: ITaskCompleteListener): Boolean{
        val realm: Realm = Realm.getDefaultInstance()
        try {
            realm.executeTransaction {
                val technical = realm.where(Technical::class.java).equalTo(
                        "code", code).findFirst()
                if (technical != null){
                    technical.trd.clear()
                    for (i in list.indices){
                        technical.trd.add(list[i])
                    }
                }
                listener.onSaveSucceeded()
            }

        }catch (e: Throwable){
            println(e.message!!)
            listener.onSaveFailed(e.message!!)
            return false
        }finally {
            realm.close()
        }

        return true
    }

    fun deleteAssignedMaterialOfNotification(idNotification: String,
                                             idAssigned: String,
                                             flagUse: Boolean,
                                             listener: ITaskCompleteListener):
            Boolean{
        val realm: Realm = Realm.getDefaultInstance()
        try {
            realm.executeTransaction{
                val assigned = realm.where(AssignedMaterial::class.java)
                        .equalTo("id", idAssigned).findFirst()
                if (assigned != null){
                    val notification = realm.where(Notification::class.java)
                            .equalTo("id", idNotification).findFirst()
                    when(flagUse){
                        true -> {
                            notification!!.materialUse.remove(assigned)
                        }
                        false -> {
                            notification!!.materialOut.remove(assigned)
                        }
                    }
                    realm.where(AssignedMaterial::class.java)
                            .equalTo("id", idAssigned)
                            .findAll()?.deleteAllFromRealm()
                }
                listener.onSaveSucceeded()
            }

        }catch (e: Throwable){
            listener.onSaveFailed(e.message!!)
            return false
        }finally {
            realm.close()
        }
        return true
    }

    fun deleteNotificationsOfTechnical(code: String,
                                       listener: ITaskCompleteListener): Boolean{
        val realm: Realm = Realm.getDefaultInstance()

        try {
            realm.executeTransaction {
                val technical = realm.where(Technical::class.java).equalTo(
                        "code", code).findFirst()
                if (technical != null){
                    val listNotify: RealmList<Notification> = technical.notifications
                    technical.notifications.removeAll(listNotify)
                    realm.where(Notification::class.java)
                            .equalTo("idTech", code)
                            .findAll()?.deleteAllFromRealm()

                }
                listener.onSaveSucceeded()
            }
        }catch (e: Throwable){
            listener.onSaveFailed(e.message!!)
            return false
        }finally {
            realm.close()
        }
        return true
    }

    fun addAssignedMaterialToNotification(list:
                                          ArrayList<String>,
                                          id: String, flagUse: Boolean,
                                          listener: ITaskCompleteListener): Boolean{
        val realm: Realm = Realm.getDefaultInstance()

        try {

            realm.executeTransaction {
                val notification = realm.where(Notification::class.java)
                        .equalTo("id", id).findFirst()
                if (notification != null){
                    for (i in list.indices){
                        val assigned = realm.where(AssignedMaterial::class.java)
                                .equalTo("id", list[i]).findFirst()
                        when(flagUse){
                            true -> {
                                notification.materialUse.add(assigned)
                                realm.insertOrUpdate(notification)
                            }
                            false ->{
                                notification.materialOut.add(assigned)
                                realm.insertOrUpdate(notification)
                            }
                        }
                    }
                }
            }

        }catch (e: Throwable){
            listener.onSaveFailed(e.message!!)
            return false
        }finally {
            realm.close()
        }
        return true
    }

    fun addCustomerToNotification(idTech: String,
                                  listener: ITaskCompleteListener): Boolean{

        val realm: Realm = Realm.getDefaultInstance()
        try {
            realm.executeTransaction {
                val listNotify: RealmResults<Notification> = realm
                        .where(Notification::class.java).equalTo(
                                "idTech", idTech).findAll()

                if (listNotify.isNotEmpty()){
                    for (notify: Notification in listNotify){

                        val customer: Customer? = realm
                                .where(Customer::class.java).equalTo(
                                        "code", notify.code).findFirst()
                        if (customer != null){
                            notify.customer = customer
                        }
                    }
                }
                listener.onSaveSucceeded()
            }

        }catch (e: Throwable){
            listener.onSaveFailed(e.message!!)
            return false
        }finally {
            realm.close()
        }
        return true
    }

    fun addListNotificationToTechnical(code: String,
                                       listener: ITaskCompleteListener): Boolean{
        val realm: Realm = Realm.getDefaultInstance()
        try {
            realm.executeTransaction {
                val technical = realm.where(Technical::class.java).equalTo(
                        "code", code).findFirst()
                val listNotify: RealmResults<Notification> = realm
                        .where(Notification::class.java).equalTo(
                        "idTech", code).findAll()
                if (technical != null && !listNotify.isEmpty()){
                    for (notify: Notification in listNotify){
                        technical.notifications.add(notify)
                    }
                }

                listener.onSaveSucceeded()
            }

        }catch (e: Throwable){
            listener.onSaveFailed(e.message!!)
            return false
        }finally {
            realm.close()
        }

        return true
    }

}
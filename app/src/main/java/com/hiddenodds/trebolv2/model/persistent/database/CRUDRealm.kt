package com.hiddenodds.trebolv2.model.persistent.database

import android.os.Parcel
import com.hiddenodds.trebolv2.model.data.Customer
import com.hiddenodds.trebolv2.model.data.Notification
import com.hiddenodds.trebolv2.model.data.Technical
import com.hiddenodds.trebolv2.model.interfaces.IDataContent
import com.hiddenodds.trebolv2.model.interfaces.IRepository
import com.hiddenodds.trebolv2.model.interfaces.ITaskCompleteListener
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
            realm.executeTransaction({
                e = it.createObject(clazz, UUID.randomUUID().toString())
                if (e is IDataContent){
                    (e as IDataContent).setContent(parcel)
                }

                listener.onSaveSucceeded()

            })
        }catch (e: Throwable){
            listener.onSaveFailed(e.message!!)
        }
        return e

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
            realm.executeTransaction({
                it.where(clazz)
                        .equalTo(fieldName, value)
                        .findAll()?.deleteAllFromRealm()
                listener.onSaveSucceeded()
            })
            true
        }catch (e: Throwable){
            listener.onSaveFailed(e.message!!)
            false
        }
    }
    override fun <E : RealmObject> deleteAll(clazz: Class<E>,
                                             listener: ITaskCompleteListener) {

        val realm: Realm = Realm.getDefaultInstance()
        try {
            realm.executeTransaction({
                it.where(clazz)
                        .findAll()?.deleteAllFromRealm()
                listener.onSaveSucceeded()
            })

        }catch (e: Throwable){
            listener.onSaveFailed(e.message!!)
        }

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
            //realm.close()
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
        }

        return true
    }

}
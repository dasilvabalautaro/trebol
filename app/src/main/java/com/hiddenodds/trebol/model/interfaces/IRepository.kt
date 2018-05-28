package com.hiddenodds.trebol.model.interfaces

import android.os.Parcel
import io.realm.RealmObject
import io.realm.RealmResults


interface IRepository {
    fun <E : RealmObject> save(clazz: Class<E>, parcel: Parcel,
                               listener: ITaskCompleteListener): E?

    fun <E : RealmObject> getDataByField(clazz: Class<E>,
                                         fieldName: String,
                                         value: String): RealmResults<E>?

    fun <E : RealmObject> getAllData(clazz: Class<E>): RealmResults<E>?
    fun <E : RealmObject> deleteByField(clazz: Class<E>, fieldName: String,
                                        value: String,
                                        listener: ITaskCompleteListener): Boolean

    fun <E : RealmObject> deleteAll(clazz: Class<E>,
                                    listener: ITaskCompleteListener): Boolean

    fun <E : RealmObject> updateField(nameField: String, valueSearch: String,
                                      nameFieldUpdate: String, newValue: String,
                                      clazz: Class<E>,
                                      listener: ITaskCompleteListener): Boolean
    fun <E : RealmObject> save(clazz: Class<E>,
                               listener: ITaskCompleteListener): String?
}
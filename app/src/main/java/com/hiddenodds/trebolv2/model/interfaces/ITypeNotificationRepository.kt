package com.hiddenodds.trebolv2.model.interfaces

import com.hiddenodds.trebolv2.domain.data.MapperTypeNotification
import com.hiddenodds.trebolv2.presentation.model.TypeNotificationModel
import io.reactivex.Observable
import java.util.*


interface ITypeNotificationRepository{
    fun save(typeNotification: MapperTypeNotification): Observable<TypeNotificationModel>
    fun saveList(list: ArrayList<MapperTypeNotification>): Observable<Boolean>
    fun getList(): Observable<List<TypeNotificationModel>>
    fun deleteList(): Observable<Boolean>
}
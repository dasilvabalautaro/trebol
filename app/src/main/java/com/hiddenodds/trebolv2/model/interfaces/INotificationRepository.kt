package com.hiddenodds.trebolv2.model.interfaces

import com.hiddenodds.trebolv2.domain.data.MapperNotification
import com.hiddenodds.trebolv2.presentation.model.NotificationModel
import io.reactivex.Observable
import java.util.*


interface INotificationRepository{
    fun save(notification: MapperNotification): Observable<NotificationModel>
    fun saveList(list: ArrayList<MapperNotification>): Observable<Boolean>
    fun addNotificationsToTechnical(codeTech: String): Observable<Boolean>
    fun addCustomer(idTech: String): Observable<Boolean>
    fun updateField(id: String, nameFieldUpdate: String,
                    newValue: String): Observable<Boolean>
    fun getFinishedNotification(): Observable<List<NotificationModel>>
    fun deleteNotification(id: String, codeTech: String): Observable<Boolean>
}
package com.hiddenodds.trebolv2.model.interfaces

import com.hiddenodds.trebolv2.domain.data.MapperNotification
import com.hiddenodds.trebolv2.presentation.model.NotificationModel
import io.reactivex.Observable
import java.util.*


interface INotificationRepository: IMessagePersistent {
    fun save(notification: MapperNotification): Observable<NotificationModel>
    fun saveList(list: ArrayList<MapperNotification>): Observable<Boolean>
    fun addNotificationsToTechnical(codeTech: String): Observable<Boolean>
}
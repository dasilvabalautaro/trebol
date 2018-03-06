package com.hiddenodds.trebolv2.model.executor

import android.os.Parcel
import com.hiddenodds.trebolv2.App
import com.hiddenodds.trebolv2.dagger.ModelsModule
import com.hiddenodds.trebolv2.domain.data.MapperNotification
import com.hiddenodds.trebolv2.model.data.Notification
import com.hiddenodds.trebolv2.model.exception.DatabaseOperationException
import com.hiddenodds.trebolv2.model.interfaces.INotificationRepository
import com.hiddenodds.trebolv2.model.persistent.database.CRUDRealm
import com.hiddenodds.trebolv2.presentation.mapper.NotificationModelDataMapper
import com.hiddenodds.trebolv2.presentation.model.NotificationModel
import io.reactivex.Observable
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificationExecutor @Inject constructor(): CRUDRealm(),
        INotificationRepository{

    private val context = App.appComponent.context()

    private val component by lazy {(context as App)
            .getAppComponent().plus(ModelsModule(context))}

    @Inject
    lateinit var taskListenerExecutor: TaskListenerExecutor
    @Inject
    lateinit var notificationModelDataMapper: NotificationModelDataMapper

    init {
        component.inject(this)
    }

    override fun userGetMessage(): Observable<String> {
        return this.taskListenerExecutor
                .observableMessage.map { s -> s }

    }

    override fun userGetError(): Observable<DatabaseOperationException> {
        return this.taskListenerExecutor
                .observableException.map { e -> e }

    }

    override fun save(notification: MapperNotification): Observable<NotificationModel> {
        val parcel: Parcel = notification.getContent()
        parcel.setDataPosition(0)

        return Observable.create { subscriber ->

            val clazz: Class<Notification> = Notification::class.java

            val newNotification = this.save(clazz, parcel, taskListenerExecutor)
            if (newNotification != null){
                val notificationModel = this.notificationModelDataMapper
                        .transform(newNotification)

                subscriber.onNext(notificationModel)
                subscriber.onComplete()
            }else{
                subscriber.onError(Throwable())
            }

        }
    }

    override fun saveList(list: ArrayList<MapperNotification>): Observable<Boolean> {
        var flag = true
        return Observable.create{subscriber ->
            for (i in list.indices){
                val parcel: Parcel = list[i].getContent()
                parcel.setDataPosition(0)

                val clazz: Class<Notification> = Notification::class.java

                val newNotification = this.save(clazz, parcel, taskListenerExecutor)
                if (newNotification == null){
                    flag = false
                    break
                }
            }
            if (flag){
                subscriber.onNext(true)
                subscriber.onComplete()
            }else{
                subscriber.onError(Throwable())
            }
        }
    }

    override fun addNotificationsToTechnical(codeTech: String): Observable<Boolean> {
        return Observable.create{subscriber ->
            if (this.addListNotificationToTechnical(codeTech,
                            taskListenerExecutor)){
                subscriber.onNext(true)
                subscriber.onComplete()
            }else{
                subscriber.onError(Throwable())
            }

        }
    }


}
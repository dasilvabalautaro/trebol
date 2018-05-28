package com.hiddenodds.trebol.model.executor

import android.os.Parcel
import com.hiddenodds.trebol.App
import com.hiddenodds.trebol.dagger.ModelsModule
import com.hiddenodds.trebol.domain.data.MapperNotification
import com.hiddenodds.trebol.model.data.Notification
import com.hiddenodds.trebol.model.interfaces.INotificationRepository
import com.hiddenodds.trebol.model.persistent.caching.CachingLruRepository
import com.hiddenodds.trebol.model.persistent.database.CRUDRealm
import com.hiddenodds.trebol.presentation.mapper.NotificationModelDataMapper
import com.hiddenodds.trebol.presentation.model.NotificationModel
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.realm.RealmResults
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificationExecutor @Inject constructor(): CRUDRealm(),
        INotificationRepository{

    private val context = App.appComponent.context()

    private val component by lazy {(context as App)
            .getAppComponent().plus(ModelsModule(context))}
    private var disposable: CompositeDisposable = CompositeDisposable()
    private var msgError: String = ""

    @Inject
    lateinit var taskListenerExecutor: TaskListenerExecutor
    @Inject
    lateinit var notificationModelDataMapper: NotificationModelDataMapper

    init {
        component.inject(this)
        val hear = this.taskListenerExecutor
                .observableException.map { s -> s }
        disposable.add(hear.observeOn(Schedulers.newThread())
                .subscribe { s ->
                    this.msgError = s.message
                })

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
                subscriber.onError(Throwable(this.msgError))
            }

        }
    }

    override fun saveList(list: ArrayList<MapperNotification>): Observable<Boolean> {
        var flag = true
        //val q = list[0].idTech
        return Observable.create{subscriber ->
            for (i in list.indices){
                val parcel: Parcel = list[i].getContent()
                parcel.setDataPosition(0)

                val clazz: Class<Notification> = Notification::class.java

                val newNotification = this.save(clazz, parcel,
                        taskListenerExecutor)
                if (newNotification == null){
                    flag = false
                    break
                }
            }
            /*val l = this.getDataByField(Notification::class.java,
                    "idTech", "T025")*/
            if (flag){
                subscriber.onNext(true)
                subscriber.onComplete()
            }else{
                subscriber.onError(Throwable(this.msgError))
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
                subscriber.onError(Throwable(this.msgError))
            }

        }
    }

    override fun deleteNotification(id: String, codeTech: String): Observable<Boolean> {
        return Observable.create{subscriber ->
            val clazz: Class<Notification> = Notification::class.java
            if (this.deleteByField(clazz, "id", id,
                            taskListenerExecutor)){
                CachingLruRepository
                        .instance
                        .delLru(codeTech)

                subscriber.onNext(true)
                subscriber.onComplete()
            }else{
                subscriber.onError(Throwable(this.msgError))
            }
        }
    }

    override fun addCustomer(idTech: String): Observable<Boolean> {
        return Observable.create{subscriber ->
            if (this.addCustomerToNotification(idTech,
                            taskListenerExecutor)){
                subscriber.onNext(true)
                subscriber.onComplete()
            }else{
                subscriber.onError(Throwable(this.msgError))
            }
        }
    }

    override fun updateField(id: String, nameFieldUpdate: String,
                             newValue: String): Observable<Boolean> {
        return Observable.create{subscriber ->
            if (this.updateFieldNotification(id, nameFieldUpdate, newValue,
                            taskListenerExecutor)){
                subscriber.onNext(true)
                subscriber.onComplete()
            }else{
                subscriber.onError(Throwable(this.msgError))
            }
        }
    }

    override fun getFinishedNotification(): Observable<List<NotificationModel>> {
        return Observable.create { subscriber ->
            val clazz: Class<Notification> = Notification::class.java
            val listNotification: RealmResults<Notification>? = this.getDataByField(clazz,
                    "state", "1")
            if (listNotification != null){
                val notificationModelCollection: Collection<NotificationModel> = this
                        .notificationModelDataMapper
                        .transform(listNotification)
                subscriber.onNext(notificationModelCollection as List<NotificationModel>)
                subscriber.onComplete()
            }else{
                subscriber.onError(Throwable("List empty."))
            }
        }
    }


}
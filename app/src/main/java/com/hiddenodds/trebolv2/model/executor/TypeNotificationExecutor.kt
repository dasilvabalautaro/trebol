package com.hiddenodds.trebolv2.model.executor

import android.os.Parcel
import com.hiddenodds.trebolv2.App
import com.hiddenodds.trebolv2.dagger.ModelsModule
import com.hiddenodds.trebolv2.domain.data.MapperTypeNotification
import com.hiddenodds.trebolv2.model.data.TypeNotification
import com.hiddenodds.trebolv2.model.exception.DatabaseOperationException
import com.hiddenodds.trebolv2.model.interfaces.ITypeNotificationRepository
import com.hiddenodds.trebolv2.model.persistent.database.CRUDRealm
import com.hiddenodds.trebolv2.presentation.mapper.TypeNotificationModelDataMapper
import com.hiddenodds.trebolv2.presentation.model.TypeNotificationModel
import io.reactivex.Observable
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TypeNotificationExecutor @Inject constructor(): CRUDRealm(),
        ITypeNotificationRepository{

    private val context = App.appComponent.context()

    private val component by lazy {(context as App)
            .getAppComponent().plus(ModelsModule(context))}

    @Inject
    lateinit var taskListenerExecutor: TaskListenerExecutor
    @Inject
    lateinit var typeNotificationModelDataMapper: TypeNotificationModelDataMapper

    init {
        component.inject(this)
    }
    

    override fun userGetMessage(): Observable<String> {
        return taskListenerExecutor.observableMessage.map { s -> s }
    }

    override fun userGetError(): Observable<DatabaseOperationException> {
        return this.taskListenerExecutor
                .observableException.map { e -> e }

    }

    override fun save(typeNotification: MapperTypeNotification): 
            Observable<TypeNotificationModel> {
        val parcel: Parcel = typeNotification.getContent()
        parcel.setDataPosition(0)

        return Observable.create { subscriber ->

            val clazz: Class<TypeNotification> = TypeNotification::class.java

            val newTypeNotification = this.save(clazz, parcel, taskListenerExecutor)
            if (newTypeNotification != null){
                val typeNotificationModel = this.typeNotificationModelDataMapper
                        .transform(newTypeNotification)

                subscriber.onNext(typeNotificationModel)
                subscriber.onComplete()
            }else{
                subscriber.onError(Throwable())
            }

        }

    }

    override fun saveList(list: ArrayList<MapperTypeNotification>): 
            Observable<Boolean> {
        var flag = true
        return Observable.create{subscriber ->
            val clazz: Class<TypeNotification> = TypeNotification::class.java
            this.deleteAll(clazz, taskListenerExecutor)
            for (i in list.indices){
                val parcel: Parcel = list[i].getContent()
                parcel.setDataPosition(0)
                val newTypeNotification = this.save(clazz, parcel, taskListenerExecutor)
                if (newTypeNotification == null){
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

    override fun getList(): Observable<List<TypeNotificationModel>> {
        return Observable.create { subscriber ->
            val clazz: Class<TypeNotification> = TypeNotification::class.java
            val listTypeNotification: List<TypeNotification>? = this.getAllData(clazz)
            if (listTypeNotification != null){
                val typeNotificationModelCollection: Collection<TypeNotificationModel> = this
                        .typeNotificationModelDataMapper
                        .transform(listTypeNotification)
                subscriber.onNext(typeNotificationModelCollection as List<TypeNotificationModel>)
                subscriber.onComplete()
            }else{
                subscriber.onError(Throwable())
            }
        }

    }

    override fun deleteList(): Observable<Boolean> {
        return Observable.create{subscriber ->
            val clazz: Class<TypeNotification> = TypeNotification::class.java
            this.deleteAll(clazz, taskListenerExecutor)
            subscriber.onNext(true)
            subscriber.onComplete()
        }
    }



}
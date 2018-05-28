package com.hiddenodds.trebol.model.executor

import android.os.Parcel
import com.hiddenodds.trebol.App
import com.hiddenodds.trebol.dagger.ModelsModule
import com.hiddenodds.trebol.domain.data.MapperTypeNotification
import com.hiddenodds.trebol.model.data.TypeNotification
import com.hiddenodds.trebol.model.interfaces.ITypeNotificationRepository
import com.hiddenodds.trebol.model.persistent.caching.CachingLruRepository
import com.hiddenodds.trebol.model.persistent.database.CRUDRealm
import com.hiddenodds.trebol.presentation.mapper.TypeNotificationModelDataMapper
import com.hiddenodds.trebol.presentation.model.TypeNotificationModel
import com.hiddenodds.trebol.tools.Constants
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TypeNotificationExecutor @Inject constructor(): CRUDRealm(),
        ITypeNotificationRepository{

    private val context = App.appComponent.context()

    private val component by lazy {(context as App)
            .getAppComponent().plus(ModelsModule(context))}
    private var disposable: CompositeDisposable = CompositeDisposable()
    private var msgError: String = ""

    @Inject
    lateinit var taskListenerExecutor: TaskListenerExecutor
    @Inject
    lateinit var typeNotificationModelDataMapper: TypeNotificationModelDataMapper

    init {
        component.inject(this)
        val hear = this.taskListenerExecutor
                .observableException.map { s -> s }
        disposable.add(hear.observeOn(Schedulers.newThread())
                .subscribe { s ->
                    this.msgError = s.message
                })

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
                subscriber.onError(Throwable(this.msgError))
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
                val newTypeNotification = this.save(clazz, parcel,
                        taskListenerExecutor)
                if (newTypeNotification == null){
                    flag = false
                    break
                }
            }
            if (flag){
                subscriber.onNext(true)
                subscriber.onComplete()
            }else{
                subscriber.onError(Throwable(this.msgError))
            }
        }
        
    }

    override fun getList(): Observable<List<TypeNotificationModel>> {
        return Observable.create { subscriber ->
            var listTypeNotificationModel: List<TypeNotificationModel>?
            val list = getListTypeNotificationOfCache()
            if (list != null && list.isNotEmpty()){
                listTypeNotificationModel = list.filterIsInstance<TypeNotificationModel>()
                subscriber.onNext(listTypeNotificationModel)
                subscriber.onComplete()
            }else{
                val clazz: Class<TypeNotification> = TypeNotification::class.java
                val listTypeNotification: List<TypeNotification>? = this.getAllData(clazz)
                if (listTypeNotification != null){
                    val typeNotificationModelCollection: Collection<TypeNotificationModel> = this
                            .typeNotificationModelDataMapper
                            .transform(listTypeNotification)
                    CachingLruRepository.instance.getLru()
                            .put(Constants.CACHE_LIST_TYPE_NOTIFICATION,
                                    typeNotificationModelCollection as List<TypeNotificationModel>)

                    subscriber.onNext(typeNotificationModelCollection)
                    subscriber.onComplete()
                }else{
                    subscriber.onError(Throwable("List empty type notification."))
                }
            }


        }

    }

    override fun deleteList(): Observable<Boolean> {
        return Observable.create{subscriber ->
            this.msgError = ""
            val clazz: Class<TypeNotification> = TypeNotification::class.java
            this.deleteAll(clazz, taskListenerExecutor)
            if (this.msgError.isEmpty()){
                subscriber.onNext(true)
                subscriber.onComplete()
            }else{
                subscriber.onError(Throwable(this.msgError))
            }
        }
    }

    private fun getListTypeNotificationOfCache(): List<*>? {
        return CachingLruRepository
                .instance
                .getLru()
                .get(Constants.CACHE_LIST_TYPE_NOTIFICATION) as List<*>?
    }

}
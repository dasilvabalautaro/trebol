package com.hiddenodds.trebol.model.executor

import com.hiddenodds.trebol.App
import com.hiddenodds.trebol.dagger.ModelsModule
import com.hiddenodds.trebol.model.data.Maintenance
import com.hiddenodds.trebol.model.interfaces.IMaintenanceRepository
import com.hiddenodds.trebol.model.persistent.caching.CachingLruRepository
import com.hiddenodds.trebol.model.persistent.database.CRUDRealm
import com.hiddenodds.trebol.presentation.interfaces.IModel
import com.hiddenodds.trebol.presentation.mapper.MaintenanceModelDataMapper
import com.hiddenodds.trebol.presentation.model.MaintenanceModel
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MaintenanceExecutor @Inject constructor(): CRUDRealm(),
        IMaintenanceRepository {
    private val context = App.appComponent.context()

    private val component by lazy {(context as App)
            .getAppComponent().plus(ModelsModule(context))}
    private var disposable: CompositeDisposable = CompositeDisposable()
    private var msgError: String = ""

    @Inject
    lateinit var taskListenerExecutor: TaskListenerExecutor
    @Inject
    lateinit var maintenanceModelDataMapper: MaintenanceModelDataMapper


    init {
        component.inject(this)
        val hear = this.taskListenerExecutor
                .observableException.map { s -> s }
        disposable.add(hear.observeOn(Schedulers.newThread())
                .subscribe { s ->
                    this.msgError = s.message
                })
    }


    override fun save(): Observable<MaintenanceModel> {
       
        return Observable.create { subscriber ->

            val clazz: Class<Maintenance> = Maintenance::class.java

            val newMaintenance = this.save(clazz, taskListenerExecutor)
            if (!newMaintenance.isNullOrEmpty()){

                val maintenanceModel = MaintenanceModel()
                maintenanceModel.id = newMaintenance
                subscriber.onNext(maintenanceModel)
                subscriber.onComplete()
            }else{
                subscriber.onError(Throwable(this.msgError))
            }

        }
    }


    override fun getMaintenance(codeNotify: String): Observable<MaintenanceModel> {
        return Observable.create { subscriber ->
            var maintenanceModel: MaintenanceModel?
            maintenanceModel = getMaintenanceOfCache(codeNotify)
            if (maintenanceModel != null){
                subscriber.onNext(maintenanceModel)
                subscriber.onComplete()

            }else{
                val clazz: Class<Maintenance> = Maintenance::class.java
                val newMaintenance: List<IModel>? = this.getDataByField(clazz,
                        "codeNotify", codeNotify,
                        this.maintenanceModelDataMapper, taskListenerExecutor)

                if (newMaintenance!!.isNotEmpty()){
                    maintenanceModel = newMaintenance[0] as MaintenanceModel

                    CachingLruRepository.instance.getLru()
                            .put(codeNotify, maintenanceModel)
                    subscriber.onNext(maintenanceModel)
                    subscriber.onComplete()

                }else{
                    subscriber.onError(Throwable("Maintenance not exist."))
                }
            }

        }
    }

    override fun update(id: String, nameFieldUpdate: String,
                        newValue: String): Observable<Boolean> {
        return Observable.create{subscriber ->
            if (this.updateFieldMaintenance(id, nameFieldUpdate, newValue,
                            taskListenerExecutor)){
                subscriber.onNext(true)
                subscriber.onComplete()
            }else{
                subscriber.onError(Throwable(this.msgError))
            }
        }
    }

    override fun delete(codeNotify: String): Observable<Boolean> {
        return Observable.create{subscriber ->
            val clazz: Class<Maintenance> = Maintenance::class.java
            if (this.deleteByField(clazz, "codeNotify",
                            codeNotify, taskListenerExecutor)){
                /*val l = this.getDataByField(Notification::class.java,
                        "idTech", code)*/
                CachingLruRepository
                        .instance
                        .getLru()
                        .remove(codeNotify)
                subscriber.onNext(true)
                subscriber.onComplete()
            }else{
                subscriber.onError(Throwable(this.msgError))
            }
        }
    }

    private fun getMaintenanceOfCache(code: String): MaintenanceModel?{
        return CachingLruRepository
                .instance
                .getLru()
                .get(code) as MaintenanceModel?
    }

}
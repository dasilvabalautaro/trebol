package com.hiddenodds.trebol.model.executor

import com.hiddenodds.trebol.App
import com.hiddenodds.trebol.dagger.ModelsModule
import com.hiddenodds.trebol.model.interfaces.IAssignedMaterialRepository
import com.hiddenodds.trebol.model.persistent.database.CRUDRealm
import com.hiddenodds.trebol.presentation.mapper.AssignedMaterialModelDataMapper
import com.hiddenodds.trebol.presentation.model.AssignedMaterialModel
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AssignedMaterialExecutor @Inject constructor(): CRUDRealm(),
        IAssignedMaterialRepository{

    private val context = App.appComponent.context()

    private val component by lazy {(context as App)
            .getAppComponent().plus(ModelsModule(context))}
    private var disposable: CompositeDisposable = CompositeDisposable()
    private var msgError: String = ""
    @Inject
    lateinit var taskListenerExecutor: TaskListenerExecutor
    @Inject
    lateinit var assignedMaterialModelDataMapper: AssignedMaterialModelDataMapper

    init {
        component.inject(this)
        val hear = this.taskListenerExecutor
                .observableException.map { s -> s }
        disposable.add(hear.observeOn(Schedulers.newThread())
                .subscribe { s ->
                    this.msgError = s.message
                })

    }

    override fun save(assigned: AssignedMaterialModel): Observable<AssignedMaterialModel> {
        return Observable.create { subscriber ->
            val newAssignedMaterial = this.saveAssignedMat(assigned,
                    taskListenerExecutor)
            if (newAssignedMaterial != null){
                val assignedMaterialModel = this.assignedMaterialModelDataMapper
                        .transform(newAssignedMaterial)

                subscriber.onNext(assignedMaterialModel)
                subscriber.onComplete()
            }else{
                subscriber.onError(Throwable(this.msgError))
            }

        }
    }

    override fun saveList(list: ArrayList<AssignedMaterialModel>):
            ArrayList<String> {
        val listAssigned: ArrayList<String> = ArrayList()

        for (i in list.indices){
            val newAssignedMaterial = this.saveAssignedMaterial(list[i],
                    taskListenerExecutor)
            if (!newAssignedMaterial.isNullOrEmpty()){
                /*val assignedMaterialModel = this.assignedMaterialModelDataMapper
                        .transform(newAssignedMaterial)*/
                listAssigned.add(newAssignedMaterial!!)
            }
        }

        return listAssigned
    }

    override fun addListAssignedMaterialToNotification(list:
                                                       ArrayList<AssignedMaterialModel>,
                                                       id: String, flagUse: Boolean):
            Observable<Boolean> {
        return Observable.create{subscriber ->
            val listAssignedSave = this.saveList(list)
            if (this.addAssignedMaterialToNotification(listAssignedSave,
                            id, flagUse, taskListenerExecutor)){
                subscriber.onNext(true)
                subscriber.onComplete()
            }else{
                subscriber.onError(Throwable(this.msgError))
            }
        }
    }

    override fun updatedAssigned(id: String, quantity: Int): Observable<Boolean> {
        return Observable.create{subscriber ->
            if (this.updateAssignedMaterial(id,
                            quantity, taskListenerExecutor)){
                subscriber.onNext(true)
                subscriber.onComplete()
            }else{
                subscriber.onError(Throwable(this.msgError))
            }
        }

    }

    override fun deleteAssignedMaterial(idNotification: String,
                                        idAssigned: String,
                                        flagUse: Boolean): Observable<Boolean> {
        return Observable.create{subscriber ->
            if (this.deleteAssignedMaterialOfNotification(idNotification,
                            idAssigned, flagUse, taskListenerExecutor)){
                subscriber.onNext(true)
                subscriber.onComplete()
            }else{
                subscriber.onError(Throwable(this.msgError))
            }
        }

    }


}
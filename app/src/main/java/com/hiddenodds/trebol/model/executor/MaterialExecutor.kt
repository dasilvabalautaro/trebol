package com.hiddenodds.trebol.model.executor

import android.os.Parcel
import com.hiddenodds.trebol.App
import com.hiddenodds.trebol.dagger.ModelsModule
import com.hiddenodds.trebol.domain.data.MapperMaterial
import com.hiddenodds.trebol.model.data.Material
import com.hiddenodds.trebol.model.interfaces.IMaterialRepository
import com.hiddenodds.trebol.model.persistent.database.CRUDRealm
import com.hiddenodds.trebol.presentation.mapper.MaterialModelDataMapper
import com.hiddenodds.trebol.presentation.model.MaterialModel
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.realm.RealmResults
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MaterialExecutor @Inject constructor(): CRUDRealm(),
        IMaterialRepository{

    private val context = App.appComponent.context()

    private val component by lazy {(context as App)
            .getAppComponent().plus(ModelsModule(context))}
    private var disposable: CompositeDisposable = CompositeDisposable()
    private var msgError: String = ""

    @Inject
    lateinit var taskListenerExecutor: TaskListenerExecutor
    @Inject
    lateinit var materialModelDataMapper: MaterialModelDataMapper

    init {
        component.inject(this)
        val hear = this.taskListenerExecutor
                .observableException.map { s -> s }
        disposable.add(hear.observeOn(Schedulers.newThread())
                .subscribe { s ->
                    this.msgError = s.message
                })
    }

    override fun save(material: MapperMaterial): Observable<MaterialModel> {
        val parcel: Parcel = material.getContent()
        parcel.setDataPosition(0)

        return Observable.create { subscriber ->

            val clazz: Class<Material> = Material::class.java

            val newMaterial = this.save(clazz, parcel, taskListenerExecutor)
            if (newMaterial != null){
                val materialModel = this.materialModelDataMapper
                        .transform(newMaterial)

                subscriber.onNext(materialModel)
                subscriber.onComplete()
            }else{
                subscriber.onError(Throwable(this.msgError))
            }

        }
    }

    override fun saveList(list: ArrayList<MapperMaterial>): Observable<Boolean> {
        var flag = true
        return Observable.create{subscriber ->
            val clazz: Class<Material> = Material::class.java
            //this.deleteAll(clazz, taskListenerExecutor)
            var l = this.getAllData(clazz)
            println("List size before: ${l!!.size}")
            for (i in list.indices){
                val parcel: Parcel = list[i].getContent()
                parcel.setDataPosition(0)
                val newMaterial = this.save(clazz, parcel, taskListenerExecutor)
                if (newMaterial == null){
                    flag = false
                    break
                }
            }
            l = this.getAllData(clazz)
            println("List size after: ${l!!.size}")
            if (flag){
                subscriber.onNext(true)
                subscriber.onComplete()
            }else{
                subscriber.onError(Throwable(this.msgError))
            }
        }
    }

    override fun getList(): Observable<List<MaterialModel>> {
        return Observable.create { subscriber ->
            val clazz: Class<Material> = Material::class.java
            val listMaterial: RealmResults<Material>? = this.getAllData(clazz)
            println("Mostrar lista: ${listMaterial!!.size}")
            if (listMaterial.isNotEmpty()){
                val materialModelCollection: Collection<MaterialModel> = this
                        .materialModelDataMapper
                        .transform(listMaterial)
                subscriber.onNext(materialModelCollection as List<MaterialModel>)
                subscriber.onComplete()
            }else{
                subscriber.onError(Throwable("List empty."))
            }
        }
    }

    override fun deleteList(): Observable<Boolean> {
        return Observable.create{subscriber ->
            this.msgError = ""
            val clazz: Class<Material> = Material::class.java
            if (this.deleteAll(clazz, taskListenerExecutor)){
                subscriber.onNext(true)
                subscriber.onComplete()
            }else{
                subscriber.onError(Throwable(this.msgError))
            }
        }
    }

}
package com.hiddenodds.trebolv2.model.executor

import android.os.Parcel
import com.hiddenodds.trebolv2.App
import com.hiddenodds.trebolv2.dagger.ModelsModule
import com.hiddenodds.trebolv2.domain.data.MapperMaterial
import com.hiddenodds.trebolv2.model.data.Material
import com.hiddenodds.trebolv2.model.exception.DatabaseOperationException
import com.hiddenodds.trebolv2.model.interfaces.IMaterialRepository
import com.hiddenodds.trebolv2.model.persistent.database.CRUDRealm
import com.hiddenodds.trebolv2.presentation.mapper.MaterialModelDataMapper
import com.hiddenodds.trebolv2.presentation.model.MaterialModel
import io.reactivex.Observable
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MaterialExecutor @Inject constructor(): CRUDRealm(),
        IMaterialRepository{

    private val context = App.appComponent.context()

    private val component by lazy {(context as App)
            .getAppComponent().plus(ModelsModule(context))}

    @Inject
    lateinit var taskListenerExecutor: TaskListenerExecutor
    @Inject
    lateinit var materialModelDataMapper: MaterialModelDataMapper

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
                subscriber.onError(Throwable())
            }

        }
    }

    override fun saveList(list: ArrayList<MapperMaterial>): Observable<Boolean> {
        var flag = true
        return Observable.create{subscriber ->
            val clazz: Class<Material> = Material::class.java
            this.deleteAll(clazz, taskListenerExecutor)
            for (i in list.indices){
                val parcel: Parcel = list[i].getContent()
                parcel.setDataPosition(0)
                val newMaterial = this.save(clazz, parcel, taskListenerExecutor)
                if (newMaterial == null){
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

    override fun getList(): Observable<List<MaterialModel>> {
        return Observable.create { subscriber ->
            val clazz: Class<Material> = Material::class.java
            val listMaterial: List<Material>? = this.getAllData(clazz)
            if (listMaterial != null){
                val materialModelCollection: Collection<MaterialModel> = this
                        .materialModelDataMapper
                        .transform(listMaterial)
                subscriber.onNext(materialModelCollection as List<MaterialModel>)
                subscriber.onComplete()
            }else{
                subscriber.onError(Throwable())
            }
        }
    }

    override fun deleteList(): Observable<Boolean> {
        return Observable.create{subscriber ->
            val clazz: Class<Material> = Material::class.java
            this.deleteAll(clazz, taskListenerExecutor)
            subscriber.onNext(true)
            subscriber.onComplete()
        }
    }

}
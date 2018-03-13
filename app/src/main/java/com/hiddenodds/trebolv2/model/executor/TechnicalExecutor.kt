package com.hiddenodds.trebolv2.model.executor

import android.os.Parcel
import com.hiddenodds.trebolv2.App
import com.hiddenodds.trebolv2.dagger.ModelsModule
import com.hiddenodds.trebolv2.domain.data.MapperTechnical
import com.hiddenodds.trebolv2.model.data.Technical
import com.hiddenodds.trebolv2.model.exception.DatabaseOperationException
import com.hiddenodds.trebolv2.model.interfaces.ITechnicalRepository
import com.hiddenodds.trebolv2.model.persistent.caching.CachingLruRepository
import com.hiddenodds.trebolv2.model.persistent.database.CRUDRealm
import com.hiddenodds.trebolv2.presentation.mapper.TechnicalModelDataMapper
import com.hiddenodds.trebolv2.presentation.model.TechnicalModel
import com.hiddenodds.trebolv2.tools.Constants
import com.hiddenodds.trebolv2.tools.PreferenceHelper
import com.hiddenodds.trebolv2.tools.PreferenceHelper.set
import io.reactivex.Observable
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton



@Singleton
class TechnicalExecutor @Inject constructor(): CRUDRealm(),
                        ITechnicalRepository{

    private val context = App.appComponent.context()

    private val component by lazy {(context as App)
            .getAppComponent().plus(ModelsModule(context))}

    @Inject
    lateinit var taskListenerExecutor: TaskListenerExecutor
    @Inject
    lateinit var technicalModelDataMapper: TechnicalModelDataMapper

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

    override fun save(technical: MapperTechnical): Observable<TechnicalModel> {
        val parcel: Parcel = technical.getContent()
        parcel.setDataPosition(0)

        return Observable.create { subscriber ->

            val clazz: Class<Technical> = Technical::class.java

            val newTechnical = this.save(clazz, parcel, taskListenerExecutor)
            if (newTechnical != null){
                val technicalModel = this.technicalModelDataMapper
                        .transform(newTechnical)

                subscriber.onNext(technicalModel)
                subscriber.onComplete()
            }else{
                subscriber.onError(Throwable())
            }

        }
    }

    override fun saveList(list: ArrayList<MapperTechnical>): Observable<Boolean> {
        var flag = true
        return Observable.create{subscriber ->
            for (i in list.indices){
                val parcel: Parcel = list[i].getContent()
                parcel.setDataPosition(0)
                val clazz: Class<Technical> = Technical::class.java

                val newTechnical = this.save(clazz, parcel, taskListenerExecutor)
                if (newTechnical == null){
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

    override fun saveListDependentTechnicians(dependentTechnical:
                                              LinkedHashMap<String,
                                                      java.util.ArrayList<String>>):
            Observable<Boolean> {
        var flag = true
        return Observable.create { subscriber ->
            dependentTechnical.iterator().forEach {
                if (!this.saveListTRD(it.key, it.value)){
                    flag = false
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

    override fun getTechnical(code: String): Observable<TechnicalModel> {
        return Observable.create { subscriber ->
            var technicalModel: TechnicalModel? = null
            technicalModel = getTechicalOfCache(code)
            if (technicalModel != null){
                subscriber.onNext(technicalModel)
                subscriber.onComplete()
            }else{
                val clazz: Class<Technical> = Technical::class.java
                val newTechnical: List<Technical>? = this.getDataByField(clazz,
                        "code", code)
                if (newTechnical!!.isNotEmpty()){
                    technicalModel = this.technicalModelDataMapper
                            .transform(newTechnical[0])
                    CachingLruRepository.instance.getLru()
                            .put(code, technicalModel)
                    subscriber.onNext(technicalModel)
                    subscriber.onComplete()
                }else{
                    subscriber.onError(Throwable())
                }
            }

        }
    }


    override fun getMasterTechnical(code: String,
                                    password: String): Observable<TechnicalModel>{


        return Observable.create { subscriber ->
            var technicalModel: TechnicalModel? = null
            technicalModel = getTechMasterOfCache()
            if (technicalModel != null){
                subscriber.onNext(technicalModel)
                subscriber.onComplete()
            }else{
                val newTechnical: List<Technical>? = this.getTechnicalMaster(code,
                        password)
                if (newTechnical!!.isNotEmpty()){
                    technicalModel = this.technicalModelDataMapper
                            .transform(newTechnical[0])
                    CachingLruRepository.instance.getLru()
                            .put(Constants.CACHE_TECHNICAL_MASTER, technicalModel)
                    val prefs = PreferenceHelper.customPrefs(context,
                            Constants.PREFERENCE_TREBOL)
                    prefs[Constants.TECHNICAL_KEY] = technicalModel.code
                    prefs[Constants.TECHNICAL_PASSWORD] = technicalModel.password
                    subscriber.onNext(technicalModel)
                    subscriber.onComplete()
                }else{
                    subscriber.onError(Throwable())
                }
            }

        }
    }

    override fun deleteNotifications(code: String):
            Observable<Boolean> {

        return Observable.create{subscriber ->
            if (this.deleteNotificationsOfTechnical(code,
                            taskListenerExecutor)){
                subscriber.onNext(true)
                subscriber.onComplete()
            }else{
                subscriber.onError(Throwable())
            }
        }
    }


    private fun getTechMasterOfCache(): TechnicalModel?{
        return CachingLruRepository
                .instance
                .getLru()
                .get(Constants.CACHE_TECHNICAL_MASTER) as TechnicalModel?
    }

    private fun getTechicalOfCache(code: String): TechnicalModel?{
        return CachingLruRepository
                .instance
                .getLru()
                .get(code) as TechnicalModel?
    }
}
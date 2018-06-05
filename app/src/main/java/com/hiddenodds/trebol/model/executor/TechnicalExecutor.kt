package com.hiddenodds.trebol.model.executor

import android.os.Parcel
import com.hiddenodds.trebol.App
import com.hiddenodds.trebol.dagger.ModelsModule
import com.hiddenodds.trebol.domain.data.MapperTechnical
import com.hiddenodds.trebol.model.data.Technical
import com.hiddenodds.trebol.model.interfaces.ITechnicalRepository
import com.hiddenodds.trebol.model.persistent.caching.CachingLruRepository
import com.hiddenodds.trebol.model.persistent.database.CRUDRealm
import com.hiddenodds.trebol.presentation.mapper.TechnicalModelDataMapper
import com.hiddenodds.trebol.presentation.model.TechnicalModel
import com.hiddenodds.trebol.tools.Constants
import com.hiddenodds.trebol.tools.PreferenceHelper
import com.hiddenodds.trebol.tools.PreferenceHelper.get
import com.hiddenodds.trebol.tools.PreferenceHelper.set
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton



@Singleton
class TechnicalExecutor @Inject constructor(): CRUDRealm(),
                        ITechnicalRepository{

    private val context = App.appComponent.context()

    private val component by lazy {(context as App)
            .getAppComponent().plus(ModelsModule(context))}

    private var disposable: CompositeDisposable = CompositeDisposable()
    private var msgError: String = ""

    @Inject
    lateinit var taskListenerExecutor: TaskListenerExecutor
    @Inject
    lateinit var technicalModelDataMapper: TechnicalModelDataMapper

    init {
        component.inject(this)
        val hear = this.taskListenerExecutor
                .observableException.map { s -> s }
        disposable.add(hear.observeOn(Schedulers.newThread())
                .subscribe { s ->
                    this.msgError = s.message
                })
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
                subscriber.onError(Throwable(this.msgError))
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
                subscriber.onError(Throwable(this.msgError))
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
                if (!this.saveListTRD(it.key, it.value,
                                taskListenerExecutor)){
                    flag = false
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



    override fun getTechnical(code: String): Observable<TechnicalModel> {
        //deleteCacheTechnical()
        return Observable.create { subscriber ->
            var technicalModel: TechnicalModel?
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
                    subscriber.onError(Throwable("Technical not exist."))
                }
            }

        }
    }


    override fun getMasterTechnical(code: String,
                                    password: String): Observable<TechnicalModel>{
        //deleteCacheTechnical()
        return Observable.create { subscriber ->
            var technicalModel: TechnicalModel?
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
                            .put(technicalModel.code, technicalModel)
                    val prefs = PreferenceHelper.customPrefs(context,
                            Constants.PREFERENCE_TREBOL)
                    prefs[Constants.TECHNICAL_KEY] = technicalModel.code
                    prefs[Constants.TECHNICAL_PASSWORD] = technicalModel.password
                    subscriber.onNext(technicalModel)
                    subscriber.onComplete()
                }else{
                    subscriber.onError(Throwable("Technical master not exist."))
                }
            }

        }
    }

    override fun deleteNotifications(code: String):
            Observable<Boolean> {

        return Observable.create{subscriber ->
            if (this.deleteNotificationsOfTechnical(code,
                            taskListenerExecutor)){
                /*val l = this.getDataByField(Notification::class.java,
                        "idTech", code)*/
                CachingLruRepository
                        .instance
                        .getLru()
                        .remove(code)
                subscriber.onNext(true)
                subscriber.onComplete()
            }else{
                subscriber.onError(Throwable(this.msgError))
            }
        }
    }


    private fun getTechMasterOfCache(): TechnicalModel?{
        val prefs = PreferenceHelper.customPrefs(context,
                Constants.PREFERENCE_TREBOL)
        val code = prefs[Constants.TECHNICAL_KEY, ""]
        if (code != null){
            return getTechicalOfCache(code)
        }
        return null
    }

    private fun getTechicalOfCache(code: String): TechnicalModel?{
        return CachingLruRepository
                .instance
                .getLru()
                .get(code) as TechnicalModel?
    }


}
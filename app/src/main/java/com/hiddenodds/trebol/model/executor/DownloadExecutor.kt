package com.hiddenodds.trebol.model.executor

import android.os.Parcel
import com.hiddenodds.trebol.App
import com.hiddenodds.trebol.dagger.ModelsModule
import com.hiddenodds.trebol.domain.data.MapperDownload
import com.hiddenodds.trebol.model.data.Download
import com.hiddenodds.trebol.model.interfaces.IDownloadRepository
import com.hiddenodds.trebol.model.persistent.database.CRUDRealm
import com.hiddenodds.trebol.presentation.interfaces.IModel
import com.hiddenodds.trebol.presentation.mapper.DownloadModelDataMapper
import com.hiddenodds.trebol.presentation.model.DownloadModel
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DownloadExecutor @Inject constructor(): CRUDRealm(),
        IDownloadRepository {

    private val context = App.appComponent.context()

    private val component by lazy {(context as App)
            .getAppComponent().plus(ModelsModule(context))}
    private var disposable: CompositeDisposable = CompositeDisposable()
    private var msgError: String = ""

    @Inject
    lateinit var taskListenerExecutor: TaskListenerExecutor
    @Inject
    lateinit var downloadModelDataMapper: DownloadModelDataMapper

    init {
        component.inject(this)
        val hear = this.taskListenerExecutor
                .observableException.map { s -> s }
        disposable.add(hear.observeOn(Schedulers.newThread())
                .subscribe { s ->
                    this.msgError = s.message
                })
        
    }

    override fun create(list: ArrayList<MapperDownload>): Observable<Boolean> {
        var flag = true
        return Observable.create{subscriber ->

            for (i in list.indices){
                val parcel: Parcel = list[i].getContent()
                parcel.setDataPosition(0)
                val clazz: Class<Download> = Download::class.java

                val newDownload = this.save(clazz, parcel, taskListenerExecutor)
                if (newDownload == null){
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

    override fun update(code: String, fieldName: String, value: String):
            Observable<Boolean> {
        return Observable.create{subscriber ->
            if (this.updateToDownload(code, fieldName,
                            value,
                            taskListenerExecutor)){
                subscriber.onNext(true)
                subscriber.onComplete()
            }else{
                subscriber.onError(Throwable(this.msgError))
            }
        }
    }

    override fun delete(list: ArrayList<String>): Observable<Boolean> {
        return Observable.create{subscriber ->
            var flag = true
            val clazz: Class<Download> = Download::class.java

            for (i in list.indices){
                if (!this.deleteByField(clazz, "code",
                                list[i], taskListenerExecutor)){

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

    override fun getDownload(code: String): Observable<DownloadModel> {
        return Observable.create { subscriber ->
            val clazz: Class<Download> = Download::class.java
            val newDownload: List<IModel>? = this.getDataByField(clazz,
                    "code", code, this.downloadModelDataMapper,
                    taskListenerExecutor)
            if (newDownload!!.isNotEmpty()){
                val downloadModel = newDownload[0] as DownloadModel

                subscriber.onNext(downloadModel)
                subscriber.onComplete()
            }else{
                subscriber.onError(Throwable(this.msgError))
            }
        }
    }

}
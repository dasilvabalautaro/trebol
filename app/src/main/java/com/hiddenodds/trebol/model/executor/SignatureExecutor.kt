package com.hiddenodds.trebol.model.executor

import com.hiddenodds.trebol.App
import com.hiddenodds.trebol.dagger.ModelsModule
import com.hiddenodds.trebol.model.interfaces.ISignatureRepository
import com.hiddenodds.trebol.model.persistent.database.CRUDRealm
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SignatureExecutor @Inject constructor(): CRUDRealm(),
        ISignatureRepository {

    private val context = App.appComponent.context()

    private val component by lazy {(context as App)
            .getAppComponent().plus(ModelsModule(context))}
    private var disposable: CompositeDisposable = CompositeDisposable()
    private var msgError: String = ""

    @Inject
    lateinit var taskListenerExecutor: TaskListenerExecutor

    init {
        component.inject(this)
        val hear = this.taskListenerExecutor
                .observableException.map { s -> s }
        disposable.add(hear.observeOn(Schedulers.newThread())
                .subscribe { s ->
                    this.msgError = s.message
                })
    }

    override fun getSignature(name: String): Observable<String> {
        return Observable.create { subscriber ->

            val nameFile = this.nameFileSignature(name,
                    taskListenerExecutor)
            if (nameFile.isNotEmpty()){
                subscriber.onNext(nameFile)
                subscriber.onComplete()
            }else{
                subscriber.onError(Throwable(this.msgError))
            }

        }
    }

}
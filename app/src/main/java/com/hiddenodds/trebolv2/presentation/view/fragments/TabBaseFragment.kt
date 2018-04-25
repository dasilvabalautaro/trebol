package com.hiddenodds.trebolv2.presentation.view.fragments

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.View
import android.widget.Toast
import com.hiddenodds.trebolv2.App
import com.hiddenodds.trebolv2.dagger.PresenterModule
import com.hiddenodds.trebolv2.presentation.interfaces.ILoadDataView
import com.hiddenodds.trebolv2.presentation.model.MaintenanceModel
import com.hiddenodds.trebolv2.presentation.presenter.GetMaintenancePresenter
import com.hiddenodds.trebolv2.presentation.presenter.UpdateFieldMaintenancePresenter
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch
import javax.inject.Inject

abstract class TabBaseFragment: Fragment(), ILoadDataView {
    companion object Factory{
        var maintenanceModel: MaintenanceModel? = null
        var codeNotify: String? = null
        var messageLoad = "NO"
        var observableMessageLoad: Subject<String> = PublishSubject.create()
    }

    val Fragment.app: App
        get() = activity.application as App

    private val component by lazy { app.
            getAppComponent().plus(PresenterModule())}

    @Inject
    lateinit var getMaintenancePresenter: GetMaintenancePresenter
    @Inject
    lateinit var updateFieldMaintenancePresenter: UpdateFieldMaintenancePresenter

    protected val YES = "YES"
    protected var disposable: CompositeDisposable = CompositeDisposable()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        component.inject(this)
        observableMessageLoad
                .subscribe { messageLoad }
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getMaintenancePresenter.view = this
        updateFieldMaintenancePresenter.view = this
    }


    override fun <T> executeTask(obj: T) {
        if (obj != null){
            maintenanceModel = (obj as MaintenanceModel)
            setCodeNotification()
            messageLoad = YES
            observableMessageLoad.onNext(messageLoad)
        }
    }

    private fun setCodeNotification(){
        if (maintenanceModel!!.codeNotify.isEmpty()){
            maintenanceModel!!.codeNotify = codeNotify!!

            async {
                updateFieldMaintenancePresenter
                        .updateMaintenance(maintenanceModel!!.id,
                                "codeNotify",
                                codeNotify!!)
            }
        }
    }

    protected fun executeGetMaintenance(){
        launch(CommonPool) {
            getMaintenancePresenter.executeGet(codeNotify!!)
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        getMaintenancePresenter.destroy()
    }

    override fun <T> executeTask(objList: List<T>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showError(message: String) {
        context.toast(message)
    }

    override fun showMessage(message: String) {
        context.toast(message)
    }

    fun Context.toast(message: CharSequence) =
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}
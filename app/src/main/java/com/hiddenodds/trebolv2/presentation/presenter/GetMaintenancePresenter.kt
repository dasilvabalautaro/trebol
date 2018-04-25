package com.hiddenodds.trebolv2.presentation.presenter

import com.hiddenodds.trebolv2.R
import com.hiddenodds.trebolv2.domain.interactor.GetMaintenanceUseCase
import com.hiddenodds.trebolv2.domain.interactor.SaveMaintenanceUseCase
import com.hiddenodds.trebolv2.presentation.model.MaintenanceModel
import io.reactivex.observers.DisposableObserver
import javax.inject.Inject


class GetMaintenancePresenter @Inject constructor(private val getMaintenanceUseCase:
                                                  GetMaintenanceUseCase,
                                                  private val saveMaintenanceUseCase:
                                                  SaveMaintenanceUseCase):
        BasePresenter(){

    var codeNotify: String? = null

    fun executeGet(codeNotify: String){
        this.codeNotify = codeNotify
        getMaintenanceUseCase.codeNotify = codeNotify
        getMaintenanceUseCase.execute(GetMaintenanceObserver())
    }

    private fun executeCreate(){
        saveMaintenanceUseCase.execute(CreateMaintenanceObserver())
    }

    private fun sendMaintenance(maintenance: MaintenanceModel){
        view!!.executeTask(maintenance)
    }

    override fun destroy() {
        super.destroy()
        getMaintenanceUseCase.dispose()
        saveMaintenanceUseCase.dispose()
    }

    inner class GetMaintenanceObserver: DisposableObserver<MaintenanceModel>(){
        override fun onNext(t: MaintenanceModel) {
            sendMaintenance(t)
        }

        override fun onComplete() {
            showMessage(context.getString(R.string.lbl_get_maintenance))
        }

        override fun onError(e: Throwable) {
            executeCreate()
            if (e.message != null) {
                showError(e.message!!)
            }
        }
    }

    inner class CreateMaintenanceObserver: DisposableObserver<MaintenanceModel>(){
        override fun onNext(t: MaintenanceModel) {
            sendMaintenance(t)
        }

        override fun onComplete() {
            showMessage(context.getString(R.string.lbl_save_maintenance))
        }

        override fun onError(e: Throwable) {
            if (e.message != null) {
                showError(e.message!!)
            }
        }
    }

}
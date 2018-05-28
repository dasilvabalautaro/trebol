package com.hiddenodds.trebolv2.presentation.presenter

import com.hiddenodds.trebolv2.domain.interactor.UpdateFieldMaintenanceUseCase
import io.reactivex.observers.DisposableObserver
import javax.inject.Inject


class UpdateFieldMaintenancePresenter @Inject constructor(private val updateFieldMaintenanceUseCase:
                                                          UpdateFieldMaintenanceUseCase):
        BasePresenter() {

    fun updateMaintenance(id: String, nameFiled: String, newValue: String){
        updateFieldMaintenanceUseCase.id = id
        updateFieldMaintenanceUseCase.nameField = nameFiled
        updateFieldMaintenanceUseCase.newValue = newValue
        updateFieldMaintenanceUseCase.execute(UpdateObserver())
    }



    inner class UpdateObserver: DisposableObserver<Boolean>(){
        override fun onNext(t: Boolean) {
            println("Update : true")
        }

        override fun onComplete() {
            println("Update : Complete")
        }

        override fun onError(e: Throwable) {
            if (e.message != null) {
                showError(e.message!!)
            }
        }
    }

}
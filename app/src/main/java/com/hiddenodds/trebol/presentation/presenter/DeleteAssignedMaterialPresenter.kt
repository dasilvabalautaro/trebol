package com.hiddenodds.trebol.presentation.presenter

import com.hiddenodds.trebol.R
import com.hiddenodds.trebol.domain.interactor.DeleteAssignedMaterialUseCase
import io.reactivex.observers.DisposableObserver
import javax.inject.Inject


class DeleteAssignedMaterialPresenter @Inject constructor(private val deleteAssignedMaterialUseCase:
                                                          DeleteAssignedMaterialUseCase):
        BasePresenter() {

    fun deleteAssignedMaterial(idNotification: String,
                               idAssigned: String, flagUse: Boolean){
        deleteAssignedMaterialUseCase.idNotification = idNotification
        deleteAssignedMaterialUseCase.idAssigned = idAssigned
        deleteAssignedMaterialUseCase.flagUse = flagUse
        deleteAssignedMaterialUseCase.execute(DeleteObserver())
    }

    private fun messageChange(){
        showMessage(context.getString(R.string.change_field))
    }


    inner class DeleteObserver: DisposableObserver<Boolean>(){
        override fun onNext(t: Boolean) {
            messageChange()
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
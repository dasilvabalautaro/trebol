package com.hiddenodds.trebol.presentation.presenter

import com.hiddenodds.trebol.R
import com.hiddenodds.trebol.domain.interactor.UpdateAssignedMaterialUseCase
import io.reactivex.observers.DisposableObserver
import javax.inject.Inject


class UpdateAssignedMaterialPresenter @Inject constructor(private val updateAssignedMaterialUseCase:
                                                          UpdateAssignedMaterialUseCase):
        BasePresenter(){

    fun updateAssignedMaterial(id: String, quantity: Int){
        updateAssignedMaterialUseCase.idAssignedMaterial = id
        updateAssignedMaterialUseCase.quantity = quantity
        updateAssignedMaterialUseCase.execute(UpdateObserver())
    }

    private fun messageChange(){
        showMessage(context.getString(R.string.change_field))
    }


    inner class UpdateObserver: DisposableObserver<Boolean>(){
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
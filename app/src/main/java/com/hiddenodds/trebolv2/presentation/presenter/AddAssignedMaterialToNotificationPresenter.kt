package com.hiddenodds.trebolv2.presentation.presenter

import com.hiddenodds.trebolv2.domain.interactor.AddAssignedMaterialToNotificationUseCase
import com.hiddenodds.trebolv2.presentation.model.AssignedMaterialModel
import io.reactivex.observers.DisposableObserver
import javax.inject.Inject


class AddAssignedMaterialToNotificationPresenter @Inject constructor(private val addAssignedMaterialToNotificationUseCase:
                                                                     AddAssignedMaterialToNotificationUseCase){

    fun addAsignedMaterial(list: ArrayList<AssignedMaterialModel>,
                           id: String, flagUse: Boolean){
        addAssignedMaterialToNotificationUseCase.listAssignedMaterial = list
        addAssignedMaterialToNotificationUseCase.idNotification = id
        addAssignedMaterialToNotificationUseCase.flagUse = flagUse
        addAssignedMaterialToNotificationUseCase.execute(UpdateObserver())
    }


    inner class UpdateObserver: DisposableObserver<Boolean>(){
        override fun onNext(t: Boolean) {
            println("Add Assigned Material to Notification Complete")
        }

        override fun onComplete() {
            println("Update : Complete")
        }

        override fun onError(e: Throwable) {
            if (e.message != null) {
                println(e.message!!)
            }
        }
    }
}
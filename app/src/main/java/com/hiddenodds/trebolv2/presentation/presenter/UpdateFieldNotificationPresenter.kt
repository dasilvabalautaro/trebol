package com.hiddenodds.trebolv2.presentation.presenter

import com.hiddenodds.trebolv2.R
import com.hiddenodds.trebolv2.domain.interactor.UpdateFieldNotificationUseCase
import io.reactivex.observers.DisposableObserver
import javax.inject.Inject


class UpdateFieldNotificationPresenter @Inject constructor(private val updateFieldNotificationUseCase:
                                                           UpdateFieldNotificationUseCase):
        BasePresenter(){

    fun updateNotification(id: String, nameFiled: String, newValue: String){
        updateFieldNotificationUseCase.id = id
        updateFieldNotificationUseCase.nameField = nameFiled
        updateFieldNotificationUseCase.newValue = newValue
        updateFieldNotificationUseCase.execute(UpdateObserver())
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
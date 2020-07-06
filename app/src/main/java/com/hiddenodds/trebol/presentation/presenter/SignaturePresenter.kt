package com.hiddenodds.trebol.presentation.presenter

import com.hiddenodds.trebol.R
import com.hiddenodds.trebol.domain.interactor.GetFileSignatureUseCase
import io.reactivex.observers.DisposableObserver
import javax.inject.Inject


class SignaturePresenter @Inject constructor(private val
                                             getFileSignatureUseCase:
                                             GetFileSignatureUseCase):
        BasePresenter(){
    fun executeGetNameFile(nameClient: String){
        getFileSignatureUseCase.nameClient = nameClient
        getFileSignatureUseCase.execute(SignatureObserver())
    }

    fun sendNameFile(nameFile: String){
        view!!.executeTask(nameFile)
    }

    override fun destroy() {
        super.destroy()
        getFileSignatureUseCase.dispose()
    }

    inner class SignatureObserver: DisposableObserver<String>(){
        override fun onNext(t: String) {
            sendNameFile(t)
        }

        override fun onComplete() {
            println(context.resources.getString(R.string.task_complete))

        }

        override fun onError(e: Throwable) {
            if (e.message != null) {
                showError(e.message!!)
            }
        }
    }
}
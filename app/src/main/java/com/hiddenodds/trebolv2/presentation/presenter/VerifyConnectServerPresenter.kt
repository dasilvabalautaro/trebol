package com.hiddenodds.trebolv2.presentation.presenter

import com.hiddenodds.trebolv2.R
import com.hiddenodds.trebolv2.domain.interactor.VerifyConnectServerUseCase
import io.reactivex.observers.DisposableObserver
import javax.inject.Inject

class VerifyConnectServerPresenter @Inject constructor(private val verifyConnectServerUseCase:
                                                       VerifyConnectServerUseCase):
        BasePresenter() {

    fun verifyConnect(){
        verifyConnectServerUseCase.execute(VerifyConnectObserver())
    }

    private fun messageConnectOK(){
        view!!.showMessage(context.getString(R.string.lbl_connect_server))
    }

    override fun destroy() {
        super.destroy()
        verifyConnectServerUseCase.dispose()
    }

    inner class VerifyConnectObserver: DisposableObserver<Boolean>(){
        override fun onNext(t: Boolean) {
            if (t){
                messageConnectOK()
            }else{
                showError(context.getString(R.string.lbl_not_connect_server))
            }
        }

        override fun onComplete() {
            println("Verify : Complete")
        }

        override fun onError(e: Throwable) {
            if (e.message != null) {
                showError(e.message!!)
            }
        }
    }
}
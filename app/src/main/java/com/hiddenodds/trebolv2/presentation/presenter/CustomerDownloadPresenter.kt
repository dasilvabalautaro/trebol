package com.hiddenodds.trebolv2.presentation.presenter

import com.hiddenodds.trebolv2.domain.interactor.DownloadCustomerUseCase
import com.hiddenodds.trebolv2.tools.Variables
import io.reactivex.android.schedulers.AndroidSchedulers
import javax.inject.Inject

class CustomerDownloadPresenter @Inject constructor(private val downloadCustomerUseCase:
                                                    DownloadCustomerUseCase):
        BasePresenter() {

    var flag = false

    init {
        val hearMessageError = downloadCustomerUseCase
                .observableMessageError.map { m -> m }
        disposable.add(hearMessageError.observeOn(AndroidSchedulers.mainThread())
                .subscribe { s ->
                    flag = true
                    showError(s)
                    downloadCustomerUseCase.destroy()
                })
        val success = downloadCustomerUseCase
                .observableFinishDownload.map { f -> f }
        disposable.add(success.observeOn(AndroidSchedulers.mainThread())
                .subscribe { f ->
                    kotlin.run {
                        if (f){
                            if (!flag){
                                view!!.executeTask(2)
                            }else{
                                view!!.executeTask(6)
                            }
                            downloadCustomerUseCase.destroy()
                        }
                    }

                })
    }

    fun executeDownloadCustomer(){
        downloadCustomerUseCase.listTechnicals = ArrayList(Variables.listTechnicals)
        downloadCustomerUseCase.listTechnicals.add(Variables.codeTechMaster)
        downloadCustomerUseCase.getCustomersNextTechnical()
    }
}
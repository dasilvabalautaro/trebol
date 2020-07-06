package com.hiddenodds.trebol.presentation.presenter

import com.hiddenodds.trebol.domain.interactor.DownloadNotificationUseCase
import com.hiddenodds.trebol.tools.Variables
import io.reactivex.android.schedulers.AndroidSchedulers
import javax.inject.Inject


class NotificationDownloadPresenter @Inject constructor(private val downloadNotificationUseCase:
                                                        DownloadNotificationUseCase):
        BasePresenter(){

    var flag = false

    init {
        val hearMessageError = downloadNotificationUseCase
                .observableMessageError.map { m -> m }
        disposable.add(hearMessageError.observeOn(AndroidSchedulers.mainThread())
                .subscribe { s ->
                    flag = true
                    showError(s)
                    //downloadNotificationUseCase.destroy()
                })
        val success = downloadNotificationUseCase
                .observableFinishDownload.map { f -> f }
        disposable.add(success.observeOn(AndroidSchedulers.mainThread())
                .subscribe { f ->
                    kotlin.run {
                        if (f){
                            if (!flag){
                                view!!.executeTask(1)
                            }else{
                                view!!.executeTask(6)
                            }
                            //downloadNotificationUseCase.destroy()
                        }
                    }

                })

    }

    fun executeDownloadNotification(){
        downloadNotificationUseCase.listTechnicals = ArrayList(Variables.listTechnicals)
        downloadNotificationUseCase.listTechnicals.add(Variables.codeTechMaster)
        downloadNotificationUseCase.executeDeleteDownload()
    }

    override fun destroy() {
        super.destroy()
        downloadNotificationUseCase.destroy()
    }
}
package com.hiddenodds.trebolv2.domain.interactor

import com.hiddenodds.trebolv2.App
import com.hiddenodds.trebolv2.model.interfaces.IPostExecutionThread
import com.hiddenodds.trebolv2.model.interfaces.IThreadExecutor
import com.hiddenodds.trebolv2.presentation.model.NotificationModel
import io.reactivex.Observable
import javax.inject.Inject

class SetRemoteNotificationDataUseCase @Inject constructor(threadExecutor: IThreadExecutor,
                                                           postExecutionThread: IPostExecutionThread):
        UseCase<Boolean>(threadExecutor, postExecutionThread){

    var notification: NotificationModel? = null

    override fun buildUseCaseObservable(): Observable<Boolean> {
        return (App.appComponent.context() as App)
                .serviceRemote.setDataNotificationObservable(notification!!)
    }

}
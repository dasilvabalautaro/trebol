package com.hiddenodds.trebol.domain.interactor

import com.hiddenodds.trebol.App
import com.hiddenodds.trebol.model.interfaces.IPostExecutionThread
import com.hiddenodds.trebol.model.interfaces.IThreadExecutor
import com.hiddenodds.trebol.presentation.model.NotificationModel
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
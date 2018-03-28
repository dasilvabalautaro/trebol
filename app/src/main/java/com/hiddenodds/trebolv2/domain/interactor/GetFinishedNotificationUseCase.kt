package com.hiddenodds.trebolv2.domain.interactor

import com.hiddenodds.trebolv2.model.interfaces.INotificationRepository
import com.hiddenodds.trebolv2.model.interfaces.IPostExecutionThread
import com.hiddenodds.trebolv2.model.interfaces.IThreadExecutor
import com.hiddenodds.trebolv2.presentation.model.NotificationModel
import io.reactivex.Observable
import javax.inject.Inject


class GetFinishedNotificationUseCase @Inject constructor(threadExecutor: IThreadExecutor,
                                                         postExecutionThread: IPostExecutionThread,
                                                         private var iNotificationRepository:
                                                         INotificationRepository):
        UseCase<List<NotificationModel>>(threadExecutor, postExecutionThread){

    override fun buildUseCaseObservable(): Observable<List<NotificationModel>> {
        return iNotificationRepository.getFinishedNotification()
    }

}
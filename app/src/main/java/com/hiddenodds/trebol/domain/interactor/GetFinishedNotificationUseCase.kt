package com.hiddenodds.trebol.domain.interactor

import com.hiddenodds.trebol.model.interfaces.INotificationRepository
import com.hiddenodds.trebol.model.interfaces.IPostExecutionThread
import com.hiddenodds.trebol.model.interfaces.IThreadExecutor
import com.hiddenodds.trebol.presentation.model.NotificationModel
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
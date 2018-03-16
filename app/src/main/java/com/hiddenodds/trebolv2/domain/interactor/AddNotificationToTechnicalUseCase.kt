package com.hiddenodds.trebolv2.domain.interactor

import com.hiddenodds.trebolv2.model.interfaces.INotificationRepository
import com.hiddenodds.trebolv2.model.interfaces.IPostExecutionThread
import com.hiddenodds.trebolv2.model.interfaces.IThreadExecutor
import io.reactivex.Observable
import javax.inject.Inject


class AddNotificationToTechnicalUseCase @Inject constructor(threadExecutor: IThreadExecutor,
                                                            postExecutionThread: IPostExecutionThread,
                                                            private var iNotificationRepository:
                                                            INotificationRepository):
        UseCase<Boolean>(threadExecutor, postExecutionThread){

    var codeTech: String = ""

    override fun buildUseCaseObservable(): Observable<Boolean> {
        return iNotificationRepository.addNotificationsToTechnical(codeTech)
    }

}
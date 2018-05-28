package com.hiddenodds.trebol.domain.interactor

import com.hiddenodds.trebol.model.interfaces.INotificationRepository
import com.hiddenodds.trebol.model.interfaces.IPostExecutionThread
import com.hiddenodds.trebol.model.interfaces.IThreadExecutor
import io.reactivex.Observable
import javax.inject.Inject


class UpdateFieldNotificationUseCase @Inject constructor(threadExecutor: IThreadExecutor,
                                                         postExecutionThread: IPostExecutionThread,
                                                         private var iNotificationRepository:
                                                         INotificationRepository):
        UseCase<Boolean>(threadExecutor, postExecutionThread){

    var id: String = ""
    var nameField: String = ""
    var newValue: String = ""

    override fun buildUseCaseObservable(): Observable<Boolean> {
        return iNotificationRepository.updateField(id, nameField, newValue)
    }

}
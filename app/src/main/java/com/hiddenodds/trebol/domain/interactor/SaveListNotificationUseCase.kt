package com.hiddenodds.trebol.domain.interactor

import com.hiddenodds.trebol.domain.data.MapperNotification
import com.hiddenodds.trebol.model.interfaces.INotificationRepository
import com.hiddenodds.trebol.model.interfaces.IPostExecutionThread
import com.hiddenodds.trebol.model.interfaces.IThreadExecutor
import io.reactivex.Observable
import javax.inject.Inject


class SaveListNotificationUseCase @Inject constructor(threadExecutor: IThreadExecutor,
                                                      postExecutionThread: IPostExecutionThread,
                                                      private var iNotificationRepository:
                                                      INotificationRepository):
        UseCase<Boolean>(threadExecutor, postExecutionThread){

    var listMapperNotification: ArrayList<MapperNotification> = ArrayList()

    override fun buildUseCaseObservable(): Observable<Boolean> {
        return iNotificationRepository.saveList(listMapperNotification)
    }

}
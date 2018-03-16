package com.hiddenodds.trebolv2.domain.interactor

import com.hiddenodds.trebolv2.domain.data.MapperNotification
import com.hiddenodds.trebolv2.model.interfaces.INotificationRepository
import com.hiddenodds.trebolv2.model.interfaces.IPostExecutionThread
import com.hiddenodds.trebolv2.model.interfaces.IThreadExecutor
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
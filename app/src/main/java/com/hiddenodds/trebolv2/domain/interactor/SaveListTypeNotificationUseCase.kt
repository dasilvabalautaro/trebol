package com.hiddenodds.trebolv2.domain.interactor

import com.hiddenodds.trebolv2.domain.data.MapperTypeNotification
import com.hiddenodds.trebolv2.model.interfaces.IPostExecutionThread
import com.hiddenodds.trebolv2.model.interfaces.IThreadExecutor
import com.hiddenodds.trebolv2.model.interfaces.ITypeNotificationRepository
import io.reactivex.Observable
import javax.inject.Inject


class SaveListTypeNotificationUseCase @Inject constructor(threadExecutor: IThreadExecutor,
                                                          postExecutionThread: IPostExecutionThread,
                                                          private var iTypeNotificationRepository:
                                                          ITypeNotificationRepository):
        UseCase<Boolean>(threadExecutor, postExecutionThread){

    var listMapperTypeNotification: ArrayList<MapperTypeNotification> = ArrayList()

    override fun buildUseCaseObservable(): Observable<Boolean> {
        return iTypeNotificationRepository.saveList(listMapperTypeNotification)
    }


}
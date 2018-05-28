package com.hiddenodds.trebol.domain.interactor

import com.hiddenodds.trebol.domain.data.MapperTypeNotification
import com.hiddenodds.trebol.model.interfaces.IPostExecutionThread
import com.hiddenodds.trebol.model.interfaces.IThreadExecutor
import com.hiddenodds.trebol.model.interfaces.ITypeNotificationRepository
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
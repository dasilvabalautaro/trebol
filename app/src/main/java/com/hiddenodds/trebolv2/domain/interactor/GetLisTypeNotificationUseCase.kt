package com.hiddenodds.trebolv2.domain.interactor

import com.hiddenodds.trebolv2.model.interfaces.IPostExecutionThread
import com.hiddenodds.trebolv2.model.interfaces.IThreadExecutor
import com.hiddenodds.trebolv2.model.interfaces.ITypeNotificationRepository
import com.hiddenodds.trebolv2.presentation.model.TypeNotificationModel
import io.reactivex.Observable
import javax.inject.Inject

class GetLisTypeNotificationUseCase @Inject constructor(threadExecutor: IThreadExecutor,
                                                        postExecutionThread: IPostExecutionThread,
                                                        private var iTypeNotificationRepository:
                                                        ITypeNotificationRepository):
        UseCase<List<TypeNotificationModel>>(threadExecutor, postExecutionThread){
    override fun buildUseCaseObservable(): Observable<List<TypeNotificationModel>> {
        return iTypeNotificationRepository.getList()
    }

}
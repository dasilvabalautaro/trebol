package com.hiddenodds.trebol.domain.interactor

import com.hiddenodds.trebol.model.interfaces.IPostExecutionThread
import com.hiddenodds.trebol.model.interfaces.IThreadExecutor
import com.hiddenodds.trebol.model.interfaces.ITypeNotificationRepository
import com.hiddenodds.trebol.presentation.model.TypeNotificationModel
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
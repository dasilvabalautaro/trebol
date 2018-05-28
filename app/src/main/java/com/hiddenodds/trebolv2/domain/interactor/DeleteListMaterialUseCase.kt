package com.hiddenodds.trebolv2.domain.interactor

import com.hiddenodds.trebolv2.model.interfaces.IMaterialRepository
import com.hiddenodds.trebolv2.model.interfaces.IPostExecutionThread
import com.hiddenodds.trebolv2.model.interfaces.IThreadExecutor
import io.reactivex.Observable
import javax.inject.Inject

class DeleteListMaterialUseCase @Inject constructor(threadExecutor: IThreadExecutor,
                                                    postExecutionThread: IPostExecutionThread,
                                                    private var iMaterialRepository:
                                                    IMaterialRepository):
        UseCase<Boolean>(threadExecutor, postExecutionThread) {
    override fun buildUseCaseObservable(): Observable<Boolean> {
        return iMaterialRepository.deleteList()
    }

}
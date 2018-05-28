package com.hiddenodds.trebolv2.domain.interactor

import com.hiddenodds.trebolv2.model.interfaces.IAssignedMaterialRepository
import com.hiddenodds.trebolv2.model.interfaces.IPostExecutionThread
import com.hiddenodds.trebolv2.model.interfaces.IThreadExecutor
import io.reactivex.Observable
import javax.inject.Inject


class DeleteAssignedMaterialUseCase @Inject constructor(threadExecutor: IThreadExecutor,
                                                        postExecutionThread: IPostExecutionThread,
                                                        private var iAssignedMaterialRepository:
                                                        IAssignedMaterialRepository):
        UseCase<Boolean>(threadExecutor, postExecutionThread){

    var idNotification: String = ""
    var idAssigned: String = ""
    var flagUse: Boolean = false

    override fun buildUseCaseObservable(): Observable<Boolean> {
        return iAssignedMaterialRepository.deleteAssignedMaterial(idNotification,
                idAssigned, flagUse)
    }


}
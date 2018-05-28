package com.hiddenodds.trebolv2.domain.interactor

import com.hiddenodds.trebolv2.model.interfaces.IAssignedMaterialRepository
import com.hiddenodds.trebolv2.model.interfaces.IPostExecutionThread
import com.hiddenodds.trebolv2.model.interfaces.IThreadExecutor
import io.reactivex.Observable
import javax.inject.Inject


class UpdateAssignedMaterialUseCase @Inject constructor(threadExecutor: IThreadExecutor,
                                                        postExecutionThread: IPostExecutionThread,
                                                        private var iAssignedMaterialRepository:
                                                        IAssignedMaterialRepository):
        UseCase<Boolean>(threadExecutor, postExecutionThread){

    var idAssignedMaterial: String = ""
    var quantity: Int = 0

    override fun buildUseCaseObservable(): Observable<Boolean> {
        return iAssignedMaterialRepository.updatedAssigned(idAssignedMaterial,
                quantity)
    }

}
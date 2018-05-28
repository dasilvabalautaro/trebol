package com.hiddenodds.trebol.domain.interactor

import com.hiddenodds.trebol.model.interfaces.IAssignedMaterialRepository
import com.hiddenodds.trebol.model.interfaces.IPostExecutionThread
import com.hiddenodds.trebol.model.interfaces.IThreadExecutor
import com.hiddenodds.trebol.presentation.model.AssignedMaterialModel
import io.reactivex.Observable
import javax.inject.Inject


class AddAssignedMaterialToNotificationUseCase @Inject constructor(threadExecutor: IThreadExecutor,
                                                                   postExecutionThread: IPostExecutionThread,
                                                                   private var iAssignedMaterialRepository:
                                                                   IAssignedMaterialRepository):
        UseCase<Boolean>(threadExecutor, postExecutionThread){

    var listAssignedMaterial: ArrayList<AssignedMaterialModel> = ArrayList()
    var idNotification: String = ""
    var flagUse: Boolean = false

    override fun buildUseCaseObservable(): Observable<Boolean> {
        return iAssignedMaterialRepository.addListAssignedMaterialToNotification(listAssignedMaterial,
                idNotification, flagUse)
    }

}
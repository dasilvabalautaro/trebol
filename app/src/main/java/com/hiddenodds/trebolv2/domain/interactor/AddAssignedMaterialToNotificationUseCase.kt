package com.hiddenodds.trebolv2.domain.interactor

import com.hiddenodds.trebolv2.model.interfaces.IAssignedMaterialRepository
import com.hiddenodds.trebolv2.model.interfaces.IPostExecutionThread
import com.hiddenodds.trebolv2.model.interfaces.IThreadExecutor
import com.hiddenodds.trebolv2.presentation.model.AssignedMaterialModel
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
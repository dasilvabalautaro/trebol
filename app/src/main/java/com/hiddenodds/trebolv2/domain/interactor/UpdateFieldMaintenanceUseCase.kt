package com.hiddenodds.trebolv2.domain.interactor

import com.hiddenodds.trebolv2.model.interfaces.IMaintenanceRepository
import com.hiddenodds.trebolv2.model.interfaces.IPostExecutionThread
import com.hiddenodds.trebolv2.model.interfaces.IThreadExecutor
import io.reactivex.Observable
import javax.inject.Inject

class UpdateFieldMaintenanceUseCase @Inject constructor(threadExecutor: IThreadExecutor,
                                                        postExecutionThread: IPostExecutionThread,
                                                        private var iMaintenanceRepository:
                                                        IMaintenanceRepository):
        UseCase<Boolean>(threadExecutor, postExecutionThread){
    var id: String = ""
    var nameField: String = ""
    var newValue: String = ""

    override fun buildUseCaseObservable(): Observable<Boolean> {
        return iMaintenanceRepository.update(id, nameField, newValue)
    }
}
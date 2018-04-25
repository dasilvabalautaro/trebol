package com.hiddenodds.trebolv2.domain.interactor

import com.hiddenodds.trebolv2.model.interfaces.IMaintenanceRepository
import com.hiddenodds.trebolv2.model.interfaces.IPostExecutionThread
import com.hiddenodds.trebolv2.model.interfaces.IThreadExecutor
import com.hiddenodds.trebolv2.presentation.model.MaintenanceModel
import io.reactivex.Observable
import javax.inject.Inject


class SaveMaintenanceUseCase @Inject constructor(threadExecutor: IThreadExecutor,
                                                 postExecutionThread: IPostExecutionThread,
                                                 private var iMaintenanceRepository:
                                                 IMaintenanceRepository):
        UseCase<MaintenanceModel>(threadExecutor, postExecutionThread){
    override fun buildUseCaseObservable(): Observable<MaintenanceModel> {
        return iMaintenanceRepository.save()
    }

}
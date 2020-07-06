package com.hiddenodds.trebol.domain.interactor

import com.hiddenodds.trebol.model.interfaces.IMaintenanceRepository
import com.hiddenodds.trebol.model.interfaces.IPostExecutionThread
import com.hiddenodds.trebol.model.interfaces.IThreadExecutor
import com.hiddenodds.trebol.presentation.model.MaintenanceModel
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
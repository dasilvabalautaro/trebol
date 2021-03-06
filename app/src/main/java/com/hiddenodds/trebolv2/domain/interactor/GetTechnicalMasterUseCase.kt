package com.hiddenodds.trebolv2.domain.interactor

import com.hiddenodds.trebolv2.model.interfaces.IPostExecutionThread
import com.hiddenodds.trebolv2.model.interfaces.ITechnicalRepository
import com.hiddenodds.trebolv2.model.interfaces.IThreadExecutor
import com.hiddenodds.trebolv2.presentation.model.TechnicalModel
import io.reactivex.Observable
import javax.inject.Inject

class GetTechnicalMasterUseCase @Inject constructor(threadExecutor: IThreadExecutor,
                                                    postExecutionThread: IPostExecutionThread,
                                                    private var iTechnicalRepository:
                                                    ITechnicalRepository):
        UseCase<TechnicalModel>(threadExecutor, postExecutionThread){

    var code: String = ""
    var password: String = ""

    override fun buildUseCaseObservable(): Observable<TechnicalModel> {
        return iTechnicalRepository.getMasterTechnical(code, password)
    }

}
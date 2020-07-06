package com.hiddenodds.trebol.domain.interactor

import com.hiddenodds.trebol.model.interfaces.IPostExecutionThread
import com.hiddenodds.trebol.model.interfaces.ITechnicalRepository
import com.hiddenodds.trebol.model.interfaces.IThreadExecutor
import com.hiddenodds.trebol.presentation.model.TechnicalModel
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
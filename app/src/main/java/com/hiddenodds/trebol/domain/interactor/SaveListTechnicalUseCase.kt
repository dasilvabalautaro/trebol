package com.hiddenodds.trebol.domain.interactor

import com.hiddenodds.trebol.domain.data.MapperTechnical
import com.hiddenodds.trebol.model.interfaces.IPostExecutionThread
import com.hiddenodds.trebol.model.interfaces.ITechnicalRepository
import com.hiddenodds.trebol.model.interfaces.IThreadExecutor
import io.reactivex.Observable
import javax.inject.Inject

class SaveListTechnicalUseCase @Inject constructor(threadExecutor: IThreadExecutor,
                                                   postExecutionThread: IPostExecutionThread,
                                                   private var iTechnicalRepository:
                                                   ITechnicalRepository):
        UseCase<Boolean>(threadExecutor, postExecutionThread) {

    var listMapperTechnical: ArrayList<MapperTechnical> = ArrayList()

    override fun buildUseCaseObservable(): Observable<Boolean> {
        return iTechnicalRepository.saveList(listMapperTechnical)
    }

}
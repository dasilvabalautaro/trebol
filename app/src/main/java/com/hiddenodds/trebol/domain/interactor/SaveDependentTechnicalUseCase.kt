package com.hiddenodds.trebol.domain.interactor

import com.hiddenodds.trebol.model.interfaces.IPostExecutionThread
import com.hiddenodds.trebol.model.interfaces.ITechnicalRepository
import com.hiddenodds.trebol.model.interfaces.IThreadExecutor
import io.reactivex.Observable
import java.util.*
import javax.inject.Inject

class SaveDependentTechnicalUseCase @Inject constructor(threadExecutor: IThreadExecutor,
                                                        postExecutionThread: IPostExecutionThread,
                                                        private var iTechnicalRepository:
                                                        ITechnicalRepository):
        UseCase<Boolean>(threadExecutor, postExecutionThread){

    var dependentTechnicians: LinkedHashMap<String,
            ArrayList<String>> = LinkedHashMap()

    override fun buildUseCaseObservable(): Observable<Boolean> {
        return iTechnicalRepository
                .saveListDependentTechnicians(dependentTechnicians)
    }

}

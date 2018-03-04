package com.hiddenodds.trebolv2.domain.interactor

import com.hiddenodds.trebolv2.model.interfaces.IPostExecutionThread
import com.hiddenodds.trebolv2.model.interfaces.ITechnicalRepository
import com.hiddenodds.trebolv2.model.interfaces.IThreadExecutor
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

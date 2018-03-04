package com.hiddenodds.trebolv2.domain.interactor

import com.hiddenodds.trebolv2.domain.data.MapperTechnical
import com.hiddenodds.trebolv2.domain.interfaces.IHearMessage
import com.hiddenodds.trebolv2.model.exception.DatabaseOperationException
import com.hiddenodds.trebolv2.model.interfaces.IPostExecutionThread
import com.hiddenodds.trebolv2.model.interfaces.ITechnicalRepository
import com.hiddenodds.trebolv2.model.interfaces.IThreadExecutor
import io.reactivex.Observable
import javax.inject.Inject

class SaveListTechnicalUseCase @Inject constructor(threadExecutor: IThreadExecutor,
                                                   postExecutionThread: IPostExecutionThread,
                                                   private var iTechnicalRepository:
                                                   ITechnicalRepository):
        UseCase<Boolean>(threadExecutor, postExecutionThread),
        IHearMessage {

    var listMapperTechnical: ArrayList<MapperTechnical> = ArrayList()

    override fun hearMessage(): Observable<String> {
        return iTechnicalRepository.userGetMessage()
    }

    override fun hearError(): Observable<DatabaseOperationException> {
        return iTechnicalRepository.userGetError()
    }

    override fun buildUseCaseObservable(): Observable<Boolean> {
        return iTechnicalRepository.saveList(listMapperTechnical)
    }

}
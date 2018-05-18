package com.hiddenodds.trebolv2.domain.interactor

import com.hiddenodds.trebolv2.model.interfaces.IPostExecutionThread
import com.hiddenodds.trebolv2.model.interfaces.ISignatureRepository
import com.hiddenodds.trebolv2.model.interfaces.IThreadExecutor
import io.reactivex.Observable
import javax.inject.Inject

class GetFileSignatureUseCase @Inject constructor(threadExecutor: IThreadExecutor,
                                                  postExecutionThread:
                                                  IPostExecutionThread,
                                                  private var iSignatureRepository:
                                                  ISignatureRepository):
        UseCase<String>(threadExecutor, postExecutionThread) {

    var nameClient: String = ""

    override fun buildUseCaseObservable(): Observable<String> {
       return iSignatureRepository.getSignature(nameClient)
    }

}
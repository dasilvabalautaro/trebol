package com.hiddenodds.trebol.domain.interactor

import com.hiddenodds.trebol.model.interfaces.IDownloadRepository
import com.hiddenodds.trebol.model.interfaces.IPostExecutionThread
import com.hiddenodds.trebol.model.interfaces.IThreadExecutor
import io.reactivex.Observable
import javax.inject.Inject


class UpdateDownloadUseCase @Inject constructor(threadExecutor: IThreadExecutor,
                                                postExecutionThread:
                                                IPostExecutionThread,
                                                private var iDownloadRepository:
                                                IDownloadRepository):
        UseCase<Boolean>(threadExecutor, postExecutionThread){
    var fieldName: String = ""
    var value: String = ""
    var code: String = ""

    override fun buildUseCaseObservable(): Observable<Boolean> {
        return iDownloadRepository.update(code, fieldName, value )
    }


}
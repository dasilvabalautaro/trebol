package com.hiddenodds.trebolv2.domain.interactor

import com.hiddenodds.trebolv2.model.interfaces.IDownloadRepository
import com.hiddenodds.trebolv2.model.interfaces.IPostExecutionThread
import com.hiddenodds.trebolv2.model.interfaces.IThreadExecutor
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
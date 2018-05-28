package com.hiddenodds.trebolv2.domain.interactor

import com.hiddenodds.trebolv2.model.interfaces.IDownloadRepository
import com.hiddenodds.trebolv2.model.interfaces.IPostExecutionThread
import com.hiddenodds.trebolv2.model.interfaces.IThreadExecutor
import io.reactivex.Observable
import javax.inject.Inject

class DeleteDownloadsUseCase @Inject constructor(threadExecutor: IThreadExecutor,
                                                 postExecutionThread:
                                                 IPostExecutionThread,
                                                 private var iDownloadRepository:
                                                 IDownloadRepository):
        UseCase<Boolean>(threadExecutor, postExecutionThread){
    var listTechnical: ArrayList<String> = ArrayList()
    override fun buildUseCaseObservable(): Observable<Boolean> {
       return iDownloadRepository.delete(listTechnical)
    }
}
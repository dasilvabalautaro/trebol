package com.hiddenodds.trebolv2.domain.interactor

import com.hiddenodds.trebolv2.model.interfaces.IDownloadRepository
import com.hiddenodds.trebolv2.model.interfaces.IPostExecutionThread
import com.hiddenodds.trebolv2.model.interfaces.IThreadExecutor
import com.hiddenodds.trebolv2.presentation.model.DownloadModel
import io.reactivex.Observable
import javax.inject.Inject

class GetDownloadUseCase @Inject constructor(threadExecutor: IThreadExecutor,
                                             postExecutionThread:
                                             IPostExecutionThread,
                                             private var iDownloadRepository:
                                             IDownloadRepository):
        UseCase<DownloadModel>(threadExecutor, postExecutionThread){
    var code: String = ""

    override fun buildUseCaseObservable(): Observable<DownloadModel> {
        return iDownloadRepository.getDownload(code)
    }

}
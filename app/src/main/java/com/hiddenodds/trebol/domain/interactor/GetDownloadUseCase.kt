package com.hiddenodds.trebol.domain.interactor

import com.hiddenodds.trebol.model.interfaces.IDownloadRepository
import com.hiddenodds.trebol.model.interfaces.IPostExecutionThread
import com.hiddenodds.trebol.model.interfaces.IThreadExecutor
import com.hiddenodds.trebol.presentation.model.DownloadModel
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
package com.hiddenodds.trebol.domain.interactor

import com.hiddenodds.trebol.domain.data.MapperDownload
import com.hiddenodds.trebol.model.interfaces.IDownloadRepository
import com.hiddenodds.trebol.model.interfaces.IPostExecutionThread
import com.hiddenodds.trebol.model.interfaces.IThreadExecutor
import io.reactivex.Observable
import javax.inject.Inject


class CreateDownloadsUseCase @Inject constructor(threadExecutor: IThreadExecutor,
                                                 postExecutionThread:
                                                 IPostExecutionThread,
                                                 private var iDownloadRepository:
                                                 IDownloadRepository):
        UseCase<Boolean>(threadExecutor, postExecutionThread){

    var listMapperDownload: ArrayList<MapperDownload> = ArrayList()

    override fun buildUseCaseObservable(): Observable<Boolean> {
        return iDownloadRepository.create(listMapperDownload)
    }


}
package com.hiddenodds.trebolv2.domain.interactor

import com.hiddenodds.trebolv2.domain.data.MapperDownload
import com.hiddenodds.trebolv2.model.interfaces.IDownloadRepository
import com.hiddenodds.trebolv2.model.interfaces.IPostExecutionThread
import com.hiddenodds.trebolv2.model.interfaces.IThreadExecutor
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
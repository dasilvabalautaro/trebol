package com.hiddenodds.trebolv2.domain.interactor

import com.hiddenodds.trebolv2.App
import com.hiddenodds.trebolv2.model.interfaces.IPostExecutionThread
import com.hiddenodds.trebolv2.model.interfaces.IThreadExecutor
import io.reactivex.Observable
import org.json.JSONArray
import javax.inject.Inject


class GetRemoteDataUseCase @Inject constructor(threadExecutor: IThreadExecutor,
                                               postExecutionThread: IPostExecutionThread):
        UseCase<JSONArray>(threadExecutor, postExecutionThread) {

    var sql: String = ""

    override fun buildUseCaseObservable(): Observable<JSONArray> {
        return (App.appComponent.context() as App)
                .serviceRemote.getDataObservable(sql)
    }

}
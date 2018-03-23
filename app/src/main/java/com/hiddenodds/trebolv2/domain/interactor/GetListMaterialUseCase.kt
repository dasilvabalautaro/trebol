package com.hiddenodds.trebolv2.domain.interactor

import com.hiddenodds.trebolv2.model.data.Material
import com.hiddenodds.trebolv2.model.interfaces.IMaterialRepository
import com.hiddenodds.trebolv2.model.interfaces.IPostExecutionThread
import com.hiddenodds.trebolv2.model.interfaces.IThreadExecutor
import io.reactivex.Observable
import io.realm.RealmResults
import javax.inject.Inject


class GetListMaterialUseCase @Inject constructor(threadExecutor: IThreadExecutor,
                                                 postExecutionThread: IPostExecutionThread,
                                                 private var iMaterialRepository:
                                                 IMaterialRepository):
        UseCase<RealmResults<Material>>(threadExecutor, postExecutionThread){
    override fun buildUseCaseObservable(): Observable<RealmResults<Material>> {
        return iMaterialRepository.getList()
    }

}
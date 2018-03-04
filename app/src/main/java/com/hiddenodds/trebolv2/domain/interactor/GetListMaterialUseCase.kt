package com.hiddenodds.trebolv2.domain.interactor

import com.hiddenodds.trebolv2.model.interfaces.IMaterialRepository
import com.hiddenodds.trebolv2.model.interfaces.IPostExecutionThread
import com.hiddenodds.trebolv2.model.interfaces.IThreadExecutor
import com.hiddenodds.trebolv2.presentation.model.MaterialModel
import io.reactivex.Observable
import javax.inject.Inject


class GetListMaterialUseCase @Inject constructor(threadExecutor: IThreadExecutor,
                                                 postExecutionThread: IPostExecutionThread,
                                                 private var iMaterialRepository:
                                                 IMaterialRepository):
        UseCase<List<MaterialModel>>(threadExecutor, postExecutionThread){
    override fun buildUseCaseObservable(): Observable<List<MaterialModel>> {
        return iMaterialRepository.getList()
    }


}
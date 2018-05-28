package com.hiddenodds.trebol.domain.interactor

import com.hiddenodds.trebol.model.interfaces.IMaterialRepository
import com.hiddenodds.trebol.model.interfaces.IPostExecutionThread
import com.hiddenodds.trebol.model.interfaces.IThreadExecutor
import com.hiddenodds.trebol.presentation.model.MaterialModel
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
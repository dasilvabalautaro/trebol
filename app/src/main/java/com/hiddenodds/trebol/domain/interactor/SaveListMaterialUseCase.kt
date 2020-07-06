package com.hiddenodds.trebol.domain.interactor

import com.hiddenodds.trebol.domain.data.MapperMaterial
import com.hiddenodds.trebol.model.interfaces.IMaterialRepository
import com.hiddenodds.trebol.model.interfaces.IPostExecutionThread
import com.hiddenodds.trebol.model.interfaces.IThreadExecutor
import io.reactivex.Observable
import javax.inject.Inject


class SaveListMaterialUseCase @Inject constructor(threadExecutor: IThreadExecutor,
                                                  postExecutionThread: IPostExecutionThread,
                                                  private var iMaterialRepository:
                                                  IMaterialRepository):
        UseCase<Boolean>(threadExecutor, postExecutionThread) {

    var listMapperMaterial: ArrayList<MapperMaterial> = ArrayList()

    override fun buildUseCaseObservable(): Observable<Boolean> {
        return iMaterialRepository.saveList(listMapperMaterial)
    }

}
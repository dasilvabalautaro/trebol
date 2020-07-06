package com.hiddenodds.trebol.presentation.presenter

import com.hiddenodds.trebol.R
import com.hiddenodds.trebol.domain.interactor.GetListMaterialUseCase
import com.hiddenodds.trebol.presentation.model.MaterialModel
import io.reactivex.observers.DisposableObserver
import javax.inject.Inject

class MaterialPresenter @Inject constructor(private val
                                            getListMaterialUseCase:
                                            GetListMaterialUseCase):
        BasePresenter(){

    fun executeGetMaterial(){
        getListMaterialUseCase.execute(MaterialObserver())
    }

    fun foundMaterial(result: List<MaterialModel>){
        view!!.executeTask(result)
    }

    inner class MaterialObserver: DisposableObserver<List<MaterialModel>>(){
        override fun onNext(t: List<MaterialModel>) {
            foundMaterial(t)
        }

        override fun onComplete() {
            showMessage(context.resources.getString(R.string.task_complete))
        }

        override fun onError(e: Throwable) {
            if (e.message != null) {
                showError(e.message!!)
            }
        }
    }

}
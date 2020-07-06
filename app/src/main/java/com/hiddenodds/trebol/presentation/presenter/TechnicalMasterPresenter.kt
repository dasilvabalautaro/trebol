package com.hiddenodds.trebol.presentation.presenter

import com.hiddenodds.trebol.R
import com.hiddenodds.trebol.domain.interactor.GetTechnicalMasterUseCase
import com.hiddenodds.trebol.presentation.model.TechnicalModel
import io.reactivex.observers.DisposableObserver
import javax.inject.Inject

class TechnicalMasterPresenter @Inject constructor(private val
                                                   getTechnicalMasterUseCase:
                                                   GetTechnicalMasterUseCase):
        BasePresenter(){

    fun executeGetTechnicalMaster(code: String, password: String){
        getTechnicalMasterUseCase.code = code
        getTechnicalMasterUseCase.password = password
        getTechnicalMasterUseCase.execute(TechObserver())
    }

    private fun foundMasterTechnical(technicalModel: TechnicalModel){
        view!!.executeTask(technicalModel)
    }

    inner class TechObserver: DisposableObserver<TechnicalModel>(){
        override fun onNext(t: TechnicalModel) {
            foundMasterTechnical(t)
        }

        override fun onComplete() {
            println(context.resources.getString(R.string.task_complete))
            //showMessage(context.resources.getString(R.string.task_complete))
        }

        override fun onError(e: Throwable) {
            if (e.message != null) {
                showError(e.message!!)
            }
        }
    }
}
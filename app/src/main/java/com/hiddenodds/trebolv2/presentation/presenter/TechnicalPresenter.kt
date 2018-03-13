package com.hiddenodds.trebolv2.presentation.presenter

import com.hiddenodds.trebolv2.R
import com.hiddenodds.trebolv2.domain.interactor.GetTechnicalUseCase
import com.hiddenodds.trebolv2.presentation.model.TechnicalModel
import io.reactivex.observers.DisposableObserver
import javax.inject.Inject


class TechnicalPresenter @Inject constructor(private val
                                             getTechnicalUseCase:
                                             GetTechnicalUseCase):
        BasePresenter(){

    fun executeGetTechnical(code: String){
        getTechnicalUseCase.code = code
        getTechnicalUseCase.execute(TechObserver())
    }


    private fun foundMasterTechnical(technicalModel: TechnicalModel){
        view!!.executeTask(technicalModel)
    }

    inner class TechObserver: DisposableObserver<TechnicalModel>(){
        override fun onNext(t: TechnicalModel) {
            foundMasterTechnical(t)
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
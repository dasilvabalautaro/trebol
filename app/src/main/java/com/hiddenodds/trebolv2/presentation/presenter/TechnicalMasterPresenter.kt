package com.hiddenodds.trebolv2.presentation.presenter

import com.hiddenodds.trebolv2.R
import com.hiddenodds.trebolv2.domain.interactor.GetTechnicalMasterUseCase
import com.hiddenodds.trebolv2.model.persistent.caching.CachingLruRepository
import com.hiddenodds.trebolv2.presentation.model.TechnicalModel
import com.hiddenodds.trebolv2.tools.Constants
import com.hiddenodds.trebolv2.tools.PreferenceHelper
import com.hiddenodds.trebolv2.tools.PreferenceHelper.set
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
        CachingLruRepository.instance.getLru()
                .put(Constants.CACHE_TECHNICAL_MASTER, technicalModel)
        val prefs = PreferenceHelper.customPrefs(context,
                Constants.PREFERENCE_TREBOL)
        prefs[Constants.TECHNICAL_KEY] = technicalModel.id
        showMessage(context.resources.getString(R.string.welcome) +
        "\n" + technicalModel.name)
        view!!.executeTask()
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
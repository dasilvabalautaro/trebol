package com.hiddenodds.trebolv2.presentation.presenter

import com.hiddenodds.trebolv2.R
import com.hiddenodds.trebolv2.domain.interactor.GetListMaterialUseCase
import com.hiddenodds.trebolv2.model.data.Material
import io.reactivex.observers.DisposableObserver
import io.realm.RealmResults
import javax.inject.Inject

class MaterialPresenter @Inject constructor(private val
                                            getListMaterialUseCase:
                                            GetListMaterialUseCase):
        BasePresenter(){

    fun executeGetMaterial(){
        getListMaterialUseCase.execute(MaterialObserver())
    }

    fun foundMaterial(result: RealmResults<Material>){
        view!!.executeTask(result)
    }

    inner class MaterialObserver: DisposableObserver<RealmResults<Material>>(){
        override fun onNext(t: RealmResults<Material>) {
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
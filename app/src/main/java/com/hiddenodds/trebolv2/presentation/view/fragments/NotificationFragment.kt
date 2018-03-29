package com.hiddenodds.trebolv2.presentation.view.fragments

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.widget.Toast
import com.hiddenodds.trebolv2.App
import com.hiddenodds.trebolv2.dagger.PresenterModule
import com.hiddenodds.trebolv2.presentation.components.PdfNotification
import com.hiddenodds.trebolv2.presentation.model.MaterialModel
import com.hiddenodds.trebolv2.presentation.presenter.*
import com.hiddenodds.trebolv2.tools.ManageImage
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

abstract class NotificationFragment: Fragment() {

    companion object Factory{
        var listMaterialSelect: ArrayList<MaterialModel> = ArrayList()
        var typeListMaterial = 0
        var flagAddMaterial = false
    }

    val Fragment.app: App
        get() = activity.application as App

    private val component by lazy { app.
            getAppComponent().plus(PresenterModule())}

    @Inject
    lateinit var technicalPresenter: TechnicalPresenter
    @Inject
    lateinit var materialPresenter: MaterialPresenter
    @Inject
    lateinit var manageImage: ManageImage
    @Inject
    lateinit var updateFieldNotificationPresenter:
            UpdateFieldNotificationPresenter
    @Inject
    lateinit var updateAssignedMaterialPresenter:
            UpdateAssignedMaterialPresenter
    @Inject
    lateinit var addAssignedMaterialToNotificationPresenter:
            AddAssignedMaterialToNotificationPresenter
    @Inject
    lateinit var deleteAssignedMaterialPresenter: DeleteAssignedMaterialPresenter
    @Inject
    lateinit var pdfNotification: PdfNotification

    var disposable: CompositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        component.inject(this)

    }

    override fun onStart() {
        super.onStart()
        val message = manageImage.observableMessage.map { m -> m }
        disposable.add(message.observeOn(AndroidSchedulers.mainThread())
                .subscribe { s ->
                    context.toast(s)
                })
    }



    fun Context.toast(message: CharSequence) =
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}
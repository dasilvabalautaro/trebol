package com.hiddenodds.trebolv2.presentation.view.fragments

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.widget.Toast
import com.hiddenodds.trebolv2.App
import com.hiddenodds.trebolv2.R
import com.hiddenodds.trebolv2.dagger.PresenterModule
import com.hiddenodds.trebolv2.presentation.components.PdfEndTask
import com.hiddenodds.trebolv2.presentation.components.PdfNotification
import com.hiddenodds.trebolv2.presentation.model.EmailModel
import com.hiddenodds.trebolv2.presentation.model.MaterialModel
import com.hiddenodds.trebolv2.presentation.model.NotificationModel
import com.hiddenodds.trebolv2.presentation.presenter.*
import com.hiddenodds.trebolv2.presentation.view.activities.MainActivity
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
    @Inject
    lateinit var sendEmailPresenter: SendEmailPresenter
    @Inject
    lateinit var pdfEndTask: PdfEndTask
    @Inject
    lateinit var verifyConnectServerPresenter: VerifyConnectServerPresenter

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
        (activity as MainActivity).displayHome(true)
    }

    fun getNotification(code: String,
                                list: java.util.ArrayList<NotificationModel>):
            NotificationModel? {
        if (list.isNotEmpty()){
            val sortedList = list.sortedWith(compareBy({ it.code }))

            sortedList.forEach { notify: NotificationModel ->
                if (notify.code == code){
                    return notify
                }
            }
        }

        return null
    }

    protected fun removeFragmentProduct(){
        try {

            val manager = activity.supportFragmentManager

            for (i in 0 until manager.backStackEntryCount){

                val fragment = manager.fragments[i]
                if (fragment is ProductFragment){
                    manager.beginTransaction().remove(fragment).commit()
                    break
                }

            }

        }catch(ex: Exception){
            println(ex.message)
        }
    }

    fun executeEmail(emailModel: EmailModel){
        val emailFragment = EmailFragment.newInstance(emailModel)
        activity.supportFragmentManager
                .beginTransaction()
                .replace(R.id.flContent, emailFragment,
                        emailFragment.javaClass.simpleName)
                .addToBackStack(null)
                .commit()
    }

    fun Context.toast(message: CharSequence) =
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}
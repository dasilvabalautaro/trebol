package com.hiddenodds.trebolv2.presentation.view.fragments

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.support.annotation.RequiresApi
import android.support.v4.app.Fragment
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import butterknife.BindView
import butterknife.ButterKnife
import com.hiddenodds.trebolv2.App
import com.hiddenodds.trebolv2.R
import com.hiddenodds.trebolv2.dagger.PresenterModule
import com.hiddenodds.trebolv2.presentation.components.EndlessRecyclerOnScrollListener
import com.hiddenodds.trebolv2.presentation.components.ItemOtAdapter
import com.hiddenodds.trebolv2.presentation.interfaces.ILoadDataView
import com.hiddenodds.trebolv2.presentation.model.EmailModel
import com.hiddenodds.trebolv2.presentation.model.NotificationModel
import com.hiddenodds.trebolv2.presentation.model.TechnicalModel
import com.hiddenodds.trebolv2.presentation.presenter.TechnicalMasterPresenter
import com.hiddenodds.trebolv2.presentation.presenter.TechnicalPresenter
import com.hiddenodds.trebolv2.tools.ChangeFormat
import com.hiddenodds.trebolv2.tools.Constants
import com.hiddenodds.trebolv2.tools.PreferenceHelper
import com.hiddenodds.trebolv2.tools.PreferenceHelper.get
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Cancellable
import kotlinx.coroutines.experimental.async
import org.jetbrains.anko.alert
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

class OtsFragment: Fragment(), ILoadDataView {

    private var adapter: ItemOtAdapter? = null
    private var techMasterCode: String? = null
    private var codeTech: String = ""
    private var disposable: CompositeDisposable = CompositeDisposable()
    private var technicalModel: TechnicalModel? = null
    private var notificationModel: NotificationModel? = null
    private var positionSpinner = -1
    var outState: Bundle? = null
    private var listNotification: ArrayList<NotificationModel> = ArrayList()
    private var listNotificationView: ArrayList<NotificationModel> = ArrayList()

    @BindView(R.id.sp_tech)
    @JvmField var spTech: Spinner? = null
    @BindView(R.id.rv_ots)
    @JvmField var rvOts: RecyclerView? = null

    val Fragment.app: App
        get() = activity.application as App

    private val component by lazy { app.
            getAppComponent().plus(PresenterModule())}

    @Inject
    lateinit var technicalMasterPresenter: TechnicalMasterPresenter
    @Inject
    lateinit var technicalPresenter: TechnicalPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        component.inject(this)

    }

    override fun onCreateView(inflater: LayoutInflater?,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val root: View = inflater!!.inflate(R.layout.view_list_ot,
                container,false)
        ButterKnife.bind(this, root)
        return root
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (outState != null){
            positionSpinner = outState!!.getInt("position")
        }

        rvOts!!.addOnScrollListener(object: EndlessRecyclerOnScrollListener(){
            override fun onLoadMore() {
                addDataToList()
            }

        })

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        technicalMasterPresenter.view = this
        technicalPresenter.view = this
        setupRecyclerView()
        getTechnicalMaster()
//        setupSwipeRefresh()
    }

    override fun onStart() {
        super.onStart()
        disposable.add( actionOnItemSelectedListenerObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .map { position ->
                    run{
                        positionSpinner = position
                        this.codeTech = spTech!!.getItemAtPosition(position) as String

                        async {
                            technicalPresenter.executeGetTechnical(codeTech)
                        }

                        return@map resources.getString(com.hiddenodds.trebolv2.R.string.new_filter)
                    }
                }
                .subscribe { result -> println(result)})
    }

    override fun onResume() {
        super.onResume()
        async {
            removeFragment()
        }
    }

    override fun onPause() {
        super.onPause()
        ChangeFormat.deleteCacheTechnical(this.codeTech)
        outState = Bundle()
        outState!!.putInt("position", positionSpinner)
    }

    override fun onDestroy() {
        super.onDestroy()
        outState = null
    }

    private fun removeFragment(){
        try {
            val manager = activity.supportFragmentManager

            for (i in 0 until manager.backStackEntryCount){
                val fragment = manager.fragments[i]
                if (fragment is MaintenanceFragment){
                    manager.beginTransaction().remove(fragment).commit()
                    fragment.onDestroy()
                }

            }

        }catch(ex: Exception){
            println(ex.message)
        }
    }

    override fun showMessage(message: String) {
        context.toast(message)
    }

    override fun showError(message: String) {
        context.toast(message)
    }

    override fun <T> executeTask(obj: T) {
        if (obj != null){
            this.technicalModel = (obj as TechnicalModel)
            val code = (obj as TechnicalModel).code
            if (code == this.techMasterCode && spTech!!.adapter == null){
                val listTech = ArrayList((obj as TechnicalModel).trd)
                setDataSpinner(listTech)
                verifyInstance()
            }else{
                setListRecycler()
            }

        }

    }

    private fun addDataToList(){
        Handler().postDelayed(Runnable {

            var firstVisibleItem = 0
            val itemView = listNotificationView.size

            println("Notifications Number: ${listNotification.size}")
            println("Notifications Number: ${listNotificationView.size}")
            if (listNotificationView.size != listNotification.size){
                println("DATOS EN CARGA")
                var count = 0
                for (i in itemView until listNotification.size){
                    listNotificationView.add(listNotification[i])
                    if (count > 6){
                        break
                    }
                    count++
                }

                async {
                    adapter!!.setObjectList(listNotificationView)
                }

                try {
                    activity.runOnUiThread {
                        if (itemView != 0){
                            firstVisibleItem = (rvOts!!
                                    .layoutManager as LinearLayoutManager)
                                    .findFirstVisibleItemPosition()
                        }
                        rvOts!!.refreshDrawableState()
                        rvOts!!.scrollToPosition(firstVisibleItem)
                    }

                }catch (ne: NullPointerException){
                    println(ne.message)
                }
            }else if ((listNotificationView.size == 0) &&
                    (listNotification.size == 0)){
                adapter!!.setObjectList(listNotificationView)
            }


        }, 1000)

    }

    private fun setListRecycler(){

        listNotification = ArrayList(technicalModel!!.notifications)
        //listNotificationView.removeAll(listNotificationView)
        listNotificationView = ArrayList()

        addDataToList()

    }

    private fun verifyInstance(){
        if (positionSpinner == -1){
            //setListRecycler()
            spTech!!.setSelection(spTech!!.adapter.count - 1)
        }else{
            spTech!!.setSelection(positionSpinner)
        }
    }


    override fun <T> executeTask(objList: List<T>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

   /* private fun setupSwipeRefresh() = srData!!.setOnRefreshListener(
            this::refreshData)

    private fun refreshData(){
        technicalPresenter.executeGetTechnical(this.codeTech)
        srData!!.isRefreshing = false
    }*/


    private fun setupRecyclerView(){
        rvOts!!.setHasFixedSize(true)
        rvOts!!.layoutManager = LinearLayoutManager(activity,
                LinearLayoutManager.VERTICAL, false)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            addDecorationRecycler()
        }
        adapter = ItemOtAdapter{
            this.notificationModel = it
            launchOptions()
        }
        rvOts!!.adapter = adapter
    }

    private fun setDataSpinner(list: ArrayList<String>){
        list.add(this.techMasterCode!!)
        val spinnerAdapter: ArrayAdapter<String> = ArrayAdapter(context,
                android.R.layout.simple_list_item_1, list)
        spTech!!.adapter = spinnerAdapter

    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun addDecorationRecycler(){
        val horizontalDecoration =
                DividerItemDecoration(rvOts!!.context,
                        DividerItemDecoration.VERTICAL)
        val horizontalDivider: Drawable = context
                .getDrawable(R.drawable.horizontal_divider)
        horizontalDecoration.setDrawable(horizontalDivider)
        rvOts!!.addItemDecoration(horizontalDecoration)
    }

    private fun actionOnItemSelectedListenerObservable(): Observable<Int> {
        return Observable.create({
            e: ObservableEmitter<Int>? ->
            spTech!!.onItemSelectedListener = object:
                    AdapterView.OnItemSelectedListener{
                override fun onItemSelected(parent:
                                            AdapterView<*>?,
                                            view: View?,
                                            position: Int, id: Long) {
                    e!!.onNext(position)
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    e!!.setCancellable { Cancellable{
                        spTech!!.onItemSelectedListener = null
                    } }
                }
            }

        })
    }


    private fun getTechnicalMaster(){
        val prefs = PreferenceHelper.customPrefs(context,
                Constants.PREFERENCE_TREBOL)
        this.techMasterCode = prefs[Constants.TECHNICAL_KEY, ""]
        val techPassword: String? = prefs[Constants.TECHNICAL_PASSWORD, ""]
        if (techMasterCode!!.isNotEmpty() && techPassword!!.isNotEmpty()){
            technicalMasterPresenter
                    .executeGetTechnicalMaster(this.techMasterCode!!,
                            techPassword)
        }else{
            context.toast(context.resources
                    .getString(R.string.input_error))
        }
    }

    private fun launchOptions(){
        activity.alert(R.string.lbl_end_instalation) {
            title = "Alerta"
            positiveButton(R.string.lbl_confirm) {
                callNotificationFinish(notificationModel!!.code,
                        technicalModel!!.code)
            }
            negativeButton(R.string.lbl_not_confirm) {
                executeEmail(buildEmailModel())
            }
            neutralPressed(R.string.lbl_cancel){}

        }.show()
    }

    private fun callNotificationFinish(codeNotification: String,
                                       codeTechnical: String){
        val finishFragment = NotificationFinishFragment.newInstance(codeNotification,
                codeTechnical)
        activity.supportFragmentManager
                .beginTransaction()
                .replace(R.id.flContent, finishFragment,
                        finishFragment.javaClass.simpleName)
                .addToBackStack(null)
                .commit()
    }

    private fun buildEmailModel(): EmailModel {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        val dateWork = sdf.format(Date())

        val emailModel = EmailModel()
        emailModel.title = "OT Nº: ${notificationModel!!.code}"
        emailModel.whoOf = "instalaciones@trebolgroup.com"
        emailModel.whoFor = "instalaciones@trebolgroup.com"
        emailModel.whoCopy = technicalModel!!.email
        emailModel.subject = notificationModel!!.series
        emailModel.message = "Se ha terminado la ${notificationModel!!.type} en el cliente " +
                "${notificationModel!!.businessName} en el día de $dateWork."
        emailModel.client = notificationModel!!.businessName
        emailModel.clip = notificationModel!!.code + ".pdf"
        return emailModel
    }

    private fun executeEmail(emailModel: EmailModel){
        val emailFragment = EmailFragment.newInstance(emailModel)
        activity.supportFragmentManager
                .beginTransaction()
                .replace(R.id.flContent, emailFragment,
                        emailFragment.javaClass.simpleName)
                .addToBackStack(null)
                .commit()
    }

    private fun Context.toast(message: CharSequence) =
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

}
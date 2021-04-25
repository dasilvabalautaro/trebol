package com.hiddenodds.trebol.presentation.view.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import com.hiddenodds.trebol.App
import com.hiddenodds.trebol.R
import com.hiddenodds.trebol.dagger.PresenterModule
import com.hiddenodds.trebol.presentation.components.EndlessRecyclerOnScrollListener
import com.hiddenodds.trebol.presentation.components.ItemOtAdapter
import com.hiddenodds.trebol.presentation.interfaces.ILoadDataView
import com.hiddenodds.trebol.presentation.model.EmailModel
import com.hiddenodds.trebol.presentation.model.NotificationModel
import com.hiddenodds.trebol.presentation.model.TechnicalModel
import com.hiddenodds.trebol.presentation.presenter.TechnicalPresenter
import com.hiddenodds.trebol.tools.ChangeFormat
import com.hiddenodds.trebol.tools.Variables
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Cancellable
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import org.jetbrains.anko.alert
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

class OtsFragment: Fragment(), ILoadDataView {

    private var adapter: ItemOtAdapter? = null
    private var codeTech: String = ""
    private var disposable: CompositeDisposable = CompositeDisposable()
    private var technicalModel: TechnicalModel? = null
    private var notificationModel: NotificationModel? = null
    private var positionSpinner = -1
    private var outState: Bundle? = null
    private var listNotification: ArrayList<NotificationModel> = ArrayList()
    private var listNotificationView: ArrayList<NotificationModel> = ArrayList()
    private var listTech: ArrayList<String> = ArrayList()
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.sp_tech)
    @JvmField var spTech: Spinner? = null
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.rv_ots)
    @JvmField var rvOts: RecyclerView? = null
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.pb_load)
    @JvmField var pbLoad: ProgressBar? = null

    val Fragment.app: App
        get() = requireActivity().application as App

    private val component by lazy { app.
            getAppComponent().plus(PresenterModule())}


    @Inject
    lateinit var technicalPresenter: TechnicalPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        component.inject(this)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        val root: View = inflater.inflate(R.layout.view_list_ot,
                container,false)
        ButterKnife.bind(this, root)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (outState != null){
            positionSpinner = outState!!.getInt("position")
            this.listTech = outState!!.getStringArrayList("listSpin")!!

        }else{
            this.listTech = ArrayList(Variables.listTechnicals)
            this.listTech.add(Variables.codeTechMaster)
        }

        setDataSpinner()
        rvOts!!.addOnScrollListener(object: EndlessRecyclerOnScrollListener(){
            override fun onLoadMore() {
                addDataToList()
            }


        })

    }

    override fun onStart() {
        super.onStart()
        disposable.add( actionOnItemSelectedListenerObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .map { position ->
                    run{

                        positionSpinner = position
                        this.codeTech = spTech!!.getItemAtPosition(position) as String
                        requireActivity().runOnUiThread {
                            pbLoad!!.visibility = View.VISIBLE
                        }

                        GlobalScope.async {
                            technicalPresenter.executeGetTechnical(codeTech)
                        }

                        return@map resources.getString(com.hiddenodds.trebol.R.string.new_filter)
                    }
                }
                .subscribe { result -> println(result)})

        technicalPresenter.view = this
        setupRecyclerView()

    }

    override fun onResume() {
        super.onResume()
        GlobalScope.async {
            removeFragment()
        }
    }

    override fun onPause() {
        super.onPause()
        ChangeFormat.deleteCacheTechnical(this.codeTech)
        Thread.sleep(1000)
        outState = Bundle()
        outState!!.putInt("position", positionSpinner)
        outState!!.putStringArrayList("listSpin", this.listTech)
    }

    override fun onDestroy() {
        super.onDestroy()
        outState = null
    }

    private fun removeFragment(){
        try {
            val manager = requireActivity().supportFragmentManager

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
        requireContext().toast(message)
    }

    override fun showError(message: String) {
        pbLoad!!.visibility = View.INVISIBLE
        requireContext().toast(message)
    }

    override fun <T> executeTask(obj: T) {
        if (obj != null){
            this.technicalModel = (obj as TechnicalModel)
            setListRecycler()
        }

    }

    private fun addDataToList(){
        Handler(Looper.getMainLooper()).postDelayed({

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

                GlobalScope.async {
                    adapter!!.setObjectList(listNotificationView)
                }

                try {
                    requireActivity().runOnUiThread {
                        if (itemView != 0){
                            firstVisibleItem = (rvOts!!
                                    .layoutManager as LinearLayoutManager)
                                    .findFirstVisibleItemPosition()
                        }
                        rvOts!!.refreshDrawableState()
                        rvOts!!.scrollToPosition(firstVisibleItem)
                        pbLoad!!.visibility = View.INVISIBLE
                    }

                }catch (ne: NullPointerException){
                    println(ne.message)
                }catch (ie: IndexOutOfBoundsException){
                    println(ie.message)
                }
            }else if ((listNotificationView.size == 0) &&
                    (listNotification.size == 0)){
                adapter!!.setObjectList(listNotificationView)
                requireActivity().runOnUiThread {
                    pbLoad!!.visibility = View.INVISIBLE
                }

            }


        }, 1000)

    }

    private fun setListRecycler(){

        listNotification = ArrayList(technicalModel!!.notifications)
        listNotificationView = ArrayList()

        addDataToList()

    }

    override fun <T> executeTask(objList: List<T>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun setupRecyclerView(){
        rvOts!!.setHasFixedSize(true)
        rvOts!!.layoutManager = LinearLayoutManager(requireActivity(),
                LinearLayoutManager.VERTICAL, false)
        addDecorationRecycler()
        adapter = ItemOtAdapter{
            this.notificationModel = it
            launchOptions()
        }
        rvOts!!.adapter = adapter
    }

    private fun setDataSpinner(){

        val spinnerAdapter: ArrayAdapter<String> = ArrayAdapter(requireContext(),
                android.R.layout.simple_list_item_1, this.listTech)
        spTech!!.adapter = spinnerAdapter

    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun addDecorationRecycler(){
        val horizontalDecoration =
                DividerItemDecoration(rvOts!!.context!!,
                        DividerItemDecoration.VERTICAL)
        val horizontalDivider: Drawable = requireContext()
                .getDrawable(R.drawable.horizontal_divider)!!
        horizontalDecoration.setDrawable(horizontalDivider)
        rvOts!!.addItemDecoration(horizontalDecoration)
    }

    private fun actionOnItemSelectedListenerObservable(): Observable<Int> {
        return Observable.create {
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

        }
    }


    private fun launchOptions(){
        requireActivity().alert(R.string.lbl_end_instalation) {
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
        requireActivity().supportFragmentManager
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
        requireActivity().supportFragmentManager
                .beginTransaction()
                .replace(R.id.flContent, emailFragment,
                        emailFragment.javaClass.simpleName)
                .addToBackStack(null)
                .commit()
    }

    private fun Context.toast(message: CharSequence) =
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

}
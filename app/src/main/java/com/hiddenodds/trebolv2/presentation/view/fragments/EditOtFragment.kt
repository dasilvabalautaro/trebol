package com.hiddenodds.trebolv2.presentation.view.fragments

import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import butterknife.*
import com.hiddenodds.trebolv2.R
import com.hiddenodds.trebolv2.presentation.interfaces.ILoadDataView
import com.hiddenodds.trebolv2.presentation.model.NotificationModel
import com.hiddenodds.trebolv2.presentation.model.TechnicalModel
import com.hiddenodds.trebolv2.tools.ChangeFormat
import kotlinx.coroutines.experimental.async
import java.util.*

class EditOtFragment: NotificationFragment(), ILoadDataView {
    @BindView(R.id.lbl_title)
    @JvmField var lbl_title: TextView? = null
    @BindView(R.id.txtHrsTrabajo)
    @JvmField var txtHrsTrabajo: TextView? = null

    @BindView(R.id.txtComercial)
    @JvmField var txtComercial: TextView? = null
    @BindView(R.id.txtMod)
    @JvmField var txtMod: TextView? = null
    @BindView(R.id.txtSerie)
    @JvmField var txtSerie: TextView? = null
    @BindView(R.id.txtTinta)
    @JvmField var txtTinta: TextView? = null
    @BindView(R.id.txtPeaje)
    @JvmField var txtPeaje: TextView? = null
    @BindView(R.id.txtSat)
    @JvmField var txtSat: TextView? = null
    @BindView(R.id.txtSatdk)
    @JvmField var txtSatdk: TextView? = null
    @BindView(R.id.txtSintomas)
    @JvmField var txtSintomas: TextView? = null

    @BindView(R.id.edtVsoft1)
    @JvmField var edtVsoft1: EditText? = null
    @BindView(R.id.edtVsoft2)
    @JvmField var edtVsoft2: EditText? = null
    @BindView(R.id.edtVsoft3)
    @JvmField var edtVsoft3: EditText? = null
    @BindView(R.id.edtHrs)
    @JvmField var edtHrs: EditText? = null
    @BindView(R.id.edtTotalesEquipo)
    @JvmField var edtTotalesEquipo: EditText? = null
    @BindView(R.id.edtUltimoMnto)
    @JvmField var edtUltimoMnto: EditText? = null
    @BindView(R.id.edtInformeTecnico)
    @JvmField var edtInformeTecnico: EditText? = null
    @BindView(R.id.edtObservaciones)
    @JvmField var edtObservaciones: EditText? = null
    @BindView(R.id.spnEntrada)
    @JvmField var spnEntrada: EditText? = null
    @BindView(R.id.spnSalida)
    @JvmField var spnSalida: EditText? = null
    @BindView(R.id.spnDieta)
    @JvmField var spnDieta: Spinner? = null

    @OnItemSelected(R.id.spnDieta)
    fun selectDiet(){
        val value = spnDieta!!.selectedItem.toString()
        if (notificationModel != null){
            notificationModel!!.diet = value
        }
    }

    @OnClick(R.id.spnEntrada)
    fun setTimeInside(){
        ChangeFormat.setTimeToControl(spnEntrada!!, context)

    }

    @OnClick(R.id.spnSalida)
    fun setTimeOutside(){
        ChangeFormat.setTimeToControl(spnSalida!!, context)
    }

    @OnTextChanged(R.id.edtObservaciones)
    fun changeObservation(){
        if (notificationModel != null){
            notificationModel!!.observations = edtObservaciones!!.text.toString()
        }
    }

    @OnTextChanged(R.id.edtInformeTecnico)
    fun changeReportTechical(){
        if (notificationModel != null){
            notificationModel!!.reportTechnical = edtInformeTecnico!!.text.toString()
        }
    }

    @OnTextChanged(R.id.edtUltimoMnto)
    fun changeLastMount(){
        if (notificationModel != null){
            notificationModel!!.lastAmount = edtUltimoMnto!!.text.toString()
        }
    }

    @OnTextChanged(R.id.edtTotalesEquipo)
    fun changeTotalTeam(){
        if (notificationModel != null){
            notificationModel!!.totalTeam = edtTotalesEquipo!!.text.toString()
        }
    }

    @OnTextChanged(R.id.edtHrs)
    fun changeHours(){
        if (notificationModel != null){
            notificationModel!!.hours = edtHrs!!.text.toString()
        }
    }

    @OnTextChanged(R.id.spnEntrada)
    fun changeInside(){
        if (notificationModel != null){
            notificationModel!!.inside = spnEntrada!!.text.toString()
        }
    }

    @OnTextChanged(R.id.edtVsoft1)
    fun changeVsoft1(){
        if (notificationModel != null){
            notificationModel!!.vSoft1 = edtVsoft1!!.text.toString()
        }
    }
    @OnTextChanged(R.id.edtVsoft2)
    fun changeVsoft2(){
        if (notificationModel != null){
            notificationModel!!.vSoft2 = edtVsoft2!!.text.toString()
        }

    }
    @OnTextChanged(R.id.edtVsoft3)
    fun changeVsoft3(){
        if (notificationModel != null){
            notificationModel!!.vSoft3 = edtVsoft3!!.text.toString()
        }
    }

    @OnTextChanged(R.id.spnSalida)
    fun setHoursWork(){
        try {
            val difHours = ChangeFormat.substracHours(spnEntrada!!, spnSalida!!)
            txtHrsTrabajo!!.text = difHours
            if (notificationModel != null){
                notificationModel!!.workHours = txtHrsTrabajo!!.text.toString()
                notificationModel!!.outside = spnSalida!!.text.toString()
            }

        }catch (ne: NumberFormatException){
            println(ne.message)
        }

    }

    @OnClick(R.id.btnMatUse)
    fun setMaterialUse(){

    }

    @OnClick(R.id.btnMatOut)
    fun setMaterialOut(){

    }

    companion object Factory {
        private const val inputNotification = "notify_"
        private const val inputTechnical = "technical_"
        fun newInstance(arg1: String? = null, arg2: String? = null):
                EditOtFragment = EditOtFragment().apply{
            this.arguments = Bundle().apply {
                this.putString(inputNotification, arg1)
                this.putString(inputTechnical, arg2)
            }

        }
    }


    private val codeNotification: String by lazy { this.arguments.getString(inputNotification) }
    private val codeTechnical: String by lazy { this.arguments.getString(inputTechnical) }

    private var technicalModel: TechnicalModel? = null
    private var notificationModel: NotificationModel? = null


    override fun onCreateView(inflater: LayoutInflater?,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val root: View = inflater!!.inflate(R.layout.view_ot,
                container,false)
        ButterKnife.bind(this, root)
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        technicalPresenter.view = this
    }

    override fun onStart() {
        super.onStart()
        initControls()

        async {
            technicalPresenter.executeGetTechnical(codeTechnical)
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
            val listNotification = ArrayList((obj as TechnicalModel).notifications)
            async {
                notificationModel = getNotification(codeNotification, listNotification)
                activity.runOnUiThread({
                    setControls(notificationModel)
                })
            }

        }
    }

    private fun getNotification(code: String,
                                list: ArrayList<NotificationModel>):
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

    private fun setControls(notify: NotificationModel?){
        if (notify != null){
            val title = "OT Nº: " + notify.code
            lbl_title!!.text = title
            txtComercial!!.text = notify.trade
            txtMod!!.text = notify.product
            txtSerie!!.text = notify.series
            txtTinta!!.text = notify.ink
            txtPeaje!!.text = notify.peaje
            txtSat!!.text = notify.satd
            txtSatdk!!.text = notify.satdk
            txtSintomas!!.text = notify.symptom
            edtVsoft1!!.setText(notify.vSoft1)
            edtVsoft2!!.setText(notify.vSoft2)
            edtVsoft3!!.setText(notify.vSoft3)
            edtHrs!!.setText(notify.hours)
            edtTotalesEquipo!!.setText(notify.totalTeam)
            edtUltimoMnto!!.setText(notify.lastAmount)
            edtInformeTecnico!!.setText(notify.reportTechnical)
            edtObservaciones!!.setText(notify.observations)
            txtHrsTrabajo!!.text = notify.workHours
            spnEntrada!!.setText(notify.inside)
            spnSalida!!.setText(notify.outside)
            setSpinnerDiet(notify.diet)
        }
    }

    private fun initControls(){
        val list: ArrayList<String> = ArrayList()
        list.add("NO")
        list.add("1/2")
        list.add("1")
        val spinnerAdapter: ArrayAdapter<String> = ArrayAdapter(context,
                android.R.layout.simple_list_item_1, list)
        spnDieta!!.adapter = spinnerAdapter

        spnEntrada!!.inputType = InputType.TYPE_NULL
        spnSalida!!.inputType = InputType.TYPE_NULL
    }

    private fun setSpinnerDiet(diet: String){
        if (diet.isNotEmpty()){
            for (i in 0 until spnDieta!!.adapter.count){
                val value = spnDieta!!.adapter.getItem(i) as String
                if (value == diet){
                    spnDieta!!.setSelection(i)
                    break
                }
            }

        }

    }

}
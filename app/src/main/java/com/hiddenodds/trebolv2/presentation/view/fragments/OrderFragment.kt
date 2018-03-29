package com.hiddenodds.trebolv2.presentation.view.fragments

import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import butterknife.*
import com.github.gcacace.signaturepad.views.SignaturePad
import com.hiddenodds.trebolv2.R
import com.hiddenodds.trebolv2.presentation.components.ItemProductSelectAdapter
import com.hiddenodds.trebolv2.presentation.interfaces.ILoadDataView
import com.hiddenodds.trebolv2.presentation.model.AssignedMaterialModel
import com.hiddenodds.trebolv2.presentation.model.MaterialModel
import com.hiddenodds.trebolv2.presentation.model.NotificationModel
import com.hiddenodds.trebolv2.presentation.model.TechnicalModel
import com.hiddenodds.trebolv2.tools.ChangeFormat
import com.hiddenodds.trebolv2.tools.Variables
import kotlinx.coroutines.experimental.async
import java.text.SimpleDateFormat
import java.util.*

class OrderFragment: NotificationFragment(), ILoadDataView {
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
    @BindView(R.id.rv_mat_out)
    @JvmField var rvMatOut: RecyclerView? = null
    @BindView(R.id.rv_mat_use)
    @JvmField var rvMatUse: RecyclerView? = null
    @BindView(R.id.signatureClient)
    @JvmField var signatureClient: SignaturePad? = null
    @BindView(R.id.btnSaveSignature)
    @JvmField var btnSaveSignature: ImageButton? = null
    @BindView(R.id.btnPDF)
    @JvmField var btnPDF: ImageButton? = null
    @BindView(R.id.btnClearSignature)
    @JvmField var btnClearSignature: ImageButton? = null

    @OnClick(R.id.btnPDF)
    fun savePdf(){
        pdfNotification.manageImage = this.manageImage
        async {
            pdfNotification.inflateView()
            pdfNotification.setData(notificationModel!!, technicalModel!!)
            pdfNotification.saveImage()
        }
    }

    @OnClick(R.id.btnSaveSignature)
    fun saveSignature(){
        val signatureBitmap: Bitmap? = signatureClient!!.signatureBitmap
        if (signatureBitmap != null){
            manageImage.image = signatureBitmap
            manageImage.code = notificationModel!!.code
            if (notificationModel!!.state != "1"){
                async {
                    updateState("1")
                }
            }
            manageImage.addFileToGallery(activity)

        }else{
            context.toast(context.getString(R.string.image_not_found))
        }
    }

    @OnClick(R.id.btnClearSignature)
    fun clearSignature(){
        if (notificationModel!!.state != "0"){
            async {
                updateState("0")
            }
        }
        signatureClient!!.clear()
        manageImage.deleteSignatureStore(notificationModel!!.code)

    }

    @OnItemSelected(R.id.spnDieta)
    fun selectDiet(){
        val value = spnDieta!!.selectedItem.toString()
        if (notificationModel != null && flagSpinner){
            if (notificationModel!!.diet != value){
                async {
                    updateFieldNotificationPresenter
                            .updateNotification(notificationModel!!.id,
                                    "diet", value)
                }
                notificationModel!!.diet = value
            }

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
            val value = edtObservaciones!!.text.toString()
            if (notificationModel!!.observations != edtObservaciones!!.text.toString()){
                async {
                    updateFieldNotificationPresenter
                            .updateNotification(notificationModel!!.id,
                                    "observations", value)
                }
                notificationModel!!.observations = edtObservaciones!!.text.toString()
            }
        }
    }

    @OnTextChanged(R.id.edtInformeTecnico)
    fun changeReportTechical(){
        if (notificationModel != null){
            val value = edtInformeTecnico!!.text.toString()
            if (notificationModel!!.reportTechnical != edtInformeTecnico!!.text.toString()){
                async {
                    updateFieldNotificationPresenter
                            .updateNotification(notificationModel!!.id,
                                    "reportTechnical", value)
                }
                notificationModel!!.reportTechnical = edtInformeTecnico!!.text.toString()

            }
        }
    }

    @OnTextChanged(R.id.edtUltimoMnto)
    fun changeLastMount(){
        if (notificationModel != null){
            val value = edtUltimoMnto!!.text.toString()
            if (notificationModel!!.lastAmount != edtUltimoMnto!!.text.toString()){
                async {
                    updateFieldNotificationPresenter
                            .updateNotification(notificationModel!!.id,
                                    "lastAmount", value)
                }
                notificationModel!!.lastAmount = edtUltimoMnto!!.text.toString()
            }

        }
    }

    @OnTextChanged(R.id.edtTotalesEquipo)
    fun changeTotalTeam(){
        if (notificationModel != null){
            val value = edtTotalesEquipo!!.text.toString()
            if (notificationModel!!.totalTeam != edtTotalesEquipo!!.text.toString()){
                async {
                    updateFieldNotificationPresenter
                            .updateNotification(notificationModel!!.id,
                                    "totalTeam", value)
                }
                notificationModel!!.totalTeam = edtTotalesEquipo!!.text.toString()
            }

        }
    }

    @OnTextChanged(R.id.edtHrs)
    fun changeHours(){
        if (notificationModel != null){
            val value = edtHrs!!.text.toString()
            if (notificationModel!!.hours != edtHrs!!.text.toString()){
                async {
                    updateFieldNotificationPresenter
                            .updateNotification(notificationModel!!.id,
                                    "hours", value)
                }
                notificationModel!!.hours = edtHrs!!.text.toString()
            }

        }
    }

    @OnTextChanged(R.id.spnEntrada)
    fun changeInside(){
        if (notificationModel != null && spnEntrada!!.text.isNotEmpty()){
            val value = spnEntrada!!.text.toString()
            if (notificationModel!!.inside != spnEntrada!!.text.toString()){
                async {
                    updateFieldNotificationPresenter
                            .updateNotification(notificationModel!!.id,
                                    "inside", value)
                }
                notificationModel!!.inside = spnEntrada!!.text.toString()
            }

        }
    }

    @OnTextChanged(R.id.edtVsoft1)
    fun changeVsoft1(){
        if (notificationModel != null){
            val value = edtVsoft1!!.text.toString()
            if (notificationModel!!.vSoft1 != edtVsoft1!!.text.toString()){
                async {
                    updateFieldNotificationPresenter
                            .updateNotification(notificationModel!!.id,
                                    "vSoft1", value)
                }
                notificationModel!!.vSoft1 = edtVsoft1!!.text.toString()
            }

        }
    }
    @OnTextChanged(R.id.edtVsoft2)
    fun changeVsoft2(){
        if (notificationModel != null){
            val value = edtVsoft2!!.text.toString()
            if (notificationModel!!.vSoft2 != edtVsoft2!!.text.toString()){
                async {
                    updateFieldNotificationPresenter
                            .updateNotification(notificationModel!!.id,
                                    "vSoft2", value)
                }
                notificationModel!!.vSoft2 = edtVsoft2!!.text.toString()
            }

        }

    }
    @OnTextChanged(R.id.edtVsoft3)
    fun changeVsoft3(){
        if (notificationModel != null){
            val value = edtVsoft3!!.text.toString()
            if (notificationModel!!.vSoft3 != edtVsoft3!!.text.toString()){
                async {
                    updateFieldNotificationPresenter
                            .updateNotification(notificationModel!!.id,
                                    "vSoft3", value)
                }
                notificationModel!!.vSoft3 = edtVsoft3!!.text.toString()
            }

        }
    }
    @OnTextChanged(R.id.txtHrsTrabajo)
    fun changeHoursWork(){
        if (notificationModel != null){
            val value = txtHrsTrabajo!!.text.toString()
            if (notificationModel!!.workHours != txtHrsTrabajo!!.text.toString()){
                async {
                    updateFieldNotificationPresenter
                            .updateNotification(notificationModel!!.id,
                                    "workHours", value)
                }
                notificationModel!!.workHours = txtHrsTrabajo!!.text.toString()
            }

        }
    }

    @OnTextChanged(R.id.spnSalida)
    fun changeOutside(){
        try {

            if (notificationModel != null &&
                    spnSalida!!.text.isNotEmpty()){

                val valueOutside = spnSalida!!.text.toString()

                if (notificationModel!!.outside != spnSalida!!.text.toString()){
                    async {

                        updateFieldNotificationPresenter
                                .updateNotification(notificationModel!!.id,
                                        "outside", valueOutside)
                    }
                    setHoursWork()
                    notificationModel!!.outside = spnSalida!!.text.toString()
                }
            }

        }catch (ne: NumberFormatException){
            println(ne.message)
        }

    }

    @OnClick(R.id.btnMatUse)
    fun setMaterialUse(){
        flagAddMaterial = true
        typeListMaterial = 0
        callProductsFragment()
    }

    @OnClick(R.id.btnMatOut)
    fun setMaterialOut(){
        flagAddMaterial = true
        typeListMaterial = 1
        callProductsFragment()
    }

    companion object Factory {
        private const val inputNotification = "notify_"
        private const val inputTechnical = "technical_"
        fun newInstance(arg1: String? = null, arg2: String? = null):
                OrderFragment = OrderFragment().apply{
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
    private var adapterMaterialUse: ItemProductSelectAdapter? = null
    private var adapterMaterialOut: ItemProductSelectAdapter? = null
    private var listMaterialUse: ArrayList<AssignedMaterialModel> = ArrayList()
    private var listMaterialOut: ArrayList<AssignedMaterialModel> = ArrayList()
    private var flagSpinner = false

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
        updateFieldNotificationPresenter.view = this
        updateAssignedMaterialPresenter.view = this
        deleteAssignedMaterialPresenter.view = this

        initControls()

        async {
            technicalPresenter.executeGetTechnical(codeTechnical)
        }


    }


    private fun setSignature(codeTech: String){
        val bitmap = manageImage.getSignatureStore(codeTech)
        if (bitmap != null){
            activity.runOnUiThread({
                signatureClient!!.signatureBitmap = bitmap
            })
        }
    }

    private fun addAssignedMaterialRepository(list: ArrayList<AssignedMaterialModel>,
                                              flagUse: Boolean){
        if (list.isNotEmpty()){
            val listSave = list.filter { it.id.trim().isEmpty() } as ArrayList
            if (listSave.isNotEmpty()){
                Variables.changeTechnical = technicalModel!!.code
                async {
                    addAssignedMaterialToNotificationPresenter.addAsignedMaterial(listSave,
                            notificationModel!!.id, flagUse)
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()
        addAssignedMaterialRepository(listMaterialUse, true)
        addAssignedMaterialRepository(listMaterialOut, false)
    }

    override fun showMessage(message: String) {
        if (message == context.getString(R.string.change_field)){
            val tech = technicalModel!!.code
            Variables.changeTechnical = tech
            println("Change technical : $tech")

        }else{
            context.toast(message)
        }
    }

    override fun showError(message: String) {
        context.toast(message)
    }
    override fun <T> executeTask(objList: List<T>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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
                setSignature(notificationModel!!.code)
            }

        }
    }

    private fun setHoursWork(){
        val valueInside = spnEntrada!!.text.toString()
        val valueOutside = spnSalida!!.text.toString()
        if (valueInside.isNotEmpty() && valueOutside.isNotEmpty()){
            println("Salida: $valueOutside Entrada: $valueInside")
            val difHours = ChangeFormat.substracHours(valueInside, valueOutside)
            if (txtHrsTrabajo!!.text.toString() != difHours){
                txtHrsTrabajo!!.text = difHours
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
            listMaterialUse = notify.materialUse
            listMaterialOut = notify.materialOut
            updateListMaterial()
            adapterMaterialUse!!.setObjectList(listMaterialUse)
            adapterMaterialOut!!.setObjectList(listMaterialOut)
            rvMatUse!!.scrollToPosition(0)
            rvMatOut!!.scrollToPosition(0)
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
        setupRecyclerViewMaterialUse()
        setupRecyclerViewMaterialOut()
    }

    private fun setupRecyclerViewMaterialUse(){
        rvMatUse!!.setHasFixedSize(true)
        rvMatUse!!.layoutManager = LinearLayoutManager(activity,
                LinearLayoutManager.VERTICAL, false)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ChangeFormat.addDecorationRecycler(rvMatUse!!, context)
        }
        adapterMaterialUse = ItemProductSelectAdapter{
            if (it.change == 1){
                it.change = 0
                async {
                    updateAssignedMaterialRepository(it.id, it.quantity)
                }

            }else{
                delRowListMaterial(it.material!!.code, listMaterialUse)
                adapterMaterialUse!!.setObjectList(listMaterialUse)
                rvMatUse!!.scrollToPosition(rvMatUse!!.adapter.itemCount - 1)
                async {
                    deleteAssignedMaterialRepository(notificationModel!!.id,
                            it.id, true)
                }
            }

        }
        rvMatUse!!.adapter = adapterMaterialUse
    }

    private fun setupRecyclerViewMaterialOut(){
        rvMatOut!!.setHasFixedSize(true)
        rvMatOut!!.layoutManager = LinearLayoutManager(activity,
                LinearLayoutManager.VERTICAL, false)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ChangeFormat.addDecorationRecycler(rvMatOut!!, context)
        }
        adapterMaterialOut = ItemProductSelectAdapter{
            if (it.change == 1){
                it.change = 0
                async {
                    updateAssignedMaterialRepository(it.id, it.quantity)
                }

            }else{
                delRowListMaterial(it.material!!.code, listMaterialOut)
                adapterMaterialOut!!.setObjectList(listMaterialOut)
                rvMatOut!!.scrollToPosition(rvMatOut!!.adapter.itemCount - 1)
                async {
                    deleteAssignedMaterialRepository(notificationModel!!.id,
                            it.id, false)
                }
            }

        }
        rvMatOut!!.adapter = adapterMaterialOut
    }
    private fun updateAssignedMaterialRepository(idAssigned: String,
                                                 quantity: Int){
        updateAssignedMaterialPresenter.updateAssignedMaterial(idAssigned,
                quantity)
    }
    private fun deleteAssignedMaterialRepository(idNotification: String,
                                                 idAssigned: String, flagUse: Boolean){
        if (idAssigned.trim().isNotEmpty()){
            deleteAssignedMaterialPresenter.deleteAssignedMaterial(idNotification,
                    idAssigned, flagUse)
        }
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
        flagSpinner = true
    }
    
    private fun updateListMaterial(){
        if (flagAddMaterial){
            when(typeListMaterial){
                0 -> {
                    addRowListMaterial(listMaterialUse)
                }
                1 -> {
                    addRowListMaterial(listMaterialOut)
                }
            }
            flagAddMaterial = false
        }
    }

    private fun addRowListMaterial(listMaterial: ArrayList<AssignedMaterialModel>){
        if (listMaterialSelect.isNotEmpty()){

            for (material: MaterialModel in listMaterialSelect){
                val list = listMaterial.filter { it.material!!.code == material.code }
                if (list.isEmpty()){
                    val materialSelect = AssignedMaterialModel()
                    materialSelect.material = material
                    listMaterial.add(materialSelect)
                }
            }
            listMaterialSelect.clear()
        }

    }

    private fun delRowListMaterial(code: String,
                                   listMaterial: ArrayList<AssignedMaterialModel>){

        val row: AssignedMaterialModel? = listMaterial
                .singleOrNull { it.material!!.code == code }
        if (row != null) listMaterial.remove(row)

    }

    private fun callProductsFragment(){
        val productFragment = ProductFragment()
        activity.supportFragmentManager
                .beginTransaction()
                .replace(R.id.flContent, productFragment,
                        productFragment.javaClass.simpleName)
                .addToBackStack(null)
                .commit()
    }

    private fun updateState(value: String){
        when(value){
            "0" -> {
                updateFieldNotificationPresenter
                        .updateNotification(notificationModel!!.id,
                                "state", "0")
                updateFieldNotificationPresenter
                        .updateNotification(notificationModel!!.id,
                                "dateEnd", "")

            }
            "1" -> {
                val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
                val dateEnd = dateFormat.format(Date())
                updateFieldNotificationPresenter
                        .updateNotification(notificationModel!!.id,
                                "state", "1")
                updateFieldNotificationPresenter
                        .updateNotification(notificationModel!!.id,
                                "dateEnd", dateEnd)
            }
        }

    }
}
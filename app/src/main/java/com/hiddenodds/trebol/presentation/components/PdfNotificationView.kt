package com.hiddenodds.trebol.presentation.components

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import com.hiddenodds.trebol.R


class PdfNotificationView: FrameLayout {
    constructor(context: Context): super(context)
    constructor(context: Context, attributeSet: AttributeSet):
            super(context, attributeSet)

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
    @BindView(R.id.txtNameTech)
    @JvmField var txtNameTech: TextView? = null
    @BindView(R.id.txtContract)
    @JvmField var txtContract: TextView? = null
    @BindView(R.id.txtFinish)
    @JvmField var txtFinish: TextView? = null
    @BindView(R.id.edtVsoft1)
    @JvmField var edtVsoft1: TextView? = null
    @BindView(R.id.edtVsoft2)
    @JvmField var edtVsoft2: TextView? = null
    @BindView(R.id.edtVsoft3)
    @JvmField var edtVsoft3: TextView? = null
    @BindView(R.id.edtHrs)
    @JvmField var edtHrs: TextView? = null
    @BindView(R.id.edtTotalesEquipo)
    @JvmField var edtTotalesEquipo: TextView? = null
    @BindView(R.id.edtUltimoMnto)
    @JvmField var edtUltimoMnto: TextView? = null
    @BindView(R.id.edtInformeTecnico)
    @JvmField var edtInformeTecnico: TextView? = null
    @BindView(R.id.edtObservaciones)
    @JvmField var edtObservaciones: TextView? = null
    @BindView(R.id.spnEntrada)
    @JvmField var spnEntrada: TextView? = null
    @BindView(R.id.spnSalida)
    @JvmField var spnSalida: TextView? = null
    @BindView(R.id.spnDieta)
    @JvmField var spnDieta: TextView? = null
    @BindView(R.id.rv_mat_out)
    @JvmField var rvMatOut: RecyclerView? = null
    @BindView(R.id.rv_mat_use)
    @JvmField var rvMatUse: RecyclerView? = null
    @BindView(R.id.signatureClient)
    @JvmField var signatureClient: ImageView? = null
    @BindView(R.id.tv_client)
    @JvmField var tvClient: TextView? = null

    init {

        LayoutInflater.from(context)
                .inflate(R.layout.view_pdf_notification, this, true)
        ButterKnife.bind(this)

    }
}
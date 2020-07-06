package com.hiddenodds.trebol.domain.data

import android.os.Parcel
import com.hiddenodds.trebol.model.interfaces.IDataContent

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class MapperMaterial: IDataContent {
    var code: String = ""
    var detail: String = ""

    override fun setContent(parcel: Parcel) {
        code = parcel.readString().toString()
        detail = parcel.readString().toString()
    }

    override fun getContent(): Parcel {
        val parcel:Parcel = Parcel.obtain()

        parcel.writeString(code)
        parcel.writeString(detail)

        return parcel
    }

}
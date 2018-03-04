package com.hiddenodds.trebolv2.domain.data

import android.os.Parcel
import com.hiddenodds.trebolv2.model.interfaces.IDataContent

class MapperMaterial: IDataContent {
    var code: String = ""
    var detail: String = ""

    override fun setContent(parcel: Parcel) {
        code = parcel.readString()
        detail = parcel.readString()
    }

    override fun getContent(): Parcel {
        val parcel:Parcel = Parcel.obtain()

        parcel.writeString(code)
        parcel.writeString(detail)

        return parcel
    }

}
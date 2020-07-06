package com.hiddenodds.trebol.domain.data

import android.os.Parcel
import com.hiddenodds.trebol.model.interfaces.IDataContent


@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class MapperTypeNotification: IDataContent {
    var code: String = ""
    var description: String = ""


    override fun setContent(parcel: Parcel) {
        code = parcel.readString().toString()
        description = parcel.readString().toString()
    }

    override fun getContent(): Parcel {
        val parcel:Parcel = Parcel.obtain()

        parcel.writeString(code)
        parcel.writeString(description)

        return parcel
    }


}
package com.hiddenodds.trebolv2.domain.data

import android.os.Parcel
import com.hiddenodds.trebolv2.model.interfaces.IDataContent


class MapperTypeNotification: IDataContent {
    var code: String = ""
    var description: String = ""


    override fun setContent(parcel: Parcel) {
        code = parcel.readString()
        description = parcel.readString()
    }

    override fun getContent(): Parcel {
        val parcel:Parcel = Parcel.obtain()

        parcel.writeString(code)
        parcel.writeString(description)

        return parcel
    }


}
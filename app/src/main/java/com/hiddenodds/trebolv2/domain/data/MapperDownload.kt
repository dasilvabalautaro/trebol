package com.hiddenodds.trebolv2.domain.data

import android.os.Parcel
import com.hiddenodds.trebolv2.model.interfaces.IDataContent


class MapperDownload: IDataContent {

    var code: String = ""
    var notification: String = ""
    var customer: String = ""
    var state: Byte = 0

    override fun setContent(parcel: Parcel) {
        code = parcel.readString()
        notification = parcel.readString()
        customer = parcel.readString()
        state = parcel.readByte()
    }

    override fun getContent(): Parcel {
        val parcel: Parcel = Parcel.obtain()

        parcel.writeString(code)
        parcel.writeString(notification)
        parcel.writeString(customer)
        parcel.writeByte(state)

        return parcel
    }

}
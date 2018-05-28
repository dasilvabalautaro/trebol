package com.hiddenodds.trebol.model.data

import android.os.Parcel
import com.hiddenodds.trebol.model.interfaces.IDataContent
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class Material: RealmObject(), IDataContent {
    @PrimaryKey
    var id: String = ""
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
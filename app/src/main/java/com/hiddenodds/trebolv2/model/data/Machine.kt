package com.hiddenodds.trebolv2.model.data

import android.os.Parcel
import com.hiddenodds.trebolv2.model.interfaces.IDataContent
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class Machine: RealmObject(), IDataContent {

    @PrimaryKey
    var id: String = ""
    var code: String = ""
    var compressor: String = ""
    var circuits: String = ""
    var others: String = ""



    override fun setContent(parcel: Parcel) {
        code = parcel.readString()
        compressor = parcel.readString()
        circuits = parcel.readString()
        others = parcel.readString()

    }

    override fun getContent(): Parcel {
        val parcel:Parcel = Parcel.obtain()

        parcel.writeString(code)
        parcel.writeString(compressor)
        parcel.writeString(circuits)
        parcel.writeString(others)

        return parcel
    }

}
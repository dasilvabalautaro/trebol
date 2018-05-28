package com.hiddenodds.trebol.model.data

import android.os.Parcel
import com.hiddenodds.trebol.model.interfaces.IDataContent
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey


open class AssignedMaterial: RealmObject(), IDataContent {
    @PrimaryKey
    var id: String = ""
    var quantity: Int = 0
    var material: Material? = null

    override fun setContent(parcel: Parcel) {
        quantity = parcel.readInt()
    }

    override fun getContent(): Parcel {
        val parcel:Parcel = Parcel.obtain()

        parcel.writeInt(quantity)

        return parcel
    }

}
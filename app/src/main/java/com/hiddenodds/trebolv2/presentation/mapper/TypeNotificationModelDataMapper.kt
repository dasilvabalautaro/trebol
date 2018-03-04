package com.hiddenodds.trebolv2.presentation.mapper

import android.content.Context
import com.hiddenodds.trebolv2.R
import com.hiddenodds.trebolv2.model.data.TypeNotification
import com.hiddenodds.trebolv2.presentation.model.TypeNotificationModel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TypeNotificationModelDataMapper @Inject constructor(val context: Context){
    fun transform(typeNotification: TypeNotification?): TypeNotificationModel {
        if (typeNotification == null)
            throw IllegalArgumentException(context.getString(R.string.value_null))
        val typeNotificationModel = TypeNotificationModel()
        typeNotificationModel.id = typeNotification.id
        typeNotificationModel.code = typeNotification.code
        typeNotificationModel.description = typeNotification.description

        return typeNotificationModel
    }

    fun transform(typeNotificationCollection: Collection<TypeNotification>?): 
            Collection<TypeNotificationModel>{
        val typeNotificationModelCollection:
                MutableCollection<TypeNotificationModel> = ArrayList()

        if (typeNotificationCollection != null &&
                !typeNotificationCollection.isEmpty())
            typeNotificationCollection.mapTo(typeNotificationModelCollection) {
                transform(it) }
        return typeNotificationModelCollection
    }
}
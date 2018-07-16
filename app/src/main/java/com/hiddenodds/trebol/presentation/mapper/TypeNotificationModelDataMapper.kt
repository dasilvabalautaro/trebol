package com.hiddenodds.trebol.presentation.mapper

import android.content.Context
import com.hiddenodds.trebol.R
import com.hiddenodds.trebol.model.data.Technical
import com.hiddenodds.trebol.model.data.TypeNotification
import com.hiddenodds.trebol.presentation.interfaces.IModel
import com.hiddenodds.trebol.presentation.interfaces.IModelDataMapper
import com.hiddenodds.trebol.presentation.model.TypeNotificationModel
import io.realm.RealmObject
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TypeNotificationModelDataMapper @Inject constructor(val context: Context):
        IModelDataMapper {
    override fun <E : RealmObject> transform(objectRealm: E?): IModel {
        if (objectRealm == null)
            throw IllegalArgumentException(context.getString(R.string.value_null))
        val typeNotification = objectRealm as TypeNotification
        val typeNotificationModel = TypeNotificationModel()
        typeNotificationModel.id = typeNotification.id
        typeNotificationModel.code = typeNotification.code
        typeNotificationModel.description = typeNotification.description

        return typeNotificationModel
    }

    override fun <E : RealmObject> transform(objectRealmCollection:
                                             Collection<E>?): Collection<IModel> {
        val typeNotificationCollection = objectRealmCollection!!
                .filterIsInstance<TypeNotification>() as Collection<TypeNotification>
        val typeNotificationModelCollection:
                MutableCollection<IModel> = ArrayList()

        if (!typeNotificationCollection.isEmpty())
            typeNotificationCollection.mapTo(typeNotificationModelCollection) {
                transform(it) }
        return typeNotificationModelCollection
    }


/*
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
*/
}
package com.hiddenodds.trebol.presentation.mapper

import android.content.Context
import com.hiddenodds.trebol.R
import com.hiddenodds.trebol.model.data.Technical
import com.hiddenodds.trebol.presentation.interfaces.IModel
import com.hiddenodds.trebol.presentation.interfaces.IModelDataMapper
import com.hiddenodds.trebol.presentation.model.NotificationModel
import com.hiddenodds.trebol.presentation.model.TechnicalModel
import io.realm.RealmObject
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TechnicalModelDataMapper @Inject constructor(val context: Context,
                                                   private val notificationModelDataMapper:
                                                   NotificationModelDataMapper):
        IModelDataMapper{
    override fun <E : RealmObject> transform(objectRealm: E?): IModel {
        if (objectRealm == null)
            throw IllegalArgumentException(context.getString(R.string.value_null))
        val technical = objectRealm as Technical
        val technicalModel = TechnicalModel()
        technicalModel.id = technical.id
        technicalModel.name = technical.name
        technicalModel.email = technical.email
        technicalModel.code = technical.code
        technicalModel.password = technical.password
        if (!technical.trd.isEmpty()){
            technicalModel.trd = technical.trd.toList() as ArrayList<String>
        }

        val notificationModelCollection: Collection<IModel> = this
                .notificationModelDataMapper
                .transform(technical.notifications)
        technicalModel.notifications = notificationModelCollection
                .filterIsInstance<NotificationModel>()
                as ArrayList<NotificationModel>

        return technicalModel
    }

    override fun <E : RealmObject> transform(objectRealmCollection:
                                             Collection<E>?): Collection<IModel> {
        val technicalCollection = objectRealmCollection!!
                .filterIsInstance<Technical>() as Collection<Technical>
        val technicalModelCollection: MutableCollection<IModel> = ArrayList()

        if (!technicalCollection.isEmpty())
            technicalCollection.mapTo(technicalModelCollection) { transform(it) }
        return technicalModelCollection
    }

}
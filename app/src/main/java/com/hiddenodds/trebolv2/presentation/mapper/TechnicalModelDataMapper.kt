package com.hiddenodds.trebolv2.presentation.mapper

import android.content.Context
import com.hiddenodds.trebolv2.R
import com.hiddenodds.trebolv2.model.data.Technical
import com.hiddenodds.trebolv2.presentation.model.NotificationModel
import com.hiddenodds.trebolv2.presentation.model.TechnicalModel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TechnicalModelDataMapper @Inject constructor(val context: Context,
                                                   private val notificationModelDataMapper:
                                                   NotificationModelDataMapper) {
    fun transform(technical: Technical?): TechnicalModel {
        if (technical == null)
            throw IllegalArgumentException(context.getString(R.string.value_null))
        val technicalModel = TechnicalModel()
        technicalModel.id = technical.id
        technicalModel.name = technical.name
        technicalModel.email = technical.email
        technicalModel.code = technical.code
        technicalModel.password = technical.password
        if (!technical.trd.isEmpty()){
            technicalModel.trd = technical.trd.toList() as ArrayList<String>
        }

        val notificationModelCollection: Collection<NotificationModel> = this
                .notificationModelDataMapper
                .transform(technical.notifications)
        technicalModel.notifications = notificationModelCollection
                as ArrayList<NotificationModel>
        
        return technicalModel
    }

    fun transform(technicalCollection: Collection<Technical>?): Collection<TechnicalModel>{
        val technicalModelCollection: MutableCollection<TechnicalModel> = ArrayList()

        if (technicalCollection != null && !technicalCollection.isEmpty())
            technicalCollection.mapTo(technicalModelCollection) { transform(it) }
        return technicalModelCollection
    }

}
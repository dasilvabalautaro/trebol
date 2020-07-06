package com.hiddenodds.trebol.presentation.mapper

import android.content.Context
import com.hiddenodds.trebol.R
import com.hiddenodds.trebol.model.data.Notification
import com.hiddenodds.trebol.presentation.interfaces.IModel
import com.hiddenodds.trebol.presentation.interfaces.IModelDataMapper
import com.hiddenodds.trebol.presentation.model.AssignedMaterialModel
import com.hiddenodds.trebol.presentation.model.NotificationModel
import io.realm.RealmObject
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificationModelDataMapper @Inject
                constructor(val context: Context,
                            private val customerModelDataMapper:
                            CustomerModelDataMapper,
                            private val assignedMaterialModelDataMapper:
                            AssignedMaterialModelDataMapper): IModelDataMapper {
    override fun <E : RealmObject> transform(objectRealm: E?): IModel {
        if (objectRealm == null)
            throw IllegalArgumentException(context.getString(R.string.value_null))
        val notification = objectRealm as Notification
        val notificationModel = NotificationModel()
        notificationModel.id = notification.id
        notificationModel.code = notification.code
        notificationModel.address = notification.address
        notificationModel.businessName = notification.businessName
        notificationModel.completed = notification.completed
        notificationModel.dateCompleted = notification.dateCompleted
        notificationModel.dateEnd = notification.dateEnd
        notificationModel.dateInit = notification.dateInit
        notificationModel.diet = notification.diet
        notificationModel.displacement = notification.displacement
        notificationModel.duration = notification.duration
        notificationModel.hours = notification.hours
        notificationModel.idTech = notification.idTech
        notificationModel.ink = notification.ink
        notificationModel.inside = notification.inside
        notificationModel.lastAmount = notification.lastAmount
        notificationModel.locality = notification.locality
        notificationModel.machine = notification.machine
        notificationModel.observations = notification.observations
        notificationModel.outside = notification.outside
        notificationModel.peaje = notification.peaje
        notificationModel.product = notification.product
        notificationModel.province = notification.province
        notificationModel.reportTechnical = notification.reportTechnical
        notificationModel.satd = notification.satd
        notificationModel.satdk = notification.satdk
        notificationModel.series = notification.series
        notificationModel.state = notification.state
        notificationModel.symptom = notification.symptom
        notificationModel.totalTeam = notification.totalTeam
        notificationModel.type = notification.type
        notificationModel.trbPartTwo = notification.trbPartTwo
        notificationModel.trade = notification.trade
        notificationModel.vSoft1 = notification.vSoft1
        notificationModel.vSoft2 = notification.vSoft2
        notificationModel.vSoft3 = notification.vSoft3
        notificationModel.workHours = notification.workHours

        if (notification.customer != null){
            notificationModel.customer = customerModelDataMapper
                    .transform(notification.customer)
        }

        var assignedMaterialModelCollection: Collection<AssignedMaterialModel> = this
                .assignedMaterialModelDataMapper
                .transform(notification.materialUse)
        notificationModel.materialUse = assignedMaterialModelCollection
                as ArrayList<AssignedMaterialModel>
        assignedMaterialModelCollection = this
                .assignedMaterialModelDataMapper
                .transform(notification.materialOut)
        notificationModel.materialOut = assignedMaterialModelCollection
                as ArrayList<AssignedMaterialModel>

        return notificationModel

    }

    override fun <E : RealmObject> transform(objectRealmCollection: Collection<E>?): Collection<IModel> {
        val notificationCollection = objectRealmCollection!!
                .filterIsInstance<Notification>() as Collection<Notification>
        val notificationModelCollection: MutableCollection<IModel> = ArrayList()

        if (!notificationCollection.isEmpty())
            notificationCollection.mapTo(notificationModelCollection) { transform(it) }
        return notificationModelCollection
    }

}
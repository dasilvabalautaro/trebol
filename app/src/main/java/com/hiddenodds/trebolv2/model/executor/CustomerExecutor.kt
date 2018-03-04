package com.hiddenodds.trebolv2.model.executor

import android.os.Parcel
import com.hiddenodds.trebolv2.App
import com.hiddenodds.trebolv2.dagger.ModelsModule
import com.hiddenodds.trebolv2.domain.data.MapperCustomer
import com.hiddenodds.trebolv2.model.data.Customer
import com.hiddenodds.trebolv2.model.exception.DatabaseOperationException
import com.hiddenodds.trebolv2.model.interfaces.ICustomerRepository
import com.hiddenodds.trebolv2.model.persistent.database.CRUDRealm
import com.hiddenodds.trebolv2.presentation.mapper.CustomerModelDataMapper
import com.hiddenodds.trebolv2.presentation.model.CustomerModel
import io.reactivex.Observable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CustomerExecutor @Inject constructor(): CRUDRealm(),
        ICustomerRepository {

    private val context = App.appComponent.context()

    private val component by lazy {(context as App)
            .getAppComponent().plus(ModelsModule(context))}

    @Inject
    lateinit var taskListenerExecutor: TaskListenerExecutor
    @Inject
    lateinit var customerModelDataMapper: CustomerModelDataMapper

    init {
        component.inject(this)
    }

    override fun userGetError(): Observable<DatabaseOperationException> {
        return this.taskListenerExecutor
                .observableException.map { e -> e }

    }

    override fun userGetMessage(): Observable<String>{
        return this.taskListenerExecutor
                .observableMessage.map { s -> s }
    }

    override fun save(customer: MapperCustomer): Observable<CustomerModel> {
        val parcel: Parcel = customer.getContent()
        parcel.setDataPosition(0)

        return Observable.create { subscriber ->

            val clazz: Class<Customer> = Customer::class.java

            val newCustomer = this.save(clazz, parcel, taskListenerExecutor)
            if (newCustomer != null){
                val customerModel = this.customerModelDataMapper
                        .transform(newCustomer)

                subscriber.onNext(customerModel)
                subscriber.onComplete()
            }else{
                subscriber.onError(Throwable())
            }

        }

    }

}
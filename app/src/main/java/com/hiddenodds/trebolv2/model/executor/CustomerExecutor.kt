package com.hiddenodds.trebolv2.model.executor

import android.os.Parcel
import com.hiddenodds.trebolv2.App
import com.hiddenodds.trebolv2.dagger.ModelsModule
import com.hiddenodds.trebolv2.domain.data.MapperCustomer
import com.hiddenodds.trebolv2.model.data.Customer
import com.hiddenodds.trebolv2.model.interfaces.ICustomerRepository
import com.hiddenodds.trebolv2.model.persistent.database.CRUDRealm
import com.hiddenodds.trebolv2.presentation.mapper.CustomerModelDataMapper
import com.hiddenodds.trebolv2.presentation.model.CustomerModel
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CustomerExecutor @Inject constructor(): CRUDRealm(),
        ICustomerRepository {

    private val context = App.appComponent.context()

    private val component by lazy {(context as App)
            .getAppComponent().plus(ModelsModule(context))}
    private var disposable: CompositeDisposable = CompositeDisposable()
    private var msgError: String = ""
    @Inject
    lateinit var taskListenerExecutor: TaskListenerExecutor
    @Inject
    lateinit var customerModelDataMapper: CustomerModelDataMapper

    init {
        component.inject(this)
        val hear = this.taskListenerExecutor
                .observableException.map { s -> s }
        disposable.add(hear.observeOn(Schedulers.newThread())
                .subscribe { s ->
                    this.msgError = s.message
                })

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
                subscriber.onError(Throwable(this.msgError))
            }

        }

    }

    override fun saveList(list: ArrayList<MapperCustomer>): Observable<Boolean> {
        var flag = true
        return Observable.create{subscriber ->
            for (i in list.indices){
                val parcel: Parcel = list[i].getContent()
                parcel.setDataPosition(0)

                val clazz: Class<Customer> = Customer::class.java

                val newCustomer = this.save(clazz, parcel, taskListenerExecutor)
                if (newCustomer == null){
                    flag = false
                    break
                }
            }
            if (flag){
                subscriber.onNext(true)
                subscriber.onComplete()
            }else{
                subscriber.onError(Throwable(this.msgError))
            }
        }
    }

    override fun deleteCustomerOfTechnical(code: String, fieldName: String):
            Observable<Boolean> {
        return Observable.create{subscriber ->

            val clazz: Class<Customer> = Customer::class.java

            if (this.deleteByField(clazz, fieldName,
                            code, taskListenerExecutor)){
                subscriber.onNext(true)
                subscriber.onComplete()
            }else{
                subscriber.onError(Throwable(this.msgError))
            }
        }
    }

}
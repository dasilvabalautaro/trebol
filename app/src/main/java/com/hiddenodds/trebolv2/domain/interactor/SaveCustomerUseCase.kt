package com.hiddenodds.trebolv2.domain.interactor

import com.hiddenodds.trebolv2.domain.data.MapperCustomer
import com.hiddenodds.trebolv2.domain.interfaces.IHearMessage
import com.hiddenodds.trebolv2.model.exception.DatabaseOperationException
import com.hiddenodds.trebolv2.model.interfaces.ICustomerRepository
import com.hiddenodds.trebolv2.model.interfaces.IPostExecutionThread
import com.hiddenodds.trebolv2.model.interfaces.IThreadExecutor
import com.hiddenodds.trebolv2.presentation.model.CustomerModel
import io.reactivex.Observable
import javax.inject.Inject


class SaveCustomerUseCase @Inject constructor(threadExecutor: IThreadExecutor,
                                              postExecutionThread: IPostExecutionThread,
                                              private var customerRepository: ICustomerRepository):
        UseCase<CustomerModel>(threadExecutor, postExecutionThread),
        IHearMessage {

    var customer: MapperCustomer = MapperCustomer()

    override fun hearMessage(): Observable<String> {
        return this.customerRepository.userGetMessage()
    }

    override fun hearError(): Observable<DatabaseOperationException> {
        return this.customerRepository.userGetError()
    }

    override fun buildUseCaseObservable(): Observable<CustomerModel> {
        return this.customerRepository.save(customer)
    }

}
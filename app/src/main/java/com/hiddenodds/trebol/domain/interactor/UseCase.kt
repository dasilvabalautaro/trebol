package com.hiddenodds.trebol.domain.interactor

import com.hiddenodds.trebol.model.interfaces.IPostExecutionThread
import com.hiddenodds.trebol.model.interfaces.IThreadExecutor
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers

abstract class UseCase<T> constructor(private val threadExecutor:
                                      IThreadExecutor,
                                      private val postExecutionThread:
                                      IPostExecutionThread) {

    private var disposable: CompositeDisposable = CompositeDisposable()

    protected abstract fun buildUseCaseObservable(): Observable<T>

    fun execute(observer: DisposableObserver<T>) {
        val observable: Observable<T> = this.buildUseCaseObservable()
                .subscribeOn(Schedulers.from(threadExecutor))
                .observeOn(postExecutionThread.scheduler)
        disposable.add(observable.subscribeWith(observer))
    }


    fun dispose() {
        if (!disposable.isDisposed) disposable.dispose()
    }

}
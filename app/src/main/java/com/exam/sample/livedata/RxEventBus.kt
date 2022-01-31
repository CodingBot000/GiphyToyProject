package com.exam.sample.livedata

import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

/*
 RxEventBus는 코드의 복잡성을 높일 리스크가 커서
 기존에 사용하던걸 모두 제거하였음. 현재 사용하고있지 않음
 */
object RxEventBus {

    private val mSubject = PublishSubject.create<String>()

    fun sendEvent(str: String) {
        mSubject.onNext(str)
    }

    fun getObservable(): Observable<String> {
        return mSubject
    }
}

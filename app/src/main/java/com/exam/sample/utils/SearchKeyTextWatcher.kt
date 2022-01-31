package com.exam.sample.utils

import android.text.Editable
import android.text.TextWatcher
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit

class SearchKeyTextWatcher : TextWatcher {
    private val textChangeSubject = PublishSubject.create<String>()

    init {
        textChangeSubject.onNext("")
    }

    override fun afterTextChanged(s: Editable?) {
        if (s.isNullOrEmpty())
            textChangeSubject.onNext("")
        else
            textChangeSubject.onNext(s.toString())
    }

    override fun beforeTextChanged(p0: CharSequence?, start: Int, count: Int, after: Int) {}
    override fun onTextChanged(p0: CharSequence?, start: Int, before: Int, count: Int) {}

    fun getSearchKeyChangeObserver(): Observable<String> {
        return textChangeSubject.debounce(Const.EMIT_INTERVAL, TimeUnit.MILLISECONDS)
            .distinctUntilChanged()
    }
}

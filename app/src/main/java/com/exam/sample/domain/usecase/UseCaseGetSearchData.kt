package com.exam.sample.domain.usecase

import com.exam.sample.domain.usecase.base.SingleUseCase
import com.exam.sample.model.data.TrendingData
import com.exam.sample.model.repository.search.SearchRepository
import io.reactivex.Single
import javax.inject.Inject

class UseCaseGetSearchData @Inject constructor(private val searchRepository: SearchRepository) :
    SingleUseCase<TrendingData>() {

    private var offset: Int = 0
    private lateinit var keyword: String

    fun setData(keyword: String, offset: Int) {
        this.offset = offset
        this.keyword = keyword
    }

    override fun buildUseCaseSingle(): Single<TrendingData> =
        searchRepository.requestSearchData(keyword, offset)
}

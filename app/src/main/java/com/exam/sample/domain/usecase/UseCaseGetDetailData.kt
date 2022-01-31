package com.exam.sample.domain.usecase

import com.exam.sample.domain.usecase.base.SingleUseCase
import com.exam.sample.model.data.TrendingData
import com.exam.sample.model.repository.detail.DetailDataRepository
import io.reactivex.Single
import javax.inject.Inject

class UseCaseGetDetailData @Inject constructor(private val detailDataRepository: DetailDataRepository) :
    SingleUseCase<TrendingData>() {

    private lateinit var dataId: String

    fun setData(id: String) {
        dataId = id
    }

    override fun buildUseCaseSingle(): Single<TrendingData> =
        detailDataRepository.requestDetailData(dataId)
}

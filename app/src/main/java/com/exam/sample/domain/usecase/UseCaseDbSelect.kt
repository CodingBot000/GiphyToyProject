package com.exam.sample.domain.usecase

import com.exam.sample.domain.usecase.base.SingleUseCase
import com.exam.sample.model.data.FavoriteInfo
import com.exam.sample.model.repository.favorite.FavoriteInfoRepository
import io.reactivex.Single
import javax.inject.Inject

class UseCaseDbSelect @Inject constructor(private val favoriteInfoRepository: FavoriteInfoRepository) :
    SingleUseCase<FavoriteInfo>() {

    private lateinit var userId: String

    fun setData(userId: String) {
        this.userId = userId
    }

    override fun buildUseCaseSingle(): Single<FavoriteInfo> =
        favoriteInfoRepository.getFavoriteDB(userId)
}

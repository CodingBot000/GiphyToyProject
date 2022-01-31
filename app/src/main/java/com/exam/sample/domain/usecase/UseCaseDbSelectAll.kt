package com.exam.sample.domain.usecase

import com.exam.sample.domain.usecase.base.SingleUseCase
import com.exam.sample.model.data.FavoriteInfo
import com.exam.sample.model.repository.favorite.FavoriteInfoRepository
import io.reactivex.Single
import javax.inject.Inject

class UseCaseDbSelectAll @Inject constructor(private val favoriteInfoRepository: FavoriteInfoRepository) :
    SingleUseCase<List<FavoriteInfo>>() {

    override fun buildUseCaseSingle(): Single<List<FavoriteInfo>> =
        favoriteInfoRepository.getFavoriteAllDB()
}

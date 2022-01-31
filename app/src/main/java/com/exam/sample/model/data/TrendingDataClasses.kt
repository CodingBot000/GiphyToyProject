package com.exam.sample.model.data

import com.google.gson.annotations.SerializedName

/**
 * api data class
 */
data class TrendingData(
    @SerializedName("data") val trendingItems: ArrayList<TrendingDetail>,
    val pagination: PaginationData,
    val meta: MetaData,
    var isAddData: Boolean
)

data class TrendingDetail(
    val type: String,
    val id: String,
    val username: String,
    val title: String,
    val embed_url: String,
    val images: ImagesData
) {
    fun convertInteractionData(): InteractionData
        = InteractionData(
            id, type, username, title, embed_url,
            images.downsized.url, images.fixed_width_small.url
        )

}

data class ImagesData(
    val original: OriginalData,
    val downsized: DownSizedData,
    val fixed_width_small: FixedWidthSmallData,
    val fixed_height_downsampled: FixedHeightDownSampledData
)

data class OriginalData(val height: String, val width: String, val url: String)
data class DownSizedData(val height: String, val width: String, val size: String, val url: String)
data class FixedWidthSmallData(val height: String?, val width: String?, val url: String)
data class FixedHeightDownSampledData(val height: String?, val width: String?, val url: String)
data class PaginationData(val total_count: Int, val count: Int, val offset: Int)
data class MetaData(val status: Int, val msg: String, val response_id: String)

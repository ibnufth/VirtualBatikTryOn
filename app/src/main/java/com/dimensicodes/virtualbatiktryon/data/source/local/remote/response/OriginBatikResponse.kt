package com.dimensicodes.virtualbatiktryon.data.source.local.remote.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class OriginBatikResponse(

    @field:SerializedName("origin")
    val origin: List<OriginItem?>? = null
)

@Parcelize
data class OriginItem(
    @field:SerializedName("id")
    val id: String? = null,

    @field:SerializedName("name")
    val name: String? = null

) : Parcelable

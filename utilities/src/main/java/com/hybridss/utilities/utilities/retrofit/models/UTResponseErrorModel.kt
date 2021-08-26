package com.hybridss.utilities.utilities.retrofit.models

import com.google.gson.annotations.SerializedName

data class UTResponseErrorModel(
    @SerializedName("codigo") val code: String? = "",
    @SerializedName("mensaje") val message: String? = ""
)
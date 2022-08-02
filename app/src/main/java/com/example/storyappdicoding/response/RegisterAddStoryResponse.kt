package com.example.storyappdicoding.response

import com.google.gson.annotations.SerializedName

data class RegisterAddStoryResponse(

	@field:SerializedName("error")
	val error: Boolean,

	@field:SerializedName("message")
	val message: String
)

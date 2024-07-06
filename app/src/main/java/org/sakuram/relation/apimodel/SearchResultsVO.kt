package org.sakuram.relation.apimodel

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class SearchResultsVO (
    val countInDb: Int,
    val resultsList: List<List<String>>,
    val queryToDb: String
)
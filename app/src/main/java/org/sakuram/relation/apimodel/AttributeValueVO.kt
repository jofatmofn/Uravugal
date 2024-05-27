package org.sakuram.relation.apimodel

import com.squareup.moshi.JsonClass
import java.sql.Date

@JsonClass(generateAdapter = true)
class AttributeValueVO(
    val id: Long?,
    val attributeDvId: Long,
    val attributeName: String,
    val attributeValue: String,
    val attributeValueList: List<String>?,
    val translatedValue: String?,
    val valueApproximate: Boolean,
    val startDate: Date?,
    val endDate: Date?,
    val isPrivate: Boolean?,
    val maybeNotRegistered: Boolean,
    val avValue: String?,
)
package org.sakuram.relation.apimodel

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class RetrieveRelationAttributesResponseVO(
    val person1GenderDVId: Long?,
    val person2GenderDVId: Long?,
    val attributeValueVOList: List<AttributeValueVO>,
    val person1Id: Long?,
    val relationGroup: String?,
)
package org.sakuram.relation.apimodel

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class PersonSearchCriteriaVO (
    val leninent: Boolean,
    val attributeValueVOList: List<AttributeValueVO>,
) {
    constructor(): this( false, emptyList<AttributeValueVO>() )
}
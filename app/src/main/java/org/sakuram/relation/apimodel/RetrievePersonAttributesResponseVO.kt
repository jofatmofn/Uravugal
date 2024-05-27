package org.sakuram.relation.apimodel

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class RetrievePersonAttributesResponseVO(
    val photo: ByteArray?,
    val label: String,
    val attributeValueVOList: List<AttributeValueVO>,
    val manageAccess: Boolean
)


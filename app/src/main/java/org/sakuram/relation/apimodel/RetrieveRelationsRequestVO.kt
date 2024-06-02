package org.sakuram.relation.apimodel

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class RetrieveRelationsRequestVO (
    val startPersonId: Long,
    val maxDepth: Short?,
    val requiredRelationsList: List<String>?,
    val exportTreeType: String?,
) {
    constructor(startPersonId: Long) : this(startPersonId, null, null, null )
}

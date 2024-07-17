package org.sakuram.relation.apimodel

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class RelatedPersonsVO (
    val person1Id: Long,
    val person2Id: Long,
    val person1ForPerson2: String?,
    val creatorId: Long?,
    val sourceId: Long?,
    val excludeRelationIdCsv: String?,
    val excludePersonIdCsv: String?
) {
    constructor(person1Id: Long, person2Id: Long, excludeRelationIdCsv: String, excludePersonIdCsv: String):
            this(person1Id = person1Id, person2Id = person2Id, person1ForPerson2 = null, creatorId = null, sourceId = null, excludeRelationIdCsv = excludeRelationIdCsv, excludePersonIdCsv = excludePersonIdCsv )
}

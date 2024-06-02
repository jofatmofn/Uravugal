package org.sakuram.relation.apimodel

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class GraphVO (
    val nodes: List<PersonVO>,
    val edges: List<RelationVO>,
)
package org.sakuram.relation.apimodel

class RelationVO (
    val id: Long,
    val source: String,
    val target: String,
    val label: String?,
    val size: Double,
    val type: String?,
)
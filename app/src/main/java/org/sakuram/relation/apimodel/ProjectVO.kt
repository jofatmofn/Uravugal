package org.sakuram.relation.apimodel

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class ProjectVO (
    val tenantId: Long,
    val projectName: String,
    val appReadOnly: Boolean,
    val personCount: Long,
)
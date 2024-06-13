package org.sakuram.relation.viewmodel

data class DetailsTabUiState (
    var attributeValueList: List<AttributeValue>,
    val detailsOf: Int,
    val title: String,
)

data class AttributeValue (
    val attributeName: String,
    val attributeValue: String,
)
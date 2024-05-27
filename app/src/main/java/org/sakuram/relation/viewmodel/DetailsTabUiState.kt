package org.sakuram.relation.viewmodel

data class DetailsTabUiState (
    var attributeValueList: List<AttributeValue>,
)

data class AttributeValue (
    val attributeName: String,
    val attributeValue: String,
)
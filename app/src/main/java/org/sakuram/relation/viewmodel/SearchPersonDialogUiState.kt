package org.sakuram.relation.viewmodel

data class SearchPersonDialogUiState (
    var searchCriteria: List<SearchCriterion>,
    val isLenient: Boolean
)

data class SearchCriterion (
    val attributeDvId: Long,
    val selectedAttributeIndex: Int,
    val attributeValue: String,
    val selectedValueIndex: Int,
    val isMaybeNotRegistered: Boolean,
)

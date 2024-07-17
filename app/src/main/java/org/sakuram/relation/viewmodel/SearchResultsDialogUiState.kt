package org.sakuram.relation.viewmodel

data class SearchResultsDialogUiState (
    val tableContent: List<List<String>>?,
    val selectedRowIndex: Int,   // tableContent includes header also, but this index excludes that
)

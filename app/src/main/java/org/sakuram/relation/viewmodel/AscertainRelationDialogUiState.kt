package org.sakuram.relation.viewmodel

data class AscertainRelationDialogUiState (
    val selectedPerson1Index: Int,
    val selectedPerson2Index: Int,
    val excludeRelationIdCsv: String,
    val excludePersonIdCsv: String
)

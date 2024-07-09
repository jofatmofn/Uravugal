package org.sakuram.relation.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.sakuram.relation.apimodel.PersonSearchCriteriaVO
import org.sakuram.relation.repository.PersonRelationApiRepository

class SearchPersonDialogViewModel: ViewModel() {
    private val _searchCriteria = mutableStateListOf<SearchCriterion>()
    private var searchCriteria: List<SearchCriterion> = _searchCriteria
    private val _searchPersonDialogUiState = MutableStateFlow(SearchPersonDialogUiState(searchCriteria, false))
    val searchPersonDialogUiState = _searchPersonDialogUiState.asStateFlow()
    private val _searchResultsDialogUiState = MutableStateFlow(SearchResultsDialogUiState(emptyList(), -1))
    val searchResultsDialogUiState = _searchResultsDialogUiState.asStateFlow()

    fun uiToStateIsLenient(isLenient: Boolean) {
        _searchPersonDialogUiState.update {
            it.copy(
                isLenient = isLenient
            )
        }
    }

    fun uiToStateSelectedAttributeIndex(searchCriterionindex: Int, selectedAttributeIndex: Int) {
        _searchCriteria[searchCriterionindex] = _searchCriteria[searchCriterionindex].copy(
            selectedAttributeIndex = selectedAttributeIndex,
        )
    }

    fun uiToStateAttributeDvId(searchCriterionindex: Int, attributeDvId: Long) {
        _searchCriteria[searchCriterionindex] = _searchCriteria[searchCriterionindex].copy(
            attributeDvId = attributeDvId,
        )
    }

    fun uiToStateAttributeValue(searchCriterionindex: Int, selectedValueIndex: Int, attributeValue: String) {
        _searchCriteria[searchCriterionindex] = _searchCriteria[searchCriterionindex].copy(
            selectedValueIndex = selectedValueIndex,
            attributeValue = attributeValue,
        )
    }

    fun uiToStateIsMaybeNotRegistered(searchCriterionindex: Int, isMaybeNotRegistered: Boolean) {
        _searchCriteria[searchCriterionindex] = _searchCriteria[searchCriterionindex].copy(
            isMaybeNotRegistered = isMaybeNotRegistered,
        )
    }

    fun addSearchCriterion() {
        _searchCriteria += SearchCriterion(-1, -1, "", -1, false)
    }

    fun deleteSearchCriterion(searchCriterionindex: Int) {
        _searchCriteria -= _searchCriteria[searchCriterionindex]
    }

    fun uiToStateSelectedRowIndex(selectedRowIndex: Int) {
        _searchResultsDialogUiState.update {
            it.copy(
                selectedRowIndex = selectedRowIndex
            )
        }
    }

    fun searchPerson(personSearchCriteriaVO: PersonSearchCriteriaVO) {
        /* job?.cancel()
        job = */ viewModelScope.launch {
            coroutineScope {
                val searchResultsVO = PersonRelationApiRepository.searchPerson(personSearchCriteriaVO)
                _searchResultsDialogUiState.update {
                    it.copy(
                        tableContent = searchResultsVO?.resultsList ?: emptyList(),
                        selectedRowIndex = -1
                    )
                }
            }
        }
    }


}
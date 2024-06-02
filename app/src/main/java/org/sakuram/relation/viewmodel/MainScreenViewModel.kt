package org.sakuram.relation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.sakuram.relation.apimodel.AttributeValueVO
import org.sakuram.relation.apimodel.ProjectVO
import org.sakuram.relation.apimodel.RetrieveRelationsRequestVO
import org.sakuram.relation.repository.PersonRelationApiRepository
import org.sakuram.relation.repository.ProjectUserApiRepository

class MainScreenViewModel: ViewModel() {
    private val _mainScreenUiState = MutableStateFlow(MainScreenUiState())
    val mainScreenUiState = _mainScreenUiState.asStateFlow()
    private val _graphTabUiState = MutableStateFlow(GraphTabUiState(emptyMap<String, Node>(), emptyList<Edge>()))
    val graphTabUiState = _graphTabUiState.asStateFlow()
    private val _detailsTabUiState = MutableStateFlow(DetailsTabUiState(emptyList<AttributeValue>()))
    val detailsTabUiState = _detailsTabUiState.asStateFlow()
    // private var job: Job? = null

    fun switchProject(projectId: String): ProjectVO? =
        ProjectUserApiRepository.switchProject(projectId)

    fun retrievePersonAttributes(entityId: Long) {
        /* job?.cancel()
        job = */ viewModelScope.launch {
            coroutineScope {
                val personDetails = PersonRelationApiRepository.retrievePersonAttributes(entityId)
                _detailsTabUiState.update {
                    it.copy(attributeValueList = personDetails?.attributeValueVOList?.map { attributeValueVO: AttributeValueVO ->
                        AttributeValue(
                            attributeValueVO.attributeName,
                            attributeValueVO.attributeValue
                        )
                    } ?: emptyList<AttributeValue>())
                }
            }
        }
    }

    fun retrieveRelations(retrieveRelationsRequestVO: RetrieveRelationsRequestVO) {
        /* job?.cancel()
        job = */ viewModelScope.launch {
            coroutineScope {
                println("In MODEL, PersonId: ${retrieveRelationsRequestVO.startPersonId}")
                val graphVO = PersonRelationApiRepository.retrieveRelations(retrieveRelationsRequestVO)
                _graphTabUiState.update {
                    it.copy(
                        nodesMap = graphVO?.nodes?.associate
                        {
                            Pair(
                                it.id,
                                Node(it.label, it.x, it.y)
                            )
                        },
                        edgesList = graphVO?.edges?.map
                        {
                            Edge(
                                it.source,
                                it.target,
                                it.label
                            )
                        },
                    )
                }
            }
        }
    }

    fun projectSwitched(projectName: String) {
        // This too works, but not recommended: _uiState.value = MainScreenUiState(projectName = projectName)
        _mainScreenUiState.update {
            it.copy(projectName = projectName)
        }
    }
}
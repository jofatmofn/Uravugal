package org.sakuram.relation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.sakuram.relation.apimodel.AttributeValueVO
import org.sakuram.relation.apimodel.DomainValueVO
import org.sakuram.relation.apimodel.GraphVO
import org.sakuram.relation.apimodel.PersonVO
import org.sakuram.relation.apimodel.ProjectVO
import org.sakuram.relation.apimodel.RelatedPersonsVO
import org.sakuram.relation.apimodel.RetrieveRelationsRequestVO
import org.sakuram.relation.repository.AlgoApiRepository
import org.sakuram.relation.repository.PersonRelationApiRepository
import org.sakuram.relation.repository.ProjectUserApiRepository
import org.sakuram.relation.utility.AppValues
import org.sakuram.relation.utility.Constants
import org.sakuram.relation.utility.Constants.PERSON_NODE_SCALE_X
import org.sakuram.relation.utility.Constants.PERSON_NODE_SCALE_Y
import org.sakuram.relation.utility.Constants.PERSON_NODE_SIZE_X
import org.sakuram.relation.utility.Constants.PERSON_NODE_SIZE_Y

class MainScreenViewModel: ViewModel() {
    private val _mainScreenUiState = MutableStateFlow(MainScreenUiState())
    val mainScreenUiState = _mainScreenUiState.asStateFlow()

    private val _graphTabUiState = MutableStateFlow(GraphTabUiState(emptyMap<Long, Node>(), emptyMap<String, Edge>()))
    val graphTabUiState = _graphTabUiState.asStateFlow()

    private val _detailsTabUiState = MutableStateFlow(DetailsTabUiState(emptyList<AttributeValue>(), Constants.DETAILS_OF_NODE, ""))
    val detailsTabUiState = _detailsTabUiState.asStateFlow()

    private val _ascertainRelationDialogUiState = MutableStateFlow(AscertainRelationDialogUiState(-1, -1, "", ""))
    val ascertainRelationDialogUiState = _ascertainRelationDialogUiState.asStateFlow()

    // private var job: Job? = null

    fun switchProject(projectId: String): ProjectVO? =
        ProjectUserApiRepository.switchProject(projectId)

    fun switchTab(tabIndex: Int) {
        _mainScreenUiState.update {
            it.copy(
                tabIndex = tabIndex
            )
        }
    }

    fun setNodeDetails(label: String) {
        _detailsTabUiState.update {
            it.copy(
                detailsOf = Constants.DETAILS_OF_NODE,
                title = "Person: $label"
            )
        }
    }

    fun setEdgeDetails(label: String, node1Label: String, node2Label: String) {
        _detailsTabUiState.update {
            it.copy(
                detailsOf = Constants.DETAILS_OF_EDGE,
                title = "Relation: $label between $node1Label and $node2Label"
            )
        }
    }

    fun uiToStateSelectedPerson1Index(selectedPerson1Index: Int) {
        _ascertainRelationDialogUiState.update {
            it.copy(
                selectedPerson1Index = selectedPerson1Index
            )
        }
    }

    fun uiToStateSelectedPerson2Index(selectedPerson2Index: Int) {
        _ascertainRelationDialogUiState.update {
            it.copy(
                selectedPerson2Index = selectedPerson2Index
            )
        }
    }

    fun uiToStateExcludeRelationIdCsv(excludeRelationIdCsv: String) {
        _ascertainRelationDialogUiState.update {
            it.copy(
                excludeRelationIdCsv = excludeRelationIdCsv
            )
        }
    }

    fun uiToStateExcludePersonIdCsv(excludePersonIdCsv: String) {
        _ascertainRelationDialogUiState.update {
            it.copy(
                excludePersonIdCsv = excludePersonIdCsv
            )
        }
    }

    fun retrievePersonAttributes(entityId: Long) {
        /* job?.cancel()
        job = */ viewModelScope.launch {
            coroutineScope {
                val personDetails = PersonRelationApiRepository.retrievePersonAttributes(entityId)
                _detailsTabUiState.update {
                    it.copy(attributeValueList = personDetails?.attributeValueVOList?.map { attributeValueVO: AttributeValueVO ->
                        AttributeValue(
                            attributeValueVO.attributeName,
                            getAttributeValue(attributeValueVO),
                        )
                    } ?: emptyList<AttributeValue>())
                }
            }
        }
    }

    fun retrieveRelationAttributes(entityId: Long) {
        /* job?.cancel()
        job = */ viewModelScope.launch {
            coroutineScope {
                val relationDetails = PersonRelationApiRepository.retrieveRelationAttributes(entityId)
                _detailsTabUiState.update {
                    it.copy(attributeValueList = relationDetails?.attributeValueVOList?.map { attributeValueVO: AttributeValueVO ->
                        AttributeValue(
                            attributeValueVO.attributeName,
                            getAttributeValue(attributeValueVO),
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
                val graphVO = PersonRelationApiRepository.retrieveRelations(retrieveRelationsRequestVO)
                graphVOToGraphTabUiState(graphVO!!)
            }
        }
    }

    fun retrieveRelationPath(relatedPersonsVO: RelatedPersonsVO) {
        /* job?.cancel()
        job = */ viewModelScope.launch {
            coroutineScope {
                val graphVO = AlgoApiRepository.retrieveRelationPath(relatedPersonsVO)
                graphVOToGraphTabUiState(graphVO!!)
            }
        }
    }

    private fun graphVOToGraphTabUiState(graphVO: GraphVO) {
        _graphTabUiState.update {
            it.copy(
                nodesMap = graphVO.nodes.associate
                {personVO ->
                    Pair(
                        personVO.id.toLong(),
                        Node(
                            personVO.label.toString(),
                            personVO.x.toFloat() * PERSON_NODE_SCALE_X,
                            personVO.x.toFloat() * PERSON_NODE_SCALE_X + PERSON_NODE_SIZE_X,
                            personVO.y.toFloat() * PERSON_NODE_SCALE_Y,
                            personVO.y.toFloat() * PERSON_NODE_SCALE_Y + PERSON_NODE_SIZE_Y
                        )
                    )
                },
            )
        }
        _graphTabUiState.update {
            it.copy(
                edgesMap = graphVO.edges.associate
                {relationVO ->
                    val sourceNode: Node? = _graphTabUiState.value.nodesMap?.get(relationVO.source.toLong())
                    val targetNode: Node? = _graphTabUiState.value.nodesMap?.get(relationVO.target.toLong())
                    var xStart = 0.0f
                    var xEnd = 0.0f
                    var yStart = 0.0f
                    var yEnd = 0.0f
                    if (sourceNode != null && targetNode != null) {
                        if (sourceNode.yStart < targetNode.yStart) {
                            yStart = sourceNode.yEnd
                            yEnd = targetNode.yStart
                            xStart = (sourceNode.xStart + sourceNode.xEnd) / 2
                            xEnd = (targetNode.xStart + targetNode.xEnd) / 2
                        } else if (sourceNode.yStart == targetNode.yStart) {
                            yStart = (sourceNode.yStart + sourceNode.yEnd) / 2
                            yEnd = (sourceNode.yStart + sourceNode.yEnd) / 2
                            if (sourceNode.xStart < targetNode.xStart) {
                                xStart = sourceNode.xEnd
                                xEnd = targetNode.xStart
                            } else {
                                xStart = targetNode.xEnd
                                xEnd = sourceNode.xStart
                            }
                        } else {
                            yStart = targetNode.yEnd
                            yEnd = sourceNode.yStart
                            xStart = (targetNode.xStart + targetNode.xEnd) / 2
                            xEnd = (sourceNode.xStart + sourceNode.xEnd) / 2
                        }
                    }
                    Pair(
                        relationVO.id,
                        Edge(
                            relationVO.source.toLong(),
                            relationVO.target.toLong(),
                            relationVO.label.toString(),
                            xStart,
                            xEnd,
                            yStart,
                            yEnd
                        )
                    )
                },
            )
        }
    }

    fun addNodeToGraph(personVO: PersonVO) {
        _graphTabUiState.update {
            it.copy(
                nodesMap = _graphTabUiState.value.nodesMap?.plus(
                    mapOf<Long, Node>(
                        personVO.id.toLong() to
                        Node(
                            personVO.label.toString(),
                            personVO.x.toFloat() * PERSON_NODE_SCALE_X,
                            personVO.x.toFloat() * PERSON_NODE_SCALE_X + PERSON_NODE_SIZE_X,
                            personVO.y.toFloat() * PERSON_NODE_SCALE_Y,
                            personVO.y.toFloat() * PERSON_NODE_SCALE_Y + PERSON_NODE_SIZE_Y
                        )
                    )
                )
            )
        }
    }

    fun retrieveAppStartValues() {
        val retrieveAppStartValuesResponseVO = PersonRelationApiRepository.retrieveAppStartValues()
        AppValues.domainValueVOMap = retrieveAppStartValuesResponseVO?.domainValueVOList!!.associateBy({it.id}, {it})
        retrieveAppStartValuesResponseVO.domainValueVOList.map {
            if (!AppValues.categorywiseDomainValueVOListMap.containsKey(it.category)) {
                AppValues.categorywiseDomainValueVOListMap[it.category] = arrayListOf<DomainValueVO>()
            }
            AppValues.categorywiseDomainValueVOListMap[it.category]?.add(it)
        }
    }

    fun projectSwitched(projectName: String) {
        // This too works, but not recommended: _uiState.value = MainScreenUiState(projectName = projectName)
        _mainScreenUiState.update {
            it.copy(projectName = projectName)
        }
    }

    private fun getAttributeValue(attributeValueVO: AttributeValueVO): String {
        val domainValueVO = AppValues.domainValueVOMap[attributeValueVO.attributeDvId]
        return if (domainValueVO == null) {
            ""
        } else if (domainValueVO.attributeDomain == null || domainValueVO.attributeDomain == "") {
            attributeValueVO.attributeValue
        } else {
            val dVVO = AppValues.domainValueVOMap[attributeValueVO.attributeValue.toLong()]
            dVVO?.value ?: ""
        }
    }
}
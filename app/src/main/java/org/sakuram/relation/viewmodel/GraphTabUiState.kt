package org.sakuram.relation.viewmodel

data class GraphTabUiState (
    val nodesMap: Map<String, Node>?,
    val edgesList: List<Edge>?,
)

data class Node (
    val label: String? = "",
    val xStart: Float = 0.0f,
    val xEnd: Float = 0.0f,
    val yStart: Float = 0.0f,
    val yEnd: Float = 0.0f,
)

data class Edge (
    val source: String = "",
    val target: String = "",
    val label: String? = "",
)
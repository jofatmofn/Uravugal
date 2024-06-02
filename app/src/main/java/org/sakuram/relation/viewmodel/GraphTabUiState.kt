package org.sakuram.relation.viewmodel

data class GraphTabUiState (
    val nodesMap: Map<String, Node>?,
    val edgesList: List<Edge>?,
)

data class Node (
    val label: String? = "",
    val x: Double = 0.0,
    val y: Double = 0.0,
)

data class Edge (
    val source: String = "",
    val target: String = "",
    val label: String? = "",
)
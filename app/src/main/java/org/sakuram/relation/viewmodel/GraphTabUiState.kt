package org.sakuram.relation.viewmodel

data class GraphTabUiState (
    val nodesMap: Map<Long, Node>?,
    val edgesMap: Map<Long, Edge>?,
)

data class Node (
    val label: String = "",
    val xStart: Float = 0.0f,
    val xEnd: Float = 0.0f,
    val yStart: Float = 0.0f,
    val yEnd: Float = 0.0f,
)

data class Edge (
    val source: Long = 0,
    val target: Long = 0,
    val label: String = "",
    val xStart: Float = 0.0f,
    val xEnd: Float = 0.0f,
    val yStart: Float = 0.0f,
    val yEnd: Float = 0.0f,
)
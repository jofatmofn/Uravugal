package org.sakuram.relation.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.sakuram.relation.apimodel.ProjectVO
import org.sakuram.relation.repository.ProjectUserApiRepository

class MainScreenViewModel: ViewModel() {
    private val _uiState = MutableStateFlow(MainScreenUiState())
    val uiState = _uiState.asStateFlow()	// (3) Exposing UI state
    /* private var job: Job? = null
    private var projectDetailsResponse: Response<ProjectVO>? = null */

    fun switchProject(projectId: String): ProjectVO? =
        ProjectUserApiRepository.switchProject(projectId)

    fun projectSwitched(projectName: String) {
        // This too works, but not recommended: _uiState.value = MainScreenUiState(projectName = projectName)
        _uiState.update {
            it.copy(projectName = projectName)
        }
    }
    /* fun xxx(projectId: String) {
        job?.cancel()
        job = viewModelScope.launch {
            coroutineScope {
                // try {
                projectDetailsResponse = ProjectUserApiRepository.switchProject(projectId)
                // println("*** Returned Project Name")
                if (projectDetailsResponse?.isSuccessful == true) {
                    println("*** Returned Project Name: ${projectDetailsResponse?.body()?.projectName}")
                } else {
                    println("Exception from ProjectUserApiRepository.switchProject: ${projectDetailsResponse?.toString()}")
                }
                /* } catch(ioe: HttpException) {
                println("Exception from ProjectUserApiRepository.switchProject: ${ioe.message}")
            } */
            }
        }
    } */
}
package ru.quipy.projections


import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import ru.quipy.api.ProjectAggregate
import ru.quipy.api.ProjectCreatedEvent
import ru.quipy.repositories.ProjectRepository
import ru.quipy.streams.AggregateSubscriptionsManager
import java.util.UUID

import javax.annotation.PostConstruct

@Service
class AllProjectsViewService (
    private val projectRepository: ProjectRepository,
    private val subscriptionsManager: AggregateSubscriptionsManager
) {


    @PostConstruct
    fun init() {
        subscriptionsManager.createSubscriber(ProjectAggregate::class, "all-projects-event-publisher-stream") {
            `when`(ProjectCreatedEvent::class) { event ->
                createProject(event.projectId, event.projectName)
            }
        }
    }

    fun getAllProjects(): List<AllProjectsViewDomain.Project> {
        return projectRepository.findAll()
    }

    fun getProject(projectId: UUID): AllProjectsViewDomain.Project? {
        return projectRepository.findByIdOrNull(projectId);

    }

    private fun createProject(projectId: UUID, projectName: String) {
        val project = AllProjectsViewDomain.Project(projectId, projectName)
        projectRepository.save(project)
    }
}
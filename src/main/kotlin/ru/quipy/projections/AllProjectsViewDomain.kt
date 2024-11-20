package ru.quipy.projections

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import ru.quipy.domain.Unique
import java.util.*

class AllProjectsViewDomain {
    @Document("all-projects-view")
    data class AllProjects(
        @Id
        override val id: String,
        val projects: MutableMap<UUID, Project> = mutableMapOf()
    ) : Unique<String>

    data class Project(
        val id: UUID,
        val name : String
    )
}
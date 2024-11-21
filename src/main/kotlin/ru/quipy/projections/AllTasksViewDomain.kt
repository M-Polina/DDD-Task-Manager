package ru.quipy.projections



import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import ru.quipy.domain.Unique
import java.util.*

class AllTasksViewDomain {
    @Document("all-tasks-view")
    data class AllTasks(
        @Id
        override val id: String, // userId

        val tasks: MutableMap<UUID, Task> = mutableMapOf()
    ) : Unique<String>

    data class Task(
        val id: UUID,
        var name: String,
        var description: String,
        val projectId: UUID,
        val executors: MutableList<UUID>,
        var statusId: UUID,
    )
}
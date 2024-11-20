package ru.quipy.projections


import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import ru.quipy.api.*
import ru.quipy.repositories.TasksRepository
import ru.quipy.streams.AggregateSubscriptionsManager
import java.util.UUID
import javax.annotation.PostConstruct

@Service
class AllTasksViewService(
    private val tasksRepository: TasksRepository,
    private val subscriptionsManager: AggregateSubscriptionsManager
) {


    @PostConstruct
    fun init() {
        subscriptionsManager.createSubscriber(TaskAndStatusAggregate::class, "all-tasks-event-publisher-stream") {
            `when`(TaskCreatedEvent::class) { event ->
                createTask(
                    event.taskId,
                    event.projectId,
                    event.taskName,
                    event.description,
                    event.statusId,
                    event.executors,
                )
            }
            `when`(TaskUpdatedEvent::class) { event ->
                updateTask(event.taskId, event.taskName, event.description)
            }

            `when`(ExecutorAddedEvent::class) { event ->
                updateExecutor(event.taskId, event.userId)
            }

            `when`(TaskStatusChangedEvent::class) { event ->
                updateStatus(event.taskId, event.statusId)
            }
        }
    }

    private fun createTask(
        taskId: UUID,
        projectId: UUID,
        taskName: String,
        description: String,
        statusId: UUID,
        executors: MutableList<UUID> = mutableListOf(),
    ) {
        val project = AllTasksViewDomain.Task(
            taskId,
            taskName,
            description,
            projectId,
            executors,
            statusId,
        )
        tasksRepository.save(project)
    }

    private fun updateTask(taskId: UUID, taskName: String, description: String, ) {


        val task = tasksRepository.findByIdOrNull(taskId) ?: throw IllegalArgumentException("Task not found")
        task.name = taskName
        task.description = description
        tasksRepository.save(task)
    }


    private fun updateExecutor(taskId: UUID, userId: UUID, ) {

        val task = tasksRepository.findByIdOrNull(taskId) ?: throw IllegalArgumentException("Task not found")
        task.executors.add(userId)
        tasksRepository.save(task)
    }


    private fun updateStatus(taskId: UUID, statusId: UUID, ) {

        val task = tasksRepository.findByIdOrNull(taskId) ?: throw IllegalArgumentException("Task not found")
        task.statusId = statusId
        tasksRepository.save(task)
    }


    fun getTask(id: UUID): AllTasksViewDomain.Task? {
        return tasksRepository.findByIdOrNull(id);
    }

    fun getAll(): List<AllTasksViewDomain.Task> {
        return tasksRepository.findAll()
    }


}
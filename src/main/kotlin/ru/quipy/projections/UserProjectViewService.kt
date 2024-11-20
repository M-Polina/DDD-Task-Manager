package ru.quipy.projections


import org.springframework.stereotype.Service
import ru.quipy.api.ParticipantAddedEvent
import ru.quipy.api.ProjectAggregate
import ru.quipy.repositories.UserProjectRepository
import ru.quipy.streams.AggregateSubscriptionsManager
import java.util.UUID
import javax.annotation.PostConstruct

@Service
class UserProjectViewService (
    private val userProjectRepository: UserProjectRepository,
    private val subscriptionsManager: AggregateSubscriptionsManager
) {


    @PostConstruct
    fun init() {
        subscriptionsManager.createSubscriber(ProjectAggregate::class, "user-project-event-publisher-stream") {
            `when`(ParticipantAddedEvent::class) { event ->
                updateRecord(event.projectId, event.userId, event.nickname, event.username)
            }
        }
    }
    private fun updateRecord(projectId: UUID, userId: UUID,  nickname: String, username: String, ) {
        val record = UserProjectViewDomain.UserProject(projectId, userId, nickname, username, )
        userProjectRepository.save(record)
    }

    fun getAll(): List<UserProjectViewDomain.UserProject> {
        return userProjectRepository.findAll()
    }

}
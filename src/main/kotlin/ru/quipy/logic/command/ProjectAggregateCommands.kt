package ru.quipy.logic.command

import ru.quipy.api.*
import ru.quipy.logic.state.ProjectAggregateState
import java.util.UUID
import javax.annotation.meta.TypeQualifierNickname

fun ProjectAggregateState.createProject(
        id: UUID,
        name: String
): ProjectCreatedEvent {
    return ProjectCreatedEvent(
            projectId = id,
            projectName = name
    )
}

fun ProjectAggregateState.addParticipantById(userId: UUID, nickname: String, name: String): ParticipantAddedEvent {
    if (getParticipants().contains(userId))
        throw IllegalArgumentException("User $userId is already a participant of the project ${getId()}.")

    return ParticipantAddedEvent(projectId = getId(), userId = userId, nickname = nickname, username = name)
}
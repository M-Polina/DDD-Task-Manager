package ru.quipy.projections


import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import ru.quipy.api.UserAggregate
import ru.quipy.api.UserCreatedEvent
import ru.quipy.repositories.UsersRepository
import ru.quipy.streams.AggregateSubscriptionsManager
import java.util.UUID

import javax.annotation.PostConstruct

@Service
class AllUsersViewService (
    private val usersRepository: UsersRepository,
    private val subscriptionsManager: AggregateSubscriptionsManager
) {

    @PostConstruct
    fun init() {
        subscriptionsManager.createSubscriber(UserAggregate::class, "all-users-event-publisher-stream") {
            `when`(UserCreatedEvent::class) { event ->
                createUser(event.userId, event.nickname, event.uname)
            }
        }
    }
    private fun createUser(userId: UUID, nickname: String, name: String) {
        val user = AllUsersViewDomain.User(userId, nickname, name)
        usersRepository.save(user)
    }

    fun getAll(): List<AllUsersViewDomain.User> {
        return usersRepository.findAll()
    }


    fun getUser(id: UUID): AllUsersViewDomain.User? {
        return usersRepository.findByIdOrNull(id);
    }

}
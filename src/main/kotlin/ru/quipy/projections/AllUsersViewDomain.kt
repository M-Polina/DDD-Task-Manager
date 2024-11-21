package ru.quipy.projections


import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import ru.quipy.domain.Unique
import java.util.*


class AllUsersViewDomain {
    @Document("all-projects-view")
    data class Users(
        @Id
        override val id: String,
        val users: MutableMap<UUID, User> = mutableMapOf()
    ) : Unique<String>

    data class User(
        val id: UUID,
        val nickname : String,
        val name : String
    )
}
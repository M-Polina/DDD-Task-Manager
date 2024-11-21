package ru.quipy.projections


import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import ru.quipy.domain.Unique
import java.util.*

class UserProjectViewDomain {
    @Document("user-project-view")
    data class UserPayments(
        @Id
        override val id: String, // userId
        val paymentMethods: MutableMap<UUID, UserProject> = mutableMapOf() // map to hold all payments
    ) : Unique<String>

    data class UserProject(
        val projectId: UUID,
        val userId: UUID,
        val nickname : String,
        val username: String,
    )
}
package ru.quipy.repositories

import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository
import ru.quipy.projections.UserProjectViewDomain
import java.util.*


@Repository
interface UserProjectRepository : MongoRepository<UserProjectViewDomain.UserProject, UUID>
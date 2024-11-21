package ru.quipy.repositories

import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository
import ru.quipy.projections.AllUsersViewDomain
import java.util.*



@Repository
interface UsersRepository : MongoRepository<AllUsersViewDomain.User, UUID>
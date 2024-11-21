package ru.quipy.repositories

import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository
import ru.quipy.projections.AllTasksViewDomain
import java.util.*



@Repository
interface TasksRepository : MongoRepository<AllTasksViewDomain.Task, UUID>
package ru.quipy.repositories

import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository
import ru.quipy.projections.AllProjectsViewDomain
import java.util.*


@Repository
interface ProjectRepository : MongoRepository<AllProjectsViewDomain.Project, UUID>
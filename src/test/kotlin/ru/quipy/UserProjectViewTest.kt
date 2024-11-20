package ru.quipy


import java.util.concurrent.TimeUnit


import org.awaitility.Awaitility
import org.junit.jupiter.api.Assertions

import org.junit.jupiter.api.Test

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

import ru.quipy.api.UserCreatedEvent
import ru.quipy.controller.ProjectController
import ru.quipy.controller.UserController

import java.util.*

@SpringBootTest
class UserProjectViewTest {


    @Autowired
    private lateinit var userController: UserController

    @Autowired
    private lateinit var projectController: ProjectController




    @Test
    fun createProject_ProjectUserIsInView() {
        val owner = createUser("Owner")
        val user = createUser("Participant")
        val project = projectController.createProject(
            "project",
            owner.userId
        )


        //проекция не пуста
        Awaitility.await().timeout(30, TimeUnit.SECONDS).untilAsserted {

            val userProjects = projectController.getAllUserProjects()
            Assertions.assertNotNull(userProjects);
            Assertions.assertNotEquals(0, userProjects?.count());


        }

        // дейстивительно запись добавилась
        Awaitility.await().timeout(30, TimeUnit.SECONDS).untilAsserted {

            val userProjects = projectController.getAllUserProjects()
            val exists = userProjects?.any { it.projectId == project.projectId && it.userId == owner.userId }
            Assertions.assertNotNull(exists);
            if (exists != null) {
                Assertions.assertTrue(exists)
            }

        }


    }

    @Test
    fun findUnexistingUserProject_Exception() {

        //проекция не пуста
        Awaitility.await().timeout(30, TimeUnit.SECONDS).untilAsserted {

            val projects = projectController.getAllProjects()
            Assertions.assertNotNull(projects);
            Assertions.assertNotEquals(0, projects?.count());

        }


        Awaitility.await().timeout(30, TimeUnit.SECONDS).untilAsserted {
            val uuidString = "550e8400-21fc-4b2c-b19d-277a31938ad7" //randomId
            val uuid = UUID.fromString(uuidString)

            val userProjects = projectController.getAllUserProjects()
            val exists = userProjects?.any { it.projectId ==uuid && it.userId == uuid }
            Assertions.assertNotNull(exists);
            if (exists != null) {
                Assertions.assertFalse(exists)
            }

        }


    }


    private fun createUser(name: String): UserCreatedEvent {
        return userController.createUser(
            "nick-$name",
            name,
            "password"
        )
    }
}
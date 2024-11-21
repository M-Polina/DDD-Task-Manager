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
class AllProjectsViewTests {


    @Autowired
    private lateinit var userController: UserController

    @Autowired
    private lateinit var projectController: ProjectController


    @Test
    fun createProject_ProjectIsInView() {
        val owner = createUser("Owner")
        val user = createUser("Participant")
        val project = projectController.createProject(
            "project",
            owner.userId
        )


        //проекция не пуста
        Awaitility.await().timeout(30, TimeUnit.SECONDS).untilAsserted {

            val projects = projectController.getAllProjects()
            Assertions.assertNotNull(projects);
            Assertions.assertNotEquals(0, projects?.count());

        }

        // дейстивительно правильный проект добавился
        Awaitility.await().timeout(30, TimeUnit.SECONDS).untilAsserted {

            val foundproject2 = projectController.getProjectById(project.projectId)
            Assertions.assertEquals("project", foundproject2?.name)
            Assertions.assertEquals(project.projectId, foundproject2?.id)

        }


    }

    @Test
    fun findUnexistingProject_Exception() {
        val owner = createUser("Owner")
        val user = createUser("Participant")


        //проекция не пуста
        Awaitility.await().timeout(30, TimeUnit.SECONDS).untilAsserted {

            val projects = projectController.getAllProjects()
            Assertions.assertNotNull(projects);
            Assertions.assertNotEquals(0, projects?.count());

        }

        // дейстивительно правильный проект добавился
        Awaitility.await().timeout(30, TimeUnit.SECONDS).untilAsserted {
            val uuidString = "550e8400-21fc-4b2c-b19d-277a31938ad7" //randomId
            val uuid = UUID.fromString(uuidString)

            val foundproject2 = projectController.getProjectById(uuid)
            Assertions.assertNull(foundproject2)

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
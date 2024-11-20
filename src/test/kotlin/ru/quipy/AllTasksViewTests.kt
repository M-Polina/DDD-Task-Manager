package ru.quipy

import java.util.concurrent.TimeUnit
import org.awaitility.Awaitility
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import ru.quipy.api.UserCreatedEvent
import ru.quipy.controller.ProjectController
import ru.quipy.controller.TaskController
import ru.quipy.controller.UserController
import ru.quipy.logic.state.ProjectAggregateState

import java.util.*

@SpringBootTest
class AllTasksViewTests {
    private lateinit var projectId: UUID
    private lateinit var taskId: UUID
    private lateinit var ownerId: UUID
    private lateinit var userId: UUID
    private lateinit var statusId2: UUID

    @Autowired
    private lateinit var userController: UserController

    @Autowired
    private lateinit var projectController: ProjectController

    @Autowired
    private lateinit var taskController: TaskController


    @Test
    fun createTask_TaskIsInView() {
        val owner = createUser("Owner")
        ownerId = owner.userId
        val user = createUser("Participant")
        userId = user.userId

        val project = createProject(owner.userId)
        projectId = project!!.getId()


        val status2 = taskController.createStatus(
            projectId,
            "In progress2",
            "YELLOW"
        )
        statusId2 = status2.statusId



        val task = taskController.createTask(
            projectId,
            "Task",
            "Task d",
            status2.statusId,
        )
        taskId = task.taskId






        //проекция не пуста
        Awaitility.await().timeout(30, TimeUnit.SECONDS).untilAsserted {

            val users = taskController.getAll();
            Assertions.assertNotNull(users);
            Assertions.assertNotEquals(0, users?.count());

        }

//         дейстивительно правильный Task добавился
        Awaitility.await().timeout(30, TimeUnit.SECONDS).untilAsserted {

            val found = taskController.getById(task.taskId)
            Assertions.assertEquals("Task", found?.name)
            Assertions.assertEquals("Task d", found?.description)

        }



    }

    @Test
    fun findUnexistingTask_Exception() {


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

            val found = taskController.getById(uuid)
            Assertions.assertNull(found)

        }


    }






    private fun createUser(name: String): UserCreatedEvent {
        return userController.createUser(
            "nick-$name",
            name,
            "password"
        )
    }

    private fun createProject(ownerId: UUID): ProjectAggregateState? {
        val response = projectController.createProject(
            "project",
            ownerId
        )

        return projectController.getProject(response.projectId)
    }
}
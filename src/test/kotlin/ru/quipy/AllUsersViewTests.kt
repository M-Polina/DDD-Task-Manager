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
class AllUsersViewTests {


    @Autowired
    private lateinit var userController: UserController


    @Test
    fun creat_ProjectIsInView() {

        val user = createUser("Participant")

        //проекция не пуста
        Awaitility.await().timeout(30, TimeUnit.SECONDS).untilAsserted {

            val users = userController.getAllUsers()
            Assertions.assertNotNull(users);
            Assertions.assertNotEquals(0, users?.count());

        }

//         дейстивительно правильный user добавился
        Awaitility.await().timeout(30, TimeUnit.SECONDS).untilAsserted {

            val foundUser = userController.getUserById(user.userId)
            Assertions.assertEquals("Participant", foundUser?.name)
            Assertions.assertEquals(user.userId, foundUser?.id)

        }


    }

    @Test
    fun findUnexistingUser_Exception() {
        //проекция не пуста
        Awaitility.await().timeout(30, TimeUnit.SECONDS).untilAsserted {

            val users = userController.getAllUsers()
            Assertions.assertNotNull(users);
            Assertions.assertNotEquals(0, users?.count());

        }

        // дейстивительно правильный user добавился
        Awaitility.await().timeout(30, TimeUnit.SECONDS).untilAsserted {
            val uuidString = "550e8400-21fc-4b2c-b19d-277a31938ad7" //randomId
            val uuid = UUID.fromString(uuidString)

            val foundUser = userController.getUserById(uuid)
            Assertions.assertNull(foundUser)

        }


    }


    private fun createUser(name: String): UserCreatedEvent {
        return userController.createUser(
            "nick-$name",
            name,
            name
        )
    }
}
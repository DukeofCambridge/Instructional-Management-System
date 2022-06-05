import com.tan.labbackend.dao.UserDAO;
import com.tan.labbackend.entity.Role;
import com.tan.labbackend.entity.User;
import com.tan.labbackend.redis.RedisService;
import com.tan.labbackend.service.RoleService;
import com.tan.labbackend.service.UserService;
import com.tan.labbackend.utils.RabbitProducer;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
//@Listeners({TestListenerAdapter.class})
public class UserServiceTest {
    @Mock
    UserDAO userDAO;
    @Mock
    RedisService redisService;
    @Mock
    RoleService roleService;
    @Mock
    RabbitProducer rabbitProducer;

    @Autowired
    private UserService userService;

    private static User user;

    @BeforeEach
    void setup() {
//        MockitoAnnotations.initMocks(this);

        userService = new UserService(userDAO,redisService,roleService,rabbitProducer);


        //User
        user = new User();
        user.setId(0);
        user.setUsername("1952111");
        user.setPassword("");
        user.setSalt("");
        user.setPhone("");
        user.setEmail("");
        user.setEnabled(false);
        user.setName("Student Yang");
        user.setRole(new Role());
    }

    @Test
    @Order(1)
    void shouldGetUser() throws Exception {
        final String username = "1952111";

        Mockito.when(userDAO.findByUsername(username)).thenReturn(user);

        User userFound = userService.findByUsername(username);
        System.out.println(userFound);

//        Assertions.assertThat(userFound.getUsername()).isEqualTo("1952111");
        assertEquals("1952111",userFound.getUsername());
//        Assertions.assertThat(userFound.getName()).isEqualTo("Student Yang");
    }
}
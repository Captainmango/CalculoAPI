package com.edward.calculoapi.sevices;

import com.edward.calculoapi.api.models.User;
import com.edward.calculoapi.services.UserCRUDService;
import com.edward.calculoapi.utils.factories.MockUserFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.TestPropertySource;

@AutoConfigureMockMvc
@SpringBootTest
@TestPropertySource(locations = "/application.properties")
public class UserCRUDServiceTest
{
    @Autowired
    private MockUserFactory mockUserFactory;

    @Autowired
    private UserCRUDService userCRUDService;

    @Test
    public void testItCanFindAUser()
    {
        var user = mockUserFactory.makeMockUser();

        User user1 = userCRUDService.findUserById(user.getId());
        Assertions.assertEquals(user1.getEmail(), user.getEmail());
    }

    @Test
    public void testItThrowsExceptionIfUserDoesNotExist()
    {
        var error = Assertions.assertThrows(
                UsernameNotFoundException.class,
                () -> userCRUDService.findUserById(Long.MAX_VALUE),
                "User not found for this ID"
        );

        Assertions.assertEquals(error.getMessage(), "User not found for this ID.");
    }
}

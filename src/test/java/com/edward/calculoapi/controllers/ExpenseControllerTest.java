package com.edward.calculoapi.controllers;

import static org.hamcrest.Matchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.edward.calculoapi.api.dto.requests.CreateExpenseRequest;
import com.edward.calculoapi.api.dto.requests.UpdateExpenseRequest;
import com.edward.calculoapi.models.Expense;
import com.edward.calculoapi.utils.MockExpenseFactory;
import com.edward.calculoapi.utils.MockUserFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Set;

@AutoConfigureMockMvc
@SpringBootTest
@TestPropertySource(locations = "/application.properties")
public class ExpenseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MockUserFactory mockUserFactory;

    @Autowired
    private MockExpenseFactory mockExpenseFactory;

    @WithMockUser(roles = {"ADMIN"})
    @Test
    public void testItCanGetAllExpensesIfUserIsAnAdmin() throws Exception
    {
        this.mockMvc.perform(get("/api/v1/admin/expenses")).andExpectAll(
            status().isOk(),
            content().contentType(MediaType.APPLICATION_JSON),
            MockMvcResultMatchers.jsonPath("$", notNullValue())
        );
    }

    @Test
    public void testOnlyAdminsCanAccessTheAdminIndexRoute() throws Exception
    {
        var user = mockUserFactory.makeMockUser();

        this.mockMvc.perform(get("/api/v1/admin/expenses").with(user(user))).andExpect(
                status().isForbidden()
        );
    }

    @Test
    public void testItCanGetAUsersExpenses() throws Exception
    {
        var user = mockUserFactory.makeMockUser();

        this.mockMvc.perform(get("/api/v1/expenses").with(user(user))).andExpectAll(
                status().isOk(),
                content().contentType(MediaType.APPLICATION_JSON),
                MockMvcResultMatchers.jsonPath("$", notNullValue())
        );
    }

    @Test
    public void testItCanCreateAnExpense() throws Exception
    {
        var user = mockUserFactory.makeMockUser();

        ObjectMapper mapper = new ObjectMapper();
        ObjectWriter writer = mapper.writer().withDefaultPrettyPrinter();
        CreateExpenseRequest request =
                new CreateExpenseRequest(
                        "Test",
                        "this is a test",
                        42,
                        Set.of("PERSONAL"));

        String requestJson = writer.writeValueAsString(request);
        this.mockMvc.perform(post("/api/v1/expenses").with(user(user))
                .content(requestJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpectAll(
                status().isAccepted(),
                content().contentType(MediaType.APPLICATION_JSON)
        );
    }

    @Test
    public void testItCanUpdateAnExpense() throws Exception
    {
        var user = mockUserFactory.makeMockUser();
        Expense expense = mockExpenseFactory.makeTransaction(user.getId());

        ObjectMapper mapper = new ObjectMapper();
        ObjectWriter writer = mapper.writer().withDefaultPrettyPrinter();

        UpdateExpenseRequest request =
                new UpdateExpenseRequest(
                        expense.getId(),
                        "this has been updated",
                        "ooooh, notes",
                        9999,
                        Set.of("BUSINESS"));
        String requestJson = writer.writeValueAsString(request);
        this.mockMvc.perform(patch("/api/v1/expenses/"+ expense.getId()).with(user(user))
                        .content(requestJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        status().isAccepted(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        MockMvcResultMatchers.jsonPath("$.Expense.categories[0]", hasValue("CATEGORY_BUSINESS"))
        );
    }

    @Test
    public void testItCanDeleteAnExpense() throws Exception
    {
        var user = mockUserFactory.makeMockUser();
        Expense expense = mockExpenseFactory.makeTransaction(user.getId());

        this.mockMvc.perform(delete("/api/v1/expenses/"+ expense.getId()).with(user(user)))
                .andExpect(status().isOk());
    }

    @Test
    public void testItCanRetrieveAnExpense() throws Exception
    {
        var user = mockUserFactory.makeMockUser();
        Expense expense = mockExpenseFactory.makeTransaction(user.getId());

        this.mockMvc.perform(get("/api/v1/expenses/"+ expense.getId()).with(user(user)))
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        MockMvcResultMatchers.jsonPath("$.Expense.id", notNullValue())
                );
    }
}

package com.ylab.app.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ylab.app.exception.userException.UserValidationException;
import com.ylab.app.model.user.User;
import com.ylab.app.model.user.UserRole;
import com.ylab.app.service.impl.UserServiceImpl;
import com.ylab.app.web.servlet.UserServlet;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * This class contains unit tests for the UserServlet class. It focuses on testing various methods of UserServlet
 * to ensure the expected behaviors under different scenarios.
 *
 * The tests cover the functionalities such as doPost, doGet, loginUser and getUserById handling different cases
 * including valid data, invalid data, exceptions, and not found situations.
 *
 * @author razlivinsky
 * @since 24.04.2024
 */
public class UserServletTest {
    @Mock
    private UserServiceImpl userService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @InjectMocks
    private UserServlet userServlet;

    private User user;
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        objectMapper = new ObjectMapper();
    }

    @Test
    @DisplayName("Test doPost method with valid data")
    void testDoPost() throws Exception {
        user = new User("testuser", "testpassword", UserRole.USER);
        String jsonInput = objectMapper.writeValueAsString(user);
        BufferedReader reader = new BufferedReader(new StringReader(jsonInput));
        when(request.getReader()).thenReturn(reader);

        when(userService.registerUser(anyString(), anyString())).thenReturn(user);

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        userServlet.doPost(request, response);

        verify(response).setContentType("application/json");
        verify(response).setStatus(HttpServletResponse.SC_CREATED);
    }

    @Test
    @DisplayName("Test doPost method with invalid data - Bad Request")
    public void testDoPost_InvalidData_BadRequest() throws IOException, ServletException {
        user = new User("testuser", "", UserRole.USER);
        String jsonInput = objectMapper.writeValueAsString(user);
        BufferedReader reader = new BufferedReader(new StringReader(jsonInput));
        when(request.getReader()).thenReturn(reader);

        doThrow(new UserValidationException("Invalid credentials")).when(userService).registerUser(anyString(), anyString());

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        userServlet.doPost(request, response);

        verify(response).sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid credentials");
    }

    @Test
    @DisplayName("Test getAllUsers method")
    void testGetAllUsers() throws Exception {
        User user1 = new User("user1", "password1", UserRole.USER);
        User user2 = new User("user2", "password2", UserRole.USER);
        List<User> users = new ArrayList<>();
        users.add(user1);
        users.add(user2);
        when(userService.getAllUsers()).thenReturn(users);

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        userServlet.doGet(request, response);

        verify(response).setContentType("application/json");
        verify(response).setStatus(HttpServletResponse.SC_OK);
    }

    @Test
    @DisplayName("Test loginUser method with UserValidationException")
    void testLoginUser_UserValidationException() throws Exception {
        when(request.getParameter("name")).thenReturn("testuser");
        when(request.getParameter("password")).thenReturn("");

        doThrow(new UserValidationException("Invalid credentials")).when(userService).loginUser("testuser", "");
        userServlet.loginUser(request, response, "testuser", "");

        verify(response).sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid credentials");
    }

    @Test
    @DisplayName("Test loginUser method with valid credentials")
    void testLoginUser() throws IOException, ServletException {
        when(request.getParameter("name")).thenReturn("testuser");
        when(request.getParameter("password")).thenReturn("testpassword");

        User user = new User("testuser", "testpassword", UserRole.USER);
        when(userService.loginUser("testuser", "testpassword")).thenReturn(user);

        HttpSession session = mock(HttpSession.class);
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(user);

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        userServlet.doGet(request, response);

        verify(response).setContentType("application/json");
        verify(response).setStatus(HttpServletResponse.SC_OK);
    }

    @Test
    @DisplayName("Test getUserById method - Positive case")
    void testGetUserById_Positive() throws Exception {
        long userId = 1;
        user = new User("testuser", "testpassword", UserRole.USER);

        when(userService.getUserById(userId)).thenReturn(user);

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        userServlet.doGet(request, response);

        verify(response).setContentType("application/json");
        verify(response).setStatus(HttpServletResponse.SC_OK);
    }

    @Test
    @DisplayName("Test getUserById method - User not found")
    void testGetUserById_UserNotFound() throws Exception {
        long userId = 123;
        when(userService.getUserById(userId)).thenReturn(null);
        userServlet.getUserById(request, response, userId);

        verify(response).sendError(HttpServletResponse.SC_NOT_FOUND, "User not found");
    }
}
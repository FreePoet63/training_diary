package com.ylab.app.web.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.ylab.app.exception.userException.UserValidationException;
import com.ylab.app.model.user.User;
import com.ylab.app.service.impl.UserServiceImpl;
import com.ylab.app.web.dto.UserDto;
import com.ylab.app.web.mapper.UserMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * UserServlet class for handling user-related HTTP requests.
 * This servlet provides endpoints for user registration, login, and retrieval of user information.
 *
 * @author razlivinsky
 * @since 18.04.2024
 */
@WebServlet(name = "UserServlets", urlPatterns = "/users/*")
public class UserServlet extends HttpServlet {
    private UserServiceImpl userService = new UserServiceImpl();
    private ObjectMapper mapper = new ObjectMapper();
    private UserMapper userMapper = UserMapper.INSTANCE;

    /**
     * Handles the POST request to register a new user.
     *
     * @param request  the HTTP request
     * @param response the HTTP response
     * @throws ServletException the servlet exception
     * @throws IOException the io exception
     * @throws UserValidationException the user validation exception
     */
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            ObjectNode json = mapper.readValue(request.getReader(), ObjectNode.class);
            String name = json.get("name").asText();
            String password = json.get("password").asText();
            User user = userService.registerUser(name, password);
            UserDto userDto = userMapper.userToUserDto(user);
            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_CREATED);
            mapper.writeValue(response.getWriter(), userDto);
        } catch (UserValidationException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        }
    }

    /**
     * Handles the GET request to retrieve user information based on the request path and parameters.
     *
     * @param request  the HTTP request
     * @param response the HTTP response
     * @throws ServletException the servlet exception
     * @throws IOException the io exception
     * @throws UserValidationException the user validation exception
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        String name = request.getParameter("name");
        String password = request.getParameter("password");
        if ("/login".equals(pathInfo) && name != null && password != null) {
            loginUser(request, response, name, password);
        } else if (pathInfo == null || pathInfo.equals("/")) {
            getAllUsers(request, response);
        } else if (pathInfo.length() > 1) {
            try {
                long id = Long.parseLong(pathInfo.substring(1));
                getUserById(request, response, id);
            } catch (NumberFormatException e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid format user ID");
            }
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid request");
        }
    }

    /**
     * Retrieves all users and sends their information as a JSON response.
     *
     * @param request  the HTTP request
     * @param response the HTTP response
     * @throws IOException the io exception
     * @throws UserValidationException the user validation exception
     */
    public void getAllUsers(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            List<User> users = userService.getAllUsers();
            List<UserDto> userDtos = users.stream()
                    .map(userMapper::userToUserDto)
                    .collect(Collectors.toList());
            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_OK);
            mapper.writeValue(response.getWriter(), userDtos);
        } catch (UserValidationException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        }
    }

    /**
     * Handles the user login request and sends the user information as a JSON response upon successful login.
     *
     * @param request  the HTTP request
     * @param response the HTTP response
     * @param name     the user's name
     * @param password the user's password
     * @throws IOException the io exception
     * @throws UserValidationException the user validation exception
     */
    public void loginUser(HttpServletRequest request, HttpServletResponse response, String name, String password) throws IOException {
        try {
            User user = userService.loginUser(name, password);
            UserDto userDto = userMapper.userToUserDto(user);
            request.getSession().setAttribute("user", userDto);
            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_OK);
            mapper.writeValue(response.getWriter(), userDto);
        } catch (UserValidationException e) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
        }
    }

    /**
     * Retrieves user information by ID and sends it as a JSON response.
     *
     * @param request  the HTTP request
     * @param response the HTTP response
     * @param id       the user's ID
     * @throws IOException the io exception
     * @throws UserValidationException the user validation exception
     */
    public void getUserById(HttpServletRequest request, HttpServletResponse response, long id) throws IOException {
        try {
            User user = userService.getUserById(id);
            UserDto userDto = userMapper.userToUserDto(user);
            if (user == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "User not found");
            } else {
                response.setContentType("application/json");
                response.setStatus(HttpServletResponse.SC_OK);
                mapper.writeValue(response.getWriter(), userDto);
            }
        } catch (UserValidationException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        }
    }
}
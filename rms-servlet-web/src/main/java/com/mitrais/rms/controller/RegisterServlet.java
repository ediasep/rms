package com.mitrais.rms.controller;

import com.mitrais.rms.dao.UserDao;
import com.mitrais.rms.dao.impl.UserDaoImpl;
import com.mitrais.rms.model.User;
import org.mindrot.jbcrypt.BCrypt;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/register/*")
public class RegisterServlet extends AbstractController {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = getTemplatePath(req.getServletPath());
        RequestDispatcher requestDispatcher = req.getRequestDispatcher(path);
        requestDispatcher.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if("/doregister".equalsIgnoreCase(req.getPathInfo())) {

            String username = req.getParameter("username");
            String password = req.getParameter("password");
            String confirm_password = req.getParameter("confirm_password");

            if(password.equals(confirm_password)) {
              // Hash password
              password = BCrypt.hashpw(password, BCrypt.gensalt(10));

              User usr = new User((long) 5, username, password);
              UserDao userDao = UserDaoImpl.getInstance();

              userDao.save(usr);
            }
        }
    }
}


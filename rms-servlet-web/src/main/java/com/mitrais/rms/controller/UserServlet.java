package com.mitrais.rms.controller;

import com.mitrais.rms.dao.UserDao;
import com.mitrais.rms.dao.impl.UserDaoImpl;
import com.mitrais.rms.model.User;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Optional;

@WebServlet("/users/*")
public class UserServlet extends AbstractController
{
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        String path = getTemplatePath(req.getServletPath()+req.getPathInfo());

        if(req.getSession(false) == null) {
            resp.sendRedirect("/rms-servlet-web/login");
            return;
        }

        if ("/list".equalsIgnoreCase(req.getPathInfo())){
            UserDao userDao = UserDaoImpl.getInstance();
            List<User> users = userDao.findAll();
            req.setAttribute("users", users);

            RequestDispatcher requestDispatcher = req.getRequestDispatcher(path);
            requestDispatcher.forward(req, resp);
        }

        if ("/delete".equalsIgnoreCase(req.getPathInfo())){
            UserDao userDao = UserDaoImpl.getInstance();
            String id = req.getParameter("id");
            Optional<User> usr = userDao.find(Long.parseLong(id));
            userDao.delete(usr.get());

            String message = "User has been successfully deleted";
            resp.sendRedirect("/rms-servlet-web/users/list?message=" + URLEncoder.encode(message, "UTF-8"));
        }

        if("/edit".equalsIgnoreCase(req.getPathInfo())) {
            UserDao userDao = UserDaoImpl.getInstance();
            String id = req.getParameter("id");

            Optional<User> usr = userDao.find(Long.parseLong(id));
            req.setAttribute("user", usr.get());

            RequestDispatcher requestDispatcher = req.getRequestDispatcher(path);
            requestDispatcher.forward(req, resp);
        }


    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = getTemplatePath(req.getServletPath()+req.getPathInfo());

        if("/submit".equalsIgnoreCase(req.getPathInfo())) {
            String username = req.getParameter("username");
            String password = req.getParameter("password");

            User usr = new User((long) 5, username, password);
            UserDao userDao = UserDaoImpl.getInstance();

            userDao.save(usr);
        }

        if("/update".equalsIgnoreCase(req.getPathInfo())) {
            String id = req.getParameter("id");
            String username = req.getParameter("username");
            String password = req.getParameter("password");

            UserDao userDao = UserDaoImpl.getInstance();
            Optional<User> usr = userDao.find(Long.valueOf(id));
            User user = usr.get();

            user.setUserName(username);
            user.setPassword(password);

            userDao.update(user);
        }

        String message = "User has been successfully saved";
        resp.sendRedirect("/rms-servlet-web/users/list?message=" + URLEncoder.encode(message, "UTF-8"));

    }
}

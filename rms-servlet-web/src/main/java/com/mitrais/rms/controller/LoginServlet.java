package com.mitrais.rms.controller;

import com.mitrais.rms.dao.UserDao;
import com.mitrais.rms.dao.impl.UserDaoImpl;
import com.mitrais.rms.model.User;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.Optional;

@WebServlet("/login/*")
public class LoginServlet extends AbstractController
{

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        String path = getTemplatePath(req.getServletPath());
        RequestDispatcher requestDispatcher = req.getRequestDispatcher(path);
        requestDispatcher.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        String path = getTemplatePath(req.getServletPath()+req.getPathInfo());

        if("/dologin".equalsIgnoreCase(req.getPathInfo())) {
            UserDao userdao = UserDaoImpl.getInstance();
            Optional<User> user = userdao.findByUserName(req.getParameter("username"));

            String name = req.getParameter("name");

            if(user.isPresent() && user.get().getPassword().equals(req.getParameter("password"))){
                HttpSession session = req.getSession();
                session.setAttribute("name", name);
                resp.sendRedirect("/rms-servlet-web/users/list");
            } else {
                String message = "User not found";
                resp.sendRedirect("/rms-servlet-web/login?message=" + URLEncoder.encode(message, "UTF-8"));
            }
        }
    }
}

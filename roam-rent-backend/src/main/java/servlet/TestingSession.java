package servlet;

import dao.SessionDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.User;

import java.io.IOException;
import java.util.Map;

@WebServlet("/api/v1/testing/session")
public class TestingSession extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println(req.getAttribute("context.userId"));
        System.out.println(req.getAttribute("context.email"));
        System.out.println(req.getAttribute("context.role"));
    }
}

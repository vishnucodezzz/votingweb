package com.onlinevotingsystem;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/login")
public class loginServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final String DB_URL = "jdbc:mysql://localhost:3306/voting_system";
    private static final String DB_USERNAME = "root";
    private static final String DB_PASSWORD = "vishnu8096";

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String username = request.getParameter("username");
        String voterId = request.getParameter("voterid");
        String password = request.getParameter("password");

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
            String query = "SELECT * FROM users WHERE voterid = ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, voterId);
            rs = preparedStatement.executeQuery();

            if (rs.next()) {
                String dbPassword = rs.getString("password");
                if (dbPassword.equals(password)) {
                    HttpSession session = request.getSession();
                    session.setAttribute("hasvoted", rs.getBoolean("has_voted"));
                    session.setAttribute("username", username);

                    response.sendRedirect("vote.html");
                } else {
                    out.println("<h3>Invalid password</h3>");
                }
            } else {
                out.println("<h3>Invalid voter ID</h3>");
            }
        } catch (ClassNotFoundException | SQLException e) {
            out.println("<h3>Internal server error</h3>");
            e.printStackTrace();
        } 
    }
}

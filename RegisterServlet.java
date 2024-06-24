package com.onlinevotingsystem;



import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
   
	private static final long serialVersionUID = 1L;
	private static final String DB_URL = "jdbc:mysql://localhost:3306/voting_system";
    private static final String DB_USERNAME = "root";
    private static final String DB_PASSWORD = "vishnu8096";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String voter_id = request.getParameter("voterid");
        String phone_no= request.getParameter("phoneno");
        String aadhar_no= request.getParameter("aadharno");
        PrintWriter out = response.getWriter();
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
            String query = "INSERT INTO users (username, password,voterid,phoneno,aadharno) VALUES (?, ?,?,?,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            preparedStatement.setString(3, voter_id);
            preparedStatement.setString(4, phone_no);
            preparedStatement.setString(5, aadhar_no);
            

            int count = preparedStatement.executeUpdate();
            
            if (count > 0) {
                out.println("<h3>User registered successfully.</h3>");
            } else {
                out.println("<h3>User registration failed.</h3>");
            }

            preparedStatement.close();
            connection.close();
        } catch (ClassNotFoundException | SQLException e) {
           out.println("byee");
        }
    }
}

package com.onlinevotingsystem;

import java.io.IOException;
import java.io.PrintWriter;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.sql.*;

@WebServlet("/vote")
public class VoterServlet extends HttpServlet{
	
	
	private static final long serialVersionUID = 1L;
	private static final String DB_URL = "jdbc:mysql://localhost:3306/voting_system";
    private static final String DB_USERNAME = "root";
    private static final String DB_PASSWORD = "vishnu8096";
	
	public void doPost(HttpServletRequest request,HttpServletResponse response) throws IOException {
		PrintWriter out=response.getWriter();
		HttpSession session =request.getSession(false);
		
		String username=(String) session.getAttribute("username");
		boolean hasvoted=(boolean)session.getAttribute("hasvoted");
		
		
		response.setContentType("text/html");
		if (username == null){
            response.sendRedirect("login.html");
            return;
		
		}
		if(hasvoted) {
			out.print("you already voted");
			return;
		}
		
		
		String candidateName=request.getParameter("candidate");
		
		try {
			 Class.forName("com.mysql.cj.jdbc.Driver");
	            Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
	            String queryCheck = "SELECT * FROM vote WHERE candidate = ?";
	            PreparedStatement preparedStatementCheck = connection.prepareStatement(queryCheck);
	            preparedStatementCheck.setString(1, candidateName);
	            ResultSet resultSet = preparedStatementCheck.executeQuery();
	            
	            if (resultSet.next()&&hasvoted==false) {
	                String queryVote = "UPDATE vote SET vote_count = vote_count + 1 WHERE candidate = ?";
	                PreparedStatement preparedStatementVote = connection.prepareStatement(queryVote);
	                preparedStatementVote.setString(1, candidateName);
	                int voteCount = preparedStatementVote.executeUpdate();

	                if (voteCount > 0) {
	                    String queryUpdateUser = "UPDATE users SET has_voted = TRUE WHERE username = ?";
	                    PreparedStatement preparedStatementUpdateUser = connection.prepareStatement(queryUpdateUser);
	                    preparedStatementUpdateUser.setString(1, username);
	                    preparedStatementUpdateUser.executeUpdate();
	                
	                    session.setAttribute("hasVoted", true);
	                    out.println("<h3>Vote cast successfully.</h3>");
	                }else {
	                    out.println("<h3>Voting failed.</h3>");
	                }
	                
	                resultSet.close();
	                preparedStatementCheck.close();
	                connection.close();
		 
	            }
		 
		}
		catch(Exception e) {
			out.print("byee");
		}
	}

 }
 

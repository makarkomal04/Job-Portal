package job;

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Servlet implementation class CreateUser
 */
@WebServlet("/createUser")
public class CreateUser extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public CreateUser() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Correct content type for GET request
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("<html><body><h1>Servlet is working</h1></body></html>");
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Set response content type to JSON
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        String fullName = request.getParameter("full_name");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String role = request.getParameter("role");
        String phoneNumber = request.getParameter("phone_number");
        String profilePicture = request.getParameter("profile_picture");

        // Check for required fields
//        if (fullName == null || email == null || password == null || role == null || fullName.isEmpty() || email.isEmpty() || password.isEmpty() || role.isEmpty()) {
//            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
//            out.println("{\"error\":\"Missing required fields\"}");
//            return;
//        }

        // Database connection string and credentials
       
        try {

        	Class.forName("com.mysql.cj.jdbc.Driver");
        	Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/jobportal", "root", "pokemon");
        	 String insertQuery = "INSERT INTO users (full_name, email, password, role, phone_number, profile_picture) VALUES (?, ?, ?, ?, ?, ?)";

             PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
            // Set parameters in the SQL query
            preparedStatement.setString(1, fullName);
            preparedStatement.setString(2, email);
            preparedStatement.setString(3, password);
            preparedStatement.setString(4, role);

            // Handle optional fields for phone number and profile picture
            if (phoneNumber == null || phoneNumber.isEmpty()) {
                preparedStatement.setNull(5, java.sql.Types.VARCHAR);
            } else {
                preparedStatement.setString(5, phoneNumber);
            }

            if (profilePicture == null || profilePicture.isEmpty()) {
                preparedStatement.setNull(6, java.sql.Types.VARCHAR);
            } else {
                preparedStatement.setString(6, profilePicture);
            }

            // Execute the insert query
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                response.setStatus(HttpServletResponse.SC_CREATED);
                out.println("{\"message\":\"User created successfully\"}");
            } else {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                out.println("{\"error\":\"Failed to create user\"}");
            }

        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.println("{\"error\":\"Database error: " + e.getMessage() + "\"}");
        } catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}

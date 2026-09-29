package job;

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

@WebServlet("/SubmitApplication")
public class SubmitApplication extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public SubmitApplication() {
        super();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        // Get form parameters
        String companyId = request.getParameter("companyId");
        String jobTitle = request.getParameter("jobTitle");
        String fullName = request.getParameter("fullName");
        String email = request.getParameter("email");
        String resume = request.getParameter("resume");  // For simplicity, we're not handling file uploads in this example
        String message = request.getParameter("message");

        // Assume a user is logged in and we have a user_id (this would usually come from session or a logged-in user's data)
        int userId = 1; // Example user ID, replace with actual logged-in user's ID

        try {
            // Database connection
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/jobportal", "root", "pokemon");

            // SQL to insert application
            String query = "INSERT INTO applications (user_id, company_id, job_title, full_name, email, resume, cover_letter) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, Integer.parseInt(companyId));
            preparedStatement.setString(3, jobTitle);
            preparedStatement.setString(4, fullName);
            preparedStatement.setString(5, email);
            preparedStatement.setString(6, resume);  // Storing the resume as a string (URL or file path)
            preparedStatement.setString(7, message);

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                out.println("<html><body>");
                out.println("<h2>Application Submitted Successfully!</h2>");
                out.println("<p>Thank you for applying! Please wait for our reply.</p>");
                out.println("<a href='job-list.html'>Go Back to Job Listings</a>");
                out.println("</body></html>");
            } else {
                out.println("<html><body>");
                out.println("<h2>Failed to Submit Application. Please try again.</h2>");
                out.println("</body></html>");
            }

            connection.close();
        } catch (SQLException | ClassNotFoundException e) {
            out.println("<html><body>");
            out.println("<h2>Database Error: " + e.getMessage() + "</h2>");
            out.println("</body></html>");
        }
    }
}

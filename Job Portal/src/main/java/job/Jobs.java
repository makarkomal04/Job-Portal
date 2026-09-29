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

@WebServlet("/JobList")
public class Jobs extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public Jobs() {
        super();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        // Retrieve form parameters
        String companyId = request.getParameter("companyId");
        String jobTitle = request.getParameter("jobTitle");
        String jobType = request.getParameter("jobType");
        String location = request.getParameter("location");
        String salaryRange = request.getParameter("salaryRange");
        String description = request.getParameter("description");
        String requirements = request.getParameter("requirements");
        String isRemote = request.getParameter("isRemote") != null ? "Yes" : "No";

        try {
            // Database connection
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/jobportal", "root", "pokemon");

            // SQL Query to insert job details
            String insertQuery = "INSERT INTO jobs (company_id, job_title, job_type, location, salary_range, description, requirements, is_remote) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

            PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
            preparedStatement.setInt(1, Integer.parseInt(companyId));
            preparedStatement.setString(2, jobTitle);
            preparedStatement.setString(3, jobType);
            preparedStatement.setString(4, location);
            preparedStatement.setString(5, salaryRange);
            preparedStatement.setString(6, description);
            preparedStatement.setString(7, requirements);
            preparedStatement.setString(8, isRemote);

            // Execute insert
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                out.println("<html><body>");
                out.println("<h2>Job Posted Successfully!</h2>");
                out.println("<a href='job-form.html'>Post Another Job</a>");
                out.println("</body></html>");
            } else {
                out.println("<html><body>");
                out.println("<h2>Failed to Post Job.</h2>");
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

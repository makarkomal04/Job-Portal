package job;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class SearchJobs
 */
@WebServlet("/searchJobs")
public class SearchJobs extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public SearchJobs() {
        super();
    }

    /**
     * Handles POST requests for job search
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Set response content type
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        // Get search parameters from the request
        String jobTitle = request.getParameter("job_title");
        String location = request.getParameter("location");

        // Database connection details
        String jdbcURL = "jdbc:mysql://localhost:3306/jobportal";
        String dbUser = "root";
        String dbPassword = "pokemon";

        // SQL Query to search jobs
        String sql = "SELECT j.job_title, j.job_type, j.location, j.salary_range, j.description, c.company_name " +
                     "FROM jobs j " +
                     "LEFT JOIN companies c ON j.company_id = c.company_id " +
                     "WHERE j.job_title LIKE ? AND j.location LIKE ?";

        try {
            // Load JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            // Establish database connection
            Connection conn = DriverManager.getConnection(jdbcURL, dbUser, dbPassword);
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, "%" + jobTitle + "%"); // Allow partial match
            stmt.setString(2, "%" + location + "%");

            ResultSet rs = stmt.executeQuery();

            // Bootstrap card layout
            out.println("<!DOCTYPE html>");
            out.println("<html lang='en'>");
            out.println("<head>");
            out.println("<meta charset='UTF-8'>");
            out.println("<meta name='viewport' content='width=device-width, initial-scale=1.0'>");
            out.println("<title>Job Listings</title>");
            out.println("<link href='https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha3/dist/css/bootstrap.min.css' rel='stylesheet'>");
            out.println("<style>");
            out.println("body { background-color: #f4f7fc; padding: 20px; }");
            out.println(".job-card { background: #fff; border-radius: 12px; box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1); padding: 20px; margin-bottom: 20px; transition: transform 0.2s, box-shadow 0.2s; }");
            out.println(".job-card:hover { transform: translateY(-5px); box-shadow: 0 8px 16px rgba(0, 0, 0, 0.2); }");
            out.println(".job-card h5 { color: #007bff; font-weight: bold; }");
            out.println(".apply-btn { background-color: #2575fc; color: white; border-radius: 25px; }");
            out.println(".apply-btn:hover { background-color: #6a11cb; }");
            out.println("</style>");
            out.println("</head>");
            out.println("<body>");
            out.println("<div class='container'>");
            out.println("<h2 class='mb-4'>Job Listings</h2>");
            out.println("<div class='row'>");

            boolean hasJobs = false;
            while (rs.next()) {
                hasJobs = true;
                out.println("<div class='col-lg-4 col-md-6'>");
                out.println("<div class='job-card'>");
                out.println("<h5>" + rs.getString("job_title") + "</h5>");
                out.println("<p><strong>Company:</strong> " + rs.getString("company_name") + "</p>");
                out.println("<p><strong>Type:</strong> " + rs.getString("job_type") + "</p>");
                out.println("<p><strong>Location:</strong> " + rs.getString("location") + "</p>");
                out.println("<p><strong>Salary:</strong> " + rs.getString("salary_range") + "</p>");
                out.println("<p><strong>Description:</strong> " + rs.getString("description") + "</p>");
                out.println("<a href='#' class='btn apply-btn btn-sm'>Apply Now</a>");
                out.println("</div>");
                out.println("</div>");
            }

            if (!hasJobs) {
            	out.println(sql); 
            	out.println(stmt);
            	out.println("<p class='text-center'>No jobs found. Try a different search.</p>");
            }

            out.println("</div>"); // row
            out.println("</div>"); // container
            out.println("</body>");
            out.println("</html>");

            // Close connections
            rs.close();
            stmt.close();
            conn.close();

        } catch (SQLException e) {
            out.println("<p class='text-danger'>Database error: " + e.getMessage() + "</p>");
        } catch (ClassNotFoundException e) {
            out.println("<p class='text-danger'>Error loading database driver: " + e.getMessage() + "</p>");
        }
    }
}

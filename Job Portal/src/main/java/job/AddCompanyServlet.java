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

@WebServlet("/AddCompany")
public class AddCompanyServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public AddCompanyServlet() {
        super();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        // Retrieve form parameters
        String companyName = request.getParameter("companyName");
        String websiteUrl = request.getParameter("websiteUrl");
        String location = request.getParameter("location");
        String description = request.getParameter("description");
        String logo = request.getParameter("logo");

        try {
            // Database connection
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/jobportal", "root", "pokemon");

            // SQL Query to insert company details
            String insertQuery = "INSERT INTO companies (company_name, website_url, location, description, logo) VALUES (?, ?, ?, ?, ?)";

            PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
            preparedStatement.setString(1, companyName);
            preparedStatement.setString(2, websiteUrl);
            preparedStatement.setString(3, location);
            preparedStatement.setString(4, description);
            preparedStatement.setString(5, logo);

            // Execute insert
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                out.println("<html><body>");
                out.println("<h2>Company Added Successfully!</h2>");
                out.println("<a href='company-form.html'>Add Another Company</a>");
                out.println("</body></html>");
            } else {
                out.println("<html><body>");
                out.println("<h2>Failed to Add Company.</h2>");
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

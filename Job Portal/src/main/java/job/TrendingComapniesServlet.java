package job;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/TrendingCompanies")
public class TrendingComapniesServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public TrendingComapniesServlet() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        try {
            // Database connection
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/jobportal", "root", "pokemon");

            // Query to fetch companies
            String query = "SELECT company_id, company_name, website_url, location, description, logo FROM companies";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            out.println("<html><head><title>Trending Companies</title>");
            out.println("<link href='https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha3/dist/css/bootstrap.min.css' rel='stylesheet'></head><body>");

            // Heading
            out.println("<h2 class='text-center mb-4'>Trending Companies</h2>");

            // Display companies as cards
            out.println("<div class='container'>");
            out.println("<div class='row'>");

            while (resultSet.next()) {
                String companyId = resultSet.getString("company_id");
                String companyName = resultSet.getString("company_name");
                String websiteUrl = resultSet.getString("website_url");
                String location = resultSet.getString("location");
                String description = resultSet.getString("description");
                String logo = resultSet.getString("logo");

                // Create a card for each company
                out.println("<div class='col-md-4 mb-4'>");
                out.println("<div class='card'>");

                // If logo is available, show it as an image, otherwise show a placeholder
                if (logo != null && !logo.isEmpty()) {
                    out.println("<img src='" + logo + "' class='card-img-top' alt='Company Logo'>");
                } else {
                    out.println("<img src='https://via.placeholder.com/150' class='card-img-top' alt='No Logo'>");
                }

                // Card body with company details
                out.println("<div class='card-body'>");
                out.println("<h5 class='card-title'>" + companyName + "</h5>");
                out.println("<p class='card-text'>" + description + "</p>");
                out.println("<p class='card-text'><strong>Location:</strong> " + location + "</p>");
                out.println("<a href='" + websiteUrl + "' class='btn btn-primary' target='_blank'>Visit Website</a>");
                out.println("</div>"); // End of card-body
                out.println("</div>"); // End of card
                out.println("</div>"); // End of col-md-4
            }

            out.println("</div>"); // End of row
            out.println("</div>"); // End of container

            out.println("</body></html>");

            connection.close();
        } catch (Exception e) {
            out.println("<html><body><h2>Error: " + e.getMessage() + "</h2></body></html>");
        }
    }
}

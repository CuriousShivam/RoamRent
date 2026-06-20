package servlet;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import config.ResponseUtil;
import dao.TripOfferDAO;
import model.OfferResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/api/customer/offers")
public class CustomerOffersServlet extends HttpServlet {

    private final TripOfferDAO offerDAO = new TripOfferDAO();
    private final Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd HH:mm:ss")
            .create();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String requestIdParam = req.getParameter("requestId");

        // 1. Validate URL parameter
        if (requestIdParam == null || requestIdParam.trim().isEmpty()) {
            ResponseUtil.sendJson(resp, HttpServletResponse.SC_BAD_REQUEST, "Missing required requestId parameter", null);
            return;
        }

        try {
            Long requestId = Long.parseLong(requestIdParam);

            // 2. Fetch all driver bids from the database
            List<OfferResponse> activeOffers = offerDAO.getOffersForCustomerRequest(requestId);

            // 3. Set standard response structures & CORS configurations
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            resp.setHeader("Access-Control-Allow-Origin", "http://localhost:3000");

            // 4. Return serialized array to Next.js
            String jsonOutput = this.gson.toJson(activeOffers);
            resp.getWriter().write(jsonOutput);

        } catch (NumberFormatException e) {
            ResponseUtil.sendJson(resp, HttpServletResponse.SC_BAD_REQUEST, "Invalid requestId numerical format pattern", null);
        } catch (Exception e) {
            e.printStackTrace();
            ResponseUtil.sendJson(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to pull customer offer data feeds", null);
        }
    }

    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setHeader("Access-Control-Allow-Origin", "http://localhost:3000");
        resp.setHeader("Access-Control-Allow-Methods", "GET, OPTIONS");
        resp.setHeader("Access-Control-Allow-Headers", "Content-Type");
        resp.setStatus(HttpServletResponse.SC_OK);
    }
}
package swd.project.swdgr3project.controller;

import swd.project.swdgr3project.entity.User;
import swd.project.swdgr3project.model.dto.OrderDTO;
import swd.project.swdgr3project.service.impl.OrderServiceImpl;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/order-details")
public class OrderDetailsServlet extends HttpServlet {

    private final OrderServiceImpl orderService = new OrderServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();

        // Get order ID from request parameter
        String orderIdParam = req.getParameter("id");

        if (orderIdParam == null || orderIdParam.trim().isEmpty()) {
            resp.sendRedirect("order-history");
            return;
        }

        try {
            Long orderId = Long.parseLong(orderIdParam);

            // Get order details from service
            OrderDTO order = orderService.getOrderById(orderId);

            if (order == null) {
                req.setAttribute("error", "Không tìm thấy đơn hàng");
                req.getRequestDispatcher("/error.jsp").forward(req, resp);
                return;
            }

            // Optional: Check if user owns this order (uncomment when user session is implemented)
            /*
            User user = (User) session.getAttribute("user");
            if (user == null) {
                resp.sendRedirect("login");
                return;
            }

            if (!order.getUserId().equals(user.getId())) {
                req.setAttribute("error", "Bạn không có quyền xem đơn hàng này");
                req.getRequestDispatcher("/error.jsp").forward(req, resp);
                return;
            }
            */

            // Set order data and forward to JSP
            req.setAttribute("order", order);
            req.getRequestDispatcher("/order-details.jsp").forward(req, resp);

        } catch (NumberFormatException e) {
            req.setAttribute("error", "ID đơn hàng không hợp lệ");
            req.getRequestDispatcher("/error.jsp").forward(req, resp);
        } catch (Exception e) {
            req.setAttribute("error", "Không thể tải thông tin đơn hàng");
            req.getRequestDispatcher("/error.jsp").forward(req, resp);
        }
    }
}
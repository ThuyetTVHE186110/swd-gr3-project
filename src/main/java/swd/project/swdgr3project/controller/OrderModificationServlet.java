package swd.project.swdgr3project.controller;

import swd.project.swdgr3project.entity.User;
import swd.project.swdgr3project.model.dto.OrderDTO;
import swd.project.swdgr3project.service.impl.OrderServiceImpl;
import swd.project.swdgr3project.service.impl.ProductServiceImpl;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

@WebServlet("/order-modification")
public class OrderModificationServlet extends HttpServlet {

    private final OrderServiceImpl orderService = new OrderServiceImpl();
    private final ProductServiceImpl productService = new ProductServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/order-modification.jsp").forward(req, resp);

        //        HttpSession session = req.getSession();
//
//        // Get parameters from request
//        String orderIdParam = req.getParameter("id");
//
//        // Validate parameters
//        if (orderIdParam == null || orderIdParam.trim().isEmpty()) {
//            req.setAttribute("error", "Thiếu thông tin đơn hàng hoặc sản phẩm");
//            req.getRequestDispatcher("/error.jsp").forward(req, resp);
//            return;
//        }
//
//        try {
//            Long orderId = Long.parseLong(orderIdParam);
//
//            // Get order details
//            OrderDTO order = orderService.getOrderById(orderId);
//            if (order == null) {
//                req.setAttribute("error", "Không tìm thấy đơn hàng");
//                req.getRequestDispatcher("/error.jsp").forward(req, resp);
//                return;
//            }
//
//            // Check if order can be modified (only pending orders)
//            if (!"Delivered".equalsIgnoreCase(order.getStatus())) {
//                req.setAttribute("error", "Đơn hàng này không thể chỉnh sửa vì đã được xác nhận");
//                req.getRequestDispatcher("/error.jsp").forward(req, resp);
//                return;
//            }
//
//            // Optional: Check if user owns this order (uncomment when user session is implemented)
//            /*
//            User user = (User) session.getAttribute("user");
//            if (user == null) {
//                resp.sendRedirect("login");
//                return;
//            }
//
//            if (!order.getUserId().equals(user.getId())) {
//                req.setAttribute("error", "Bạn không có quyền chỉnh sửa đơn hàng này");
//                req.getRequestDispatcher("/error.jsp").forward(req, resp);
//                return;
//            }
//            */
//
//            // Get alternative products (similar products from same category)
//
//            // Set attributes for JSP
//            req.setAttribute("order", order);
//
//            // Forward to modification page
//            req.getRequestDispatcher("/order-modification.jsp").forward(req, resp);
//
//        } catch (NumberFormatException e) {
//            req.setAttribute("error", "ID đơn hàng hoặc sản phẩm không hợp lệ");
//            req.getRequestDispatcher("/error.jsp").forward(req, resp);
//        } catch (Exception e) {
//            req.setAttribute("error", "Không thể tải thông tin chỉnh sửa đơn hàng");
//            req.getRequestDispatcher("/error.jsp").forward(req, resp);
//        }
    }
}
package swd.project.swdgr3project.controller;

import swd.project.swdgr3project.entity.ShoppingCart;
import swd.project.swdgr3project.model.dto.OrderDTO;
import swd.project.swdgr3project.service.CheckoutService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/checkout")
public class CheckoutServlet extends HttpServlet {

    private final CheckoutService checkoutService = new CheckoutService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/checkout.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        HttpSession session = req.getSession();
        ShoppingCart cart = (ShoppingCart) session.getAttribute("cart");

        if (cart == null || cart.getItems().isEmpty()) {
            resp.sendRedirect("/checkout.jsp?error=cart_empty");
            return;
        }

        // --- SỬA LỖI: Lấy tất cả các tham số từ form ---
        String recipientName = req.getParameter("recipientName");
        String recipientEmail = req.getParameter("recipientEmail");
        String recipientPhone = req.getParameter("recipientPhone");

        // Dữ liệu địa chỉ
        String shippingAddress = req.getParameter("shippingAddress"); // Ví dụ: "123 Đường ABC"
        String shippingCity = req.getParameter("shippingCity");         // Ví dụ: "Hà Nội"
        String shippingDistrict = req.getParameter("shippingDistrict"); // Ví dụ: "Quận Ba Đình"
        String shippingWard = req.getParameter("shippingWard");         // Ví dụ: "Phường Cống Vị"

        String paymentMethod = req.getParameter("paymentMethod");

        // Kiểm tra các trường bắt buộc
        if (recipientName == null || recipientName.trim().isEmpty() ||
                recipientEmail == null || recipientEmail.trim().isEmpty() ||
                recipientPhone == null || recipientPhone.trim().isEmpty() ||
                shippingAddress == null || shippingAddress.trim().isEmpty() ||
                shippingCity == null || shippingCity.trim().isEmpty() ||
                shippingDistrict == null || shippingDistrict.trim().isEmpty() ||
                shippingWard == null || shippingWard.trim().isEmpty()) {

            req.setAttribute("errorMessage", "Vui lòng điền đầy đủ tất cả các trường bắt buộc.");
            req.getRequestDispatcher("/checkout.jsp").forward(req, resp);
            return;
        }

        try {
            // SỬA LỖI: Truyền vào đúng và đủ 9 tham số theo yêu cầu của phương thức
            OrderDTO order = checkoutService.placeGuestOrder(
                    cart,
                    recipientName,
                    recipientEmail,
                    recipientPhone,
                    shippingAddress,
                    shippingCity,
                    shippingDistrict,
                    shippingWard,
                    paymentMethod
            );

            session.removeAttribute("cart"); // Xóa giỏ hàng sau khi đặt hàng thành công
            req.setAttribute("order", order);
            req.getRequestDispatcher("/order-confirmation.jsp").forward(req, resp);
        } catch (IllegalArgumentException e) {
            req.setAttribute("errorMessage", e.getMessage());
            req.getRequestDispatcher("/checkout.jsp").forward(req, resp);
        }
    }
}
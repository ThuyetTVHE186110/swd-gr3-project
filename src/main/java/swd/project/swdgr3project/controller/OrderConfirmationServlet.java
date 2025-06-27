package swd.project.swdgr3project.controller;

import swd.project.swdgr3project.model.dto.OrderDTO;
import swd.project.swdgr3project.service.impl.OrderServiceImpl;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/order-confirmation")
public class OrderConfirmationServlet extends HttpServlet {

    private final OrderServiceImpl orderService = new OrderServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 1. Lấy tham số từ URL mà PayOS trả về
        String orderCode = req.getParameter("orderCode");

        // 2. Dùng orderCode để tìm lại đơn hàng trong database
        // (Đây là mã giao dịch, không phải ID đơn hàng)
        OrderDTO order = orderService.getOrderByPaymentTransactionId(orderCode);

        // 3. Đặt đơn hàng vào request attribute và chuyển đến trang JSP
        req.setAttribute("order", order);
        req.getRequestDispatcher("/order-confirmation.jsp").forward(req, resp);
    }
}
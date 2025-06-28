package swd.project.swdgr3project.controller;

import swd.project.swdgr3project.entity.User;
import swd.project.swdgr3project.model.dto.OrderDTO;
import swd.project.swdgr3project.service.OrderService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import swd.project.swdgr3project.service.impl.OrderServiceImpl;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet("/order-history")
public class OrderHistoryServlet extends HttpServlet {

    private final OrderServiceImpl orderService = new OrderServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
//        User user = (User) session.getAttribute("user");
//
//        if (user == null) {
//            resp.sendRedirect("login");
//            return;
//        }

        try {
            List<OrderDTO> orders = orderService.getOrdersByCustomerId(2L);
            req.setAttribute("orders", orders);
            req.getRequestDispatcher("/order-list.jsp").forward(req, resp);
        } catch (Exception e) {
            req.setAttribute("error", "Unable to load order history");
            req.getRequestDispatcher("/error.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            resp.sendRedirect("login");
            return;
        }

        try {
            String status = req.getParameter("status");
            String fromDateStr = req.getParameter("fromDate");
            String toDateStr = req.getParameter("toDate");

            List<OrderDTO> orders = orderService.getOrdersByCustomerId(user.getId());

            // Filter by status if provided
            if (status != null && !status.trim().isEmpty() && !status.equals("all")) {
                orders = orders.stream()
                        .filter(order -> order.getStatus().equalsIgnoreCase(status))
                        .collect(Collectors.toList());
            }

            // Filter by date range if provided
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            if (fromDateStr != null && !fromDateStr.trim().isEmpty()) {
                LocalDate fromDate = LocalDate.parse(fromDateStr, formatter);
                orders = orders.stream()
                        .filter(order -> !order.getCreatedAt().toLocalDate().isBefore(fromDate))
                        .collect(Collectors.toList());
            }

            if (toDateStr != null && !toDateStr.trim().isEmpty()) {
                LocalDate toDate = LocalDate.parse(toDateStr, formatter);
                orders = orders.stream()
                        .filter(order -> !order.getCreatedAt().toLocalDate().isAfter(toDate))
                        .collect(Collectors.toList());
            }

            req.setAttribute("orders", orders);
            req.setAttribute("selectedStatus", status);
            req.setAttribute("fromDate", fromDateStr);
            req.setAttribute("toDate", toDateStr);
            req.getRequestDispatcher("/order-history.jsp").forward(req, resp);

        } catch (Exception e) {
            req.setAttribute("error", "Unable to filter orders");
            req.getRequestDispatcher("/error.jsp").forward(req, resp);
        }
    }
}
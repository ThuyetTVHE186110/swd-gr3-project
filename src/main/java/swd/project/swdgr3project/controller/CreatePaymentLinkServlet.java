package swd.project.swdgr3project.controller;

import com.google.gson.Gson;
import vn.payos.PayOS;
import vn.payos.type.ItemData;
import vn.payos.type.PaymentData;
import vn.payos.type.CheckoutResponseData;

import swd.project.swdgr3project.config.PayOSConfig;
import swd.project.swdgr3project.entity.ShoppingCart;
import swd.project.swdgr3project.model.dto.OrderDTO;
import swd.project.swdgr3project.service.OrderService;
import swd.project.swdgr3project.service.impl.OrderServiceImpl;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@WebServlet("/create-link")
public class CreatePaymentLinkServlet extends HttpServlet {

    private final OrderService orderService = new OrderServiceImpl();
    private final PayOS payos = new PayOS(PayOSConfig.PAYOS_CLIENT_ID, PayOSConfig.PAYOS_API_KEY, PayOSConfig.PAYOS_CHECKSUM_KEY);
    private final Gson gson = new Gson();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        try {
            System.out.println("--- [CreatePaymentLinkServlet] Bắt đầu xử lý request ---");

            HttpSession session = req.getSession();
            ShoppingCart cart = (ShoppingCart) session.getAttribute("cart");

            if (cart == null || cart.getItems().isEmpty()) {
                throw new IllegalStateException("Giỏ hàng rỗng hoặc đã hết hạn.");
            }
            System.out.println("Bước 1: Lấy giỏ hàng thành công.");

            // SỬA LỖI CÚ PHÁP
            String recipientName = req.getParameter("recipientName");
            String recipientEmail = req.getParameter("recipientEmail");
            String recipientPhone = req.getParameter("recipientPhone");
            String shippingAddress = req.getParameter("shippingAddress");
            String shippingCity = req.getParameter("shippingCity");
            String shippingDistrict = req.getParameter("shippingDistrict");
            String shippingWard = req.getParameter("shippingWard"); // Sửa lại dòng này
            String paymentMethod = req.getParameter("paymentMethod");
            System.out.println("Bước 2: Lấy thông tin form thành công.");

            System.out.println("Bước 3: Bắt đầu gọi OrderService...");
            OrderDTO pendingOrder = ((OrderServiceImpl) orderService).createOrderFromGuestCart(
                    cart, recipientName, recipientEmail, recipientPhone,
                    shippingAddress, shippingCity, shippingDistrict, shippingWard,
                    paymentMethod
            );
            if (pendingOrder == null || pendingOrder.getId() == null) {
                throw new RuntimeException("Tạo đơn hàng PENDING thất bại.");
            }
            System.out.println("Bước 3: Tạo đơn hàng PENDING thành công.");

            System.out.println("Bước 4: Chuẩn bị dữ liệu cho PayOS...");
            Long orderCode = System.currentTimeMillis();
            int amount = pendingOrder.getTotal().intValue();
            String description = "Thanh toan DH:" + pendingOrder.getOrderNumber();
            String returnUrl = req.getScheme() + "://" + req.getServerName() + ":" + req.getServerPort() + req.getContextPath() + "/order-confirmation";
            String cancelUrl = req.getScheme() + "://" + req.getServerName() + ":" + req.getServerPort() + req.getContextPath() + "/checkout";

            List<ItemData> items = new ArrayList<>();
            for (OrderDTO.OrderItemDTO item : pendingOrder.getItems()) {
                // SỬA LỖI: Kiểm tra null trước khi gọi getter
                String productName = item.getProductName();
                if (productName == null || productName.trim().isEmpty()) {
                    // Xử lý lỗi hoặc đặt giá trị mặc định
                    productName = "Sản phẩm không xác định";
                    // Hoặc throw một exception nếu tên sản phẩm là bắt buộc
                    // throw new IllegalArgumentException("Tên sản phẩm trong đơn hàng không được để trống.");
                }

                ItemData itemData = ItemData.builder()
                        .name(productName) // Sử dụng biến đã kiểm tra
                        .quantity(item.getQuantity())
                        .price(item.getPrice().intValue())
                        .build();
                items.add(itemData);
            }

            PaymentData paymentData = PaymentData.builder()
                    .orderCode(orderCode)
                    .amount(amount)
                    .description(description)
                    .items(items)
                    .cancelUrl(cancelUrl)
                    .returnUrl(returnUrl)
                    .buyerName(recipientName)
                    .buyerEmail(recipientEmail)
                    .buyerPhone(recipientPhone)
                    .buyerAddress(shippingAddress + ", " + shippingWard + ", " + shippingDistrict + ", " + shippingCity)
                    .build();
            System.out.println("Bước 4: Chuẩn bị dữ liệu PayOS thành công.");

            System.out.println("Bước 5: Bắt đầu gọi API createPaymentLink của PayOS...");
            CheckoutResponseData paymentLinkResponse = payos.createPaymentLink(paymentData);

            // SỬA LỖI LOGIC: Kiểm tra response một cách an toàn hơn
            if (paymentLinkResponse == null || paymentLinkResponse.getCheckoutUrl() == null || paymentLinkResponse.getCheckoutUrl().isEmpty()) {
                throw new IOException("Tạo link thanh toán PayOS thất bại: Phản hồi không hợp lệ hoặc không có URL.");
            }
            System.out.println("Bước 5: Tạo link PayOS thành công.");

            System.out.println("Bước 6: Cập nhật mã giao dịch PayOS vào đơn hàng...");
            orderService.updatePaymentStatus(pendingOrder.getId(), "PENDING", String.valueOf(orderCode));
            System.out.println("Bước 6: Cập nhật thành công.");

            System.out.println("Bước 7: Gửi JSON response về cho frontend.");
            String jsonResponse = gson.toJson(paymentLinkResponse);
            resp.getWriter().write(jsonResponse);
            System.out.println("--- [CreatePaymentLinkServlet] Xử lý request thành công ---");

        } catch (Exception e) {
            System.err.println("--- !!! [CreatePaymentLinkServlet] ĐÃ XẢY RA LỖI !!! ---");
            e.printStackTrace();

            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            Map<String, String> errorResponse = Map.of("error", "Lỗi từ phía server: " + e.getMessage());
            String jsonError = gson.toJson(errorResponse);
            resp.getWriter().write(jsonError);
        }
    }
}
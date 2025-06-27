package swd.project.swdgr3project.controller;

// SỬA LỖI: Import Gson
import com.google.gson.Gson;
import vn.payos.PayOS;
import vn.payos.type.Webhook;
import vn.payos.type.WebhookData;

import swd.project.swdgr3project.config.PayOSConfig;
import swd.project.swdgr3project.service.OrderService;
import swd.project.swdgr3project.service.impl.OrderServiceImpl;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/payos-webhook")
public class WebhookServlet extends HttpServlet {

    private final PayOS payos = new PayOS(PayOSConfig.PAYOS_CLIENT_ID, PayOSConfig.PAYOS_API_KEY, PayOSConfig.PAYOS_CHECKSUM_KEY);
    private final OrderService orderService = new OrderServiceImpl();
    // SỬA LỖI: Dùng Gson
    private final Gson gson = new Gson();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        try {
            // SỬA LỖI: Dùng gson.fromJson() để đọc request body vào đối tượng Webhook
            Webhook webhookBody = gson.fromJson(req.getReader(), Webhook.class);

            WebhookData verifiedData = payos.verifyPaymentWebhookData(webhookBody);

            if (verifiedData != null) {
                Long orderCode = verifiedData.getOrderCode();
                String status = verifiedData.getCode();

                if ("00".equals(status)) {
                    orderService.processPaymentCallback(String.valueOf(orderCode), "SUCCESS", 0);
                } else {
                    orderService.processPaymentCallback(String.valueOf(orderCode), "FAILED", 0);
                }

                resp.setStatus(HttpServletResponse.SC_OK);
                resp.getWriter().write("Webhook processed");
            } else {
                throw new IOException("Webhook data is null or verification failed.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("Webhook processing failed: " + e.getMessage());
        }
    }
}
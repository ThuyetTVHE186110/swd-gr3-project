<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
  <title>Đặt hàng thành công</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
<div class="container">
  <div class="confirmation-box" style="border: 1px solid #ddd; padding: 40px; border-radius: 8px; text-align: center; max-width: 600px; margin: 40px auto;">
    <div class="checkmark" style="width: 80px; height: 80px; border-radius: 50%; display: block; margin: 0 auto 20px; background: #28a745; color: white; font-size: 50px; line-height: 80px;">✓</div>
    <h2>Cảm ơn! Bạn đã đặt hàng thành công!</h2>
    <p class="text-muted">Đơn hàng của bạn đã được tiếp nhận và đang chờ xử lý.</p>
    <hr>
    <p><strong>Mã đơn hàng:</strong> ${requestScope.order.id}</p>
    <p><strong>Người nhận:</strong> ${requestScope.order.recipientName}</p>
    <p><strong>Tổng thanh toán:</strong> <fmt:formatNumber value="${requestScope.order.total}" type="number" pattern="#,##0"/> VNĐ</p>
    <p><strong>Trạng thái:</strong> ${requestScope.order.status}</p>
    <hr>
    <a href="home" class="btn btn-primary">Tiếp tục mua sắm</a>
  </div>
</div>
</body>
</html>
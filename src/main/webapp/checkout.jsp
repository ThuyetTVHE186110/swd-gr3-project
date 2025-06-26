<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
  <title>Thanh toán Ngay</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
<div class="container mt-5">
  <h1 class="mb-4">Thanh toán đơn hàng</h1>

  <c:if test="${not empty errorMessage}"><div class="alert alert-danger">${errorMessage}</div></c:if>

  <div class="row">
    <div class="col-md-7">
      <h4>Thông tin giao hàng</h4>
      <form action="checkout" method="post">
        <div class="mb-3"><label for="recipientName" class="form-label">Tên người nhận</label><input type="text" class="form-control" id="recipientName" name="recipientName" required></div>
        <div class="mb-3"><label for="recipientPhone" class="form-label">Số điện thoại người nhận</label><input type="tel" class="form-control" id="recipientPhone" name="recipientPhone" required></div>
        <div class="mb-3"><label for="recipientEmail" class="form-label">Email (để nhận hóa đơn)</label><input type="email" class="form-control" id="recipientEmail" name="recipientEmail" required></div>
        <div class="mb-3"><label for="shippingAddress" class="form-label">Địa chỉ giao hàng</label><input type="text" class="form-control" id="shippingAddress" name="shippingAddress" placeholder="Số nhà, đường, phường/xã, quận/huyện, tỉnh/thành phố" required></div>

        <hr>
        <h4>Phương thức thanh toán</h4>
        <div class="form-check"><input class="form-check-input" type="radio" name="paymentMethod" id="cod" value="COD" checked><label class="form-check-label" for="cod">Thanh toán khi nhận hàng (COD)</label></div>
        <div class="form-check"><input class="form-check-input" type="radio" name="paymentMethod" id="vnpay" value="VNPAY"><label class="form-check-label" for="vnpay">Thanh toán qua VNPAY</label></div>

        <button type="submit" class="btn btn-primary w-100 mt-4">Đặt hàng</button>
      </form>
    </div>
    <div class="col-md-5">
      <div class="card">
        <div class="card-body">
          <h4 class="card-title">Tóm tắt đơn hàng</h4>
          <c:choose>
            <c:when test="${not empty sessionScope.cart and not empty sessionScope.cart.items}">
              <ul class="list-group list-group-flush">
                <c:forEach var="item" items="${sessionScope.cart.items}">
                  <li class="list-group-item d-flex justify-content-between align-items-center">
                      ${item.product.name} (x${item.quantity})
                    <span><fmt:formatNumber value="${item.subtotal}" type="number" pattern="#,##0"/> VNĐ</span>
                  </li>
                </c:forEach>
                <li class="list-group-item d-flex justify-content-between align-items-center fw-bold">
                  Tổng cộng
                  <span><fmt:formatNumber value="${sessionScope.cart.subtotal}" type="number" pattern="#,##0"/> VNĐ</span>
                </li>
              </ul>
            </c:when>
            <c:otherwise><p>Giỏ hàng của bạn đang trống.</p></c:otherwise>
          </c:choose>
        </div>
      </div>
      <a href="setup-cart" class="btn btn-secondary mt-3">Thêm sản phẩm mẫu vào giỏ</a>
    </div>
  </div>
</div>
</body>
</html>
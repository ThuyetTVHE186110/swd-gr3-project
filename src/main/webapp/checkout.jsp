<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<html>
<head>
  <title>Thanh Toán Đơn Hàng</title>
  <meta name="viewport" content="width=device-width, initial-scale=1.0">

  <!-- Google Fonts -->
  <link rel="preconnect" href="https://fonts.googleapis.com">
  <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
  <link href="https://fonts.googleapis.com/css2?family=Roboto:wght@400;500;700&display=swap" rel="stylesheet">

  <!-- Bootstrap 5 -->
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">

  <!-- Font Awesome for Icons -->
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.1.1/css/all.min.css">

  <style>
    /* --- Custom CSS inspired by CellphoneS --- */
    :root {
      --cps-red: #d70018;
      --cps-red-dark: #b80014;
      --cps-background: #f4f6f8;
      --cps-text-dark: #333;
      --cps-text-light: #666;
      --cps-border: #e0e0e0;
    }

    body {
      font-family: 'Roboto', sans-serif;
      background-color: var(--cps-background);
      color: var(--cps-text-dark);
    }

    .checkout-header {
      background-color: white;
      padding: 1rem 0;
      border-bottom: 1px solid var(--cps-border);
      text-align: center;
      margin-bottom: 2rem;
    }

    .checkout-header h1 {
      color: var(--cps-red);
      font-weight: 700;
      font-size: 2rem;
      margin: 0;
    }

    .form-section, .summary-section {
      background-color: white;
      border-radius: 12px;
      padding: 2rem;
      box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
    }

    .form-section h4, .summary-section h4 {
      font-weight: 700;
      margin-bottom: 1.5rem;
      border-bottom: 2px solid var(--cps-red);
      padding-bottom: 0.5rem;
      display: inline-block;
    }

    .form-label {
      font-weight: 500;
      margin-bottom: 0.5rem;
    }

    .form-control {
      border-radius: 8px;
      border: 1px solid var(--cps-border);
      padding: 0.75rem 1rem;
      transition: border-color 0.2s, box-shadow 0.2s;
    }

    .form-control:focus {
      border-color: var(--cps-red);
      box-shadow: 0 0 0 0.25rem rgba(215, 0, 24, 0.25);
    }

    .form-check-label {
      font-weight: 500;
    }

    .form-check-input:checked {
      background-color: var(--cps-red);
      border-color: var(--cps-red);
    }

    .payment-method {
      border: 1px solid var(--cps-border);
      border-radius: 8px;
      padding: 1rem;
      margin-bottom: 0.5rem;
      cursor: pointer;
      transition: all 0.2s;
    }

    .payment-method:has(input:checked) {
      border-color: var(--cps-red);
      background-color: #fff5f6;
      box-shadow: 0 0 0 2px var(--cps-red);
    }

    .btn-submit-order {
      background-color: var(--cps-red);
      border-color: var(--cps-red);
      font-weight: 700;
      font-size: 1.1rem;
      padding: 0.8rem;
      border-radius: 8px;
      transition: background-color 0.2s;
    }

    .btn-submit-order:hover {
      background-color: var(--cps-red-dark);
      border-color: var(--cps-red-dark);
    }

    .summary-section ul {
      padding-left: 0;
    }

    .summary-item {
      list-style: none;
      display: flex;
      justify-content: space-between;
      padding: 1rem 0;
      border-bottom: 1px solid var(--cps-border);
    }

    .summary-item:last-child {
      border-bottom: none;
    }

    .product-name {
      font-weight: 500;
    }

    .product-quantity {
      color: var(--cps-text-light);
      font-size: 0.9rem;
    }

    .summary-total {
      border-top: 2px solid var(--cps-border);
      padding-top: 1rem;
      margin-top: 1rem;
      font-size: 1.2rem;
    }

    .summary-total .total-label {
      font-weight: 700;
    }

    .summary-total .total-price {
      font-weight: 700;
      color: var(--cps-red);
    }

    .btn-add-sample {
      background-color: #6c757d;
      border-color: #6c757d;
    }
  </style>
</head>
<body>

<div class="checkout-header">
  <h1>XÁC NHẬN ĐƠN HÀNG</h1>
</div>

<div class="container pb-5">
  <c:if test="${not empty errorMessage}">
    <div class="alert alert-danger d-flex align-items-center" role="alert">
      <i class="fa-solid fa-triangle-exclamation me-2"></i>
      <div>${errorMessage}</div>
    </div>
  </c:if>

  <form action="checkout" method="post">
    <div class="row g-4">
      <!-- Left Column: Shipping & Payment -->
      <div class="col-lg-7">
        <div class="form-section">
          <h4><i class="fa-solid fa-truck-fast me-2"></i>Thông Tin Giao Hàng</h4>
          <div class="mb-3">
            <label for="recipientName" class="form-label">Tên người nhận</label>
            <input type="text" class="form-control" id="recipientName" name="recipientName" placeholder="Nguyễn Văn A" required>
          </div>
          <div class="row">
            <div class="col-md-6 mb-3">
              <label for="recipientPhone" class="form-label">Số điện thoại</label>
              <input type="tel" class="form-control" id="recipientPhone" name="recipientPhone" placeholder="09xxxxxxxx" required>
            </div>
            <div class="col-md-6 mb-3">
              <label for="recipientEmail" class="form-label">Email</label>
              <input type="email" class="form-control" id="recipientEmail" name="recipientEmail" placeholder="nguyenvana@email.com" required>
            </div>
          </div>
          <div class="mb-3">
            <label for="shippingAddress" class="form-label">Địa chỉ nhận hàng</label>
            <input type="text" class="form-control" id="shippingAddress" name="shippingAddress" placeholder="Số nhà, tên đường" required>
          </div>
          <div class="row">
            <div class="col-md-4 mb-3">
              <label for="shippingWard" class="form-label">Phường/Xã</label>
              <input type="text" class="form-control" id="shippingWard" name="shippingWard" placeholder="Phường Cống Vị" required>
            </div>
            <div class="col-md-4 mb-3">
              <label for="shippingDistrict" class="form-label">Quận/Huyện</label>
              <input type="text" class="form-control" id="shippingDistrict" name="shippingDistrict" placeholder="Quận Ba Đình" required>
            </div>
            <div class="col-md-4 mb-3">
              <label for="shippingCity" class="form-label">Tỉnh/Thành phố</label>
              <input type="text" class="form-control" id="shippingCity" name="shippingCity" placeholder="Hà Nội" required>
            </div>
          </div>
        </div>

        <div class="form-section mt-4">
          <h4><i class="fa-solid fa-credit-card me-2"></i>Phương Thức Thanh Toán</h4>
          <div class="payment-method">
            <div class="form-check">
              <input class="form-check-input" type="radio" name="paymentMethod" id="cod" value="COD" checked>
              <label class="form-check-label" for="cod">
                Thanh toán khi nhận hàng (COD)
              </label>
            </div>
          </div>
          <div class="payment-method">
            <div class="form-check">
              <input class="form-check-input" type="radio" name="paymentMethod" id="vnpay" value="VNPAY">
              <label class="form-check-label" for="vnpay">
                Thanh toán qua VNPAY
              </label>
            </div>
          </div>
        </div>
      </div>

      <!-- Right Column: Order Summary -->
      <div class="col-lg-5">
        <div class="summary-section">
          <h4><i class="fa-solid fa-basket-shopping me-2"></i>Tóm Tắt Đơn Hàng</h4>
          <c:choose>
            <c:when test="${not empty sessionScope.cart and not empty sessionScope.cart.items}">
              <ul>
                <c:forEach var="item" items="${sessionScope.cart.items}">
                  <li class="summary-item">
                    <div>
                      <div class="product-name">${item.product.name}</div>
                      <div class="product-quantity">Số lượng: ${item.quantity}</div>
                    </div>
                    <div class="fw-bold">
                      <fmt:formatNumber value="${item.subtotal}" type="number" pattern="#,##0"/> VNĐ
                    </div>
                  </li>
                </c:forEach>
              </ul>

              <div class="summary-total d-flex justify-content-between align-items-center">
                <span class="total-label">TỔNG CỘNG</span>
                <span class="total-price">
                                        <fmt:formatNumber value="${sessionScope.cart.subtotal}" type="number" pattern="#,##0"/> VNĐ
                                    </span>
              </div>

              <button type="submit" class="btn btn-submit-order w-100 mt-4">
                <i class="fa-solid fa-lock me-2"></i>ĐẶT HÀNG
              </button>
            </c:when>
            <c:otherwise>
              <p class="text-center text-muted">Giỏ hàng của bạn đang trống.</p>
              <a href="setup-cart" class="btn btn-danger w-100">Bắt đầu mua sắm</a>
            </c:otherwise>
          </c:choose>
        </div>

        <c:if test="${empty sessionScope.cart or empty sessionScope.cart.items}">
          <a href="setup-cart" class="btn btn-secondary w-100 mt-3">Thêm sản phẩm mẫu vào giỏ</a>
        </c:if>
      </div>
    </div>
  </form>
</div>

</body>
</html>
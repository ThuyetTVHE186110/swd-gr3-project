<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<html>
<head>
    <title>Thông tin đặt hàng</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Roboto:wght@400;500;700&display=swap" rel="stylesheet">

    <style>
        :root {
            --primary-red: #d70018;
            --background-color: #f1f1f1;
            --border-color: #e0e0e0;
            --text-color: #333;
            --subtext-color: #888;
            --smem-bg: #fef1f1;
            --smem-border: #f9d8d9;
            --smem-text: #df3a48;
        }

        body {
            font-family: 'Roboto', sans-serif;
            background-color: var(--background-color);
            color: var(--text-color);
            margin: 0;
            font-size: 14px;
        }

        .container-fluid {
            max-width: 1200px;
            margin: 0 auto;
        }

        /* --- Header --- */
        .checkout-header {
            background-color: white;
            padding: 15px 20px;
            border-bottom: 1px solid var(--border-color);
            display: flex;
            align-items: center;
            justify-content: space-between;
        }
        .header-main {
            display: flex;
            align-items: center;
            gap: 20px;
        }
        .header-main .back-arrow {
            font-size: 20px;
            color: var(--text-color);
            text-decoration: none;
        }
        .header-main .title {
            font-size: 20px;
            font-weight: 500;
            margin: 0;
        }

        .checkout-steps {
            display: flex;
            gap: 15px;
            font-weight: 500;
        }
        .checkout-steps .step {
            color: var(--subtext-color);
        }
        .checkout-steps .step.active {
            color: var(--primary-red);
            border-bottom: 2px solid var(--primary-red);
            padding-bottom: 15px;
            margin-bottom: -16px;
        }

        /* --- Main Content --- */
        .checkout-container {
            display: flex;
            gap: 20px;
            padding: 20px;
            align-items: flex-start;
        }

        .checkout-left {
            flex: 2;
            display: flex;
            flex-direction: column;
            gap: 20px;
        }

        .checkout-right {
            flex: 1;
            position: sticky;
            top: 20px;
        }

        .card {
            background-color: white;
            border-radius: 8px;
            padding: 20px;
            border: 1px solid var(--border-color);
        }

        .card-title {
            font-size: 16px;
            font-weight: 700;
            margin-top: 0;
            margin-bottom: 20px;
            text-transform: uppercase;
        }

        /* --- Product Item in Header --- */
        .product-summary-item {
            display: flex;
            align-items: center;
            gap: 15px;
            margin-bottom: 15px; /* Thêm khoảng cách giữa các sản phẩm nếu có nhiều */
        }
        .product-summary-item:last-child {
            margin-bottom: 0; /* Bỏ margin cho item cuối cùng */
        }
        .product-summary-item img {
            width: 60px;
            height: 60px;
            object-fit: contain;
            border: 1px solid var(--border-color);
            border-radius: 4px;
        }
        .product-summary-info {
            flex-grow: 1;
        }
        .product-summary-info .name {
            font-weight: 500;
            margin-bottom: 5px;
        }
        .product-summary-info .price {
            font-weight: 700;
            color: var(--primary-red);
        }
        /* Giá cũ không hiển thị vì Product entity không có trường oldPrice */
        /*.product-summary-info .price .old-price {*/
        /*    font-weight: 400;*/
        /*    color: var(--subtext-color);*/
        /*    text-decoration: line-through;*/
        /*    margin-left: 8px;*/
        /*}*/
        .product-summary-quantity {
            color: var(--subtext-color);
        }

        /* --- Customer Info --- */
        .customer-info-fields .form-group {
            margin-bottom: 15px;
        }

        /* --- Shipping Options (now just a label) --- */
        .shipping-method-label {
            display: flex;
            align-items: center;
            gap: 10px;
            padding: 15px;
            border: 1px solid var(--border-color);
            border-radius: 8px;
            font-weight: 500;
            background-color: #f8f8f8;
        }
        .shipping-method-label .icon {
            font-size: 18px;
            color: var(--primary-red);
        }


        /* --- Form Fields --- */
        .form-row {
            display: flex;
            gap: 15px;
        }
        .form-group {
            flex: 1;
            margin-bottom: 15px;
        }
        input[type="text"], input[type="tel"], input[type="email"], select, textarea {
            width: 100%;
            padding: 12px;
            border: 1px solid var(--border-color);
            border-radius: 8px;
            font-size: 14px;
            background-color: white;
        }
        input[type="text"]:focus, input[type="tel"]:focus, input[type="email"]:focus, select:focus, textarea:focus {
            outline: none;
            border-color: var(--primary-red);
        }
        textarea {
            resize: vertical;
            min-height: 80px;
        }
        .vat-options {
            display: flex;
            align-items: center;
            gap: 20px;
        }
        .vat-options label {
            cursor: pointer;
            display: flex;
            align-items: center;
            gap: 8px;
        }
        .vat-options input[type="radio"] {
            display: inline-block;
            width: auto;
            padding: 0;
            margin: 0;
        }
        .vat-options input[type="radio"] + label {
            border: none;
            padding: 0;
        }
        .vat-options input[type="radio"] + label::before {
            display: none;
        }

        /* --- Payment Method --- */
        .payment-method-option {
            border: 1px solid var(--border-color);
            border-radius: 8px;
            padding: 1rem;
            margin-bottom: 0.5rem;
            cursor: pointer;
        }

        .payment-method-option:has(input:checked) {
            border-color: var(--primary-red);
            background-color: #fff5f6;
            box-shadow: 0 0 0 1px var(--primary-red);
        }
        .payment-method-option .form-check {
            display: flex;
            align-items: center;
            gap: 10px;
        }
        .payment-method-option .form-check-input {
            width: 18px;
            height: 18px;
            margin: 0;
            border-radius: 50%;
            border: 2px solid var(--border-color);
            box-sizing: border-box;
            -webkit-appearance: none;
            -moz-appearance: none;
            appearance: none;
            outline: none;
            cursor: pointer;
            position: relative;
        }
        .payment-method-option .form-check-input:checked {
            border-color: var(--primary-red);
            background-color: var(--primary-red);
        }
        .payment-method-option .form-check-input:checked::after {
            content: '';
            position: absolute;
            top: 50%;
            left: 50%;
            transform: translate(-50%, -50%);
            width: 8px;
            height: 8px;
            border-radius: 50%;
            background: white;
        }
        .payment-method-option .form-check-label {
            font-weight: 500;
        }


        /* --- Right Column: Summary --- */
        .order-summary .summary-row {
            display: flex;
            justify-content: space-between;
            margin-bottom: 12px;
            font-size: 14px;
        }
        .order-summary .summary-row.total {
            font-size: 18px;
            font-weight: 700;
            margin-top: 15px;
        }
        .order-summary .summary-row.total .value {
            color: var(--primary-red);
        }

        /* --- Footer Button --- */
        .checkout-footer {
            padding-top: 15px;
            border-top: 1px solid var(--border-color);
            margin-top: 20px;
        }
        .total-price-final .label {
            font-size: 16px;
        }
        .total-price-final .value {
            font-size: 22px;
            font-weight: 700;
            color: var(--primary-red);
        }
        .btn-submit {
            width: 100%;
            background-color: var(--primary-red);
            color: white;
            border: none;
            padding: 15px;
            font-size: 16px;
            font-weight: 700;
            border-radius: 8px;
            cursor: pointer;
            text-transform: uppercase;
        }
        .btn-submit:hover {
            opacity: 0.9;
        }

        /* Responsive */
        @media (max-width: 992px) {
            .checkout-container {
                flex-direction: column;
            }
            .checkout-right {
                position: static;
                width: 100%;
            }
        }
        @media (max-width: 768px) {
            .checkout-header {
                flex-direction: column;
                align-items: flex-start;
                gap: 15px;
            }
            .checkout-steps .step.active {
                padding-bottom: 5px;
                margin-bottom: -1px;
            }
            .form-row {
                flex-direction: column;
                gap: 0;
            }
        }

    </style>
</head>
<body>

<form id="checkout-form" action="checkout" method="post">
    <header class="checkout-header">
        <div class="header-main">
            <a href="cart.jsp" class="back-arrow"><i class="fa-solid fa-arrow-left"></i></a>
            <h1 class="title">Thông tin</h1>
        </div>
        <div class="checkout-steps">
            <span class="step active">1. THÔNG TIN</span>
            <span class="step">2. THANH TOÁN</span>
        </div>
    </header>

    <div class="container-fluid">
        <c:if test="${not empty errorMessage}">
            <div style="background-color: #f8d7da; color: #721c24; border: 1px solid #f5c6cb; border-radius: 5px; padding: 10px; margin-bottom: 20px;">
                    ${errorMessage}
            </div>
            <% session.removeAttribute("errorMessage"); // Xóa thông báo lỗi sau khi hiển thị %>
        </c:if>

        <div class="checkout-container">
            <!-- ========== CỘT TRÁI - THÔNG TIN ========== -->
            <div class="checkout-left">
                <!-- Sản phẩm -->
                <div class="card">
                    <h3 class="card-title">SẢN PHẨM</h3>
                    <c:choose>
                        <%-- Giỏ hàng được lưu trong session, bạn có thể truy cập bằng sessionScope.cart --%>
                        <c:when test="${not empty sessionScope.cart and not empty sessionScope.cart.items}">
                            <c:forEach var="cartItem" items="${sessionScope.cart.items}">
                                <div class="product-summary-item">
                                        <%-- Sử dụng product.imageUrl từ CartItem.product --%>
                                    <img src="${cartItem.product.imageUrl}" alt="${cartItem.product.name}">
                                    <div class="product-summary-info">
                                            <%-- Sử dụng product.name từ CartItem.product --%>
                                        <div class="name">${cartItem.product.name}</div>
                                        <div class="price">
                                                <%-- Sử dụng price từ CartItem (giá tại thời điểm thêm vào giỏ) --%>
                                            <fmt:formatNumber value="${cartItem.price}" type="currency" currencySymbol="₫" />
                                                <%--
                                                    Ghi chú: Không thể hiển thị giá cũ vì Product entity bạn cung cấp
                                                    không có trường oldPrice. Nếu bạn muốn có giá cũ bị gạch ngang,
                                                    cần thêm trường oldPrice vào Product.java và ProductDTO.java
                                                    và truyền giá trị đó vào CartItem khi thêm sản phẩm.
                                                --%>
                                        </div>
                                    </div>
                                        <%-- Số lượng từ CartItem --%>
                                    <div class="product-summary-quantity">Số lượng: ${cartItem.quantity}</div>
                                </div>
                            </c:forEach>
                        </c:when>
                        <c:otherwise>
                            <p class="text-center text-muted">Giỏ hàng của bạn đang trống.</p>
                            <%-- Điều chỉnh link này nếu trang danh sách sản phẩm của bạn không phải product-list.jsp --%>
                            <a href="product-list.jsp" class="btn-submit" style="background-color: #007bff; text-align: center; text-decoration: none; display: block;">Tiếp tục mua sắm</a>
                        </c:otherwise>
                    </c:choose>
                </div>

                <!-- Thông tin khách hàng -->
                <div class="card">
                    <h3 class="card-title">Thông tin khách hàng</h3>
                    <%-- Giả định bạn có đối tượng user đã đăng nhập trong session, ví dụ: sessionScope.user --%>
                    <%-- Nếu không có user đăng nhập, các trường sẽ trống và người dùng phải nhập --%>
                    <div class="customer-info-fields">
                        <div class="form-group">
                            <label for="recipientName" class="form-label">Tên người nhận</label>
                            <input type="text" class="form-control" id="recipientName" name="recipientName"
                                   placeholder="Nguyễn Văn A" value="${not empty sessionScope.user ? sessionScope.user.fullName : ''}" required>
                        </div>
                        <div class="form-row">
                            <div class="form-group">
                                <label for="recipientPhone" class="form-label">Số điện thoại</label>
                                <input type="tel" class="form-control" id="recipientPhone" name="recipientPhone"
                                       placeholder="09xxxxxxxx" value="${not empty sessionScope.user ? sessionScope.user.phone : ''}" required>
                            </div>
                            <div class="form-group">
                                <label for="recipientEmail" class="form-label">Email</label>
                                <input type="email" class="form-control" id="recipientEmail" name="recipientEmail"
                                       placeholder="nguyenvana@email.com" value="${not empty sessionScope.user ? sessionScope.user.email : ''}" required>
                            </div>
                        </div>
                        <div>
                            <input type="checkbox" id="promo-email" name="promo-email">
                            <label for="promo-email" style="cursor:pointer;">Nhận email thông báo và ưu đãi từ CellphoneS</label>
                        </div>
                    </div>
                </div>

                <!-- Thông tin nhận hàng (chỉ còn Giao hàng tận nơi) -->
                <div class="card">
                    <h3 class="card-title">Thông tin nhận hàng</h3>
                    <div class="shipping-method-label">
                        <span class="icon"><i class="fa-solid fa-truck-fast"></i></span>
                        <span>Giao hàng tận nơi</span>
                        <input type="hidden" name="shippingMethod" value="delivery">
                    </div>

                    <div id="delivery-fields">
                        <div class="form-group" style="margin-top: 15px;">
                            <label for="shippingAddress" class="form-label">Địa chỉ nhận hàng</label>
                            <input type="text" class="form-control" id="shippingAddress" name="shippingAddress"
                                   placeholder="Số nhà, tên đường" required>
                        </div>
                        <div class="form-row">
                            <div class="form-group">
                                <label for="shippingWard" class="form-label">Phường/Xã</label>
                                <input type="text" class="form-control" id="shippingWard" name="shippingWard"
                                       placeholder="Phường Cống Vị" required>
                            </div>
                            <div class="form-group">
                                <label for="shippingDistrict" class="form-label">Quận/Huyện</label>
                                <input type="text" class="form-control" id="shippingDistrict" name="shippingDistrict"
                                       placeholder="Quận Ba Đình" required>
                            </div>
                            <div class="form-group">
                                <label for="shippingCity" class="form-label">Tỉnh/Thành phố</label>
                                <input type="text" class="form-control" id="shippingCity" name="shippingCity"
                                       placeholder="Hà Nội" required>
                            </div>
                        </div>
                    </div>

                    <div class="form-group">
                        <textarea name="note" placeholder="Ghi chú khác (nếu có)"></textarea>
                    </div>

                    <div class="form-group">
                        <p>Quý khách có muốn xuất hóa đơn công ty không?</p>
                        <div class="vat-options">
                            <label><input type="radio" name="vat_invoice" value="no" checked> Không</label>
                            <label><input type="radio" name="vat_invoice" value="yes"> Có</label>
                        </div>
                    </div>
                </div>

                <!-- Phương Thức Thanh Toán (Luôn hiển thị) -->
                <div class="card" id="payment-method-section">
                    <h3 class="card-title">Phương Thức Thanh Toán</h3>
                    <div class="payment-method-option">
                        <div class="form-check">
                            <input class="form-check-input" type="radio" name="paymentMethod" id="cod" value="COD" checked>
                            <label class="form-check-label" for="cod">Thanh toán khi nhận hàng (COD)</label>
                        </div>
                    </div>
                    <div class="payment-method-option">
                        <div class="form-check">
                            <input class="form-check-input" type="radio" name="paymentMethod" id="vnpay" value="VNPAY">
                            <label class="form-check-label" for="vnpay">Thanh toán qua VNPAY</label>
                        </div>
                    </div>
                </div>

            </div>

            <!-- ========== CỘT PHẢI - TÓM TẮT ========== -->
            <div class="checkout-right">
                <div class="card">
                    <div class="order-summary">
                        <div class="summary-row">
                            <span class="label">Tổng tiền hàng</span>
                            <%-- Lấy từ sessionScope.cart.rawTotal --%>
                            <span class="value">
                                <fmt:formatNumber value="${sessionScope.cart.rawTotal}" type="currency" currencySymbol="₫" />
                            </span>
                        </div>
                        <div class="summary-row">
                            <span class="label">Phí vận chuyển</span>
                            <%-- Lấy từ sessionScope.cart.shippingFee --%>
                            <span class="value">
                                <fmt:formatNumber value="${sessionScope.cart.shippingFee}" type="currency" currencySymbol="₫" />
                            </span>
                        </div>
                        <div class="summary-row">
                            <span class="label">Chiết khấu Smember</span>
                            <%-- Lấy từ sessionScope.cart.discount --%>
                            <span class="value" style="color: var(--primary-red);">
                                - <fmt:formatNumber value="${sessionScope.cart.discount}" type="currency" currencySymbol="₫" />
                            </span>
                        </div>
                        <div class="summary-row total">
                            <span class="label">Tổng tiền tạm tính</span>
                            <%-- Lấy từ sessionScope.cart.finalTotal --%>
                            <span class="value">
                                <fmt:formatNumber value="${sessionScope.cart.finalTotal}" type="currency" currencySymbol="₫" />
                            </span>
                        </div>
                    </div>
                    <div class="checkout-footer">
                        <input type="hidden" id="finalPaymentMethod" name="finalPaymentMethod" value="COD">
                        <button type="submit" id="submit-order-btn" class="btn-submit">Tiếp tục</button>
                    </div>
                </div>
            </div>
        </div>
    </div>
</form>

<script>
    const checkoutForm = document.getElementById('checkout-form');
    const submitBtn = document.getElementById('submit-order-btn');
    const finalPaymentMethodInput = document.getElementById('finalPaymentMethod');
    const deliveryFields = document.getElementById('delivery-fields');

    const paymentMethodRadios = document.querySelectorAll('input[name="paymentMethod"]');
    paymentMethodRadios.forEach(radio => {
        radio.addEventListener('change', () => {
            finalPaymentMethodInput.value = radio.value;
        });
    });

    if (submitBtn) {
        checkoutForm.addEventListener('submit', function (event) {
            event.preventDefault();

            submitBtn.disabled = true;
            submitBtn.innerHTML = '<span class="spinner-border spinner-border-sm" role="status" aria-hidden="true"></span> Đang xử lý...';

            const chosenPaymentMethod = finalPaymentMethodInput.value;

            if (chosenPaymentMethod === 'COD') {
                checkoutForm.action = 'checkout';
                checkoutForm.submit();
            } else if (chosenPaymentMethod === 'VNPAY') {
                const formData = new FormData(checkoutForm);
                fetch('create-link', {
                    method: 'POST',
                    body: new URLSearchParams(formData)
                })
                    .then(response => {
                        if (!response.ok) {
                            return response.json().then(err => {
                                throw new Error(err.error || 'Server error');
                            });
                        }
                        return response.json();
                    })
                    .then(data => {
                        if (data.checkoutUrl) {
                            window.location.href = data.checkoutUrl;
                        } else {
                            throw new Error('Phản hồi không hợp lệ từ server.');
                        }
                    })
                    .catch(error => {
                        console.error('Fetch Error:', error);
                        alert('Lỗi: ' + error.message);
                        submitBtn.disabled = false;
                        submitBtn.innerHTML = 'Tiếp tục';
                    });
            } else {
                alert('Vui lòng chọn phương thức thanh toán.');
                submitBtn.disabled = false;
                submitBtn.innerHTML = 'Tiếp tục';
            }
        });
    }

    // Đảm bảo tất cả các trường input cần thiết cho Delivery được enabled khi submit
    checkoutForm.addEventListener('submit', function(event) {
        const deliveryFieldsInputs = deliveryFields.querySelectorAll('input, select, textarea');
        deliveryFieldsInputs.forEach(field => {
            field.disabled = false;
        });
        paymentMethodRadios.forEach(radio => radio.disabled = false);
    });

</script>

</body>
</html>
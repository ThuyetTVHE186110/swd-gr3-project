<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Chi tiết đơn hàng #${order.id} - CellphoneS</title>
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }

        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background-color: #f5f5f5;
            color: #333;
        }

        .container {
            max-width: 1200px;
            margin: 20px auto;
            padding: 0 20px;
        }

        .back-button {
            margin-bottom: 20px;
        }

        .btn {
            padding: 10px 20px;
            border: none;
            border-radius: 6px;
            cursor: pointer;
            font-size: 14px;
            text-decoration: none;
            display: inline-flex;
            align-items: center;
            gap: 8px;
            transition: all 0.3s ease;
        }

        .btn-back {
            background: #6c757d;
            color: white;
        }

        .btn-back:hover {
            background: #545b62;
        }

        .order-detail-card {
            background: white;
            border-radius: 12px;
            overflow: hidden;
            box-shadow: 0 4px 12px rgba(0,0,0,0.1);
            margin-bottom: 20px;
        }

        .order-header {
            background: linear-gradient(135deg, #d70018, #ff4444);
            color: white;
            padding: 30px;
        }

        .order-title {
            display: flex;
            justify-content: space-between;
            align-items: center;
            flex-wrap: wrap;
            gap: 15px;
        }

        .order-id {
            font-size: 28px;
            font-weight: bold;
        }

        .order-status-badge {
            padding: 8px 16px;
            border-radius: 25px;
            font-size: 14px;
            font-weight: 500;
            text-transform: uppercase;
            background: rgba(255,255,255,0.2);
            border: 1px solid rgba(255,255,255,0.3);
        }

        .order-meta {
            margin-top: 20px;
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
            gap: 20px;
        }

        .meta-item {
            display: flex;
            flex-direction: column;
            gap: 5px;
        }

        .meta-label {
            font-size: 14px;
            opacity: 0.9;
        }

        .meta-value {
            font-size: 16px;
            font-weight: 500;
        }

        .order-body {
            padding: 30px;
        }

        .section {
            margin-bottom: 30px;
        }

        .section-title {
            font-size: 20px;
            font-weight: 600;
            color: #d70018;
            margin-bottom: 20px;
            padding-bottom: 10px;
            border-bottom: 2px solid #f0f0f0;
            display: flex;
            align-items: center;
            justify-content: space-between;
        }

        .edit-toggle {
            font-size: 14px;
            padding: 8px 16px;
            background: #d70018;
            color: white;
            border: none;
            border-radius: 20px;
            cursor: pointer;
            transition: all 0.3s ease;
        }

        .edit-toggle:hover {
            background: #b50015;
        }

        .edit-toggle:disabled {
            background: #ccc;
            cursor: not-allowed;
        }

        .info-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
            gap: 30px;
        }

        .info-card {
            background: #f8f9fa;
            padding: 20px;
            border-radius: 8px;
            border-left: 4px solid #d70018;
        }

        .info-card h4 {
            color: #d70018;
            margin-bottom: 15px;
            font-size: 16px;
        }

        .info-row {
            display: flex;
            justify-content: space-between;
            align-items: center;
            padding: 8px 0;
            border-bottom: 1px solid #eee;
        }

        .info-row:last-child {
            border-bottom: none;
        }

        .info-label {
            color: #666;
            font-weight: 500;
            flex: 1;
        }

        .info-value {
            font-weight: 600;
            text-align: right;
            flex: 2;
        }

        .info-input {
            width: 100%;
            padding: 8px 12px;
            border: 1px solid #ddd;
            border-radius: 4px;
            font-size: 14px;
            background: white;
        }

        .info-input:focus {
            outline: none;
            border-color: #d70018;
            box-shadow: 0 0 0 2px rgba(215, 0, 24, 0.2);
        }

        .editable-field {
            position: relative;
        }

        .edit-mode .info-value {
            display: none;
        }

        .edit-mode .info-input {
            display: block;
        }

        .info-input {
            display: none;
        }

        .product-list {
            display: flex;
            flex-direction: column;
            gap: 20px;
        }

        .product-item {
            display: flex;
            gap: 20px;
            padding: 20px;
            background: #f8f9fa;
            border-radius: 10px;
            border: 1px solid #eee;
            transition: transform 0.2s ease;
        }

        .product-item:hover {
            transform: translateY(-2px);
            box-shadow: 0 4px 12px rgba(0,0,0,0.1);
        }

        .product-image {
            width: 100px;
            height: 100px;
            border-radius: 8px;
            object-fit: cover;
            border: 1px solid #ddd;
        }

        .product-details {
            flex: 1;
            display: flex;
            flex-direction: column;
            gap: 8px;
        }

        .product-name {
            font-size: 18px;
            font-weight: 600;
            color: #333;
            line-height: 1.4;
        }

        .product-specs {
            font-size: 14px;
            color: #666;
            display: flex;
            flex-wrap: wrap;
            gap: 15px;
        }

        .spec-item {
            display: flex;
            align-items: center;
            gap: 5px;
        }

        .product-pricing {
            margin-top: auto;
            display: flex;
            justify-content: space-between;
            align-items: center;
            flex-wrap: wrap;
            gap: 10px;
        }

        .quantity-info {
            background: #d70018;
            color: white;
            padding: 6px 12px;
            border-radius: 20px;
            font-size: 14px;
            font-weight: 500;
            display: flex;
            align-items: center;
            gap: 8px;
        }

        .quantity-controls {
            display: none;
            align-items: center;
            gap: 8px;
            background: white;
            padding: 4px;
            border-radius: 20px;
            border: 2px solid #d70018;
        }

        .edit-mode .quantity-controls {
            display: flex;
        }

        .edit-mode .quantity-display {
            display: none;
        }

        .quantity-btn {
            width: 30px;
            height: 30px;
            border: none;
            background: #d70018;
            color: white;
            border-radius: 50%;
            cursor: pointer;
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 16px;
        }

        .quantity-btn:hover {
            background: #b50015;
        }

        .quantity-btn:disabled {
            background: #ccc;
            cursor: not-allowed;
        }

        .quantity-input {
            width: 60px;
            text-align: center;
            border: none;
            font-weight: bold;
            color: #d70018;
        }

        .price-info {
            text-align: right;
        }

        .unit-price {
            font-size: 14px;
            color: #666;
        }

        .total-price {
            font-size: 18px;
            font-weight: bold;
            color: #d70018;
        }

        .order-summary {
            background: #f8f9fa;
            padding: 20px;
            border-radius: 8px;
            border: 2px solid #d70018;
        }

        .summary-row {
            display: flex;
            justify-content: space-between;
            padding: 10px 0;
            border-bottom: 1px solid #ddd;
        }

        .summary-row:last-child {
            border-bottom: none;
            font-size: 18px;
            font-weight: bold;
            color: #d70018;
            border-top: 2px solid #d70018;
            margin-top: 10px;
            padding-top: 15px;
        }

        .order-timeline {
            position: relative;
            padding-left: 30px;
        }

        .timeline-item {
            position: relative;
            padding: 15px 0;
            border-left: 2px solid #eee;
            padding-left: 30px;
        }

        .timeline-item::before {
            content: '';
            position: absolute;
            left: -8px;
            top: 20px;
            width: 14px;
            height: 14px;
            border-radius: 50%;
            background: #eee;
        }

        .timeline-item.completed::before {
            background: #28a745;
        }

        .timeline-item.current::before {
            background: #d70018;
            box-shadow: 0 0 0 3px rgba(215, 0, 24, 0.2);
        }

        .timeline-content {
            background: white;
            padding: 15px;
            border-radius: 8px;
            box-shadow: 0 2px 4px rgba(0,0,0,0.1);
        }

        .timeline-title {
            font-weight: 600;
            color: #333;
            margin-bottom: 5px;
        }

        .timeline-time {
            font-size: 14px;
            color: #666;
        }

        .action-buttons {
            display: flex;
            gap: 15px;
            justify-content: center;
            padding: 20px;
            background: #f8f9fa;
            border-top: 1px solid #eee;
        }

        .btn-primary {
            background: #d70018;
            color: white;
        }

        .btn-primary:hover {
            background: #b50015;
        }

        .btn-secondary {
            background: #6c757d;
            color: white;
        }

        .btn-secondary:hover {
            background: #545b62;
        }

        .btn-outline {
            background: transparent;
            color: #d70018;
            border: 2px solid #d70018;
        }

        .btn-outline:hover {
            background: #d70018;
            color: white;
        }

        .btn-success {
            background: #28a745;
            color: white;
        }

        .btn-success:hover {
            background: #218838;
        }

        .edit-controls {
            display: none;
            gap: 10px;
            margin-top: 20px;
            justify-content: center;
        }

        .edit-mode .edit-controls {
            display: flex;
        }

        .readonly-notice {
            background: #fff3cd;
            color: #856404;
            padding: 15px;
            border-radius: 8px;
            margin-bottom: 20px;
            border: 1px solid #ffeaa7;
            display: flex;
            align-items: center;
            gap: 10px;
        }

        @media (max-width: 768px) {
            .container {
                padding: 0 15px;
            }

            .order-title {
                flex-direction: column;
                align-items: flex-start;
            }

            .order-meta {
                grid-template-columns: 1fr;
            }

            .info-grid {
                grid-template-columns: 1fr;
            }

            .product-item {
                flex-direction: column;
                text-align: center;
            }

            .product-pricing {
                flex-direction: column;
                align-items: center;
                gap: 15px;
            }

            .action-buttons, .edit-controls {
                flex-direction: column;
            }

            .section-title {
                flex-direction: column;
                align-items: flex-start;
                gap: 10px;
            }
        }
    </style>
</head>
<body>
<div class="container">
    <!-- Back Button -->
    <div class="back-button">
        <a href="order-history" class="btn btn-back">
            <i class="fas fa-arrow-left"></i>
            Quay lại danh sách đơn hàng
        </a>
    </div>

    <!-- Readonly Notice for non-pending orders -->
    <c:if test="${order.status != 'pending'}">
        <div class="readonly-notice">
            <i class="fas fa-info-circle"></i>
            <span>Đơn hàng này không thể chỉnh sửa vì đã được xác nhận hoặc đang trong quá trình xử lý.</span>
        </div>
    </c:if>

    <!-- Order Detail Card -->
    <div class="order-detail-card">
        <!-- Order Header -->
        <div class="order-header">
            <div class="order-title">
                <h1 class="order-id">Đơn hàng #${order.id}</h1>
                <span class="order-status-badge">
                    <c:choose>
                        <c:when test="${order.status == 'Pending'}">
                            <i class="fas fa-clock"></i> Chờ xác nhận
                        </c:when>
                        <c:when test="${order.status == 'Confirmed'}">
                            <i class="fas fa-check"></i> Đã xác nhận
                        </c:when>
                        <c:when test="${order.status == 'Shipping'}">
                            <i class="fas fa-truck"></i> Đang giao
                        </c:when>
                        <c:when test="${order.status == 'Delivered'}">
                            <i class="fas fa-check-circle"></i> Đã giao
                        </c:when>
                        <c:when test="${order.status == 'Cancelled'}">
                            <i class="fas fa-times-circle"></i> Đã hủy
                        </c:when>
                    </c:choose>
                </span>
            </div>

            <div class="order-meta">
                <div class="meta-item">
                    <span class="meta-label">Ngày đặt hàng</span>
                    <span class="meta-value">
                        <i class="fas fa-calendar"></i>
                        ${order.createdAt}
                    </span>
                </div>
                <div class="meta-item">
                    <span class="meta-label">Tổng tiền</span>
                    <span class="meta-value" id="orderTotal">
                        <fmt:formatNumber value="${order.total}" type="currency" currencySymbol="₫"/>
                    </span>
                </div>
                <div class="meta-item">
                    <span class="meta-label">Phương thức thanh toán</span>
                    <span class="meta-value">
                        <i class="fas fa-credit-card"></i>
                        ${order.paymentMethod == 'Credit Card' ? 'Chuyển khoản' : 'Thanh toán khi nhận hàng'}
                    </span>
                </div>
            </div>
        </div>

        <!-- Order Body -->
        <div class="order-body">
            <!-- Customer & Shipping Info -->
            <div class="section">
                <h3 class="section-title">
                    <span>
                        <i class="fas fa-info-circle"></i>
                        Thông tin đơn hàng
                    </span>
                    <c:if test="${order.status == 'Delivered'}">
<%--                        <a href="order-modification?orderId=${order.id}"--%>
<%--                           class="btn btn-outline btn-sm">--%>
<%--                            <i class="fas fa-edit"></i>--%>
<%--                            Chỉnh sửa--%>
<%--                        </a>--%>
                        <button class="btn btn-outline btn-sm" onclick="changeOrderDetail(${order.id})">
                            <i class="fas fa-edit"></i> Chỉnh sửa
                        </button>
                    </c:if>
                </h3>
                <form id="customer-info-form">
                    <div class="info-grid" id="customer-info">
                        <div class="info-card">
                            <h4><i class="fas fa-user"></i> Thông tin người nhận</h4>
                            <div class="info-row editable-field">
                                <span class="info-label">Họ tên:</span>
                                <span class="info-value">${order.recipientName}</span>
                                <input type="text" class="info-input" name="customerName" value="${order.recipientName}">
                            </div>
                            <div class="info-row editable-field">
                                <span class="info-label">Số điện thoại:</span>
                                <span class="info-value">${order.recipientPhone}</span>
                                <input type="text" class="info-input" name="customerPhone" value="${order.recipientPhone}">
                            </div>
                            <div class="info-row editable-field">
                                <span class="info-label">Email:</span>
                                <span class="info-value">${order.guestEmail}</span>
                                <input type="email" class="info-input" name="customerEmail" value="${order.guestEmail}">
                            </div>
                        </div>

                        <div class="info-card">
                            <h4><i class="fas fa-map-marker-alt"></i> Địa chỉ giao hàng</h4>
                            <div class="info-row editable-field">
                                <span class="info-label">Địa chỉ:</span>
                                <span class="info-value">${order.shippingAddress}</span>
                                <input type="text" class="info-input" name="shippingAddress" value="${order.shippingAddress}">
                            </div>
<%--                            <div class="info-row editable-field">--%>
<%--                                <span class="info-label">Ghi chú:</span>--%>
<%--                                <span class="info-value">${empty order.getShippingNote() ? 'Không có' : order.getShippingNote()}</span>--%>
<%--                                <input type="text" class="info-input" name="notes" value="${order.getShippingNote()}">--%>
<%--                            </div>--%>
                        </div>
                    </div>

                    <div class="edit-controls">
                        <button type="button" class="btn btn-success" onclick="saveCustomerInfo()">
                            <i class="fas fa-save"></i> Lưu thay đổi
                        </button>
                        <button type="button" class="btn btn-secondary" onclick="cancelEdit('customer-info')">
                            <i class="fas fa-times"></i> Hủy
                        </button>
                    </div>
                </form>
            </div>

<%--            <!-- Product List -->--%>
            <div class="section">
                <h3 class="section-title">
                    <span>
                        <i class="fas fa-box"></i>
                        Sản phẩm đã đặt (<span id="totalItems">${order.items.size()}</span> sản phẩm)
                    </span>
                </h3>
                <div class="product-list" id="product-list">
                    <c:forEach var="item" items="${order.items}" varStatus="status">
                        <div class="product-item" data-item-id="${item.id}">
<%--                            <img src="${item.product.imageUrl}" alt="${item.product.name}" class="product-image">--%>
                            <div class="product-details">
                                <div class="product-name">${item.productName}</div>
<%--                                <div class="product-specs">--%>
<%--                                    <div class="spec-item">--%>
<%--                                        <i class="fas fa-tag"></i>--%>
<%--                                        <span>${item.product.brand}</span>--%>
<%--                                    </div>--%>
<%--                                </div>--%>
                                <div class="product-pricing">
                                    <div class="quantity-info">
                                        <i class="fas fa-shopping-cart"></i>
                                        <span class="quantity-display">Số lượng: ${item.quantity}</span>
                                        <div class="quantity-controls">
                                            <button type="button" class="quantity-btn" onclick="updateQuantity(${item.id}, -1)">
                                                <i class="fas fa-minus"></i>
                                            </button>
                                            <input type="number" class="quantity-input"
                                                   value="${item.quantity}"
                                                   min="1"
                                                   onchange="updateQuantityDirect(${item.id}, this.value)">
                                            <button type="button" class="quantity-btn" onclick="updateQuantity(${item.id}, 1)">
                                                <i class="fas fa-plus"></i>
                                            </button>
                                        </div>
                                    </div>
                                    <div class="price-info">
                                        <div class="unit-price">
                                            Đơn giá: <fmt:formatNumber value="${item.price}" type="currency" currencySymbol="₫"/>
                                        </div>
                                        <div class="total-price" id="item-total-${item.id}">
                                            <fmt:formatNumber value="${item.subtotal}" type="currency" currencySymbol="₫"/>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                </div>
                <div class="edit-controls">
                    <button type="button" class="btn btn-success" onclick="saveQuantityChanges()">
                        <i class="fas fa-save"></i> Lưu thay đổi
                    </button>
                    <button type="button" class="btn btn-secondary" onclick="cancelEdit('product-list')">
                        <i class="fas fa-times"></i> Hủy
                    </button>
                </div>
            </div>

<%--            <!-- Order Summary -->--%>
            <div class="section">
                <h3 class="section-title">
                    <span>
                        <i class="fas fa-calculator"></i>
                        Tổng kết đơn hàng
                    </span>
                </h3>
                <div class="order-summary">
                    <div class="summary-row">
                        <span>Tạm tính:</span>
                        <span id="subtotal"><fmt:formatNumber value="${order.subtotal}" type="currency" currencySymbol="₫"/></span>
                    </div>
                    <div class="summary-row">
                        <span>Phí vận chuyển:</span>
                        <span id="shippingFee"><fmt:formatNumber value="${order.shippingFee}" type="currency" currencySymbol="₫"/></span>
                    </div>
                    <div class="summary-row">
                        <span>Tổng cộng:</span>
                        <span id="totalAmount"><fmt:formatNumber value="${order.total}" type="currency" currencySymbol="₫"/></span>
                    </div>
                </div>
            </div>
        </div>

<%--        <!-- Action Buttons -->--%>
        <div class="action-buttons">
            <c:if test="${order.status == 'pending'}">
                <button class="btn btn-secondary" onclick="cancelOrder(${order.id})">
                    <i class="fas fa-times"></i> Hủy đơn hàng
                </button>
            </c:if>

            <c:if test="${order.status == 'delivered'}">
                <button class="btn btn-primary" onclick="reorder(${order.id})">
                    <i class="fas fa-redo"></i> Mua lại
                </button>
            </c:if>
        </div>
    </div>
</div>

<script>
    let originalData = {};
    let isEditing = false;

    function changeOrderDetail(orderId) {
        window.location.href = 'order-modification?id=' + orderId;
    }

    function toggleEditMode(sectionId) {
        const section = document.getElementById(sectionId);
        const editToggle = section.closest('.section').querySelector('.edit-toggle');

        if (!isEditing) {
            // Save original data
            saveOriginalData(sectionId);

            section.classList.add('edit-mode');
            editToggle.innerHTML = '<i class="fas fa-times"></i> Hủy chỉnh sửa';
            editToggle.onclick = () => cancelEdit(sectionId);
            isEditing = true;
        }
    }

    function saveOriginalData(sectionId) {
        originalData[sectionId] = {};

        if (sectionId === 'customer-info') {
            const form = document.getElementById('customer-info-form');
            const inputs = form.querySelectorAll('input');
            inputs.forEach(input => {
                originalData[sectionId][input.name] = input.value;
            });
        } else if (sectionId === 'product-list') {
            const quantityInputs = document.querySelectorAll('.quantity-input');
            quantityInputs.forEach(input => {
                const itemId = input.closest('[data-item-id]').getAttribute('data-item-id');
                originalData[sectionId][itemId] = input.value;
            });
        }
    }

    function cancelEdit(sectionId) {
        const section = document.getElementById(sectionId);
        const editToggle = section.closest('.section').querySelector('.edit-toggle');

        // Restore original data
        if (sectionId === 'customer-info') {
            const form = document.getElementById('customer-info-form');
            const inputs = form.querySelectorAll('input');
            inputs.forEach(input => {
                input.value = originalData[sectionId][input.name];
            });
        } else if (sectionId === 'product-list') {
            const quantityInputs = document.querySelectorAll('.quantity-input');
            quantityInputs.forEach(input => {
                const itemId = input.closest('[data-item-id]').getAttribute('data-item-id');
                input.value = originalData[sectionId][itemId];
            });
        }

        section.classList.remove('edit-mode');
        editToggle.innerHTML = '<i class="fas fa-edit"></i> Chỉnh sửa';
        editToggle.onclick = () => toggleEditMode(sectionId);
        isEditing = false;
    }

    function saveCustomerInfo() {
        const form = document.getElementById('customer-info-form');
        const formData = new FormData(form);
        formData.append('orderId', '${order.id}');

        fetch('updateOrderInfo', {
            method: 'POST',
            body: formData
        })
            .then(response => response.json())
            .then(data => {
                if (data.success) {
                    alert('Thông tin đã được cập nhật thành công!');
                    location.reload();
                } else {
                    alert('Có lỗi xảy ra: ' + data.message);
                }
            })
            .catch(error => {
                console.error('Error:', error);
                alert('Có lỗi xảy ra khi cập nhật thông tin!');
            });
    }

    function updateQuantity(itemId, change) {
        const input = document.querySelector(`[data-item-id="${itemId}"] .quantity-input`);
        const currentValue = parseInt(input.value);
        const newValue = currentValue + change;

        if (newValue >= 1) {
            input.value = newValue;
            updateItemTotal(itemId, newValue);
        }
    }

    function updateQuantityDirect(itemId, newValue) {
        const value = parseInt(newValue);
        if (value >= 1) {
            updateItemTotal(itemId, value);
        } else {
            const input = document.querySelector(`[data-item-id="${itemId}"] .quantity-input`);
            input.value = 1;
            updateItemTotal(itemId, 1);
        }
    }

    function updateItemTotal(itemId, quantity) {
        // This would normally fetch unit price from the server or have it stored in data attributes
        // For now, we'll calculate based on the current total divided by current quantity
        const item = document.querySelector(`[data-item-id="${itemId}"]`);
        const priceElement = item.querySelector('.total-price');
        const currentTotal = parseFloat(priceElement.textContent.replace(/[₫,]/g, ''));
        const currentQuantity = parseInt(originalData['product-list'][itemId]);
        const unitPrice = currentTotal / currentQuantity;
        const newTotal = unitPrice * quantity;

        priceElement.textContent = new Intl.NumberFormat('vi-VN', {
            style: 'currency',
            currency: 'VND',
            currencyDisplay: 'symbol'
        }).format(newTotal).replace('VND', '₫');

        updateOrderSummary();
    }

    function updateOrderSummary() {
        let subtotal = 0;
        document.querySelectorAll('.total-price').forEach(element => {
            const value = parseFloat(element.textContent.replace(/[₫,]/g, ''));
            subtotal += value;
        });

        const shippingFee = parseFloat(document.getElementById('shippingFee').textContent.replace(/[₫,]/g, ''));
        const discount = parseFloat((document.getElementById('discount')?.textContent || '0').replace(/[₫,-]/g, ''));
        const total = subtotal + shippingFee - discount;

        document.getElementById('subtotal').textContent = new Intl.NumberFormat('vi-VN', {
            style: 'currency',
            currency: 'VND'
        }).format(subtotal).replace('VND', '₫');

        document.getElementById('totalAmount').textContent = new Intl.NumberFormat('vi-VN', {
            style: 'currency',
            currency: 'VND'
        }).format(total).replace('VND', '₫');

        document.getElementById('orderTotal').textContent = new Intl.NumberFormat('vi-VN', {
            style: 'currency',
            currency: 'VND'
        }).format(total).replace('VND', '₫');
    }

    function saveQuantityChanges() {
        const quantityData = {};
        document.querySelectorAll('.quantity-input').forEach(input => {
            const itemId = input.closest('[data-item-id]').getAttribute('data-item-id');
            quantityData[itemId] = input.value;
        });

        fetch('updateOrderQuantities', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                orderId: '${order.id}',
                quantities: quantityData
            })
        })
            .then(response => response.json())
            .then(data => {
                if (data.success) {
                    alert('Số lượng đã được cập nhật thành công!');
                    location.reload();
                } else {
                    alert('Có lỗi xảy ra: ' + data.message);
                }
            })
            .catch(error => {
                console.error('Error:', error);
                alert('Có lỗi xảy ra khi cập nhật số lượng!');
            });
    }

    function cancelOrder(orderId) {
        if (confirm('Bạn có chắc chắn muốn hủy đơn hàng này?')) {
            fetch('cancelOrder', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded',
                },
                body: 'orderId=' + orderId
            })
                .then(response => response.json())
                .then(data => {
                    if (data.success) {
                        alert('Đơn hàng đã được hủy thành công!');
                        location.reload();
                    } else {
                        alert('Có lỗi xảy ra khi hủy đơn hàng: ' + data.message);
                    }
                })
                .catch(error => {
                    console.error('Error:', error);
                    alert('Có lỗi xảy ra khi hủy đơn hàng!');
                });
        }
    }

    function reorder(orderId) {
        if (confirm('Bạn muốn mua lại các sản phẩm trong đơn hàng này?')) {
            window.location.href = 'reorder?orderId=' + orderId;
        }
    }

    function printOrder() {
        window.print();
    }

    function downloadInvoice(orderId) {
        window.location.href = 'download-invoice?orderId=' + orderId;
    }

    // Auto refresh for pending/shipping orders
    if ('${order.status}' === 'pending' || '${order.status}' === 'shipping') {
        setInterval(function() {
            if (!isEditing) {
                location.reload();
            }
        }, 30000);
    }
</script>
</body>
</html>
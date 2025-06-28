<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Lịch sử đơn hàng - CellphoneS</title>
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

        .header {
            background: linear-gradient(135deg, #d70018, #ff4444);
            color: white;
            padding: 1rem 0;
            box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
        }

        .header-content {
            max-width: 1200px;
            margin: 0 auto;
            padding: 0 20px;
            display: flex;
            align-items: center;
            justify-content: space-between;
        }

        .logo {
            font-size: 24px;
            font-weight: bold;
        }

        .user-info {
            display: flex;
            align-items: center;
            gap: 10px;
        }

        .container {
            max-width: 1200px;
            margin: 20px auto;
            padding: 0 20px;
        }

        .page-title {
            background: white;
            padding: 20px;
            border-radius: 8px;
            margin-bottom: 20px;
            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
        }

        .page-title h1 {
            color: #d70018;
            font-size: 28px;
            margin-bottom: 10px;
        }

        .filter-section {
            background: white;
            padding: 20px;
            border-radius: 8px;
            margin-bottom: 20px;
            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
        }

        .filter-row {
            display: flex;
            gap: 15px;
            align-items: center;
            flex-wrap: wrap;
        }

        .filter-group {
            display: flex;
            flex-direction: column;
            gap: 5px;
        }

        .filter-group label {
            font-weight: 500;
            color: #555;
        }

        .filter-group select,
        .filter-group input {
            padding: 8px 12px;
            border: 1px solid #ddd;
            border-radius: 4px;
            font-size: 14px;
        }

        .btn {
            padding: 8px 20px;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            font-size: 14px;
            transition: all 0.3s ease;
        }

        .btn-primary {
            background: #d70018;
            color: white;
        }

        .btn-primary:hover {
            background: #b50015;
        }

        .order-list {
            display: flex;
            flex-direction: column;
            gap: 20px;
        }

        .order-card {
            background: white;
            border-radius: 8px;
            overflow: hidden;
            box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
            transition: transform 0.2s ease;
        }

        .order-card:hover {
            transform: translateY(-2px);
        }

        .order-header {
            background: #f8f9fa;
            padding: 15px 20px;
            border-bottom: 1px solid #eee;
            display: flex;
            justify-content: space-between;
            align-items: center;
            flex-wrap: wrap;
            gap: 10px;
        }

        .order-info {
            display: flex;
            gap: 20px;
            align-items: center;
            flex-wrap: wrap;
        }

        .order-id {
            font-weight: bold;
            color: #d70018;
        }

        .order-date {
            color: #666;
            font-size: 14px;
        }

        .order-status {
            padding: 4px 12px;
            border-radius: 20px;
            font-size: 12px;
            font-weight: 500;
            text-transform: uppercase;
        }

        .status-pending {
            background: #fff3cd;
            color: #856404;
        }

        .status-confirmed {
            background: #d4edda;
            color: #155724;
        }

        .status-shipping {
            background: #cce7ff;
            color: #004085;
        }

        .status-delivered {
            background: #d1ecf1;
            color: #0c5460;
        }

        .status-cancelled {
            background: #f8d7da;
            color: #721c24;
        }

        .order-total {
            font-weight: bold;
            color: #d70018;
            font-size: 18px;
        }

        .order-body {
            padding: 20px;
        }

        .product-list {
            display: flex;
            flex-direction: column;
            gap: 15px;
        }

        .product-item {
            display: flex;
            gap: 15px;
            padding: 15px;
            background: #f8f9fa;
            border-radius: 8px;
        }

        .product-image {
            width: 80px;
            height: 80px;
            border-radius: 8px;
            object-fit: cover;
            border: 1px solid #eee;
        }

        .product-details {
            flex: 1;
            display: flex;
            flex-direction: column;
            gap: 5px;
        }

        .product-name {
            font-weight: 500;
            color: #333;
            line-height: 1.4;
        }

        .product-specs {
            font-size: 13px;
            color: #666;
        }

        .product-price-info {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-top: 10px;
        }

        .product-quantity {
            font-size: 14px;
            color: #666;
        }

        .product-price {
            font-weight: bold;
            color: #d70018;
        }

        .order-actions {
            padding: 15px 20px;
            background: #f8f9fa;
            border-top: 1px solid #eee;
            display: flex;
            justify-content: space-between;
            align-items: center;
            flex-wrap: wrap;
            gap: 10px;
        }

        .delivery-info {
            font-size: 14px;
            color: #666;
        }

        .action-buttons {
            display: flex;
            gap: 10px;
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
            border: 1px solid #d70018;
        }

        .btn-outline:hover {
            background: #d70018;
            color: white;
        }

        .empty-state {
            text-align: center;
            padding: 60px 20px;
            background: white;
            border-radius: 8px;
            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
        }

        .empty-state i {
            font-size: 64px;
            color: #ccc;
            margin-bottom: 20px;
        }

        .empty-state h3 {
            color: #666;
            margin-bottom: 10px;
        }

        .empty-state p {
            color: #999;
            margin-bottom: 20px;
        }

        @media (max-width: 768px) {
            .header-content {
                flex-direction: column;
                gap: 10px;
            }

            .filter-row {
                flex-direction: column;
                align-items: stretch;
            }

            .order-header {
                flex-direction: column;
                align-items: flex-start;
            }

            .order-info {
                width: 100%;
                justify-content: space-between;
            }

            .product-item {
                flex-direction: column;
                text-align: center;
            }

            .product-image {
                align-self: center;
            }

            .product-price-info {
                flex-direction: column;
                gap: 10px;
                text-align: center;
            }

            .order-actions {
                flex-direction: column;
                align-items: stretch;
            }

            .action-buttons {
                justify-content: center;
            }
        }
    </style>
</head>
<body>
<!-- Header -->
<%--<header class="header">--%>
<%--    <div class="header-content">--%>
<%--        <div class="logo">--%>
<%--            <i class="fas fa-mobile-alt"></i> CellphoneS--%>
<%--        </div>--%>
<%--        <div class="user-info">--%>
<%--            <i class="fas fa-user-circle"></i>--%>
<%--            <span>Xin chào, ${sessionScope.user.fullName}</span>--%>
<%--        </div>--%>
<%--    </div>--%>
<%--</header>--%>

<div class="container">
    <!-- Page Title -->
    <div class="page-title">
        <h1><i class="fas fa-shopping-bag"></i> Lịch sử đơn hàng</h1>
        <p>Quản lý và theo dõi tình trạng các đơn hàng của bạn</p>
    </div>

    <!-- Filter Section -->
    <div class="filter-section">
        <form method="GET" action="order-history.jsp">
            <div class="filter-row">
                <div class="filter-group">
                    <label>Trạng thái đơn hàng</label>
                    <select name="status">
                        <option value="">Tất cả</option>
                        <option value="pending" ${param.status == 'pending' ? 'selected' : ''}>Chờ xác nhận</option>
                        <option value="confirmed" ${param.status == 'confirmed' ? 'selected' : ''}>Đã xác nhận</option>
                        <option value="shipping" ${param.status == 'shipping' ? 'selected' : ''}>Đang giao</option>
                        <option value="delivered" ${param.status == 'delivered' ? 'selected' : ''}>Đã giao</option>
                        <option value="cancelled" ${param.status == 'cancelled' ? 'selected' : ''}>Đã hủy</option>
                    </select>
                </div>

                <div class="filter-group">
                    <label>Từ ngày</label>
                    <input type="date" name="fromDate" value="${param.fromDate}">
                </div>

                <div class="filter-group">
                    <label>Đến ngày</label>
                    <input type="date" name="toDate" value="${param.toDate}">
                </div>

                <div class="filter-group" style="margin-top: 20px;">
                    <button type="submit" class="btn btn-primary">
                        <i class="fas fa-search"></i> Tìm kiếm
                    </button>
                </div>
            </div>
        </form>
    </div>

    <!-- Order List -->
    <div class="order-list">
        <c:choose>
            <c:when test="${empty orders}">
                <div class="empty-state">
                    <i class="fas fa-shopping-bag"></i>
                    <h3>Chưa có đơn hàng nào</h3>
                    <p>Bạn chưa có đơn hàng nào. Hãy bắt đầu mua sắm ngay!</p>
                    <a href="products.jsp" class="btn btn-primary">
                        <i class="fas fa-shopping-cart"></i> Mua sắm ngay
                    </a>
                </div>
            </c:when>
            <c:otherwise>
                <c:forEach var="order" items="${orders}">
                    <div class="order-card">
                        <!-- Order Header -->
                        <div class="order-header">
                            <div class="order-info">
                                <span class="order-id">Đơn hàng #${order.getOrderNumber()}</span>
                                <span class="order-date">
                                    <i class="fas fa-calendar"></i>
                                        ${order.getCreatedAt()}
                                    </span>
                                <span class="order-status status-${order.getStatus()}">
                                    <c:choose>
                                        <c:when test="${order.getStatus() == 'Pending'}">Chờ xác nhận</c:when>
                                        <c:when test="${order.getStatus() == 'Confirmed'}">Đã xác nhận</c:when>
                                        <c:when test="${order.getStatus() == 'Shipping'}">Đang giao</c:when>
                                        <c:when test="${order.getStatus() == 'Delivered'}">Đã giao</c:when>
                                        <c:when test="${order.getStatus() == 'Cancelled'}">Đã hủy</c:when>
                                    </c:choose>
                                </span>
                            </div>
                            <div class="order-total">
                                <fmt:formatNumber value="${order.getTotal()}" type="currency" currencySymbol="₫"/>
                            </div>
                        </div>

                        <!-- Order Body -->
                        <div class="order-body">
                            <div class="product-list">
                                <c:forEach var="item" items="${order.items}">
                                    <div class="product-item">
                                        <img src="https://cdn.hoanghamobile.com/i/productlist/dsp/Uploads/2023/09/12/iphone-15-pro-max-1.jpg" alt="${item.productName}" class="product-image">
                                    <%--                                        <img src="${item.product.imageUrl}" alt="${item.product.name}" class="product-image">--%>
                                        <div class="product-details">
                                            <div class="product-name">${item.productName}</div>
<%--                                            <div class="product-specs">--%>
<%--                                                    ${item.product.description}--%>
<%--                                            </div>--%>
                                            <div class="product-price-info"><span class="product-quantity">Số lượng: ${item.quantity}</span>
                                                <span class="product-price">
                                                    <fmt:formatNumber value="${item.price}" type="currency" currencySymbol="₫"/>
                                                </span>
                                            </div>
                                        </div>
                                    </div>
                                </c:forEach>
                            </div>
                        </div>

                        <!-- Order Actions -->
                        <div class="order-actions">
                            <div class="delivery-info">
                                <i class="fas fa-truck"></i>
                                Giao đến: ${order.shippingAddress}
                            </div>
                            <div class="action-buttons">
                                <c:if test="${order.status == 'Pending'}">
                                    <button class="btn btn-secondary" onclick="cancelOrder(${order.id})">
                                        <i class="fas fa-times"></i> Hủy đơn
                                    </button>
                                </c:if>
                                <button class="btn btn-outline" onclick="viewOrderDetail(${order.id})">
                                    <i class="fas fa-eye"></i> Xem chi tiết
                                </button>
                                <c:if test="${order.status == 'Delivered'}">
                                    <button class="btn btn-primary" onclick="reorder(${order.id})">
                                        <i class="fas fa-redo"></i> Mua lại
                                    </button>
                                </c:if>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </c:otherwise>
        </c:choose>
    </div>
</div>

<script>
    function viewOrderDetail(orderId) {
        window.location.href = 'order-details?id=' + orderId;
    }

    function cancelOrder(orderId) {
        if (confirm('Bạn có chắc chắn muốn hủy đơn hàng này?')) {
            // Gửi request để hủy đơn hàng
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

    // Auto refresh page every 30 seconds for status updates
    setInterval(function () {
        // Only refresh if there are pending or shipping orders
        const hasPendingOrders = document.querySelector('.status-pending, .status-shipping');
        if (hasPendingOrders) {
            location.reload();
        }
    }, 30000);
</script>
</body>
</html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Chỉnh sửa đơn hàng #${order.orderNumber}</title>
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

        .header {
            display: flex;
            align-items: center;
            gap: 20px;
            margin-bottom: 30px;
            padding: 20px;
            background: white;
            border-radius: 12px;
            box-shadow: 0 2px 8px rgba(0,0,0,0.1);
        }

        .back-button {
            padding: 10px 20px;
            background: #6c757d;
            color: white;
            border: none;
            border-radius: 6px;
            cursor: pointer;
            text-decoration: none;
            display: inline-flex;
            align-items: center;
            gap: 8px;
            font-size: 14px;
            transition: all 0.3s ease;
        }

        .back-button:hover {
            background: #545b62;
        }

        .header-info {
            flex: 1;
        }

        .header-title {
            font-size: 24px;
            font-weight: bold;
            color: #d70018;
            margin-bottom: 5px;
        }

        .header-subtitle {
            color: #666;
            font-size: 16px;
        }

        .edit-sections {
            display: grid;
            grid-template-columns: 1fr 1fr;
            gap: 30px;
            margin-bottom: 30px;
        }

        .edit-section {
            background: white;
            border-radius: 12px;
            overflow: hidden;
            box-shadow: 0 4px 12px rgba(0,0,0,0.1);
        }

        .section-header {
            background: linear-gradient(135deg, #d70018, #ff4444);
            color: white;
            padding: 20px;
            text-align: center;
        }

        .section-header h2 {
            font-size: 18px;
            margin-bottom: 5px;
        }

        .section-header p {
            opacity: 0.9;
            font-size: 13px;
        }

        .section-body {
            padding: 25px;
        }

        .form-group {
            margin-bottom: 20px;
        }

        .form-label {
            font-weight: 600;
            color: #495057;
            margin-bottom: 8px;
            display: flex;
            align-items: center;
            gap: 6px;
        }

        .form-control {
            width: 100%;
            padding: 12px 15px;
            border: 2px solid #e9ecef;
            border-radius: 8px;
            font-size: 14px;
            transition: all 0.3s ease;
            background: white;
        }

        .form-control:focus {
            outline: none;
            border-color: #d70018;
            box-shadow: 0 0 0 3px rgba(215, 0, 24, 0.1);
        }

        .form-control:disabled {
            background: #f8f9fa;
            color: #6c757d;
            cursor: not-allowed;
        }

        .order-items-section {
            background: white;
            border-radius: 12px;
            overflow: hidden;
            box-shadow: 0 4px 12px rgba(0,0,0,0.1);
            margin-bottom: 30px;
        }

        .order-items-header {
            background: linear-gradient(135deg, #28a745, #20c997);
            color: white;
            padding: 20px;
            text-align: center;
        }

        .order-items-list {
            padding: 20px;
        }

        .order-item {
            display: flex;
            gap: 20px;
            padding: 20px;
            border: 2px solid #e9ecef;
            border-radius: 10px;
            margin-bottom: 15px;
            background: #f8f9fa;
            transition: all 0.3s ease;
        }

        .order-item:hover {
            border-color: #d70018;
            transform: translateY(-2px);
            box-shadow: 0 4px 8px rgba(0,0,0,0.1);
        }

        .item-image {
            width: 100px;
            height: 100px;
            border-radius: 8px;
            object-fit: cover;
            border: 2px solid #ddd;
        }

        .item-details {
            flex: 1;
            display: flex;
            flex-direction: column;
            gap: 8px;
        }

        .item-name {
            font-size: 16px;
            font-weight: bold;
            color: #333;
        }

        .item-specs {
            display: flex;
            gap: 10px;
            flex-wrap: wrap;
        }

        .spec-badge {
            background: #e9ecef;
            padding: 4px 8px;
            border-radius: 12px;
            font-size: 12px;
            color: #495057;
        }

        .item-controls {
            display: flex;
            flex-direction: column;
            align-items: flex-end;
            justify-content: space-between;
            min-width: 200px;
        }

        .item-price {
            font-size: 16px;
            font-weight: bold;
            color: #d70018;
        }

        .quantity-control {
            display: flex;
            align-items: center;
            gap: 10px;
            margin: 10px 0;
        }

        .quantity-btn {
            width: 35px;
            height: 35px;
            border: none;
            background: #d70018;
            color: white;
            border-radius: 6px;
            cursor: pointer;
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 14px;
            transition: all 0.3s ease;
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
            padding: 8px;
            text-align: center;
            border: 2px solid #d70018;
            border-radius: 6px;
            font-size: 14px;
            font-weight: bold;
        }

        .item-actions {
            display: flex;
            gap: 8px;
            margin-top: 10px;
        }

        .btn-small {
            padding: 6px 12px;
            font-size: 12px;
            border: none;
            border-radius: 6px;
            cursor: pointer;
            text-decoration: none;
            display: inline-flex;
            align-items: center;
            gap: 4px;
            transition: all 0.3s ease;
        }

        .btn-edit {
            background: #ffc107;
            color: #212529;
        }

        .btn-edit:hover {
            background: #e0a800;
        }

        .btn-remove {
            background: #dc3545;
            color: white;
        }

        .btn-remove:hover {
            background: #c82333;
        }

        .order-summary {
            background: white;
            border-radius: 12px;
            overflow: hidden;
            box-shadow: 0 4px 12px rgba(0,0,0,0.1);
            margin-bottom: 30px;
        }

        .summary-header {
            background: linear-gradient(135deg, #6f42c1, #8b5cf6);
            color: white;
            padding: 20px;
            text-align: center;
        }

        .summary-body {
            padding: 25px;
        }

        .summary-row {
            display: flex;
            justify-content: space-between;
            align-items: center;
            padding: 10px 0;
            border-bottom: 1px solid #e9ecef;
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

        .action-buttons {
            display: flex;
            gap: 15px;
            justify-content: center;
            padding: 25px;
            background: white;
            border-radius: 12px;
            box-shadow: 0 4px 12px rgba(0,0,0,0.1);
        }

        .btn {
            padding: 12px 25px;
            border: none;
            border-radius: 8px;
            cursor: pointer;
            font-size: 15px;
            font-weight: 500;
            text-decoration: none;
            display: inline-flex;
            align-items: center;
            gap: 8px;
            transition: all 0.3s ease;
            min-width: 140px;
            justify-content: center;
        }

        .btn-primary {
            background: #d70018;
            color: white;
        }

        .btn-primary:hover {
            background: #b50015;
            transform: translateY(-2px);
        }

        .btn-secondary {
            background: #6c757d;
            color: white;
        }

        .btn-secondary:hover {
            background: #545b62;
        }

        .btn-success {
            background: #28a745;
            color: white;
        }

        .btn-success:hover {
            background: #218838;
        }

        .warning-message {
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

        .success-message {
            background: #d4edda;
            color: #155724;
            padding: 15px;
            border-radius: 8px;
            margin-bottom: 20px;
            border: 1px solid #c3e6cb;
            display: flex;
            align-items: center;
            gap: 10px;
        }

        .loading-overlay {
            position: fixed;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            background: rgba(0,0,0,0.5);
            display: none;
            align-items: center;
            justify-content: center;
            z-index: 1000;
        }

        .loading-content {
            background: white;
            padding: 30px;
            border-radius: 10px;
            text-align: center;
            display: flex;
            flex-direction: column;
            align-items: center;
            gap: 15px;
        }

        .spinner {
            width: 40px;
            height: 40px;
            border: 4px solid #f3f3f3;
            border-top: 4px solid #d70018;
            border-radius: 50%;
            animation: spin 1s linear infinite;
        }

        @keyframes spin {
            0% { transform: rotate(0deg); }
            100% { transform: rotate(360deg); }
        }

        @media (max-width: 768px) {
            .container {
                padding: 0 15px;
            }

            .edit-sections {
                grid-template-columns: 1fr;
                gap: 20px;
            }

            .order-item {
                flex-direction: column;
                text-align: center;
            }

            .item-controls {
                align-items: center;
                min-width: auto;
            }

            .action-buttons {
                flex-direction: column;
            }
        }
    </style>
</head>
<body>
<div class="container">
    <!-- Header -->
    <div class="header">
        <a href="#" class="back-button" onclick="history.back()">
            <i class="fas fa-arrow-left"></i>
            Quay lại
        </a>
        <div class="header-info">
            <div class="header-title">Chỉnh sửa sản phẩm</div>
            <div class="header-subtitle">Đơn hàng #12345 - Trạng thái: Chờ xử lý</div>
        </div>
    </div>

    <!-- Edit Form -->
    <div class="edit-form">
        <div class="form-header">
            <h2><i class="fas fa-edit"></i> Chỉnh sửa thông tin sản phẩm</h2>
            <p>Bạn có thể thay đổi số lượng hoặc chọn sản phẩm tương tự</p>
        </div>

        <div class="form-body">
            <form id="editProductForm">
                <input type="hidden" name="orderId" value="12345">
                <input type="hidden" name="orderItemId" value="67890">

                <!-- Current Product Preview -->
                <div class="product-preview">
                    <div class="product-image">Hình ảnh<br>sản phẩm</div>
                    <div class="product-info">
                        <div class="product-name">iPhone 14 Pro Max 256GB</div>
                        <div class="product-specs">
                                <span class="spec-badge">
                                    <i class="fas fa-tag"></i>
                                    Apple
                                </span>
                            <span class="spec-badge">
                                    <i class="fas fa-hdd"></i>
                                    256GB
                                </span>
                            <span class="spec-badge">
                                    <i class="fas fa-palette"></i>
                                    Deep Purple
                                </span>
                        </div>
                        <div class="current-price">29.990.000₫</div>
                    </div>
                </div>

                <!-- Quantity Section -->
                <div class="form-section">
                    <h3 class="section-title">
                        <i class="fas fa-calculator"></i>
                        Điều chỉnh số lượng
                    </h3>

                    <div class="quantity-control">
                        <div class="quantity-label">Số lượng:</div>
                        <div class="quantity-input-group">
                            <button type="button" class="quantity-btn" onclick="changeQuantity(-1)">
                                <i class="fas fa-minus"></i>
                            </button>
                            <input type="number"
                                   class="quantity-input"
                                   name="quantity"
                                   value="2"
                                   min="1"
                                   max="99"
                                   onchange="updatePrice()">
                            <button type="button" class="quantity-btn" onclick="changeQuantity(1)">
                                <i class="fas fa-plus"></i>
                            </button>
                        </div>
                        <div class="quantity-info">
                            (Tối đa 99 sản phẩm)
                        </div>
                    </div>

                    <!-- Price Calculation -->
                    <div class="price-calculation">
                        <div class="price-row">
                            <span>Đơn giá:</span>
                            <span id="unitPrice">29.990.000₫</span>
                        </div>
                        <div class="price-row">
                            <span>Số lượng:</span>
                            <span id="displayQuantity">2</span>
                        </div>
                        <div class="price-row">
                            <span>Thành tiền:</span>
                            <span id="totalPrice">59.980.000₫</span>
                        </div>
                    </div>
                </div>

                <!-- Product Specifications -->
                <div class="form-section">
                    <h3 class="section-title">
                        <i class="fas fa-info-circle"></i>
                        Thông tin sản phẩm
                    </h3>

                    <div class="form-grid">
                        <div class="form-group">
                            <label class="form-label">
                                <i class="fas fa-tag"></i>
                                Thương hiệu
                            </label>
                            <input type="text" class="form-control" value="Apple" disabled>
                        </div>

                        <div class="form-group">
                            <label class="form-label">
                                <i class="fas fa-hdd"></i>
                                Dung lượng
                            </label>
                            <input type="text" class="form-control" value="256GB" disabled>
                        </div>

                        <div class="form-group">
                            <label class="form-label">
                                <i class="fas fa-palette"></i>
                                Màu sắc
                            </label>
                            <input type="text" class="form-control" value="Deep Purple" disabled>
                        </div>

                        <div class="form-group">
                            <label class="form-label">
                                <i class="fas fa-shield-alt"></i>
                                Bảo hành
                            </label>
                            <input type="text" class="form-control" value="12 tháng" disabled>
                        </div>
                    </div>
                </div>

                <!-- Alternative Products -->
                <div class="form-section">
                    <div class="alternative-products">
                        <div class="alternative-title">
                            <i class="fas fa-exchange-alt"></i>
                            Sản phẩm tương tự (tùy chọn)
                        </div>
                        <div class="alternative-list" id="alternativeProducts">
                            <div class="alternative-item" onclick="selectAlternative(1001)">
                                <div class="alternative-image">Ảnh SP</div>
                                <div class="alternative-details">
                                    <div class="alternative-name">iPhone 14 Pro Max 128GB</div>
                                    <div class="alternative-price">25.990.000₫</div>
                                </div>
                            </div>
                            <div class="alternative-item" onclick="selectAlternative(1002)">
                                <div class="alternative-image">Ảnh SP</div>
                                <div class="alternative-details">
                                    <div class="alternative-name">iPhone 14 Pro Max 512GB</div>
                                    <div class="alternative-price">35.990.000₫</div>
                                </div>
                            </div>
                            <div class="alternative-item" onclick="selectAlternative(1003)">
                                <div class="alternative-image">Ảnh SP</div>
                                <div class="alternative-details">
                                    <div class="alternative-name">iPhone 14 Pro Max 1TB</div>
                                    <div class="alternative-price">41.990.000₫</div>
                                </div>
                            </div>
                        </div>
                        <input type="hidden" name="alternativeProductId" id="alternativeProductId">
                    </div>
                </div>

                <!-- Action Buttons -->
                <div class="action-buttons">
                    <button type="submit" class="btn btn-primary">
                        <i class="fas fa-save"></i>
                        Lưu thay đổi
                    </button>
                    <button type="button" class="btn btn-outline" onclick="resetForm()">
                        <i class="fas fa-undo"></i>
                        Khôi phục
                    </button>
                    <a href="#" class="btn btn-secondary" onclick="history.back()">
                        <i class="fas fa-times"></i>
                        Hủy bỏ
                    </a>
                </div>
            </form>
        </div>
    </div>
</div>

<!-- Loading Overlay -->
<div class="loading-overlay" id="loadingOverlay">
    <div class="loading-content">
        <div class="spinner"></div>
        <div>Đang xử lý...</div>
    </div>
</div>

<script>
    const originalQuantity = 2;
    const unitPrice = 29990000;
    let selectedAlternativeId = null;

    function changeQuantity(change) {
        const quantityInput = document.querySelector('input[name="quantity"]');
        const currentValue = parseInt(quantityInput.value);
        const newValue = currentValue + change;

        if (newValue >= 1 && newValue <= 99) {
            quantityInput.value = newValue;
            updatePrice();
        }
    }

    function updatePrice() {
        const quantityInput = document.querySelector('input[name="quantity"]');
        const quantity = parseInt(quantityInput.value) || 1;

        // Ensure quantity is within bounds
        if (quantity < 1) {
            quantityInput.value = 1;
            return;
        }
        if (quantity > 99) {
            quantityInput.value = 99;
            return;
        }

        const total = unitPrice * quantity;

        document.getElementById('displayQuantity').textContent = quantity;
        document.getElementById('totalPrice').textContent = new Intl.NumberFormat('vi-VN', {
            style: 'currency',
            currency: 'VND'
        }).format(total).replace('VND', '₫');
    }

    function selectAlternative(productId) {
        // Remove previous selection
        document.querySelectorAll('.alternative-item').forEach(item => {
            item.classList.remove('selected');
        });

        // Add selection to clicked item
        event.currentTarget.classList.add('selected');

        // Store selected alternative ID
        selectedAlternativeId = productId;
        document.getElementById('alternativeProductId').value = productId;
    }

    function resetForm() {
        const quantityInput = document.querySelector('input[name="quantity"]');
        quantityInput.value = originalQuantity;

        // Clear alternative selection
        document.querySelectorAll('.alternative-item').forEach(item => {
            item.classList.remove('selected');
        });
        selectedAlternativeId = null;
        document.getElementById('alternativeProductId').value = '';

        updatePrice();
    }

    // Form submission
    document.getElementById('editProductForm').addEventListener('submit', function(e) {
        e.preventDefault();

        const formData = new FormData(this);
        const loadingOverlay = document.getElementById('loadingOverlay');

        // Show loading
        loadingOverlay.style.display = 'flex';

        // Simulate API call
        setTimeout(() => {
            loadingOverlay.style.display = 'none';

            // Show success message
            const successDiv = document.createElement('div');
            successDiv.className = 'success-message';
            successDiv.innerHTML = `
                    <i class="fas fa-check-circle"></i>
                    <span>Cập nhật sản phẩm thành công!</span>
                `;

            document.querySelector('.form-body').insertBefore(successDiv, document.querySelector('.form-section'));

            // Auto remove success message after 3 seconds
            setTimeout(() => {
                successDiv.remove();
            }, 3000);
        }, 2000);
    });

    // Prevent form submission on Enter in quantity input
    document.querySelector('input[name="quantity"]').addEventListener('keydown', function(e) {
        if (e.key === 'Enter') {
            e.preventDefault();
            updatePrice();
        }
    });
</script>
</body>
</html>
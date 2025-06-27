package swd.project.swdgr3project.controller;

import swd.project.swdgr3project.entity.CartItem;
import swd.project.swdgr3project.entity.Product;
import swd.project.swdgr3project.entity.ShoppingCart;
import swd.project.swdgr3project.model.dto.ProductDTO;
import swd.project.swdgr3project.service.ProductService;
import swd.project.swdgr3project.service.impl.ProductServiceImpl;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/setup-cart")
public class SetupCartServlet extends HttpServlet {

    private final ProductService productService = new ProductServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("--- [SetupCartServlet] Bắt đầu ---");
        try {
            // 1. Lấy sản phẩm từ service
            ProductDTO p1_dto = productService.getProductById(1L);
            System.out.println("Sản phẩm DTO lấy từ service: " + (p1_dto != null ? p1_dto.getName() : "NULL"));

            if (p1_dto == null) {
                System.err.println("Lỗi SetupCartServlet: Không tìm thấy sản phẩm ID 1.");
                resp.sendRedirect("checkout?error=product_not_found");
                return;
            }

            // Chuyển DTO thành Entity để dùng trong CartItem
            Product p1_entity = toProductEntity(p1_dto);
            if (p1_entity == null) {
                System.err.println("Lỗi SetupCartServlet: Không thể chuyển đổi ProductDTO sang Product.");
                resp.sendRedirect("checkout");
                return;
            }
            System.out.println("Sản phẩm Entity sau khi chuyển đổi: " + (p1_entity != null ? p1_entity.getName() : "NULL"));

            // Tạo CartItem mới
            CartItem newItem = new CartItem();
            newItem.setProduct(p1_entity);
            newItem.setQuantity(1);
            if (p1_entity.getPrice() != null) {
                newItem.setPrice(p1_entity.getPrice()); // Lấy price từ Product entity (là BigDecimal)
                System.out.println("Đã tạo CartItem: " + newItem.getProduct().getName() + ", Giá: " + newItem.getPrice());
            } else {
                System.err.println("Lỗi SetupCartServlet: Giá sản phẩm là null!");
                resp.sendRedirect("checkout");
                return;
            }

            // Lấy giỏ hàng từ session, nếu chưa có thì tạo mới
            HttpSession session = req.getSession();
            ShoppingCart cart = (ShoppingCart) session.getAttribute("cart");

            if (cart == null) {
                System.out.println("Không có giỏ hàng trong session, tạo mới.");
                cart = new ShoppingCart();
            } else {
                System.out.println("Đã có giỏ hàng trong session. Số loại sản phẩm hiện tại: " + cart.getItems().size());
            }

            // SỬA LỖI: Gọi đúng phương thức addItem trong ShoppingCart
            // Phương thức addItem của ShoppingCart mong đợi một đối tượng CartItem
            cart.addItem(newItem);
            System.out.println("Đã thêm sản phẩm vào giỏ hàng. Tổng số loại sản phẩm trong giỏ: " + cart.getItems().size());
            // Bạn có thể gọi cart.getItemCount() nếu phương thức đó tồn tại
            // System.out.println("Tổng số lượng sản phẩm: " + cart.getItemCount());

            session.setAttribute("cart", cart);
            System.out.println("Đã lưu giỏ hàng vào session.");

        } catch (IllegalArgumentException e) {
            System.err.println("Lỗi SetupCartServlet (IllegalArgumentException): " + e.getMessage());
        } catch (Exception e) {
            System.err.println("--- !!! LỖI KHÔNG LƯỜNG TRƯỚC TRONG SetupCartServlet !!! ---");
            e.printStackTrace();
        }
        System.out.println("--- [SetupCartServlet] Kết thúc, chuyển hướng đến checkout ---");
        resp.sendRedirect("checkout");
    }

    private Product toProductEntity(ProductDTO dto) {
        if (dto == null) {
            return null;
        }
        Product entity = new Product();
        entity.setId(dto.getId());
        entity.setName(dto.getName());
        // Đảm bảo setPrice của Product nhận BigDecimal
        entity.setPrice(dto.getPrice());
        return entity;
    }
}
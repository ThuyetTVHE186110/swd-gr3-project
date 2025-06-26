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
import java.io.IOException;

@WebServlet("/setup-cart")
public class SetupCartServlet extends HttpServlet {

    private final ProductService productService = new ProductServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        try {
            ProductDTO p1_dto = productService.getProductById(1L);

            // Chuyển DTO thành Entity để dùng trong CartItem
            Product p1_entity = toProductEntity(p1_dto);

            // Tạo CartItem
            CartItem item1 = new CartItem();
            item1.setProduct(p1_entity);
            item1.setQuantity(2);
            // SỬA LỖI: Bây giờ không còn lỗi vì setPrice và getPrice đều là BigDecimal
            item1.setPrice(p1_entity.getPrice());

            ShoppingCart cart = new ShoppingCart();
            cart.addItem(item1);

            req.getSession().setAttribute("cart", cart);

        } catch (IllegalArgumentException e) {
            System.err.println("SetupCartServlet Error: " + e.getMessage());
        }

        resp.sendRedirect("checkout");
    }

    // Hàm helper để chuyển đổi
    private Product toProductEntity(ProductDTO dto) {
        if (dto == null) return null;
        Product entity = new Product();
        entity.setId(dto.getId());
        entity.setName(dto.getName());
        entity.setPrice(dto.getPrice());
        return entity;
    }
}
package swd.project.swdgr3project.controller;

import swd.project.swdgr3project.model.dto.ProductDTO;
import swd.project.swdgr3project.service.ProductService;
import swd.project.swdgr3project.service.impl.ProductServiceImpl;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/search")
public class SearchServlet extends HttpServlet {
    
    private final ProductService productService = new ProductServiceImpl();
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String query = req.getParameter("q");
        String pageParam = req.getParameter("page");
        
        if (query == null || query.trim().isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/products");
            return;
        }
        
        int page = 1;
        try {
            if (pageParam != null) {
                page = Integer.parseInt(pageParam);
            }
        } catch (NumberFormatException e) {
            page = 1;
        }
        
        try {
            List<ProductDTO> products = productService.searchProducts(query.trim(), page, 12);
            
            req.setAttribute("products", products);
            req.setAttribute("searchQuery", query);
            req.setAttribute("currentPage", page);
            req.setAttribute("pageTitle", "Search Results for \"" + query + "\"");
            
            req.getRequestDispatcher("/index.jsp").forward(req, resp);
            
        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("error", "Failed to search products: " + e.getMessage());
            req.getRequestDispatcher("/index.jsp").forward(req, resp);
        }
    }
}

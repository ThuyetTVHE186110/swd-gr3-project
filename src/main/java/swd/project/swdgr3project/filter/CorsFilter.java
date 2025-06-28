package swd.project.swdgr3project.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter("/*") // Áp dụng filter này cho tất cả các request
public class CorsFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        // Cho phép tất cả các domain. Trong production, bạn nên giới hạn lại.
        response.setHeader("Access-Control-Allow-Origin", "*");

        // Các phương thức HTTP được cho phép
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");

        // Các header được cho phép
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");

        // Cho phép trình duyệt cache kết quả preflight request
        response.setHeader("Access-Control-Max-Age", "3600");

        // Nếu là một OPTIONS request (preflight), chỉ cần trả về OK
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
        } else {
            // Nếu không, tiếp tục chuỗi filter và xử lý request bình thường
            filterChain.doFilter(request, response);
        }
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void destroy() {}
}
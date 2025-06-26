package swd.project.swdgr3project.service;

import swd.project.swdgr3project.entity.ShoppingCart;
import swd.project.swdgr3project.model.dto.OrderDTO;
import swd.project.swdgr3project.service.impl.OrderServiceImpl;

public class CheckoutService {

    private final OrderServiceImpl orderService = new OrderServiceImpl();

    public OrderDTO placeGuestOrder(ShoppingCart cart, String guestName, String guestEmail, String guestPhone,
                                    String shippingAddress, String city, String district, String ward,
                                    String paymentMethod) {

        return orderService.createOrderFromGuestCart(cart, guestName, guestEmail, guestPhone,
                shippingAddress, city, district, ward,
                paymentMethod);
    }
}
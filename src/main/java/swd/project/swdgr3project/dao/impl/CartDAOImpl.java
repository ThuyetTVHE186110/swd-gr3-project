package swd.project.swdgr3project.dao.impl;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import swd.project.swdgr3project.dao.CartDAO;
import swd.project.swdgr3project.model.entity.Cart;
import swd.project.swdgr3project.model.entity.Product;
import swd.project.swdgr3project.model.entity.User;
import swd.project.swdgr3project.utils.HibernateUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Implementation of the CartDAO interface using Hibernate.
 */
public class CartDAOImpl implements CartDAO {

    @Override
    public Cart save(Cart cart) {
        Transaction transaction = null;
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            
            // Set creation and update timestamps
            LocalDateTime now = LocalDateTime.now();
            cart.setCreatedAt(now);
            cart.setUpdatedAt(now);
            
            // Calculate total
            updateCartTotal(cart);
            
            // Save the cart and get the generated ID
            session.persist(cart);
            
            transaction.commit();
            return cart;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            throw new RuntimeException("Error saving cart: " + e.getMessage(), e);
        }
    }

    @Override
    public Cart update(Cart cart) {
        Transaction transaction = null;
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            
            // Update the timestamp
            cart.setUpdatedAt(LocalDateTime.now());
            
            // Calculate total
            updateCartTotal(cart);
            
            // Update the cart
            session.merge(cart);
            
            transaction.commit();
            return cart;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            throw new RuntimeException("Error updating cart: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Cart> findById(Long id) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            Cart cart = session.get(Cart.class, id);
            return Optional.ofNullable(cart);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error finding cart by ID: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Cart> findByUserId(Long userId) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            Query<Cart> query = session.createQuery(
                "FROM Cart WHERE user.id = :userId", 
                Cart.class
            );
            query.setParameter("userId", userId);
            return query.uniqueResultOptional();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error finding cart by user ID: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Cart> findBySessionId(String sessionId) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            Query<Cart> query = session.createQuery(
                "FROM Cart WHERE sessionId = :sessionId", 
                Cart.class
            );
            query.setParameter("sessionId", sessionId);
            return query.uniqueResultOptional();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error finding cart by session ID: " + e.getMessage(), e);
        }
    }

    @Override
    public Cart addItem(Cart cart, Product product, int quantity) {
        Transaction transaction = null;
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            
            // Check if the product already exists in the cart
            boolean productExists = false;
            for (Cart.CartItem item : cart.getItems()) {
                if (item.getProduct().getId().equals(product.getId())) {
                    // Update quantity
                    item.setQuantity(item.getQuantity() + quantity);
                    // Calculate subtotal using BigDecimal for precision
                    BigDecimal price = product.getPrice();
                    BigDecimal subtotal = price.multiply(BigDecimal.valueOf(item.getQuantity()));
                    item.setSubtotal(subtotal);
                    productExists = true;
                    break;
                }
            }
            
            // If product doesn't exist in cart, add it
            if (!productExists) {
                Cart.CartItem newItem = new Cart.CartItem();
                newItem.setProduct(product);
                newItem.setQuantity(quantity);
                // Calculate subtotal using BigDecimal for precision
                BigDecimal price = product.getPrice();
                BigDecimal subtotal = price.multiply(BigDecimal.valueOf(quantity));
                newItem.setSubtotal(subtotal);
                cart.getItems().add(newItem);
            }
            
            // Update cart total and timestamp
            cart.setUpdatedAt(LocalDateTime.now());
            updateCartTotal(cart);
            
            // Update the cart
            session.merge(cart);
            
            transaction.commit();
            return cart;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            throw new RuntimeException("Error adding item to cart: " + e.getMessage(), e);
        }
    }

    @Override
    public Cart updateItemQuantity(Cart cart, Long productId, int quantity) {
        Transaction transaction = null;
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            
            // Find the item in the cart
            for (Cart.CartItem item : cart.getItems()) {
                if (item.getProduct().getId().equals(productId)) {
                    if (quantity <= 0) {
                        // If quantity is 0 or negative, remove the item
                        return removeItem(cart, productId);
                    } else {
                        // Update quantity and subtotal
                        item.setQuantity(quantity);
                        // Calculate subtotal using BigDecimal for precision
                        BigDecimal price = item.getProduct().getPrice();
                        BigDecimal subtotal = price.multiply(BigDecimal.valueOf(quantity));
                        item.setSubtotal(subtotal);
                        break;
                    }
                }
            }
            
            // Update cart total and timestamp
            cart.setUpdatedAt(LocalDateTime.now());
            updateCartTotal(cart);
            
            // Update the cart
            session.merge(cart);
            
            transaction.commit();
            return cart;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            throw new RuntimeException("Error updating item quantity in cart: " + e.getMessage(), e);
        }
    }

    @Override
    public Cart removeItem(Cart cart, Long productId) {
        Transaction transaction = null;
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            
            // Find and remove the item from the cart
            cart.getItems().removeIf(item -> item.getProduct().getId().equals(productId));
            
            // Update cart total and timestamp
            cart.setUpdatedAt(LocalDateTime.now());
            updateCartTotal(cart);
            
            // Update the cart
            session.merge(cart);
            
            transaction.commit();
            return cart;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            throw new RuntimeException("Error removing item from cart: " + e.getMessage(), e);
        }
    }

    @Override
    public Cart clearItems(Cart cart) {
        Transaction transaction = null;
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            
            // Clear all items
            cart.getItems().clear();
            
            // Update cart total and timestamp
            cart.setTotal(BigDecimal.ZERO);
            cart.setUpdatedAt(LocalDateTime.now());
            
            // Update the cart
            session.merge(cart);
            
            transaction.commit();
            return cart;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            throw new RuntimeException("Error clearing cart items: " + e.getMessage(), e);
        }
    }

    @Override
    public Cart mergeGuestCartWithUserCart(String sessionId, User user) {
        Transaction transaction = null;
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            
            // Find guest cart by session ID
            Optional<Cart> guestCartOpt = findBySessionId(sessionId);
            if (guestCartOpt.isEmpty()) {
                // No guest cart found, nothing to merge
                transaction.commit();
                return findByUserId(user.getId()).orElseGet(() -> {
                    // Create a new cart for the user if none exists
                    Cart newCart = new Cart();
                    newCart.setUser(user);
                    return save(newCart);
                });
            }
            
            Cart guestCart = guestCartOpt.get();
            
            // Find or create user cart
            Optional<Cart> userCartOpt = findByUserId(user.getId());
            Cart userCart;
            
            if (userCartOpt.isPresent()) {
                // User already has a cart, merge items
                userCart = userCartOpt.get();
                
                // Add guest cart items to user cart
                for (Cart.CartItem guestItem : guestCart.getItems()) {
                    boolean itemExists = false;
                    
                    // Check if product already exists in user cart
                    for (Cart.CartItem userItem : userCart.getItems()) {
                        if (userItem.getProduct().getId().equals(guestItem.getProduct().getId())) {
                            // Update quantity
                            userItem.setQuantity(userItem.getQuantity() + guestItem.getQuantity());
                            BigDecimal price = userItem.getProduct().getPrice();
                            BigDecimal subtotal = price.multiply(BigDecimal.valueOf(userItem.getQuantity()));
                            userItem.setSubtotal(subtotal);
                            itemExists = true;
                            break;
                        }
                    }
                    
                    // If product doesn't exist in user cart, add it
                    if (!itemExists) {
                        Cart.CartItem newItem = new Cart.CartItem();
                        newItem.setProduct(guestItem.getProduct());
                        newItem.setQuantity(guestItem.getQuantity());
                        newItem.setSubtotal(guestItem.getSubtotal());
                        userCart.getItems().add(newItem);
                    }
                }
            } else {
                // User doesn't have a cart, convert guest cart to user cart
                userCart = guestCart;
                userCart.setUser(user);
                userCart.setSessionId(null); // Clear session ID
            }
            
            // Update cart total and timestamp
            userCart.setUpdatedAt(LocalDateTime.now());
            updateCartTotal(userCart);
            
            // Update the user cart
            session.merge(userCart);
            
            // Delete the guest cart if it's different from the user cart
            if (guestCart != userCart) {
                session.remove(guestCart);
            }
            
            transaction.commit();
            return userCart;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            throw new RuntimeException("Error merging guest cart with user cart: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean delete(Long id) {
        Transaction transaction = null;
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            
            Cart cart = session.get(Cart.class, id);
            if (cart != null) {
                session.remove(cart);
                transaction.commit();
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            throw new RuntimeException("Error deleting cart: " + e.getMessage(), e);
        }
    }
    
    /**
     * Helper method to update the cart total based on item subtotals.
     *
     * @param cart The cart to update
     */
    private void updateCartTotal(Cart cart) {
        BigDecimal total = cart.getItems().stream()
                .map(Cart.CartItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        cart.setTotal(total);
    }
}
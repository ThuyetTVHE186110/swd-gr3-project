package swd.project.swdgr3project.dao.impl;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import swd.project.swdgr3project.dao.CartDAO;
import swd.project.swdgr3project.entity.Cart;
import swd.project.swdgr3project.entity.CartItem;
import swd.project.swdgr3project.entity.Product;
import swd.project.swdgr3project.entity.User;
import swd.project.swdgr3project.exception.DAOException;
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
            throw new DAOException("Error saving cart: " + e.getMessage(), e);
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
            throw new DAOException("Error updating cart: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Cart> findById(Long id) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            Cart cart = session.get(Cart.class, id);
            return Optional.ofNullable(cart);
        } catch (Exception e) {
            e.printStackTrace();
            throw new DAOException("Error finding cart by ID: " + e.getMessage(), e);
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
            throw new DAOException("Error finding cart by user ID: " + e.getMessage(), e);
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
            throw new DAOException("Error finding cart by session ID: " + e.getMessage(), e);
        }
    }

    @Override
    public Cart addItem(Cart cart, Product product, int quantity) {
        Transaction transaction = null;
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            
            // Check if the product already exists in the cart
            boolean productExists = false;
            for (CartItem item : cart.getItems()) {
                if (item.getProduct().getId().equals(product.getId())) {
                    // Update quantity
                    item.setQuantity(item.getQuantity() + quantity);
                    item.setUpdatedAt(LocalDateTime.now());
                    productExists = true;
                    break;
                }
            }
            
            // If product doesn't exist in cart, add it
            if (!productExists) {
                CartItem newItem = new CartItem();
                newItem.setCart(cart);
                newItem.setProduct(product);
                newItem.setQuantity(quantity);
                newItem.setPrice(product.getPrice());
                LocalDateTime now = LocalDateTime.now();
                newItem.setCreatedAt(now);
                newItem.setUpdatedAt(now);
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
            throw new DAOException("Error adding item to cart: " + e.getMessage(), e);
        }
    }

    @Override
    public Cart updateItemQuantity(Cart cart, Long productId, int quantity) {
        Transaction transaction = null;
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            
            // Find the item in the cart
            for (CartItem item : cart.getItems()) {
                if (item.getProduct().getId().equals(productId)) {
                    if (quantity <= 0) {
                        // If quantity is 0 or negative, remove the item
                        return removeItem(cart, productId);
                    } else {
                        // Update quantity and timestamp
                        item.setQuantity(quantity);
                        item.setUpdatedAt(LocalDateTime.now());
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
            throw new DAOException("Error updating item quantity in cart: " + e.getMessage(), e);
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
            throw new DAOException("Error removing item from cart: " + e.getMessage(), e);
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
            throw new DAOException("Error clearing cart items: " + e.getMessage(), e);
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
                transaction.commit();
                return findOrCreateUserCart(user);
            }
            
            Cart guestCart = guestCartOpt.get();
            Optional<Cart> userCartOpt = findByUserId(user.getId());
            Cart userCart = mergeOrConvertCart(guestCart, userCartOpt, user);
            
            // Update cart and cleanup
            userCart.setUpdatedAt(LocalDateTime.now());
            updateCartTotal(userCart);
            session.merge(userCart);
            
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
            throw new DAOException("Error merging guest cart with user cart: " + e.getMessage(), e);
        }
    }
    
    private Cart findOrCreateUserCart(User user) {
        return findByUserId(user.getId()).orElseGet(() -> {
            Cart newCart = new Cart();
            newCart.setUser(user);
            return save(newCart);
        });
    }
    
    private Cart mergeOrConvertCart(Cart guestCart, Optional<Cart> userCartOpt, User user) {
        if (userCartOpt.isPresent()) {
            Cart userCart = userCartOpt.get();
            mergeGuestItemsIntoUserCart(guestCart, userCart);
            return userCart;
        } else {
            // Convert guest cart to user cart
            guestCart.setUser(user);
            guestCart.setSessionId(null);
            return guestCart;
        }
    }
    
    private void mergeGuestItemsIntoUserCart(Cart guestCart, Cart userCart) {
        for (CartItem guestItem : guestCart.getItems()) {
            boolean itemExists = false;
            
            // Check if product already exists in user cart
            for (CartItem userItem : userCart.getItems()) {
                if (userItem.getProduct().getId().equals(guestItem.getProduct().getId())) {
                    userItem.setQuantity(userItem.getQuantity() + guestItem.getQuantity());
                    userItem.setUpdatedAt(LocalDateTime.now());
                    itemExists = true;
                    break;
                }
            }
            
            // If product doesn't exist in user cart, add it
            if (!itemExists) {
                CartItem newItem = new CartItem();
                newItem.setCart(userCart);
                newItem.setProduct(guestItem.getProduct());
                newItem.setQuantity(guestItem.getQuantity());
                newItem.setPrice(guestItem.getPrice());
                LocalDateTime now = LocalDateTime.now();
                newItem.setCreatedAt(now);
                newItem.setUpdatedAt(now);
                userCart.getItems().add(newItem);
            }
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
            throw new DAOException("Error deleting cart: " + e.getMessage(), e);
        }
    }
    
    /**
     * Helper method to update the cart total based on item subtotals.
     *
     * @param cart The cart to update
     */
    /**
     * Helper method to update the cart total based on item subtotals.
     *
     * @param cart The cart to update
     */
    private void updateCartTotal(Cart cart) {
        BigDecimal total = cart.calculateTotal();
        cart.setTotal(total);
    }
}
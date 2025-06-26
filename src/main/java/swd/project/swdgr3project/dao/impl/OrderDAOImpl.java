package swd.project.swdgr3project.dao.impl;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import swd.project.swdgr3project.dao.OrderDAO;
import swd.project.swdgr3project.model.entity.Order;
import swd.project.swdgr3project.utils.HibernateUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Implementation of the OrderDAO interface using Hibernate.
 */
public class OrderDAOImpl implements OrderDAO {

    @Override
    public Order save(Order order) {
        Transaction transaction = null;
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            
            // Set creation and update timestamps
            LocalDateTime now = LocalDateTime.now();
            order.setCreatedAt(now);
            order.setUpdatedAt(now);
            
            // Save the order and get the generated ID
            session.persist(order);
            
            transaction.commit();
            return order;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            throw new RuntimeException("Error saving order: " + e.getMessage(), e);
        }
    }

    @Override
    public Order update(Order order) {
        Transaction transaction = null;
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            
            // Update the timestamp
            order.setUpdatedAt(LocalDateTime.now());
            
            // Update the order
            session.merge(order);
            
            transaction.commit();
            return order;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            throw new RuntimeException("Error updating order: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Order> findById(Long id) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            Order order = session.get(Order.class, id);
            return Optional.ofNullable(order);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error finding order by ID: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Order> findAll() {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            return session.createQuery("FROM Order o ORDER BY o.createdAt DESC", Order.class).list();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error finding all orders: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Order> findByCustomerId(Long customerId) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            Query<Order> query = session.createQuery(
                "FROM Order o WHERE o.customer.id = :customerId ORDER BY o.createdAt DESC", 
                Order.class
            );
            query.setParameter("customerId", customerId);
            return query.list();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error finding orders by customer ID: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Order> findByStatus(Order.OrderStatus status) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            Query<Order> query = session.createQuery(
                "FROM Order o WHERE o.status = :status ORDER BY o.createdAt DESC", 
                Order.class
            );
            query.setParameter("status", status);
            return query.list();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error finding orders by status: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Order> findByPaymentMethod(Order.PaymentMethod paymentMethod) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            Query<Order> query = session.createQuery(
                "FROM Order o WHERE o.paymentMethod = :paymentMethod ORDER BY o.createdAt DESC", 
                Order.class
            );
            query.setParameter("paymentMethod", paymentMethod);
            return query.list();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error finding orders by payment method: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Order> findByPaymentStatus(Order.PaymentStatus paymentStatus) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            Query<Order> query = session.createQuery(
                "FROM Order o WHERE o.paymentStatus = :paymentStatus ORDER BY o.createdAt DESC", 
                Order.class
            );
            query.setParameter("paymentStatus", paymentStatus);
            return query.list();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error finding orders by payment status: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Order> findByPaymentTransactionId(String transactionId) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            Query<Order> query = session.createQuery(
                "FROM Order o WHERE o.paymentTransactionId = :transactionId", 
                Order.class
            );
            query.setParameter("transactionId", transactionId);
            return query.uniqueResultOptional();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error finding order by transaction ID: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean delete(Long id) {
        Transaction transaction = null;
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            
            Order order = session.get(Order.class, id);
            if (order != null) {
                session.remove(order);
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
            throw new RuntimeException("Error deleting order: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean updateStatus(Long id, Order.OrderStatus status) {
        Transaction transaction = null;
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            
            Order order = session.get(Order.class, id);
            if (order != null) {
                order.setStatus(status);
                order.setUpdatedAt(LocalDateTime.now());
                session.merge(order);
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
            throw new RuntimeException("Error updating order status: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean updatePaymentStatus(Long id, Order.PaymentStatus paymentStatus) {
        Transaction transaction = null;
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            
            Order order = session.get(Order.class, id);
            if (order != null) {
                order.setPaymentStatus(paymentStatus);
                order.setUpdatedAt(LocalDateTime.now());
                session.merge(order);
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
            throw new RuntimeException("Error updating order payment status: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean updateTrackingNumber(Long id, String trackingNumber) {
        Transaction transaction = null;
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            
            Order order = session.get(Order.class, id);
            if (order != null) {
                order.setTrackingNumber(trackingNumber);
                order.setUpdatedAt(LocalDateTime.now());
                session.merge(order);
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
            throw new RuntimeException("Error updating order tracking number: " + e.getMessage(), e);
        }
    }
}
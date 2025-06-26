package swd.project.swdgr3project.dao.impl;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import swd.project.swdgr3project.dao.OrderDAO;
import swd.project.swdgr3project.entity.Order;
import swd.project.swdgr3project.utils.HibernateUtils;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Implementation of the OrderDAO interface using Hibernate.
 * This is the final, corrected, and complete version.
 */
public class OrderDAOImpl implements OrderDAO {

    @Override
    public Order save(Order order) {
        Transaction transaction = null;
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            LocalDateTime now = LocalDateTime.now();
            if (order.getCreatedAt() == null) {
                order.setCreatedAt(now);
            }
            order.setUpdatedAt(now);
            session.persist(order);
            transaction.commit();
            return order;
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
            throw new RuntimeException("Error saving order: " + e.getMessage(), e);
        }
    }

    @Override
    public Order update(Order order) {
        Transaction transaction = null;
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            order.setUpdatedAt(LocalDateTime.now());
            session.merge(order);
            transaction.commit();
            return order;
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
            throw new RuntimeException("Error updating order: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Order> findById(Long id) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            return Optional.ofNullable(session.get(Order.class, id));
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error finding order by ID: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Order> findAll() {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            return session.createQuery("FROM Order", Order.class).list();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error finding all orders: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Order> findByCustomerId(Long customerId) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            Query<Order> query = session.createQuery("FROM Order o WHERE o.user.id = :customerId", Order.class);
            query.setParameter("customerId", customerId);
            return query.list();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error finding orders by customer ID: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Order> findByStatus(String status) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            Query<Order> query = session.createQuery("FROM Order o WHERE o.status = :status", Order.class);
            query.setParameter("status", status);
            return query.list();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error finding orders by status: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Order> findByPaymentMethod(String paymentMethod) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            Query<Order> query = session.createQuery("FROM Order o WHERE o.paymentMethod = :paymentMethod", Order.class);
            query.setParameter("paymentMethod", paymentMethod);
            return query.list();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error finding orders by payment method: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Order> findByPaymentStatus(String paymentStatus) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            Query<Order> query = session.createQuery("FROM Order o WHERE o.paymentStatus = :paymentStatus", Order.class);
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
            Query<Order> query = session.createQuery("FROM Order o WHERE o.paymentTransactionId = :transactionId", Order.class);
            query.setParameter("transactionId", transactionId);
            return query.uniqueResultOptional();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error finding order by transaction ID: " + e.getMessage(), e);
        }
    }

    private boolean executeUpdate(Long id, java.util.function.Consumer<Order> updateAction) {
        Transaction transaction = null;
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Order order = session.get(Order.class, id);
            if (order != null) {
                updateAction.accept(order);
                order.setUpdatedAt(LocalDateTime.now());
                session.merge(order);
                transaction.commit();
                return true;
            }
            return false;
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
            throw new RuntimeException("Error performing update on order ID: " + id, e);
        }
    }

    @Override
    public boolean updateStatus(Long id, String status) {
        return executeUpdate(id, order -> order.setStatus(status));
    }

    @Override
    public boolean updatePaymentStatus(Long id, String paymentStatus) {
        return executeUpdate(id, order -> order.setPaymentStatus(paymentStatus));
    }

    @Override
    public boolean updateTrackingNumber(Long id, String trackingNumber) {
        return executeUpdate(id, order -> order.setTrackingNumber(trackingNumber));
    }
}
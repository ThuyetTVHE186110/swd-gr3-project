package swd.project.swdgr3project.dao.impl;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import swd.project.swdgr3project.dao.ProductDAO;
import swd.project.swdgr3project.model.entity.Product;
import swd.project.swdgr3project.utils.HibernateUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Implementation of the ProductDAO interface using Hibernate.
 */
public class ProductDAOImpl implements ProductDAO {

    @Override
    public Product save(Product product) {
        Transaction transaction = null;
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            
            // Set creation and update timestamps
            LocalDateTime now = LocalDateTime.now();
            product.setCreatedAt(now);
            product.setUpdatedAt(now);
            
            // Save the product and get the generated ID
            session.persist(product);
            
            transaction.commit();
            return product;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            throw new RuntimeException("Error saving product: " + e.getMessage(), e);
        }
    }

    @Override
    public Product update(Product product) {
        Transaction transaction = null;
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            
            // Update the timestamp
            product.setUpdatedAt(LocalDateTime.now());
            
            // Update the product
            session.merge(product);
            
            transaction.commit();
            return product;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            throw new RuntimeException("Error updating product: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Product> findById(Long id) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            Product product = session.get(Product.class, id);
            return Optional.ofNullable(product);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error finding product by ID: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Product> findAll() {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            return session.createQuery("FROM Product", Product.class).list();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error finding all products: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Product> findAllActive() {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            return session.createQuery("FROM Product WHERE active = true", Product.class).list();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error finding active products: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Product> findByCategory(Long categoryId) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            Query<Product> query = session.createQuery(
                "FROM Product WHERE category.id = :categoryId", 
                Product.class
            );
            query.setParameter("categoryId", categoryId);
            return query.list();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error finding products by category: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Product> findActiveByCategoryId(Long categoryId) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            Query<Product> query = session.createQuery(
                "FROM Product WHERE category.id = :categoryId AND active = true", 
                Product.class
            );
            query.setParameter("categoryId", categoryId);
            return query.list();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error finding active products by category: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Product> search(String keyword) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            String searchPattern = "%" + keyword + "%";
            Query<Product> query = session.createQuery(
                "FROM Product WHERE name LIKE :pattern OR description LIKE :pattern", 
                Product.class
            );
            query.setParameter("pattern", searchPattern);
            return query.list();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error searching products: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean delete(Long id) {
        Transaction transaction = null;
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            
            Product product = session.get(Product.class, id);
            if (product != null) {
                session.remove(product);
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
            throw new RuntimeException("Error deleting product: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean deactivate(Long id) {
        Transaction transaction = null;
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            
            Product product = session.get(Product.class, id);
            if (product != null) {
                product.setActive(false);
                product.setUpdatedAt(LocalDateTime.now());
                session.merge(product);
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
            throw new RuntimeException("Error deactivating product: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean activate(Long id) {
        Transaction transaction = null;
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            
            Product product = session.get(Product.class, id);
            if (product != null) {
                product.setActive(true);
                product.setUpdatedAt(LocalDateTime.now());
                session.merge(product);
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
            throw new RuntimeException("Error activating product: " + e.getMessage(), e);
        }
    }

    @Override
    public Product.Category saveCategory(Product.Category category) {
        Transaction transaction = null;
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            
            // Save the category and get the generated ID
            session.persist(category);
            
            transaction.commit();
            return category;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            throw new RuntimeException("Error saving category: " + e.getMessage(), e);
        }
    }

    @Override
    public Product.Category updateCategory(Product.Category category) {
        Transaction transaction = null;
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            
            // Update the category
            session.merge(category);
            
            transaction.commit();
            return category;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            throw new RuntimeException("Error updating category: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Product.Category> findCategoryById(Long id) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            Product.Category category = session.get(Product.Category.class, id);
            return Optional.ofNullable(category);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error finding category by ID: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Product.Category> findAllCategories() {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            return session.createQuery("FROM Product$Category", Product.Category.class).list();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error finding all categories: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean deleteCategory(Long id) {
        Transaction transaction = null;
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            
            Product.Category category = session.get(Product.Category.class, id);
            if (category != null) {
                // Check if there are any products in this category
                Query<Long> query = session.createQuery(
                    "SELECT COUNT(p) FROM Product p WHERE p.category.id = :categoryId", 
                    Long.class
                );
                query.setParameter("categoryId", id);
                Long count = query.uniqueResult();
                
                if (count > 0) {
                    throw new RuntimeException("Cannot delete category with associated products");
                }
                
                session.remove(category);
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
            throw new RuntimeException("Error deleting category: " + e.getMessage(), e);
        }
    }
}
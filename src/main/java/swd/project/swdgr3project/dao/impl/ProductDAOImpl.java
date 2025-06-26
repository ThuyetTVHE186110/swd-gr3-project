package swd.project.swdgr3project.dao.impl;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import swd.project.swdgr3project.dao.ProductDAO;
import swd.project.swdgr3project.entity.Product;
import swd.project.swdgr3project.entity.ProductCategory;
import swd.project.swdgr3project.utils.HibernateUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Implementation of the ProductDAO interface using Hibernate.
 * This is the final, corrected version.
 */
public class ProductDAOImpl implements ProductDAO {

    @Override
    public Product save(Product product) {
        Transaction transaction = null;
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            LocalDateTime now = LocalDateTime.now();
            if (product.getCreatedAt() == null) {
                product.setCreatedAt(now);
            }
            product.setUpdatedAt(now);

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

            product.setUpdatedAt(LocalDateTime.now());

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
            String searchPattern = "%" + keyword.toLowerCase() + "%";
            Query<Product> query = session.createQuery(
                    "FROM Product WHERE lower(name) LIKE :pattern OR lower(description) LIKE :pattern",
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
    public ProductCategory saveCategory(ProductCategory category) {
        Transaction transaction = null;
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

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
    public ProductCategory updateCategory(ProductCategory category) {
        Transaction transaction = null;
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

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
    public Optional<ProductCategory> findCategoryById(Long id) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            ProductCategory category = session.get(ProductCategory.class, id);
            return Optional.ofNullable(category);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error finding category by ID: " + e.getMessage(), e);
        }
    }

    @Override
    public List<ProductCategory> findAllCategories() {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            // Corrected HQL query
            return session.createQuery("FROM ProductCategory", ProductCategory.class).list();
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

            ProductCategory category = session.get(ProductCategory.class, id);
            if (category != null) {
                // Check if there are any products in this category
                Query<Long> query = session.createQuery(
                        "SELECT COUNT(p) FROM Product p WHERE p.category.id = :categoryId",
                        Long.class
                );
                query.setParameter("categoryId", id);
                Long count = query.uniqueResult();

                if (count > 0) {
                    throw new IllegalStateException("Cannot delete category with associated products. Found " + count + " products.");
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
            // Re-throw specific exception for better error handling in service layer
            if (e instanceof IllegalStateException) {
                throw e;
            }
            e.printStackTrace();
            throw new RuntimeException("Error deleting category: " + e.getMessage(), e);
        }
    }
}
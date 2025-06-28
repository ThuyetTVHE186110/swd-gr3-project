package swd.project.swdgr3project.service.impl;

import org.hibernate.Hibernate;
import swd.project.swdgr3project.config.HibernateConfig; // Import HibernateConfig mới
import swd.project.swdgr3project.entity.Province;
import swd.project.swdgr3project.entity.District;
import swd.project.swdgr3project.entity.Ward;
import swd.project.swdgr3project.service.LocationService;

import org.hibernate.Session;
import org.hibernate.query.Query; // Dùng org.hibernate.query.Query

import java.util.Collections;
import java.util.List;

public class LocationServiceImpl implements LocationService {

    @Override
    public List<Province> getAllProvinces() {
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            // Sử dụng HQL để lấy dữ liệu, ORDER BY name để sắp xếp
            Query<Province> query = session.createQuery("FROM Province ORDER BY name", Province.class);
            return query.list();
        } catch (Exception e) {
            System.err.println("Error fetching all provinces: " + e.getMessage());
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    @Override
    public List<District> getAllDistricts() {
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            return session.createQuery("FROM District ORDER BY name", District.class).list();
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    @Override
    public List<Ward> getAllWards() {
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            return session.createQuery("FROM Ward ORDER BY name", Ward.class).list();
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    @Override
    public Province getProvinceWithFullChildren(Long provinceId) {
        if (provinceId == null) {
            return null;
        }
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            // Câu query này sẽ tải Province, cùng với tất cả District và Ward của nó
            String hql = "FROM Province p " +
                    "LEFT JOIN FETCH p.districts d " +
                    "LEFT JOIN FETCH d.wards w " +
                    "WHERE p.id = :provinceId";

            Query<Province> query = session.createQuery(hql, Province.class);
            query.setParameter("provinceId", provinceId);

            // uniqueResult() sẽ trả về một đối tượng Province duy nhất đã được "hydrate" đầy đủ
            Province province = query.uniqueResult();

            // Mặc dù đã FETCH JOIN, đôi khi vẫn cần initialize để chắc chắn
            if (province != null) {
                Hibernate.initialize(province.getDistricts());
                for (District district : province.getDistricts()) {
                    Hibernate.initialize(district.getWards());
                }
            }
            return province;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
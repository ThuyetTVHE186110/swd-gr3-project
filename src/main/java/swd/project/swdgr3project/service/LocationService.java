package swd.project.swdgr3project.service; // Đặt vào package service của bạn

import swd.project.swdgr3project.entity.Province;
import swd.project.swdgr3project.entity.District;
import swd.project.swdgr3project.entity.Ward;

import java.util.List;

public interface LocationService {
    List<Province> getAllProvinces();
    List<District> getAllDistricts(); // Thêm mới
    List<Ward> getAllWards();
    Province getProvinceWithFullChildren(Long provinceId);

}
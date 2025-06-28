package swd.project.swdgr3project.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProvinceDTO {
    // Đảm bảo tên trường là 'id' và 'name'
    private Long id;
    private String name;
}
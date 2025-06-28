package swd.project.swdgr3project.entity; // Đặt vào package entity của bạn

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Entity
@Table(name = "wards")
@Data
@ToString(exclude = {"district"})
@EqualsAndHashCode(exclude = {"district"})
public class Ward {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "district_id", nullable = false)
    private District district;
}
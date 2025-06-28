package swd.project.swdgr3project.entity; // Đặt vào package entity của bạn

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import com.google.gson.annotations.Expose;

import java.util.List;

@Entity
@Table(name = "provinces")
@Data
@ToString(exclude = {"districts"})
@EqualsAndHashCode(exclude = {"districts"})
public class Province {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Expose
    private Long id;

    @Expose
    private String name;

    @OneToMany(mappedBy = "province", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<District> districts;
}
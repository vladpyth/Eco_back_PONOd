package com.example.eco_service.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.envers.Audited;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Audited
@Table(name = "Technology")
public class Technology {


    @ManyToOne
    @JoinColumn(name = "id_class_danger")
    private ClassDanger id_class_danger;

    @ManyToOne
    @JoinColumn(name = "id_magazin_trash")
    private MagazinTrash id_magazin_trash;

    @ManyToOne
    @JoinColumn(name = "id_phys_trash")
    private PhysStateTrash id_phys_trash;

    /** Предприятие, к которому относится технология. */
    @ManyToOne
    @JoinColumn(name = "id_magasin_factory", nullable = true)
    @JsonIgnoreProperties({"id_technology"})
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private MagasinFactory id_magasin_factory;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_technology;

}

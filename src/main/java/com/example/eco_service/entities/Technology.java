package com.example.eco_service.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
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

    /** Предприятие через MagasinFactory.id_technology (обратная связь). */
    @Transient
    private MagasinFactory id_magasin_factory;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_technology;

}
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
@Table(name = "DropAir")
public class DropAir {

    @ManyToOne
    @JoinColumn(name = "id_class_danger")
    private ClassDanger id_class_danger;

    @ManyToOne
    @JoinColumn(name = "id_name_grope_air")
    private NameDropAirTrash id_name_grope_air;

    @ManyToOne
    @JoinColumn(name = "id_magasin_factory")
    private MagasinFactory id_magasin_factory;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_drop_air;

    @Column(nullable = false,  length = 50)
    private float value_drop_trash;

}
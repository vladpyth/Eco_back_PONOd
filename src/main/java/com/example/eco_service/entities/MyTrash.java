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
@Table(name = "MyTrash")
public class MyTrash {

    @ManyToOne
    @JoinColumn(name = "id_class_danger")
    private ClassDanger id_class_danger;

    @ManyToOne
    @JoinColumn(name = "id_magazin_trash")
    private MagazinTrash id_magazin_trash;

    /** Предприятие через MyTrashCount (не колонка таблицы MyTrash). */
    @Transient
    private MagasinFactory id_magasin_factory;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_my_trash;

    @Column(nullable = false,  length = 50)
    private float value_trash;

}
package com.example.eco_service.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
@Table(name = "MyTrashCount")
public class MyTrashCount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_my_trash_count;

    @ManyToOne
    @JoinColumn(name = "id_my_trash")
    private MyTrash id_my_trash;

    @ManyToOne
    @JoinColumn(name = "id_object_place_trash")
    @JsonIgnore
    private MagasinFactory id_object_place_trash;

}
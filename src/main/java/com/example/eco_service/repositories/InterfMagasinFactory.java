package com.example.eco_service.repositories;

import com.example.eco_service.entities.MagasinFactory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.history.RevisionRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface InterfMagasinFactory extends JpaRepository<MagasinFactory, Long>, RevisionRepository<MagasinFactory, Long, Integer> {

    Optional<MagasinFactory> findFirstById_technology_Id(Long idTechnology);

    List<MagasinFactory> findAllById_technology_Id(Long idTechnology);
}
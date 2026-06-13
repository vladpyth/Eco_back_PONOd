package com.example.eco_service.repositories;

import com.example.eco_service.entities.DropAir;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.history.RevisionRepository;
import org.springframework.stereotype.Repository;
import com.example.eco_service.entities.MagasinFactory;

@Repository
public interface InterfMagasinFactory extends JpaRepository<MagasinFactory, Long>, RevisionRepository<MagasinFactory, Long, Integer> {

}
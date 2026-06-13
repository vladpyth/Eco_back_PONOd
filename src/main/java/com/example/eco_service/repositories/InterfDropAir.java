package com.example.eco_service.repositories;

import com.example.eco_service.entities.District;
import com.example.eco_service.entities.DropAir;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.history.RevisionRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InterfDropAir extends JpaRepository<DropAir, Long>, RevisionRepository<DropAir, Long, Integer> {

}
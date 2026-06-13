package com.example.eco_service.repositories;

import com.example.eco_service.entities.ShortDiscribeTechnology;
import com.example.eco_service.entities.Technology;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.history.RevisionRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface InterfTechnology extends JpaRepository<Technology, Long>, RevisionRepository<Technology, Long, Integer> {

}
package com.example.demo.repositories;

import com.example.demo.model.Predmet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PredmetRepository extends JpaRepository<Predmet, Long> {
}

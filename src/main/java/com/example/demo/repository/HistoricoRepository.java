package com.example.demo.repository;

import com.example.demo.model.HistoricoViagem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HistoricoRepository extends JpaRepository<HistoricoViagem, Long> {
}
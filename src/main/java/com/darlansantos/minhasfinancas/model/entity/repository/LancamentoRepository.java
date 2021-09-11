package com.darlansantos.minhasfinancas.model.entity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.darlansantos.minhasfinancas.model.entity.Lancamento;

@Repository
public interface LancamentoRepository extends JpaRepository<Lancamento, Long> {

}

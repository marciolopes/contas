package com.cbyk.domain.conta.repository;

import com.cbyk.domain.conta.model.Conta;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface ContaRepository extends JpaRepository<Conta, Long>{

    Page findAll(Pageable pageable);

    Page<Conta> findByDataPagamentoBetween(LocalDate dataPagamentoInicio, LocalDate dataPagamentoFim, Pageable pageable);

}

package com.lojas.moria.repository;

import com.lojas.moria.model.MovimentacaoFinanceira;
import com.lojas.moria.model.OrigemMovimentacaoFinanceira;
import com.lojas.moria.model.StatusMovimentacaoFinanceira;
import com.lojas.moria.model.TipoMovimentacaoFinanceira;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface MovimentacaoFinanceiraRepository
        extends JpaRepository<MovimentacaoFinanceira, Long> {

    boolean existsByVendaId(Long vendaId);

    List<MovimentacaoFinanceira> findAllByOrderByDataHoraDesc();

    List<MovimentacaoFinanceira> findByOrigemOrderByDataHoraDesc(
            OrigemMovimentacaoFinanceira origem);

    @Query("""
    SELECT COALESCE(SUM(m.valor), 0)
    FROM MovimentacaoFinanceira m
    WHERE m.status = :status
      AND m.tipo = :tipo
      AND COALESCE(m.dataPagamento, m.dataHora) >= :inicio
      AND COALESCE(m.dataPagamento, m.dataHora) < :fim
""")
    BigDecimal somarPorTipoStatusEPeriodo(
            @Param("tipo") TipoMovimentacaoFinanceira tipo,
            @Param("status") StatusMovimentacaoFinanceira status,
            @Param("inicio") LocalDateTime inicio,
            @Param("fim") LocalDateTime fim
    );
}
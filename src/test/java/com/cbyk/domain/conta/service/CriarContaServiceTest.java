package com.cbyk.domain.conta.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.cbyk.domain.conta.dto.ContaDto;
import com.cbyk.domain.conta.enums.SituacaoEnum;
import com.cbyk.domain.conta.model.Conta;
import com.cbyk.domain.conta.repository.ContaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDate;

class CriarContaServiceTest {

    @Mock
    private ContaRepository contaRepository;

    @InjectMocks
    private CriarContaService criarContaService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void handle_QuandoContaValida_DeveSalvarContaCorretamente() {

        Long contaId = 1L;
        LocalDate now = LocalDate.now();

        ContaDto contaDTO = ContaDto.builder()
                .dataVencimento(now)
                .dataPagamento(now)
                .valor(new BigDecimal("100.00"))
                .descricao("Conta de teste")
                .situacao(SituacaoEnum.PAGA)
                .build();

        Conta conta = Conta.builder()
                .id(contaId)
                .dataVencimento(now)
                .dataPagamento(now)
                .valor(new BigDecimal("100.00"))
                .descricao("Conta de teste")
                .situacao(SituacaoEnum.PAGA.getValor())
                .build();

        when(contaRepository.save(any(Conta.class))).thenReturn(conta);

        ContaDto result = criarContaService.handle(contaDTO);

        assertNotNull(result);
        assertEquals(contaDTO.getDataVencimento(), result.getDataVencimento());
        assertEquals(contaDTO.getDataPagamento(), result.getDataPagamento());
        assertEquals(contaDTO.getValor(), result.getValor());
        assertEquals(contaDTO.getDescricao(), result.getDescricao());
        assertEquals(contaDTO.getSituacao(), result.getSituacao());
    }


}


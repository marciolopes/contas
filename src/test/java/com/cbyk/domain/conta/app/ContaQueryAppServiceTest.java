package com.cbyk.domain.conta.app;

import com.cbyk.domain.conta.dto.ContaDto;
import com.cbyk.domain.conta.enums.SituacaoEnum;
import com.cbyk.domain.conta.exception.ContaNotFoundException;
import com.cbyk.domain.conta.exception.DataPagamentoException;
import com.cbyk.domain.conta.mapper.ContaMapper;
import com.cbyk.domain.conta.model.Conta;
import com.cbyk.domain.conta.repository.ContaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageImpl;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ContaQueryAppServiceTest {

    @InjectMocks
    private ContaQueryAppService contaQueryAppService;

    @Mock
    private ContaRepository contaRepository;





    @Test
    void testObterContaPeloId_ReturnsContaDto_WhenContaExists() {
        Long contaId = 1L;
        Conta conta = Conta.builder()
                .id(contaId)
                .situacao("1")
                .build();

        ContaDto contaDto = ContaDto.builder().id(contaId).situacao(SituacaoEnum.PENDENTE).build();

        when(contaRepository.findById(contaId)).thenReturn(Optional.of(conta));

        ContaDto result = contaQueryAppService.obterContaPeloId(contaId);

        assertEquals(contaDto, result);
        verify(contaRepository).findById(contaId);
    }

    @Test
    void testObterContaPeloId_ThrowsContaNotFoundException_WhenContaDoesNotExist() {
        Long contaId = 1L;

        when(contaRepository.findById(contaId)).thenReturn(Optional.empty());

        assertThrows(ContaNotFoundException.class, () -> {
            contaQueryAppService.obterContaPeloId(contaId);
        });
    }

    @Test
    void testFiltrarContasPorSituacaoEDataVencimento_ReturnsFilteredPage() {
        SituacaoEnum situacao = SituacaoEnum.PENDENTE;
        LocalDate now = LocalDate.now();
        Pageable pageable = Pageable.ofSize(10);

        Conta conta = Conta.builder()
                .situacao(situacao.getValor())
                .dataVencimento(now)
                .dataPagamento(now)
                .build();

        ContaDto contaDto = ContaDto.builder().situacao(situacao).dataPagamento(now).dataVencimento(now).build();
        Page<Conta> contasPage = new PageImpl<>(Collections.singletonList(conta));

        when(contaRepository.findAll(any(), eq(pageable))).thenReturn(contasPage);

        Page<ContaDto> result = contaQueryAppService.filtrarContasPorSituacaoEDataVencimento(situacao, now, pageable);

        assertEquals(1, result.getTotalElements());
        assertEquals(contaDto, result.getContent().get(0));
    }

    @Test
    void testFindByDataPagamentoBetween_ThrowsException_WhenDataFimIsBeforeDataInicio() {
        LocalDate dataPagamentoInicio = LocalDate.of(2024, 9, 10);
        LocalDate dataPagamentoFim = LocalDate.of(2024, 9, 5);
        Pageable pageable = Pageable.ofSize(10);

        assertThrows(DataPagamentoException.class, () -> {
            contaQueryAppService.findByDataPagamentoBetween(dataPagamentoInicio, dataPagamentoFim, pageable);
        });
    }

    @Test
    void testFindByDataPagamentoBetween_ReturnsFilteredPage() {
        LocalDate dataPagamentoInicio = LocalDate.of(2024, 9, 1);
        LocalDate dataPagamentoFim = LocalDate.of(2024, 9, 10);
        Pageable pageable = Pageable.ofSize(10);

        LocalDate dataPagamento = LocalDate.of(2024, 9, 5);
        Conta conta = Conta.builder()
                .dataPagamento(dataPagamento)
                .situacao("1")
                .build();

        ContaDto contaDto = ContaDto.builder().dataPagamento(dataPagamento).situacao(SituacaoEnum.PENDENTE).build();
        Page<Conta> contasPage = new PageImpl<>(Collections.singletonList(conta));

        when(contaRepository.findByDataPagamentoBetween(dataPagamentoInicio, dataPagamentoFim, pageable)).thenReturn(contasPage);

        Page<ContaDto> result = contaQueryAppService.findByDataPagamentoBetween(dataPagamentoInicio, dataPagamentoFim, pageable);

        assertEquals(1, result.getTotalElements());
        assertEquals(contaDto, result.getContent().get(0));
    }
}


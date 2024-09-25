package com.cbyk.domain.conta.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.cbyk.domain.conta.dto.ContaDto;
import com.cbyk.domain.conta.enums.SituacaoEnum;
import com.cbyk.domain.conta.exception.ContaNotFoundException;
import com.cbyk.domain.conta.model.Conta;
import com.cbyk.domain.conta.repository.ContaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class AlterarSituacaoDaContaServiceTest {

    @Mock
    private ContaRepository contaRepository;

    @InjectMocks
    private AlterarSituacaoDaContaService alterarSituacaoDaContaService;

    private Conta conta;

    @BeforeEach
    void setUp() {
        conta = new Conta();
        conta.setId(1L);
        conta.setSituacao("1"); // Situação inicial
    }

    @Test
    void handle_QuandoContaExistente_DeveAlterarSituacao() {
        when(contaRepository.findById(1L)).thenReturn(Optional.of(conta));
        when(contaRepository.save(any(Conta.class))).thenReturn(conta);

        ContaDto resultado = alterarSituacaoDaContaService.handle(1L, SituacaoEnum.PAGA);

        assertEquals("PAGA", resultado.getSituacao().name());
        verify(contaRepository).save(conta);
        assertEquals("2", conta.getSituacao()); // Verifica se a situação foi alterada
    }

    @Test
    void handle_QuandoContaNaoExistente_DeveLancarContaNotFoundException() {
        when(contaRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ContaNotFoundException.class, () -> {
            alterarSituacaoDaContaService.handle(1L, SituacaoEnum.PAGA);
        });
    }
}

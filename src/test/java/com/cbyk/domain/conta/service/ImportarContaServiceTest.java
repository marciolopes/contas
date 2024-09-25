package com.cbyk.domain.conta.service;

import com.cbyk.domain.conta.exception.FileException;
import com.cbyk.domain.conta.model.Conta;
import com.cbyk.domain.conta.repository.ContaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ImportarContaServiceTest {

    @InjectMocks
    private ImportarContaService importarContaService;

    @Mock
    private ContaRepository contaRepository;

    @Mock
    private MultipartFile file;

    @Test
    void testHandleThrowsExceptionForInvalidFileType() {
        when(file.getContentType()).thenReturn("application/json"); // Mocking a non-CSV content type

        Exception exception = assertThrows(FileException.class, () -> {
            importarContaService.handle(file);
        });

        assertEquals("O arquivo deve ser um CSV.", exception.getMessage());
    }

    @Test
    void testHandleSavesContaList() throws IOException {
        // Prepare mock data
        String csvContent = "data_vencimento,data_pagamento,valor,descricao,situacao\n" +
                "2024-09-01,2024-09-10,100.00,Descrição 1,PENDENTE\n" +
                "2024-09-02,2024-09-12,200.00,Descrição 2,PAGA\n";

        ByteArrayInputStream inputStream = new ByteArrayInputStream(csvContent.getBytes());
        when(file.getInputStream()).thenReturn(inputStream);
        when(file.getContentType()).thenReturn("text/csv");

        importarContaService.handle(file);

        ArgumentCaptor<List<Conta>> captor = ArgumentCaptor.forClass(List.class);
        verify(contaRepository).saveAll(captor.capture());

        // Verify the captured argument
        List<Conta> savedContas = captor.getValue();
        assertEquals(2, savedContas.size());
        assertEquals(LocalDate.of(2024, 9, 1), savedContas.get(0).getDataVencimento());
        assertEquals(new BigDecimal("100.00"), savedContas.get(0).getValor());
    }

    @Test
    void testGetContaDTOListThrowsExceptionOnError() throws IOException {

        String invalidCsvContent = "data_vencimento,data_pagamento,valor\n" +
                "invalid_data,invalid_data,invalid_value\n";

        ByteArrayInputStream inputStream = new ByteArrayInputStream(invalidCsvContent.getBytes());
        when(file.getInputStream()).thenReturn(inputStream);
        when(file.getContentType()).thenReturn("text/csv");

        Exception exception = assertThrows(RuntimeException.class, () -> {
            importarContaService.handle(file);
        });

        assertTrue(exception.getMessage().contains("Falha ao carregar arquivo csv"));
    }
}


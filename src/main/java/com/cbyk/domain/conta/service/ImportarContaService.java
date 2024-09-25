package com.cbyk.domain.conta.service;

import com.cbyk.domain.conta.dto.ContaDto;
import com.cbyk.domain.conta.enums.SituacaoEnum;
import com.cbyk.domain.conta.exception.FileException;
import com.cbyk.domain.conta.mapper.ContaMapper;
import com.cbyk.domain.conta.model.Conta;
import com.cbyk.domain.conta.repository.ContaRepository;
import com.cbyk.domain.conta.usecase.ImportarContaUseCase;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.commons.csv.CSVFormat;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Transactional
public class ImportarContaService implements ImportarContaUseCase {

    private ContaRepository contaRepository;

    @Override
    public void handle(MultipartFile file) throws IOException {
        if (!file.getContentType().equals("text/csv")) {
            throw new FileException("O arquivo deve ser um CSV.");
        }
        List<Conta> contaList = getContaDTOList(file.getInputStream())
                .stream()
                .map(ContaMapper::convertToEntity)
                .toList();

        contaRepository.saveAll(contaList);
    }

    private List<ContaDto> getContaDTOList(InputStream is) {
        try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            return CSVFormat.DEFAULT
                    .withHeader("data_vencimento", "data_pagamento", "valor", "descricao", "situacao")
                    .withIgnoreHeaderCase()
                    .withTrim()
                    .withSkipHeaderRecord()
                    .parse(fileReader)
                    .getRecords()
                    .stream()
                    .map(csvRecord -> ContaDto.builder()
                            .dataVencimento(LocalDate.parse(csvRecord.get("data_vencimento")))
                            .dataPagamento(LocalDate.parse(csvRecord.get("data_pagamento")))
                            .valor(new BigDecimal(csvRecord.get("valor")))
                            .descricao(csvRecord.get("descricao"))
                            .situacao(SituacaoEnum.get(csvRecord.get("situacao")))
                            .build())
                    .toList();
        } catch (Exception e) {
            throw new RuntimeException("Falha ao carregar arquivo csv: " + e.getMessage());
        }
    }

}

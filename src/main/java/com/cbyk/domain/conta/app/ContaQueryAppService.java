package com.cbyk.domain.conta.app;


import com.cbyk.domain.conta.mapper.ContaMapper;
import com.cbyk.domain.conta.dto.ContaDto;
import com.cbyk.domain.conta.enums.SituacaoEnum;
import com.cbyk.domain.conta.exception.ContaNotFoundException;
import com.cbyk.domain.conta.exception.DataPagamentoException;
import com.cbyk.domain.conta.model.Conta;
import com.cbyk.domain.conta.repository.ContaRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ContaQueryAppService {

    ContaRepository contaRepository;

    public ContaDto obterContaPeloId(Long contaId) {
        return ContaMapper.convertToDto(contaRepository.findById(contaId).orElseThrow(ContaNotFoundException::new));
    }

    public Page<ContaDto> filtrarContasPorSituacaoEDataVencimento(SituacaoEnum situacao, LocalDate dataVencimento, Pageable pageable) {
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withIgnoreNullValues(); // Ignora atributos null
        Conta conta = Conta.builder()
                .situacao(situacao == null ? null : situacao.getValor())
                .dataVencimento(dataVencimento)
                .build();
        Example<Conta> example = Example.of(conta, matcher);
        return contaRepository.findAll(example, pageable).map(ContaMapper::convertToDto);
    }

    public Page<ContaDto> findByDataPagamentoBetween(LocalDate dataPagamentoInicio, LocalDate dataPagamentoFim, Pageable pageable) {
        if (dataPagamentoFim.isBefore(dataPagamentoInicio)) {
            throw new DataPagamentoException("Data de pagamento fim não pode ser anterior à data de pagamento início");
        }
        return contaRepository.findByDataPagamentoBetween(dataPagamentoInicio, dataPagamentoFim, pageable).map(ContaMapper::convertToDto);
    }




}


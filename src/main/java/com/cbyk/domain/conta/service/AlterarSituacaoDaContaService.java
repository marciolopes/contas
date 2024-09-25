package com.cbyk.domain.conta.service;

import com.cbyk.domain.conta.mapper.ContaMapper;
import com.cbyk.domain.conta.dto.ContaDto;
import com.cbyk.domain.conta.enums.SituacaoEnum;
import com.cbyk.domain.conta.exception.ContaNotFoundException;
import com.cbyk.domain.conta.model.Conta;
import com.cbyk.domain.conta.repository.ContaRepository;
import com.cbyk.domain.conta.usecase.AlterarSituacaoDaContaUseCase;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Transactional
public class AlterarSituacaoDaContaService implements AlterarSituacaoDaContaUseCase {

    ContaRepository contaRepository;
    @Override
    public ContaDto handle(Long id, SituacaoEnum novaSituacao) {
        Conta conta = contaRepository.findById(id).orElseThrow(ContaNotFoundException::new);
        conta.setSituacao(novaSituacao.getValor());
        return ContaMapper.convertToDto(contaRepository.save(conta));
    }


}

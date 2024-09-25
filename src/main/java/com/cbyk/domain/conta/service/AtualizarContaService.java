package com.cbyk.domain.conta.service;

import com.cbyk.domain.conta.mapper.ContaMapper;
import com.cbyk.domain.conta.dto.ContaDto;
import com.cbyk.domain.conta.exception.ContaNotFoundException;
import com.cbyk.domain.conta.model.Conta;
import com.cbyk.domain.conta.repository.ContaRepository;
import com.cbyk.domain.conta.usecase.AtualizarContaUseCase;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Transactional
public class AtualizarContaService implements AtualizarContaUseCase {

    private ContaRepository contaRepository;
    @Override
    public ContaDto handle(Long contaId, ContaDto contaDTO) {
        contaRepository.findById(contaId)
                .orElseThrow(ContaNotFoundException::new);

        Conta contaAtualizada = ContaMapper.convertToEntity(contaDTO);
        contaAtualizada.setId(contaId);
        return ContaMapper.convertToDto(contaRepository.save(contaAtualizada));
    }


}

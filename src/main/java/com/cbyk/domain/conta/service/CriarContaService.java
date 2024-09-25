package com.cbyk.domain.conta.service;

import com.cbyk.domain.conta.mapper.ContaMapper;
import com.cbyk.domain.conta.dto.ContaDto;
import com.cbyk.domain.conta.model.Conta;
import com.cbyk.domain.conta.repository.ContaRepository;
import com.cbyk.domain.conta.usecase.CriarContaUseCase;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Transactional
public class CriarContaService implements CriarContaUseCase {

    private ContaRepository contaRepository;
    @Override
    public ContaDto handle(ContaDto contaDTO) {
        Conta conta = ContaMapper.convertToEntity(contaDTO);
        return ContaMapper.convertToDto(contaRepository.save(conta));
    }


}

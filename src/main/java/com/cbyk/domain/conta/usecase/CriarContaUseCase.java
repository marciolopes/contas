package com.cbyk.domain.conta.usecase;

import com.cbyk.domain.conta.dto.ContaDto;

public interface CriarContaUseCase {

    ContaDto handle(ContaDto contaDTO);
}

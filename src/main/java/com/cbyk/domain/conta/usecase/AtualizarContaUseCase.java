package com.cbyk.domain.conta.usecase;

import com.cbyk.domain.conta.dto.ContaDto;

public interface AtualizarContaUseCase {

    ContaDto handle(Long contaId, ContaDto contaDTO);
}

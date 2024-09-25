package com.cbyk.domain.conta.usecase;

import com.cbyk.domain.conta.dto.ContaDto;
import com.cbyk.domain.conta.enums.SituacaoEnum;

public interface AlterarSituacaoDaContaUseCase {

    ContaDto handle(Long id, SituacaoEnum novaSituacao);
}

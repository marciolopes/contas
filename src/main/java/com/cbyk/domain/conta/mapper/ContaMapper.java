package com.cbyk.domain.conta.mapper;

import com.cbyk.domain.conta.dto.ContaDto;
import com.cbyk.domain.conta.enums.SituacaoEnum;
import com.cbyk.domain.conta.model.Conta;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ContaMapper {

    public static ContaDto convertToDto(Conta conta) {
        return ContaDto.builder()
                .id(conta.getId())
                .dataVencimento(conta.getDataVencimento())
                .dataPagamento(conta.getDataPagamento())
                .valor(conta.getValor())
                .descricao(conta.getDescricao())
                .situacao(SituacaoEnum.fromValue(conta.getSituacao()))
                .build();
    }

    public static Conta convertToEntity(ContaDto contaDTO) {
        return Conta.builder()
                .dataVencimento(contaDTO.getDataVencimento())
                .dataPagamento(contaDTO.getDataPagamento())
                .valor(contaDTO.getValor())
                .descricao(contaDTO.getDescricao())
                .situacao(contaDTO.getSituacao().getValor())
                .build();
    }
}

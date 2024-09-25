package com.cbyk.domain.conta.enums;


import com.cbyk.domain.conta.exception.SituacaoException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@AllArgsConstructor
@Getter
public enum SituacaoEnum {
    PENDENTE("1"),
    PAGA("2"),
    VENCIDA("3"),
    CANCELADA("4");


    private final String valor;

    public static SituacaoEnum get(String situacao) {
        return Arrays.stream(values())
                .filter(opcao -> opcao.name().equalsIgnoreCase(situacao))
                .findFirst()
                .orElseThrow(() -> new SituacaoException("Não foi encontrada a situação: " + situacao));
    }

    public static SituacaoEnum fromValue(String valor) {
        return Arrays.stream(values())
                .filter(opcao -> opcao.getValor().equals(valor))
                .findFirst()
                .orElseThrow(() -> new SituacaoException("Não foi encontrada a situação: " + valor));
    }
}

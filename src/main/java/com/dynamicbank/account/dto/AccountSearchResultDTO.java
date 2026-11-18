package com.dynamicbank.account.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

/** Resultado da busca de contas de destino: só o que a tela precisa (sem email e sem número da conta). */
@Getter
@AllArgsConstructor
public class AccountSearchResultDTO {

    private final UUID accountId;
    private final String fullName;
    private final String username;

}
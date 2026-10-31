package com.lojas.moria.execption;

public class MovimentacaoNaoEncontradaException extends RuntimeException {
    public MovimentacaoNaoEncontradaException(String message) {
        super(message);
    }
}

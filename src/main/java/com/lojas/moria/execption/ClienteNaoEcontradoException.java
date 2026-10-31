package com.lojas.moria.execption;

public class ClienteNaoEcontradoException extends RuntimeException {
  public ClienteNaoEcontradoException(String message) {
    super(message);
  }
}

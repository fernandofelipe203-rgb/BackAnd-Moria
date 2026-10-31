package com.lojas.moria.execption;

import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler  {


    @ExceptionHandler(ProdutoNaoEncontradoException.class)
    public ResponseEntity<RespostaErro> tratarProdutoNaoEncontrado(
            ProdutoNaoEncontradoException ex) {

        RespostaErro resposta = new RespostaErro(
                404,
                "Não encontrado",
                ex.getMessage()
        );

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(resposta);
    }


    @ExceptionHandler(ClienteNaoEcontradoException.class)
    public ResponseEntity<RespostaErro> tratarClienteNaoEncontrado(
            ClienteNaoEcontradoException ex) {

        RespostaErro resposta = new RespostaErro(
                404,
                "Não encontrado",
                ex.getMessage()
        );

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(resposta);
    }



    @ExceptionHandler(TelefoneClienteDuplicadoException.class)
    public ResponseEntity<RespostaErro> tratarTelefoneClienteDuplicado(
            TelefoneClienteDuplicadoException ex) {

        RespostaErro resposta = new RespostaErro(
                409,
                "Conflito",
                ex.getMessage()
        );

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(resposta);
    }



    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<RespostaErro> tratarValidacao(
            MethodArgumentNotValidException ex) {

        String mensagem = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(erro -> erro.getDefaultMessage())
                .collect(Collectors.joining(", "));

        RespostaErro resposta = new RespostaErro(
                400,
                "Dados inválidos",
                mensagem
        );

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(resposta);
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<RespostaErro> tratarErroInterno(Exception ex) {
        ex.printStackTrace();

        RespostaErro resposta = new RespostaErro(
                500,
                "Erro interno",
                "Ocorreu um erro interno. Tente novamente mais tarde."
        );

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(resposta);
    }


    @ExceptionHandler(EstoqueInsuficienteException.class)
    public ResponseEntity<RespostaErro> tratarEstoqueInsuficiente(
            EstoqueInsuficienteException ex) {

        RespostaErro resposta = new RespostaErro(
                409,
                "Estoque insuficiente",
                ex.getMessage()
        );

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(resposta);
    }

    @ExceptionHandler(MovimentacaoNaoEncontradaException.class)
    public ResponseEntity<RespostaErro> tratarMovimentacaoNaoEncontrada(
            MovimentacaoNaoEncontradaException ex) {

        RespostaErro resposta = new RespostaErro(
                404,
                "Não encontrado",
                ex.getMessage()
        );

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(resposta);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<RespostaErro> tratarJsonInvalido(
            HttpMessageNotReadableException ex) {

        RespostaErro resposta = new RespostaErro(
                400,
                "Requisição inválida",
                "O corpo da requisição está vazio ou contém dados inválidos."
        );

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(resposta);
    }

    @ExceptionHandler(ProdutoDuplicadoException.class)
    public ResponseEntity<RespostaErro> tratarProdutoDuplicado(
            ProdutoDuplicadoException ex) {

        RespostaErro resposta = new RespostaErro(
                409,
                "Produto duplicado",
                ex.getMessage()
        );

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(resposta);
    }
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<RespostaErro> tratarArgumentoInvalido(
            IllegalArgumentException ex) {

        RespostaErro resposta = new RespostaErro(
                400,
                "Dados inválidos",
                ex.getMessage()
        );

        return ResponseEntity.badRequest().body(resposta);
    }
    @ExceptionHandler(ProdutoComMovimentacoesException.class)
    public ResponseEntity<String> tratarProdutoComMovimentacoes(
            ProdutoComMovimentacoesException excecao) {

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(excecao.getMessage());
    }

}

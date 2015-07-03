package br.ufg.inf.dsdm.mural.ouvidoria.model;

import java.io.File;

/**
 * Created by Laerte on 03/07/2015.
 */
public class Mensagem {
    private String nome = "";
    private String email = "";
    private String cpf = "";
    private String telefone = "";

    private String assunto;
    private String mensagem;

    private File anexo;

    public Mensagem(String nome, String email, String cpf, String telefone, String assunto, String mensagem) {
        this.nome = nome;
        this.email = email;
        this.cpf = cpf;
        this.telefone = telefone;
        this.assunto = assunto;
        this.mensagem = mensagem;
    }

    public Mensagem(String assunto, String mensagem) {
        this.assunto = assunto;
        this.mensagem = mensagem;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public String getCpf() {
        return cpf;
    }

    public String getTelefone() {
        return telefone;
    }

    public String getAssunto() {
        return assunto;
    }

    public String getMensagem() {
        return mensagem;
    }

    public File getAnexo() {
        return anexo;
    }

    public void setAnexo(File anexo) {
        this.anexo = anexo;
    }
}

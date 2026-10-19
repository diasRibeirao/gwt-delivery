package br.com.exemplo.delivery.shared;

import java.io.Serializable;

public class Restaurante implements Serializable {
    private static final long serialVersionUID = 1L;
    private int id;
    private String nome;
    private String categoria;

    public Restaurante() {} // Obrigatorio para serializacao GWT RPC
    public Restaurante(int id, String nome, String categoria) {
        this.id = id; this.nome = nome; this.categoria = categoria;
    }
    public int getId() { return id; }
    public String getNome() { return nome; }
    public String getCategoria() { return categoria; }
}

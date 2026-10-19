package br.com.exemplo.delivery.shared;

import java.io.Serializable;

public class Pedido implements Serializable {
    private static final long serialVersionUID = 1L;
    private int id;
    private int restauranteId;
    private String cliente;
    private String produto;
    private int quantidade;

    public Pedido() {}
    public Pedido(int id, int restauranteId, String cliente, String produto, int quantidade) {
        this.id = id; this.restauranteId = restauranteId; this.cliente = cliente;
        this.produto = produto; this.quantidade = quantidade;
    }
    public int getId() { return id; }
    public int getRestauranteId() { return restauranteId; }
    public String getCliente() { return cliente; }
    public String getProduto() { return produto; }
    public int getQuantidade() { return quantidade; }
}

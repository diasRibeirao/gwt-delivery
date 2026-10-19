package br.com.exemplo.delivery.client;

import java.util.ArrayList;
import br.com.exemplo.delivery.shared.Pedido;
import br.com.exemplo.delivery.shared.Restaurante;
import com.google.gwt.user.client.rpc.AsyncCallback;

/** Presenter: coordena validacoes de interacao e chamadas RPC. */
public class DeliveryPresenter {
    private final DeliveryViewContract view;
    private final DeliveryServiceAsync service;

    public DeliveryPresenter(DeliveryViewContract view, DeliveryServiceAsync service) {
        this.view = view;
        this.service = service;
    }

    public void iniciar() { buscar(""); }

    public void cadastrar(String nome, String categoria) {
        if (vazio(nome) || vazio(categoria)) {
            view.exibirMensagem("Preencha nome e categoria."); return;
        }
        service.cadastrarRestaurante(nome, categoria, new AsyncCallback<Restaurante>() {
            @Override public void onFailure(Throwable erro) { falha(erro); }
            @Override public void onSuccess(Restaurante restaurante) {
                view.limparCadastro();
                buscar("");
                view.exibirMensagem("Restaurante cadastrado: " + restaurante.getNome());
            }
        });
    }

    public void buscar(String termo) {
        service.buscarRestaurantes(termo, new AsyncCallback<ArrayList<Restaurante>>() {
            @Override public void onFailure(Throwable erro) { falha(erro); }
            @Override public void onSuccess(ArrayList<Restaurante> restaurantes) {
                view.exibirRestaurantes(restaurantes);
                view.exibirMensagem(restaurantes.size() + " restaurante(s) encontrado(s).");
            }
        });
    }

    public void pedir(Integer restauranteId, String cliente, String produto, Integer quantidade) {
        if (restauranteId == null) { view.exibirMensagem("Selecione um restaurante."); return; }
        if (vazio(cliente) || vazio(produto) || quantidade == null || quantidade <= 0) {
            view.exibirMensagem("Informe cliente, produto e quantidade positiva."); return;
        }
        service.realizarPedido(restauranteId, cliente, produto, quantidade, new AsyncCallback<Pedido>() {
            @Override public void onFailure(Throwable erro) { falha(erro); }
            @Override public void onSuccess(Pedido pedido) {
                view.exibirMensagem("Pedido #" + pedido.getId() + " criado! Cliente: " + pedido.getCliente()
                    + " | " + pedido.getQuantidade() + "x " + pedido.getProduto());
            }
        });
    }

    private boolean vazio(String valor) { return valor == null || valor.trim().isEmpty(); }
    private void falha(Throwable erro) { view.exibirMensagem("Erro: " + erro.getMessage()); }
}

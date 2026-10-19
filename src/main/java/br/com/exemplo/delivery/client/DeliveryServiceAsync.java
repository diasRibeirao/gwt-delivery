package br.com.exemplo.delivery.client;

import java.util.ArrayList;
import br.com.exemplo.delivery.shared.Pedido;
import br.com.exemplo.delivery.shared.Restaurante;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface DeliveryServiceAsync {
    void cadastrarRestaurante(String nome, String categoria, AsyncCallback<Restaurante> callback);
    void buscarRestaurantes(String termo, AsyncCallback<ArrayList<Restaurante>> callback);
    void realizarPedido(int restauranteId, String cliente, String produto, int quantidade, AsyncCallback<Pedido> callback);
}

package br.com.exemplo.delivery.client;

import java.util.ArrayList;
import br.com.exemplo.delivery.shared.Pedido;
import br.com.exemplo.delivery.shared.Restaurante;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("deliveryService")
public interface DeliveryService extends RemoteService {
    Restaurante cadastrarRestaurante(String nome, String categoria);
    ArrayList<Restaurante> buscarRestaurantes(String termo);
    Pedido realizarPedido(int restauranteId, String cliente, String produto, int quantidade);
}

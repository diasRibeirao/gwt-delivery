package br.com.exemplo.delivery.server;

import java.util.ArrayList;
import java.util.List;
import br.com.exemplo.delivery.client.DeliveryService;
import br.com.exemplo.delivery.shared.Pedido;
import br.com.exemplo.delivery.shared.Restaurante;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class DeliveryServiceImpl extends RemoteServiceServlet implements DeliveryService {
    private static final long serialVersionUID = 1L;
    // Dados compartilhados entre requisicoes; apenas demonstracao, sem persistencia.
    private static final List<Restaurante> RESTAURANTES = new ArrayList<>();
    private static final List<Pedido> PEDIDOS = new ArrayList<>();
    private static int proximoRestaurante = 1;
    private static int proximoPedido = 1;

    static {
        RESTAURANTES.add(new Restaurante(proximoRestaurante++, "Pizzaria Central", "Pizza"));
        RESTAURANTES.add(new Restaurante(proximoRestaurante++, "Burger House", "Hamburguer"));
    }

    @Override
    public synchronized Restaurante cadastrarRestaurante(String nome, String categoria) {
        if (vazio(nome) || vazio(categoria)) throw new IllegalArgumentException("Informe nome e categoria.");
        Restaurante r = new Restaurante(proximoRestaurante++, nome.trim(), categoria.trim());
        RESTAURANTES.add(r);
        return r;
    }

    @Override
    public synchronized ArrayList<Restaurante> buscarRestaurantes(String termo) {
        String filtro = termo == null ? "" : termo.trim().toLowerCase(java.util.Locale.ROOT);
        ArrayList<Restaurante> resultado = new ArrayList<>();
        for (Restaurante r : RESTAURANTES) {
            if (r.getNome().toLowerCase(java.util.Locale.ROOT).contains(filtro)) resultado.add(r);
        }
        return resultado;
    }

    @Override
    public synchronized Pedido realizarPedido(int restauranteId, String cliente, String produto, int quantidade) {
        if (vazio(cliente) || vazio(produto) || quantidade <= 0) {
            throw new IllegalArgumentException("Informe cliente, produto e quantidade positiva.");
        }
        boolean existe = false;
        for (Restaurante r : RESTAURANTES) if (r.getId() == restauranteId) existe = true;
        if (!existe) throw new IllegalArgumentException("Restaurante nao encontrado.");
        Pedido pedido = new Pedido(proximoPedido++, restauranteId, cliente.trim(), produto.trim(), quantidade);
        PEDIDOS.add(pedido);
        return pedido;
    }
    private boolean vazio(String s) { return s == null || s.trim().isEmpty(); }
}

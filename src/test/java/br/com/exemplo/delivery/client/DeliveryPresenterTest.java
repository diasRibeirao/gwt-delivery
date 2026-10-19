package br.com.exemplo.delivery.client;

import static org.junit.Assert.*;
import java.util.ArrayList;
import org.junit.Before;
import org.junit.Test;
import br.com.exemplo.delivery.shared.Pedido;
import br.com.exemplo.delivery.shared.Restaurante;
import com.google.gwt.user.client.rpc.AsyncCallback;

/** Testes Java puros: sem DOM, sem navegador e sem Jetty. */
public class DeliveryPresenterTest {
    private FakeView view;
    private FakeService service;
    private DeliveryPresenter presenter;

    @Before public void configurar() {
        view = new FakeView();
        service = new FakeService();
        presenter = new DeliveryPresenter(view, service);
    }

    @Test public void cadastroInvalidoNaoChamaRpc() {
        presenter.cadastrar("  ", "Pizza");
        assertEquals(0, service.cadastros);
        assertEquals("Preencha nome e categoria.", view.mensagem);
    }

    @Test public void cadastroValidoChamaRpcELimpaFormulario() {
        presenter.cadastrar("Pizzaria R8", "Pizza");
        assertEquals(1, service.cadastros);
        assertTrue(view.cadastroLimpo);
        assertEquals(1, service.buscas); // atualiza lista apos cadastro
        assertEquals("Restaurante cadastrado: Pizzaria R8", view.mensagem);
    }

    @Test public void buscaAtualizaListaEMensagem() {
        presenter.buscar("Pizza");
        assertEquals("Pizza", service.termoBuscado);
        assertEquals(1, view.restaurantes.size());
        assertEquals("1 restaurante(s) encontrado(s).", view.mensagem);
    }

    @Test public void pedidoSemRestauranteNaoChamaRpc() {
        presenter.pedir(null, "Emerson", "Pizza", 1);
        assertEquals(0, service.pedidos);
        assertEquals("Selecione um restaurante.", view.mensagem);
    }

    @Test public void quantidadeInvalidaNaoChamaRpc() {
        presenter.pedir(1, "Emerson", "Pizza", 0);
        assertEquals(0, service.pedidos);
        assertEquals("Informe cliente, produto e quantidade positiva.", view.mensagem);
    }

    @Test public void pedidoValidoRetornaConfirmacao() {
        presenter.pedir(1, "Emerson", "Pizza", 2);
        assertEquals(1, service.pedidos);
        assertEquals("Pedido #15 criado! Cliente: Emerson | 2x Pizza", view.mensagem);
    }

    @Test public void falhaRpcExibeErro() {
        service.falhar = true;
        presenter.buscar("Pizza");
        assertEquals("Erro: simulacao de falha RPC", view.mensagem);
    }

    private static class FakeView implements DeliveryViewContract {
        String mensagem;
        boolean cadastroLimpo;
        ArrayList<Restaurante> restaurantes;
        @Override public void exibirRestaurantes(ArrayList<Restaurante> r) { restaurantes = r; }
        @Override public void limparCadastro() { cadastroLimpo = true; }
        @Override public void exibirMensagem(String texto) { mensagem = texto; }
    }

    private static class FakeService implements DeliveryServiceAsync {
        int cadastros, buscas, pedidos;
        String termoBuscado;
        boolean falhar;
        @Override public void cadastrarRestaurante(String nome, String categoria, AsyncCallback<Restaurante> cb) {
            cadastros++;
            if (falhar) cb.onFailure(new RuntimeException("simulacao de falha RPC"));
            else cb.onSuccess(new Restaurante(1, nome, categoria));
        }
        @Override public void buscarRestaurantes(String termo, AsyncCallback<ArrayList<Restaurante>> cb) {
            buscas++; termoBuscado = termo;
            if (falhar) { cb.onFailure(new RuntimeException("simulacao de falha RPC")); return; }
            ArrayList<Restaurante> r = new ArrayList<>();
            r.add(new Restaurante(1, "Pizzaria R8", "Pizza"));
            cb.onSuccess(r);
        }
        @Override public void realizarPedido(int id, String cliente, String produto, int quantidade, AsyncCallback<Pedido> cb) {
            pedidos++;
            if (falhar) cb.onFailure(new RuntimeException("simulacao de falha RPC"));
            else cb.onSuccess(new Pedido(15, id, cliente, produto, quantidade));
        }
    }
}

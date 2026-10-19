package br.com.exemplo.delivery.client;

import java.util.ArrayList;
import br.com.exemplo.delivery.shared.Pedido;
import br.com.exemplo.delivery.shared.Restaurante;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.IntegerBox;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class DeliveryView extends Composite {
    interface Binder extends UiBinder<Widget, DeliveryView> {}
    private static final Binder BINDER = GWT.create(Binder.class);
    private final DeliveryServiceAsync service = GWT.create(DeliveryService.class);

    @UiField TextBox nomeRestaurante;
    @UiField TextBox categoriaRestaurante;
    @UiField TextBox termoBusca;
    @UiField TextBox cliente;
    @UiField TextBox produto;
    @UiField IntegerBox quantidade;
    @UiField ListBox listaRestaurantes;
    @UiField Button botaoCadastrar;
    @UiField Button botaoBuscar;
    @UiField Button botaoPedir;
    @UiField Label status;

    public DeliveryView() {
        initWidget(BINDER.createAndBindUi(this));
        nomeRestaurante.getElement().setPropertyString("placeholder", "Nome do restaurante");
        categoriaRestaurante.getElement().setPropertyString("placeholder", "Categoria");
        termoBusca.getElement().setPropertyString("placeholder", "Buscar por nome");
        cliente.getElement().setPropertyString("placeholder", "Cliente");
        produto.getElement().setPropertyString("placeholder", "Produto");
        quantidade.setValue(1);
        carregarRestaurantes("");
    }

    @UiHandler("botaoCadastrar") void cadastrar(ClickEvent event) {
        if (vazio(nomeRestaurante.getText()) || vazio(categoriaRestaurante.getText())) {
            status.setText("Preencha nome e categoria."); return;
        }
        service.cadastrarRestaurante(nomeRestaurante.getText(), categoriaRestaurante.getText(), new AsyncCallback<Restaurante>() {
            @Override public void onFailure(Throwable error) { falha(error); }
            @Override public void onSuccess(Restaurante r) {
                nomeRestaurante.setText(""); categoriaRestaurante.setText("");
                carregarRestaurantes("");
                status.setText("Restaurante cadastrado: " + r.getNome());
            }
        });
    }

    @UiHandler("botaoBuscar") void buscar(ClickEvent event) { carregarRestaurantes(termoBusca.getText()); }

    private void carregarRestaurantes(String termo) {
        service.buscarRestaurantes(termo, new AsyncCallback<ArrayList<Restaurante>>() {
            @Override public void onFailure(Throwable error) { falha(error); }
            @Override public void onSuccess(ArrayList<Restaurante> restaurantes) {
                listaRestaurantes.clear();
                for (Restaurante r : restaurantes) {
                    listaRestaurantes.addItem(r.getNome() + " - " + r.getCategoria(), String.valueOf(r.getId()));
                }
                status.setText(restaurantes.size() + " restaurante(s) encontrado(s).");
            }
        });
    }

    @UiHandler("botaoPedir") void pedir(ClickEvent event) {
        if (listaRestaurantes.getItemCount() == 0 || listaRestaurantes.getSelectedIndex() < 0) {
            status.setText("Selecione um restaurante."); return;
        }
        Integer qtd = quantidade.getValue();
        if (vazio(cliente.getText()) || vazio(produto.getText()) || qtd == null || qtd <= 0) {
            status.setText("Informe cliente, produto e quantidade positiva."); return;
        }
        int id = Integer.parseInt(listaRestaurantes.getSelectedValue());
        service.realizarPedido(id, cliente.getText(), produto.getText(), qtd, new AsyncCallback<Pedido>() {
            @Override public void onFailure(Throwable error) { falha(error); }
            @Override public void onSuccess(Pedido p) {
                status.setText("Pedido #" + p.getId() + " criado! Cliente: " + p.getCliente()
                    + " | " + p.getQuantidade() + "x " + p.getProduto());
            }
        });
    }
    private boolean vazio(String s) { return s == null || s.trim().isEmpty(); }
    private void falha(Throwable error) { status.setText("Erro: " + error.getMessage()); }
}

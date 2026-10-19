package br.com.exemplo.delivery.client;

import java.util.ArrayList;
import br.com.exemplo.delivery.shared.Restaurante;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.IntegerBox;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

/** View: apresenta dados e repassa eventos, sem conhecer o servico RPC. */
public class DeliveryView extends Composite implements DeliveryViewContract {
    interface Binder extends UiBinder<Widget, DeliveryView> {}
    private static final Binder BINDER = GWT.create(Binder.class);
    private DeliveryPresenter presenter;

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
    }

    public void setPresenter(DeliveryPresenter presenter) { this.presenter = presenter; }

    @UiHandler("botaoCadastrar") void cadastrar(ClickEvent e) {
        if (presenter != null) presenter.cadastrar(nomeRestaurante.getText(), categoriaRestaurante.getText());
    }
    @UiHandler("botaoBuscar") void buscar(ClickEvent e) {
        if (presenter != null) presenter.buscar(termoBusca.getText());
    }
    @UiHandler("botaoPedir") void pedir(ClickEvent e) {
        if (presenter != null) presenter.pedir(getRestauranteSelecionado(), cliente.getText(), produto.getText(), quantidade.getValue());
    }

    public void exibirRestaurantes(ArrayList<Restaurante> restaurantes) {
        listaRestaurantes.clear();
        for (Restaurante r : restaurantes) {
            listaRestaurantes.addItem(r.getNome() + " - " + r.getCategoria(), String.valueOf(r.getId()));
        }
    }
    public Integer getRestauranteSelecionado() {
        if (listaRestaurantes.getItemCount() == 0 || listaRestaurantes.getSelectedIndex() < 0) return null;
        return Integer.valueOf(listaRestaurantes.getSelectedValue());
    }
    public void limparCadastro() { nomeRestaurante.setText(""); categoriaRestaurante.setText(""); }
    public void exibirMensagem(String mensagem) { status.setText(mensagem); }
}

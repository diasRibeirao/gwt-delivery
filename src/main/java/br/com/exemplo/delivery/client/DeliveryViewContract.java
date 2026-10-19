package br.com.exemplo.delivery.client;

import java.util.ArrayList;
import br.com.exemplo.delivery.shared.Restaurante;

/** Contrato minimo para isolar o Presenter dos widgets GWT. */
public interface DeliveryViewContract {
    void exibirRestaurantes(ArrayList<Restaurante> restaurantes);
    void limparCadastro();
    void exibirMensagem(String mensagem);
}

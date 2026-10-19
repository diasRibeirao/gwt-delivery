package br.com.exemplo.delivery.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.RootPanel;

public class DeliveryEntryPoint implements EntryPoint {
    @Override public void onModuleLoad() {
        DeliveryView view = new DeliveryView();
        DeliveryPresenter presenter = new DeliveryPresenter(view, GWT.create(DeliveryService.class));
        view.setPresenter(presenter);
        RootPanel.get("app").add(view);
        presenter.iniciar();
    }
}

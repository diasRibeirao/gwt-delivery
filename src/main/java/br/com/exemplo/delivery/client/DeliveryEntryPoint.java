package br.com.exemplo.delivery.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootPanel;

public class DeliveryEntryPoint implements EntryPoint {
    @Override public void onModuleLoad() {
        RootPanel.get("app").add(new DeliveryView());
    }
}

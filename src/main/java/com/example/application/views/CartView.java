package com.example.application.views;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.Route;
import jakarta.annotation.PostConstruct;

@Route(value = "cart",layout = CustomerView.class)
public class CartView extends Div {
    @PostConstruct
    void build(){
        add(new Div("Cart"));
    }
}

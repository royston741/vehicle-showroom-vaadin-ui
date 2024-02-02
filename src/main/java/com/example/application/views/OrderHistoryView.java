package com.example.application.views;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.Route;
import jakarta.annotation.PostConstruct;

@Route(value = "orderHistory",layout = CustomerView.class)
public class OrderHistoryView extends Div {

    @PostConstruct
    void build(){
        add(new Div("Order history"));
    }
}

package com.example.application.views;

import com.example.application.dto.CustomerDTO;
import com.example.application.service.UserService;
import com.example.application.views.home.HomeView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.*;
import com.vaadin.flow.server.VaadinSession;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;


@Slf4j
@RoutePrefix("customer")
public class CustomerView extends AppLayout implements RouterLayout, BeforeEnterObserver {

    @Autowired
    private UserService userService;

    private CustomerDTO loggedCustomer = new CustomerDTO();

    private  Integer userId;

    {
        userId = (Integer) VaadinSession.getCurrent().getAttribute("userId");
    }

    @PostConstruct
    void printVal() {
        if (userId != null) {
            getCustomerInfo();
            buildNavBar().forEach(this::addToNavbar);
        }
    }

    private List<Component> buildNavBar() {

        H1 title = new H1("MyDrive");
        title.getStyle()
                .set("font-size", "var(--lumo-font-size-l)")
                .set("left", "var(--lumo-space-l)")
                .set("margin", "0")
                .set("position", "absolute");

        Tabs tabs = getTabs();

        Button logOutBtn = new Button("Log out.", clickEvent -> onLogOutEventHandler());

        Button profile = new Button(loggedCustomer.getFirstName(), clickEvent -> {
        });
        profile.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        Icon icon = VaadinIcon.USER.create();
        profile.setIcon(icon);

        Div profileAndLogOutBtnContainer = new Div(profile, logOutBtn);
        return List.of(title, tabs, profileAndLogOutBtnContainer);
    }

    private Tabs getTabs() {
        Tabs tabs = new Tabs();
        tabs.getStyle().set("margin", "auto");
        tabs.add(
                createTab("Home", HomeView.class),
                createTab("Cart", CartView.class),
                createTab("Order history", OrderHistoryView.class)
        );
        return tabs;
    }

    private Tab createTab(String viewName, Class className) {
        RouterLink link = new RouterLink(viewName, className);
        return new Tab(link);
    }

    private void onLogOutEventHandler() {
        VaadinSession.getCurrent().getSession().invalidate();
    }

    private void getCustomerInfo() {
        try {
            loggedCustomer = userService.getCustomerById(userId).getBody().getResponseData();
        } catch (Exception e) {
            log.error("Error in getCustomerInfo {}", e);
        }
    }


    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        if (VaadinSession.getCurrent().getAttribute("userId") == null) {
            log.info("Logging out");
            UI.getCurrent().getPage().setLocation("login");
        }
    }
}

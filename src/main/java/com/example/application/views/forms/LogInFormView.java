package com.example.application.views.forms;

import com.example.application.dto.CustomerDTO;
import com.example.application.exception.ErrorResponseException;
import com.example.application.service.UserService;
import com.example.application.views.CustomerView;
import com.example.application.views.home.HomeView;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@CssImport("./styles/logInForm.css")
@Route(value = "login")
public class LogInFormView extends VerticalLayout{

    CustomerDTO customer = new CustomerDTO();
    Binder<CustomerDTO> binder = new Binder<>(CustomerDTO.class);
    List<Paragraph> errMessageList = new ArrayList<>();
    TextField userName = new TextField("Username");
    PasswordField password = new PasswordField("Password");
    Div errMessages = new Div();
    Button logInBtn = new Button();

    @Autowired
    UserService userService;

    @PostConstruct
    private void buildComponents(){
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
        configureInputFields();
        add(createFormContainer());
    }

    private void configureInputFields() {
        // Add validation
        binder.forField(userName).asRequired("Username is required").bind(CustomerDTO::getFirstName, CustomerDTO::setFirstName);
        binder.forField(password).asRequired("Password is required").bind(CustomerDTO::getPassword, CustomerDTO::setPassword);
    }

    private Div createFormContainer() {
        Div formContainer = new Div();
        formContainer.setClassName("form_container");
        H1 formHeading = new H1("Log In");
        formContainer.add(formHeading, createForm(), createFormFooter());
        return formContainer;
    }

    private FormLayout createForm() {
        FormLayout loginForm = new FormLayout();
        loginForm.add(userName, password, errMessages, createButton("Log in", ButtonVariant.LUMO_PRIMARY, onLogInBtnClick()));
        return loginForm;
    }

    private Button createButton(String btnName, ButtonVariant btnTheme, ComponentEventListener<ClickEvent<Button>> clickFunction) {
        logInBtn.setText(btnName);
        logInBtn.addClickListener(clickFunction);
        logInBtn.addThemeVariants(btnTheme);
        logInBtn.getStyle().setMarginTop("10px");
        return logInBtn;
    }

    private Div createFormFooter() {
        Div formFooter = new Div();
        formFooter.setClassName("form_footer");
        formFooter.add(new Anchor("signup", "Sign Up."), new Anchor("forgotPassword", "Forgot password?"));
        return formFooter;
    }

    private ComponentEventListener<ClickEvent<Button>> onLogInBtnClick() {
        return clickEvent -> {
            errMessageList.forEach(err -> errMessages.remove(err));
            try {
                binder.writeBean(customer);
                CustomerDTO loggedInCustomer = userService.logIn(customer.getFirstName(), customer.getPassword()).getBody().getResponseData();
                VaadinSession.getCurrent().setAttribute("userId",loggedInCustomer.getId());
                VaadinSession.getCurrent().setAttribute("userType",loggedInCustomer.getUserType());
                UI.getCurrent().navigate(HomeView.class);
            } catch (Exception e) {
                if (e instanceof ValidationException) {
                    Notification.show("Log in credentials are required");
                } else if (e instanceof ErrorResponseException errorResponse) {
                     Notification.show(errorResponse.getResponse().getErrMssg().toString());
                } else {
                    errMessages.add(new Paragraph("Log in Failed"));
                    log.error("Error in onLogInBtnClick {}", e);
                }
            }
        };
    }
}

package com.example.application.views.forms;

import com.example.application.constants.UserType;
import com.example.application.dto.CustomerDTO;
import com.example.application.exception.ErrorResponseException;
import com.example.application.service.UserService;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.Route;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@CssImport("./styles/logInForm.css")
@Route(value = "signup")
public class SignUpFormView extends VerticalLayout {

    CustomerDTO customer = new CustomerDTO();
    Binder<CustomerDTO> binder = new BeanValidationBinder<>(CustomerDTO.class);
    List<Paragraph> errMessageList = new ArrayList<>();
    TextField firstName = new TextField("First name");
    TextField lastName = new TextField("Last name");
    TextField email = new TextField("Email");
    TextField phoneNo = new TextField("Phone no.");
    PasswordField password = new PasswordField("Password");
    PasswordField confirmPassword = new PasswordField("Confirm password");
    TextArea address = new TextArea("Address");
    Div errMessages = new Div();
    Button logInBtn = new Button();

    @Autowired
    private UserService userService;

    @PostConstruct
    private void buildComponents() {
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
        configureInputFields();
        add(createFormContainer());
    }

    private void configureInputFields() {
        // Add validation
        binder.bindInstanceFields(this);
        password.addInputListener(event ->confirmPassword.setEnabled(true));

        confirmPassword.setEnabled(false);
        confirmPassword.addBlurListener(event -> {
            if (!password.getValue().equals(confirmPassword.getValue())) {
                confirmPassword.setInvalid(true);
                confirmPassword.setErrorMessage("Password doesn't match.");
                Notification.show("Incorrect Password");
            }
        });
    }

    private Div createFormContainer() {
        Div formContainer = new Div();
        formContainer.setClassName("form_container");
        H1 formHeading = new H1("Sign up");
        formContainer.add(formHeading, createForm(), createFormFooter());
        return formContainer;
    }

    private FormLayout createForm() {
        FormLayout loginForm = new FormLayout();
        loginForm.add(firstName, lastName, email, phoneNo, password, confirmPassword, address, errMessages, createButton("Sign up", ButtonVariant.LUMO_PRIMARY, onSignUpClick()));
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
        Paragraph footerMessage = new Paragraph(new Span("Already have a account? "), new Anchor("login", "Log In."));
        formFooter.add(footerMessage);
        return formFooter;
    }

    private ComponentEventListener<ClickEvent<Button>> onSignUpClick() {
        return clickEvent -> {
            errMessageList.forEach(err -> errMessages.remove(err));
            try {
                binder.writeBean(customer);
                customer.setUserType(UserType.CUSTOMER);
                userService.createCustomer(customer).getBody().getResponseData();
            } catch (Exception e) {
                if (e instanceof ValidationException) {
                    Notification.show("User not registered");
                } else if (e instanceof ErrorResponseException errorResponse) {
                   log.info(errorResponse.getResponse().getErrMssg().toString());
                } else {
                    errMessages.add(new Paragraph("User Registration Failed"));
                    log.error("Error in onLogInBtnClick {}", e);
                }
            }
        };
    }
}

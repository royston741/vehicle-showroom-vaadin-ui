package com.example.application.views.home;

import com.example.application.constants.SortDirection;
import com.example.application.constants.TwoWheelerType;
import com.example.application.constants.VehicleType;
import com.example.application.dto.*;
import com.example.application.views.CustomerView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.accordion.AccordionPanel;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.details.DetailsVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.router.Route;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import com.vaadin.flow.component.icon.Icon;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

@CssImport("./styles/vehicleView.css")
@Route(value = "home", layout = CustomerView.class)
public class HomeView extends Div {

    @Autowired
    private HomePresenter homePresenter;

    private OrderedList vehicleListView = new OrderedList();

    private Page<VehicleDTO> pageConfig = new Page<>();

    private FilterConfig filterConfig = new FilterConfig();

    private SortConfig sortConfig = new SortConfig();

   private  Accordion filterAndSortBox = new Accordion();

    private Select<String> twoWheelerTypeSelectField = new Select<>();

    private int pageSize=200;

    @PostConstruct
    private void build() {
        configProperties();
        addVehiclesToView();
        createFilterAndSortBox();
        Div homeView = new Div();
        homeView.addClassName("home_view");
        homeView.add(filterAndSortBox, vehicleListView);
        add(homeView);
    }

    private void configProperties() {
        List<Double> minAndMaxPrice = homePresenter.getStartAndEndPrice();
        filterConfig.setStartingPrice(minAndMaxPrice.get(1));
        filterConfig.setEndingPrice(minAndMaxPrice.get(0));
        pageConfig = homePresenter.getAllAvailableVehicles(0, pageSize, sortConfig, filterConfig);

//        vehicleListView.getElement().addEventListener("scroll",e->{
//            System.out.println("Clicked");
//        });
    }

    private void createFilterAndSortBox() {
        filterAndSortBox.getStyle().setPaddingTop("30px");
        createAccordionPanel("Sort by", createSortByOptions());
        createAccordionPanel("Filter", createFilterBox());
    }

    private AccordionPanel createAccordionPanel(String panelName, Component panelData) {
        AccordionPanel accordionPanel = filterAndSortBox.add(panelName, panelData);
        accordionPanel.addThemeVariants(DetailsVariant.FILLED);
        return accordionPanel;
    }

    public ComboBox<String> createSortByOptions() {
        ComboBox<String> comboBox = new ComboBox<>("Sort By");
        comboBox.setWidth("100%");
        comboBox.setItems(List.of("Relevance", "Rating", "price -- low to high", "price -- high to low"));
        comboBox.setValue("Relevance");
        comboBox.addValueChangeListener(e -> onSortByChangEvent(e.getValue()));
        return comboBox;
    }

    public void onSortByChangEvent(String sortValue) {
        SortConfig sort = new SortConfig();
        sort.setDirection(SortDirection.ASC);
        switch (sortValue) {
            case "Rating" -> {
                sort.setColumn(sortValue.toLowerCase());
                sort.setDirection(SortDirection.DESC);
            }
            case "price -- low to high" -> {
                sort.setColumn("price");
                sort.setDirection(SortDirection.ASC);
            }
            case "price -- high to low" -> {
                sort.setColumn("price");
                sort.setDirection(SortDirection.DESC);
            }
            default -> sort.setColumn("id");
        }
        sortConfig = sort;
        pageConfig = homePresenter.getAllAvailableVehicles(0, pageSize, sortConfig, filterConfig);
        addVehiclesToView();
    }

    public Component createFilterBox() {

        FormLayout filterDialogBoxLayout = new FormLayout();

        Select<String> vehicleTypeSelectField = createFilterSelectFields("Vehicle Type", homePresenter.getAllVehicleType());
        vehicleTypeSelectField.addValueChangeListener(event -> onChangeVehicleType(event.getValue()));

        twoWheelerTypeSelectField = createFilterSelectFields("Two Wheeler Type", homePresenter.getAllTwoWheelerType());
        twoWheelerTypeSelectField.addValueChangeListener(event -> onChangeTwoWheelerType(event.getValue()));
        twoWheelerTypeSelectField.setEnabled(false);

        NumberField startPriceField = createFilterNumberFields("Start price", filterConfig.getStartingPrice(), true);
        startPriceField.addValueChangeListener(event -> filterConfig.setStartingPrice(event.getValue()));

        NumberField endPriceField = createFilterNumberFields("End price", filterConfig.getEndingPrice(), false);
        endPriceField.addValueChangeListener(event -> filterConfig.setEndingPrice(event.getValue()));

        Button filterBtn = new Button("Filter", e -> {
            pageConfig = homePresenter.getAllAvailableVehicles(0, pageSize, sortConfig, filterConfig);
            addVehiclesToView();
        });
        filterBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        filterBtn.setWidth("100%");
        filterDialogBoxLayout.add(vehicleTypeSelectField, twoWheelerTypeSelectField, startPriceField, endPriceField, filterBtn);

        return filterDialogBoxLayout;

    }

    private Select<String> createFilterSelectFields(String label, List<String> listOfOptions) {
        Select<String> select = new Select<>();
        select.setWidth("100%");
        select.setLabel(label);
        List<String> selectOptionList = new ArrayList<>();
        selectOptionList.add("ALL");
        selectOptionList.addAll(listOfOptions);
        select.setItems(selectOptionList);
        select.setValue("ALL");
        return select;
    }

    private NumberField createFilterNumberFields(String label, double price, boolean isStartPrice) {
        NumberField numberField = new NumberField(label);
        numberField.setWidth("100%");
        numberField.setValue(price);
        numberField.setMin(filterConfig.getStartingPrice());
        numberField.setMax(filterConfig.getEndingPrice());
        numberField.setErrorMessage("Price range should be between "+filterConfig.getStartingPrice()+" and "+filterConfig.getEndingPrice());
        return numberField;
    }

    private void onChangeVehicleType(String vehicleType) {
        if (vehicleType.equals("BIKE") || vehicleType.equals("CAR")) {
            filterConfig.setVehicleType(VehicleType.valueOf(vehicleType));
            if (vehicleType.equals("BIKE")) {
                twoWheelerTypeSelectField.setEnabled(true);
            } else {
                filterConfig.setTwoWheelerType(null);
                twoWheelerTypeSelectField.setEnabled(false);
                twoWheelerTypeSelectField.setValue("ALL");
            }
        } else {
            filterConfig.setTwoWheelerType(null);
            twoWheelerTypeSelectField.setEnabled(false);
            twoWheelerTypeSelectField.setValue("ALL");
            filterConfig.setVehicleType(null);
        }
    }

    private void onChangeTwoWheelerType(String twoWheelerType) {
        if (twoWheelerType.equals("SCOOTY") || twoWheelerType.equals("SPORTS")) {
            filterConfig.setTwoWheelerType(TwoWheelerType.valueOf(twoWheelerType));
        } else {
            filterConfig.setTwoWheelerType(null);
        }
    }

    private void addVehiclesToView() {
        vehicleListView.removeAll();
        vehicleListView.addClassName("vehicle_list");
        pageConfig.getContent().forEach(vehicle -> vehicleListView.add(vehicleView(vehicle)));
    }

    private ListItem vehicleView(VehicleDTO vehicleData) {
        ListItem vehicle = new ListItem();
        vehicle.addClassName("vehicle_card");

        Div vehicleImageContainer = new Div(new Image(vehicleData.getVehicleImg(), vehicleData.getName()));
        vehicleImageContainer.addClassName("vehicle_img_container");

        Paragraph rating = new Paragraph();
        rating.addClassName("vehicle_rating");
        rating.add(vehicleData.getRating() + " ");
        Icon icon = VaadinIcon.STAR.create();
        icon.setSize("15px");
        icon.getStyle().setMarginLeft("8px");
        rating.add(icon);

        Paragraph price = new Paragraph("Price : ");
        DecimalFormat df = new DecimalFormat("₹,###,###,###.00");
        String formattedPrice = df.format(vehicleData.getPrice());
        vehicle.add(price);
        price.add(formattedPrice);

        vehicle.add(vehicleImageContainer, new H3(vehicleData.getName()), rating, price);
        return vehicle;
    }

}

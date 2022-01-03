package ru.samcold.rtks.controllers.customer;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.controlsfx.validation.Severity;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;
import org.springframework.stereotype.Component;
import ru.samcold.rtks._utils.Worder;
import ru.samcold.rtks.controllers._utils.AnimationDirection;
import ru.samcold.rtks.controllers._utils.DetailController;
import ru.samcold.rtks.controllers._utils.ShowView;
import ru.samcold.rtks.domain.Customer;
import ru.samcold.rtks.domain.proxy.CustomerProxy;
import ru.samcold.rtks.services.customer.CustomerService;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Component
@FxmlView("/fxml/customer/customerDetail.fxml")
public class CustomerDetailController implements DetailController<Customer> {

    //region FXML
    @FXML
    private AnchorPane anchorRoot;
    @FXML
    private Accordion accordion;

    @FXML
    private TextField name2_tf;
    @FXML
    private TextField name_tf;
    @FXML
    private TextField address_tf;
    @FXML
    private TextField address2_tf;

    @FXML
    private TextField inn_tf;
    @FXML
    private TextField kpp_tf;
    @FXML
    private TextField ogrn_tf;
    @FXML
    private TextField bank_tf;
    @FXML
    private TextField rs_tf;
    @FXML
    private TextField ks_tf;
    @FXML
    private TextField bik_tf;

    @FXML
    private TextField boss_tf;
    @FXML
    private TextField post_tf;

    @FXML
    private TextField phone_tf;
    @FXML
    private TextField fax_tf;
    @FXML
    private TextField email_tf;
    @FXML
    private TextField web_tf;

    @FXML
    private TextArea note_tf;

    @FXML
    private Button equalName_btn;
    @FXML
    private Button equalAddress_btn;

    @FXML
    private Button card_btn;
    @FXML
    private Button save_btn;
    @FXML
    private Button cancel_btn;
    //endregion

    private final FxWeaver fxWeaver;
    private final CustomerService customerService;
    private final Worder worder;
    private final ShowView showView;

    private CustomerProxy proxy;
    private int saveMode;

    public CustomerDetailController(FxWeaver fxWeaver,
                                    CustomerService customerService,
                                    Worder worder,
                                    ShowView showView
    ) {
        this.fxWeaver = fxWeaver;
        this.customerService = customerService;
        this.worder = worder;
        this.showView = showView;
    }

    @Override
    public void initialize(Customer entity) {
        saveMode = entity.getId();
        proxy = new CustomerProxy(entity);

        DetailController.super.initialize(entity);

    }


//    private void test1(List<Node> nodeList) {
//        for (Node node : nodeList) {
//            if (node instanceof TextInputControl) {
//                ((TextInputControl) node).textProperty().set("Ok");
//                vs.registerValidator((Control) node, Validator.createEmptyValidator("Это поле должно быть заполнено.", Severity.ERROR));
//            }
//
//            // recursive
//            if (node instanceof Pane) {
//                test1(((Pane) node).getChildren(), vs);
//            }
//        }
//    }

//    private void test() {
//        for (TitledPane titledPane : accordion.getPanes()) {
//            if (!titledPane.getText().equals("Контакты")) {
//                Pane pane = (Pane) titledPane.getContent();
//                test1(pane.getChildren());
//            }
//        }
//    }

    @Override
    public void initFields() {
        name_tf.textProperty().bindBidirectional(proxy.nameProperty());
        name2_tf.textProperty().bindBidirectional(proxy.name2Property());
        address_tf.textProperty().bindBidirectional(proxy.addressProperty());
        address2_tf.textProperty().bindBidirectional(proxy.address2Property());
        inn_tf.textProperty().bindBidirectional(proxy.innProperty());
        kpp_tf.textProperty().bindBidirectional(proxy.kppProperty());
        ogrn_tf.textProperty().bindBidirectional(proxy.ogrnProperty());
        rs_tf.textProperty().bindBidirectional(proxy.rsProperty());
        ks_tf.textProperty().bindBidirectional(proxy.ksProperty());
        bank_tf.textProperty().bindBidirectional(proxy.bankProperty());
        bik_tf.textProperty().bindBidirectional(proxy.bikProperty());
        boss_tf.textProperty().bindBidirectional(proxy.bossProperty());
        post_tf.textProperty().bindBidirectional(proxy.postProperty());
        phone_tf.textProperty().bindBidirectional(proxy.phoneProperty());
        fax_tf.textProperty().bindBidirectional(proxy.faxProperty());
        email_tf.textProperty().bindBidirectional(proxy.emailProperty());
        web_tf.textProperty().bindBidirectional(proxy.webProperty());
        note_tf.textProperty().bindBidirectional(proxy.noteProperty());

        // validator
        ValidationSupport vs = new ValidationSupport();

        for (TitledPane titledPane : accordion.getPanes()) {
            if (!titledPane.getText().equals("Примечание") && !titledPane.getText().equals("Контакты")) {
                Pane pane = (Pane) titledPane.getContent();
                setValidator(pane.getChildren(), vs);
            }
        }

        save_btn.disableProperty().bind(vs.invalidProperty());
    }

    private void setValidator(List<Node> nodeList, ValidationSupport vs) {
        for (Node node : nodeList) {
            if (node instanceof TextInputControl) {
                vs.registerValidator((Control) node,
                        Validator.createEmptyValidator("Это поле должно быть заполнено.", Severity.ERROR));
            }

            // recursive
            if (node instanceof Pane) {
                setValidator(((Pane) node).getChildren(), vs);
            }
        }
    }

    @Override
    public void initButtons() {
        equalName_btn.setOnAction(actionEvent -> proxy.name2Property().set(proxy.nameProperty().get()));
        equalAddress_btn.setOnAction(actionEvent -> proxy.address2Property().set(proxy.addressProperty().get()));
        card_btn.setOnAction(actionEvent -> printCustomerCard());

        initGeneralButtons(save_btn, cancel_btn);
    }

    @Override
    public void save() {
        cancel_btn.setText("<< Назад");

        Customer changedCustomer = customerService.save(getEntity());

        if (saveMode == 0) {
            exit(changedCustomer);
        } else {
            initialize(changedCustomer);
            cancel_btn.setOnAction(actionEvent -> exit(changedCustomer));
        }
    }

    @Override
    public void exit(Customer entity) {
        Parent root = fxWeaver.loadView(CustomerListController.class);
        CustomerListController controller = fxWeaver.getBean(CustomerListController.class);

        if (entity != null) {
            for (Customer customer : controller.getTable().getItems()) {
                if (Objects.equals(customer.getId(), entity.getId())) {
                    controller.getTable().getSelectionModel().select(customer);
                }
            }
            controller.getTable().scrollTo(controller.getTable().getSelectionModel().getSelectedItem());
        }

        AnchorPane parentContainer = (AnchorPane) anchorRoot.getParent();
        showView.show(parentContainer, root, AnimationDirection.HORIZONTAL);
    }

    @Override
    public Customer getEntity() {
        return new Customer(proxy);
    }

    private void printCustomerCard() {
        if (getEntity().getId() != 0) {
            try {
                worder.printCard(getEntity());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

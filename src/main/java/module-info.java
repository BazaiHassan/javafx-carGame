module com.hbazai.carcrashprj {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.hbazai.carcrashprj to javafx.fxml;
    exports com.hbazai.carcrashprj;
}
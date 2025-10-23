module com.example.swe316hw01 {
    requires javafx.controls;
    requires javafx.fxml;

    // Apache POI
    requires org.apache.poi.poi;
    requires org.apache.poi.ooxml;

    // Required by POI (correct module names)
    requires org.apache.commons.compress;
    requires org.apache.commons.collections4;
    requires org.apache.commons.codec;
    requires org.apache.logging.log4j;

    opens com.example.swe316hw01 to javafx.fxml;
    exports com.example.swe316hw01;
}
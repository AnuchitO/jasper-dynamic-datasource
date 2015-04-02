package com.nong.jasperDynamic;

import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.builder.DynamicReports;
import net.sf.dynamicreports.report.builder.column.Columns;
import net.sf.dynamicreports.report.builder.component.Components;
import net.sf.dynamicreports.report.builder.datatype.DataTypes;
import net.sf.dynamicreports.report.constant.HorizontalAlignment;
import net.sf.dynamicreports.report.exception.DRException;
import org.apache.commons.io.IOUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

//import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
//import net.sf.dynamicreports.report.builder.DynamicReports;
//import net.sf.dynamicreports.report.builder.column.Columns;
//import net.sf.dynamicreports.report.builder.component.Components;
//import net.sf.dynamicreports.report.builder.datatype.DataTypes;
//import net.sf.dynamicreports.report.constant.HorizontalAlignment;
//import net.sf.dynamicreports.report.exception.DRException;

/**
 * SQL
 * <p/>
 * <p/>
 * CREATE TABLE `customers` (
 * `id` int(11) NOT NULL AUTO_INCREMENT,
 * `first_name` varchar(50) DEFAULT NULL,
 * `last_name` varchar(50) DEFAULT NULL,
 * `date` date DEFAULT NULL,
 * PRIMARY KEY (`id`)
 * ) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8
 * <p/>
 * INSERT INTO `customers` (`id`,`first_name`, `last_name`, `date`) VALUES (1,'Ricardo', 'Mariaca', CURRENT_DATE);
 * INSERT INTO `customers` (`id`,`first_name`, `last_name`, `date`) VALUES (2,'YONG', 'MOOK KIM', CURRENT_DATE);
 */
public class SimpleReportExample {

    public static void main(String[] args) {

        Connection connection = getConnection();

        ByteArrayOutputStream pdfByteStream = generatePDFByteSteam(connection);
        File file = new File("/home/nong/KATA/PDF/JavaDynamicReports/Bytenewfile.pdf");

        try {
            FileOutputStream fop = new FileOutputStream(file);

            if (!file.exists()) {
                file.createNewFile();
            }

            fop.write(pdfByteStream.toByteArray());
            fop.flush();
            fop.close();

            System.out.println("Done");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ByteArrayOutputStream generatePDFByteSteam(Connection connection) {
        JasperReportBuilder report = DynamicReports.report();
        report.columns(
                Columns.column("Customer Id", "id", DataTypes.integerType())
                        .setHorizontalAlignment(HorizontalAlignment.LEFT),
                Columns.column("First Name", "first_name", DataTypes.stringType()),
                Columns.column("Last Name", "last_name", DataTypes.stringType()),
                Columns.column("Date", "date", DataTypes.dateType())
                        .setHorizontalAlignment(HorizontalAlignment.LEFT)
        );

        report.title(
                Components.text("SimpleReportExample")
                        .setHorizontalAlignment(HorizontalAlignment.CENTER))
                .pageFooter(Components.pageXofY())//show page number on the page footer
                .setDataSource("SELECT id, first_name, last_name, date FROM customers", connection);

        ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();

        try {
			report.show();
            report.toPdf(outByteStream);
            report.getConnection().close();
            outByteStream.flush();
            return outByteStream;
        } catch (DRException e) {
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                System.out.println("+++++++++++++SQLException+++++++++++");
                e.printStackTrace();
            }
            IOUtils.closeQuietly(outByteStream);
        }
    }

    public static Connection getConnection() {
        Connection connection = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/ops", "root", "1q2w3e4r");
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        return connection;
    }
}

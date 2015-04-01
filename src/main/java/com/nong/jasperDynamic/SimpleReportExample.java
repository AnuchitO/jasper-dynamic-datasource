package com.nong.jasperDynamic;

import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.builder.DynamicReports;
import net.sf.dynamicreports.report.builder.column.Columns;
import net.sf.dynamicreports.report.builder.component.Components;
import net.sf.dynamicreports.report.builder.datatype.DataTypes;
import net.sf.dynamicreports.report.constant.HorizontalAlignment;
import net.sf.dynamicreports.report.exception.DRException;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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
 * 

CREATE TABLE `customers` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `first_name` varchar(50) DEFAULT NULL,
  `last_name` varchar(50) DEFAULT NULL,
  `date` date DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8
 
INSERT INTO `customers` (`id`,`first_name`, `last_name`, `date`) VALUES (1,'Ricardo', 'Mariaca', CURRENT_DATE);
INSERT INTO `customers` (`id`,`first_name`, `last_name`, `date`) VALUES (2,'YONG', 'MOOK KIM', CURRENT_DATE);

 */
public class SimpleReportExample {

	public static void main(String[] args) {
		Connection connection = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/ops","root", "1q2w3e4r");
		} catch (SQLException e) {
			e.printStackTrace();
			return;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return;
		}

		JasperReportBuilder report = DynamicReports.report();//a new report
		report
		  .columns(
		  	Columns.column("Customer Id", "id", DataTypes.integerType())
		  		.setHorizontalAlignment(HorizontalAlignment.LEFT),
		  	Columns.column("First Name", "first_name", DataTypes.stringType()),
		  	Columns.column("Last Name", "last_name", DataTypes.stringType()),
		  	Columns.column("Date", "date", DataTypes.dateType())
		  		.setHorizontalAlignment(HorizontalAlignment.LEFT)
		  	)
		  .title(//title of the report
		  	Components.text("SimpleReportExample")
		  		.setHorizontalAlignment(HorizontalAlignment.CENTER))
		  .pageFooter(Components.pageXofY())//show page number on the page footer
		  .setDataSource("SELECT id, first_name, last_name, date FROM customers", connection);

		try {
			report.show();
			report.toPdf(new FileOutputStream("./reportDynamic.pdf"));
		} catch (DRException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}

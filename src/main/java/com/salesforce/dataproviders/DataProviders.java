/**
 * 
 */
package com.salesforce.dataproviders;


import org.testng.annotations.DataProvider;

import com.salesforce.utils.ExcelLibrary;



public class DataProviders {
	

/*  VerifySuccessfulLogin.java -> verifyLogin
 */
	@DataProvider(name = "addspaces")
	public Object[][] getSpacesData() {
		
		String path = System.getProperty("user.dir") + "\\src\\test\\resources\\testdata\\SpacesDataMandatory.xlsx";
		ExcelLibrary obj = new ExcelLibrary(path);
		
		// Totals rows count
		int rows = obj.getRowCount("SpaceMandatory");
		// Total Columns
		int column = obj.getColumnCount("SpaceMandatory");
		int actRows = rows - 1;

		Object[][] data = new Object[actRows][column];

		for (int i = 0; i < actRows; i++) {
			for (int j = 0; j < column; j++) {
				data[i][j] = obj.getCellData("loginData", j, i + 2);
			}
		}
		return data;
	}
	
	
	
	@DataProvider(name = "add_spaces_types")
	public Object[][] getSpaces() {
		
		String path = System.getProperty("user.dir") + "\\src\\test\\resources\\testdata\\addSpaceTypes2.xlsx";
		ExcelLibrary obj = new ExcelLibrary(path);
		
		// Totals rows count
		int rows = obj.getRowCount("loginData");
		// Total Columns
		int column = obj.getColumnCount("loginData");
		int actRows = rows - 1;

		Object[][] data2 = new Object[actRows][column];

		for (int i = 0; i < actRows; i++) {
			for (int j = 0; j < column; j++) {
				data2[i][j] = obj.getCellData("loginData", j, i + 2);
			}
		
			System.out.println("testing");
		}
		return data2;
	}
	

}

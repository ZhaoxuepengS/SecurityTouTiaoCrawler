package cn.uniview.securityCrawler;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import components.Components;


public class readOSExcel {
	
	public ArrayList<Components> readOSComponentExcel(String path) throws FileNotFoundException, IOException{
			ArrayList<Components> components = new ArrayList();
			Workbook wb = new XSSFWorkbook(new FileInputStream(path));
			Sheet sheet = wb.getSheetAt(8);
			Components component = null;
			for (int i = 2; i < sheet.getPhysicalNumberOfRows(); i++) {
				component = new Components();
				Row row = sheet.getRow(i);
				String CodeVersion = row.getCell(0).getStringCellValue().toString();
				String Department = row.getCell(1).getStringCellValue().toString();
				String componentName = row.getCell(2).getStringCellValue().toString();
				String componentWebsite = row.getCell(3).toString();
				String componentVersion = row.getCell(4).toString();
				String componentLastestVersion = row.getCell(5).toString();
				component.setCodeVersion(CodeVersion);
				component.setDepartment(Department);
				component.setcomponentName(componentName);
				component.setcomponentVersion(componentVersion);
				component.setcomponentWebsite(componentWebsite);
				component.setcomponentLastestVersion(componentLastestVersion);
				components.add(component);	
			}
			return components;
	}
}
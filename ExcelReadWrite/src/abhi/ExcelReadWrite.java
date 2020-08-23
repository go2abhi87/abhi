package abhi;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

public class ExcelReadWrite {

	public static void main(String[] args) throws FileNotFoundException, IOException {

		File file = new File("D:\\test.xls");
		FileInputStream fis = new FileInputStream(file);
		HSSFWorkbook wb = new HSSFWorkbook(fis);
		// System.out.println(wb.getSheetAt(0).getSheetName());

		Sheet sheet = wb.getSheetAt(0);
		Iterator<Row> rowIt = sheet.iterator();
		rowIt.next();
		while (rowIt.hasNext()) {
			Row row = rowIt.next();
			Iterator<Cell> cellIt = row.iterator();
			while (cellIt.hasNext()) {
				System.out.print(cellIt.next() + ", ");
			}
			row.createCell(3).setCellValue("PASS");
			System.out.println();
		}

		fis.close();

		FileOutputStream fos = new FileOutputStream(file);
		wb.write(fos);
		fos.close();

		fis = new FileInputStream(file);
		HSSFWorkbook wb1 = new HSSFWorkbook(fis);
		Sheet sheet1 = wb1.getSheetAt(0);
		for (Row row : sheet1) {
			for (Cell cell : row) {
				System.out.print(cell + ", ");
			}
			System.out.println();
		}

		fis.close();

	}

}

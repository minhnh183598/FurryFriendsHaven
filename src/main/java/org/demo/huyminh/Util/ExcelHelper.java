package org.demo.huyminh.Util;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.demo.huyminh.Entity.Pet;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * @author Minh
 * Date: 10/21/2024
 * Time: 9:24 AM
 */

@Slf4j
public class ExcelHelper {

    public static String TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
    static String[] HEADERs = {"pet_weight", "pet_age", "pet_breed", "pet_color", "pet_description", "pet_gender", "pet_name", "pet_size", "pet_status", "pet_type", "pet_vaccin"};
    static String SHEET = "Sheet1";

    public static boolean hasExcelFormat(MultipartFile file) {
        if (!TYPE.equals(file.getContentType())) {
            return false;
        }

        return true;
    }

    public static List<Pet> excelToPet(InputStream is) {
        try {
            Workbook workbook = new XSSFWorkbook(is);

            log.info("FileName: {}", workbook.getSheetName(0));
            Sheet sheet = workbook.getSheet(SHEET);
            if (sheet == null) {
                throw new RuntimeException("Sheet with name '" + SHEET + "' not found in the Excel file.");
            }

            Iterator<Row> rows = sheet.iterator();

            List<Pet> pets = new ArrayList<>();
            Set<String> duplicatedPetNames = new HashSet<>();

            int rowNumber = 0;
            while (rows.hasNext()) {
                Row currentRow = rows.next();

                if (rowNumber == 0 || currentRow == null) {
                    rowNumber++;
                    continue;
                }

                Iterator<Cell> cellsInRow = currentRow.iterator();
                Pet pet = new Pet();

                int cellIdx = 0;
                while (cellsInRow.hasNext()) {
                    Cell currentCell = cellsInRow.next();

                    switch (cellIdx) {
                        case 0:
                            pet.setPetWeight((int) currentCell.getNumericCellValue());
                            break;

                        case 1:
                            pet.setPetAge(currentCell.getStringCellValue());
                            break;

                        case 2:
                            pet.setPetBreed(currentCell.getStringCellValue());
                            break;

                        case 3:
                            pet.setPetColor(currentCell.getStringCellValue());
                            break;

                        case 4:
                            pet.setPetDescription(currentCell.getStringCellValue());
                            break;

                        case 5:
                            pet.setPetGender(currentCell.getStringCellValue());
                            break;

                        case 6:
                            pet.setPetName(currentCell.getStringCellValue());
                            break;

                        case 7:
                            pet.setPetSize(String.valueOf(currentCell.getNumericCellValue()));
                            break;

                        case 8:
                            pet.setPetStatus(currentCell.getStringCellValue());
                            break;

                        case 9:
                            pet.setPetType(currentCell.getStringCellValue());
                            break;

                        case 10:
                            pet.setPetVaccin(currentCell.getStringCellValue());
                            break;

                        default:
                            break;
                    }

                    cellIdx++;
                }

                if (!duplicatedPetNames.contains(pet.getPetName())) {
                    pets.add(pet);
                    duplicatedPetNames.add(pet.getPetName());
                }
            }

            workbook.close();

            return pets;
        } catch (IOException e) {
            throw new RuntimeException("fail to parse Excel file: " + e.getMessage());
        }
    }
}

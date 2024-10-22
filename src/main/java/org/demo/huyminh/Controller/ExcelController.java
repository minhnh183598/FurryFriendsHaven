package org.demo.huyminh.Controller;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.demo.huyminh.DTO.Reponse.ApiResponse;
import org.demo.huyminh.DTO.Reponse.ExcelResponse;
import org.demo.huyminh.Service.ExcelService;
import org.demo.huyminh.Util.ExcelHelper;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Minh
 * Date: 10/21/2024
 * Time: 9:48 AM
 */

@RestController
@RequestMapping("/api/v1/excel")
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class ExcelController {

    ExcelService excelService;

    @PostMapping("/upload")
    public ApiResponse<ExcelResponse> upload(@RequestParam("file") MultipartFile file) {
        String message = "";

        if (ExcelHelper.hasExcelFormat(file)) {
            try {
                log.info("Upload endpoint in ExcelController 🗿");
                excelService.save(file);

                message = "Uploaded the file successfully: " + file.getOriginalFilename();
                return ApiResponse.<ExcelResponse>builder()
                        .code(HttpStatus.OK.value())
                        .message(message)
                        .build();
            } catch (Exception e) {
                message = e.getMessage();
                return ApiResponse.<ExcelResponse>builder()
                        .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .message(message)
                        .build();
            }
        }

        message = "Please upload an excel file!";
        return ApiResponse.<ExcelResponse>builder()
                .code(HttpStatus.BAD_REQUEST.value())
                .message(message)
                .build();
    }
}

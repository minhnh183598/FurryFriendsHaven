package org.demo.huyminh.Service;

import org.springframework.web.multipart.MultipartFile;

/**
 * @author Minh
 * Date: 10/26/2024
 * Time: 9:48 PM
 */

public interface ExcelService {
    void save(MultipartFile file);
}

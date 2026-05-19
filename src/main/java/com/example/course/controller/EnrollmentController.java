package com.example.course.controller;

import com.example.course.entity.EnrollRecord;
import com.example.course.service.EnrollmentService;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/enrollments")
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    public EnrollmentController(EnrollmentService enrollmentService) {
        this.enrollmentService = enrollmentService;
    }

    @GetMapping("/samples")
    public Map<String, List<EnrollRecord>> samples() {
        return enrollmentService.classifyByCourseType(enrollmentService.getSamples());
    }

    @PostMapping("/import")
    public Map<String, List<EnrollRecord>> importCsv(@RequestBody String csvText) {
        return enrollmentService.classifyByCourseType(enrollmentService.importCsv(csvText));
    }

    @PostMapping("/import-file")
    public ResponseEntity<?> importCsvFile(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("文件为空");
        }
        String fileName = file.getOriginalFilename();
        if (fileName == null || !fileName.toLowerCase().endsWith(".csv")) {
            return ResponseEntity.badRequest().body("请上传CSV文件");
        }
        try {
            return ResponseEntity.ok(enrollmentService.classifyByCourseType(enrollmentService.importCsvFile(file)));
        } catch (IOException exception) {
            return ResponseEntity.badRequest().body("读取CSV文件失败");
        }
    }

    @GetMapping("/search")
    public SearchResult search(@RequestParam(required = false, defaultValue = "") String keyword) {
        List<EnrollRecord> result = enrollmentService.search(keyword);
        String message = result.isEmpty() ? "无匹配选课记录" : "";
        return new SearchResult(message, enrollmentService.classifyByCourseType(result));
    }

    public record SearchResult(String message, Map<String, List<EnrollRecord>> data) {
    }
}

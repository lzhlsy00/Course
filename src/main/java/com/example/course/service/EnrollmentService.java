package com.example.course.service;

import com.example.course.entity.EnrollRecord;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class EnrollmentService {

    private final List<EnrollRecord> records = new ArrayList<>();

    public EnrollmentService() {
        records.addAll(processRecords(List.of(
                new EnrollRecord("S000001", "C000001", "Java程序设计", "专业课"),
                new EnrollRecord("S000002", "C000003", "计算机网络", "公共课"),
                new EnrollRecord("S000003", "C000002", "大学英语", "公共课"),
                new EnrollRecord("S000004", "C000004", "音乐鉴赏", "选修课")
        )));
    }

    public synchronized List<EnrollRecord> getSamples() {
        return List.copyOf(records);
    }

    public synchronized List<EnrollRecord> importCsv(String csvText) {
        List<EnrollRecord> importedRecords = parseCsv(csvText);
        records.addAll(importedRecords);
        List<EnrollRecord> processedRecords = processRecords(records);
        records.clear();
        records.addAll(processedRecords);
        return List.copyOf(records);
    }

    public List<EnrollRecord> importCsvFile(MultipartFile file) throws IOException {
        String csvText = new String(file.getBytes(), StandardCharsets.UTF_8);
        return importCsv(csvText);
    }

    public synchronized List<EnrollRecord> search(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return List.copyOf(records);
        }
        String lowerKeyword = keyword.trim().toLowerCase(Locale.ROOT);
        return records.stream()
                .filter(record -> contains(record.getStudentId(), lowerKeyword)
                        || contains(record.getCourseId(), lowerKeyword)
                        || contains(record.getCourseName(), lowerKeyword)
                        || contains(record.getCourseType(), lowerKeyword))
                .toList();
    }

    public Map<String, List<EnrollRecord>> classifyByCourseType(List<EnrollRecord> source) {
        Map<String, List<EnrollRecord>> result = new LinkedHashMap<>();
        result.put("公共课", new ArrayList<>());
        result.put("专业课", new ArrayList<>());
        result.put("选修课", new ArrayList<>());
        for (EnrollRecord record : source) {
            result.computeIfAbsent(record.getCourseType(), key -> new ArrayList<>()).add(record);
        }
        return result;
    }

    private List<EnrollRecord> parseCsv(String csvText) {
        if (csvText == null || csvText.isBlank()) {
            return List.of();
        }

        List<EnrollRecord> parsedRecords = new ArrayList<>();
        String[] lines = csvText.split("\\R");
        for (String line : lines) {
            if (line == null || line.isBlank()) {
                continue;
            }
            String[] columns = line.split(",", -1);
            if (columns.length < 3) {
                continue;
            }
            String studentId = columns[0].trim();
            String courseId = columns[1].trim();
            String courseName = columns[2].trim();
            String courseType = columns.length >= 4 ? columns[3].trim() : "";
            parsedRecords.add(new EnrollRecord(studentId, courseId, courseName, normalizeCourseType(courseType, courseName)));
        }
        return parsedRecords;
    }

    private List<EnrollRecord> processRecords(List<EnrollRecord> source) {
        Map<String, EnrollRecord> uniqueRecords = new LinkedHashMap<>();
        for (EnrollRecord record : source) {
            String key = record.getStudentId() + "#" + record.getCourseId();
            uniqueRecords.putIfAbsent(key, record);
        }

        List<EnrollRecord> result = new ArrayList<>(uniqueRecords.values());
        result.sort(Comparator.comparing(EnrollRecord::getStudentId)
                .thenComparing(EnrollRecord::getCourseId));
        return result;
    }

    private String normalizeCourseType(String courseType, String courseName) {
        if ("公共课".equals(courseType) || "专业课".equals(courseType) || "选修课".equals(courseType)) {
            return courseType;
        }
        return autoDetectCourseType(courseName);
    }

    private String autoDetectCourseType(String courseName) {
        if (courseName == null) {
            return "选修课";
        }
        if (courseName.contains("英语") || courseName.contains("体育") || courseName.contains("数学")) {
            return "公共课";
        }
        if (courseName.contains("Java") || courseName.contains("计算机") || courseName.contains("网络")
                || courseName.contains("数据库")) {
            return "专业课";
        }
        return "选修课";
    }

    private boolean contains(String value, String lowerKeyword) {
        return value != null && value.toLowerCase(Locale.ROOT).contains(lowerKeyword);
    }
}

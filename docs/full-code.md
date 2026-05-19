# AI 生成完整代码汇总

本文档汇总本项目中 AI 生成和协助修改的完整代码文件。源码文件仍以项目目录中的实际文件为准。

## pom.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>3.3.5</version>
        <relativePath/>
    </parent>

    <groupId>com.example</groupId>
    <artifactId>course</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <name>course</name>
    <description>高校选课管理系统 - 学生选课基础处理工具</description>

    <properties>
        <java.version>17</java.version>
    </properties>

    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>
</project>
```

## CourseApplication.java

```java
package com.example.course;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CourseApplication {

    public static void main(String[] args) {
        SpringApplication.run(CourseApplication.class, args);
    }
}
```

## EnrollRecord.java

```java
package com.example.course.entity;

/**
 * 选课记录实体类。
 */
public class EnrollRecord {

    /**
     * 学生ID，格式：S+6位数字。
     */
    private String studentId;

    /**
     * 课程ID，格式：C+6位数字。
     */
    private String courseId;

    /**
     * 课程名称。
     */
    private String courseName;

    /**
     * 课程类型：公共课、专业课、选修课。
     */
    private String courseType;

    public EnrollRecord() {
    }

    public EnrollRecord(String studentId, String courseId, String courseName) {
        this.studentId = studentId;
        this.courseId = courseId;
        this.courseName = courseName;
    }

    public EnrollRecord(String studentId, String courseId, String courseName, String courseType) {
        this.studentId = studentId;
        this.courseId = courseId;
        this.courseName = courseName;
        this.courseType = courseType;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCourseType() {
        return courseType;
    }

    public void setCourseType(String courseType) {
        this.courseType = courseType;
    }

    /**
     * 重写toString方法，满足输出格式要求。
     */
    @Override
    public String toString() {
        return String.format("学生ID：%s，课程ID：%s，课程名称：%s", studentId, courseId, courseName);
    }
}
```

## EnrollRecordProcessor.java

```java
package com.example.course.service;

import com.example.course.entity.EnrollRecord;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 第一题：学生选课基础处理工具。
 */
public class EnrollRecordProcessor {

    public List<EnrollRecord> process(List<EnrollRecord> records) {
        Map<String, EnrollRecord> uniqueRecords = new LinkedHashMap<>();
        for (EnrollRecord record : records) {
            String key = record.getStudentId() + "#" + record.getCourseId();
            uniqueRecords.putIfAbsent(key, record);
        }

        List<EnrollRecord> result = new ArrayList<>(uniqueRecords.values());
        result.sort(Comparator.comparing(EnrollRecord::getStudentId)
                .thenComparing(EnrollRecord::getCourseId));

        for (EnrollRecord record : result) {
            System.out.println(record);
        }
        return result;
    }
}
```

## EnrollmentService.java

```java
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
```

## EnrollmentController.java

```java
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
```

## application.properties

```properties
spring.application.name=course
server.port=8080
```

## index.html

```html
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>高校选课管理系统</title>
    <style>
        :root {
            --primary: #1d4ed8;
            --primary-dark: #1e40af;
            --primary-light: #dbeafe;
            --success: #0f766e;
            --danger: #dc2626;
            --text-main: #172033;
            --text-muted: #64748b;
            --border: #d8e0ec;
            --bg-page: #eef3f9;
            --bg-card: #ffffff;
            --shadow: 0 10px 28px rgba(15, 23, 42, 0.08);
        }

        * {
            box-sizing: border-box;
        }

        body {
            margin: 0;
            font-family: "Microsoft YaHei", sans-serif;
            background: var(--bg-page);
            color: var(--text-main);
        }

        .topbar {
            height: 64px;
            background: linear-gradient(90deg, #123c7c, #1d4ed8);
            color: #ffffff;
            display: flex;
            align-items: center;
            justify-content: space-between;
            padding: 0 32px;
            box-shadow: 0 4px 18px rgba(15, 23, 42, 0.18);
        }

        .brand {
            display: flex;
            align-items: center;
            gap: 12px;
            font-weight: 700;
            font-size: 18px;
        }

        .brand-mark {
            width: 34px;
            height: 34px;
            border-radius: 10px;
            background: rgba(255, 255, 255, 0.18);
            display: grid;
            place-items: center;
            border: 1px solid rgba(255, 255, 255, 0.28);
        }

        .topbar-subtitle {
            font-size: 13px;
            opacity: 0.86;
        }

        main {
            max-width: 1180px;
            margin: 28px auto 40px;
            padding: 0 24px;
        }

        .hero {
            background: linear-gradient(135deg, #ffffff, #f7fbff);
            border: 1px solid var(--border);
            border-radius: 16px;
            padding: 24px 28px;
            box-shadow: var(--shadow);
            display: flex;
            align-items: center;
            justify-content: space-between;
            gap: 20px;
        }

        .hero h1 {
            margin: 0 0 8px;
            font-size: 26px;
            color: #0f2f63;
        }

        .hero p {
            margin: 0;
            color: var(--text-muted);
            line-height: 1.7;
        }

        .hero-tags {
            display: flex;
            flex-wrap: wrap;
            justify-content: flex-end;
            gap: 8px;
            min-width: 260px;
        }

        .tag {
            display: inline-flex;
            align-items: center;
            border-radius: 999px;
            background: var(--primary-light);
            color: var(--primary-dark);
            padding: 6px 12px;
            font-size: 13px;
            font-weight: 600;
        }

        .content-stack {
            margin-top: 18px;
        }

        .panel {
            background: var(--bg-card);
            border: 1px solid var(--border);
            border-radius: 14px;
            padding: 22px;
            box-shadow: var(--shadow);
        }

        .panel + .panel {
            margin-top: 18px;
        }

        .panel-header {
            display: flex;
            align-items: flex-start;
            justify-content: space-between;
            gap: 12px;
            margin-bottom: 16px;
        }

        .panel h2 {
            margin: 0;
            font-size: 18px;
            color: #0f2f63;
        }

        .panel-desc {
            margin: 6px 0 0;
            color: var(--text-muted);
            font-size: 13px;
            line-height: 1.6;
        }

        textarea {
            width: 100%;
            min-height: 140px;
            border: 1px solid var(--border);
            border-radius: 10px;
            padding: 12px;
            font-size: 14px;
            resize: vertical;
            outline: none;
            background: #fbfdff;
            color: var(--text-main);
            transition: border-color 0.2s, box-shadow 0.2s;
        }

        textarea:focus,
        input:focus {
            border-color: var(--primary);
            box-shadow: 0 0 0 3px rgba(29, 78, 216, 0.12);
        }

        input {
            width: 100%;
            border: 1px solid var(--border);
            border-radius: 10px;
            padding: 11px 12px;
            font-size: 14px;
            outline: none;
            background: #fbfdff;
        }

        button {
            border: none;
            border-radius: 10px;
            background: var(--primary);
            color: #ffffff;
            padding: 11px 16px;
            cursor: pointer;
            font-weight: 600;
            transition: background 0.2s, transform 0.1s, box-shadow 0.2s;
            box-shadow: 0 6px 14px rgba(29, 78, 216, 0.18);
        }

        button:hover {
            background: var(--primary-dark);
        }

        button:active {
            transform: translateY(1px);
        }

        .btn-secondary {
            background: #ffffff;
            color: var(--primary-dark);
            border: 1px solid #b8c8e6;
            box-shadow: none;
        }

        .btn-secondary:hover {
            background: #f1f6ff;
        }

        .action-row {
            display: flex;
            flex-wrap: wrap;
            gap: 10px;
            margin-top: 12px;
        }

        .search-row {
            display: grid;
            grid-template-columns: 1fr auto auto;
            gap: 10px;
            align-items: center;
        }

        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 12px;
            background: #ffffff;
            overflow: hidden;
            border-radius: 10px;
        }

        th, td {
            border-bottom: 1px solid #e5ebf3;
            padding: 12px 14px;
            text-align: left;
            font-size: 14px;
        }

        th {
            background: #f1f5fb;
            color: #334155;
            font-weight: 700;
        }

        tr:hover td {
            background: #f8fbff;
        }

        td:first-child,
        td:nth-child(2) {
            font-family: Consolas, "Microsoft YaHei", sans-serif;
        }

        .message {
            margin-top: 12px;
            color: var(--danger);
            font-weight: 600;
        }

        .type-title {
            display: flex;
            align-items: center;
            justify-content: space-between;
            margin: 22px 0 0;
            color: #0f2f63;
            font-size: 16px;
        }

        .count-badge {
            display: inline-flex;
            align-items: center;
            justify-content: center;
            min-width: 32px;
            height: 24px;
            border-radius: 999px;
            background: #e0f2fe;
            color: #0369a1;
            font-size: 12px;
            padding: 0 9px;
        }

        .tabs {
            display: flex;
            flex-wrap: wrap;
            gap: 10px;
            margin: 18px 0 14px;
            border-bottom: 1px solid var(--border);
            padding-bottom: 10px;
        }

        .tab {
            background: #ffffff;
            color: var(--text-muted);
            border: 1px solid var(--border);
            box-shadow: none;
            padding: 9px 14px;
        }

        .tab.active {
            background: var(--primary);
            border-color: var(--primary);
            color: #ffffff;
            box-shadow: 0 6px 14px rgba(29, 78, 216, 0.18);
        }

        .file-row {
            display: flex;
            flex-wrap: wrap;
            align-items: center;
            gap: 10px;
            margin-top: 12px;
        }

        .drop-zone {
            margin-top: 14px;
            border: 2px dashed #b7c7de;
            border-radius: 12px;
            background: linear-gradient(135deg, #f8fbff, #f1f7ff);
            padding: 28px 18px;
            text-align: center;
            color: var(--text-muted);
            font-weight: 600;
            transition: border-color 0.2s, background 0.2s, color 0.2s;
        }

        .drop-zone.drag-over {
            border-color: var(--primary);
            background: var(--primary-light);
            color: var(--primary-dark);
        }

        .record-panel {
            margin-top: 18px;
        }

        .empty-state {
            padding: 34px;
            border: 1px dashed var(--border);
            border-radius: 12px;
            text-align: center;
            color: var(--text-muted);
            background: #fbfdff;
        }

        .pagination {
            display: flex;
            align-items: center;
            justify-content: flex-end;
            gap: 10px;
            margin-top: 18px;
            color: var(--text-muted);
            font-size: 14px;
        }

        .pagination button {
            box-shadow: none;
            padding: 8px 12px;
        }

        .pagination button:disabled {
            background: #e2e8f0;
            color: #94a3b8;
            cursor: not-allowed;
        }

        @media (max-width: 860px) {
            .topbar {
                height: auto;
                align-items: flex-start;
                flex-direction: column;
                gap: 4px;
                padding: 16px 20px;
            }

            main {
                padding: 0 14px;
                margin-top: 18px;
            }

            .hero {
                display: block;
            }

            .hero-tags {
                justify-content: flex-start;
                margin-top: 16px;
                min-width: 0;
            }

            .panel {
                margin-top: 16px;
                padding: 18px;
            }

            .search-row {
                grid-template-columns: 1fr;
            }

            .pagination {
                justify-content: center;
                flex-wrap: wrap;
            }
        }
    </style>
</head>
<body>
<header class="topbar">
    <div class="brand">
        <div class="brand-mark">选</div>
        <span>高校选课管理系统</span>
    </div>
    <div class="topbar-subtitle">学生选课基础处理工具</div>
</header>
<main>
    <section class="hero">
        <div>
            <h1>选课数据处理工作台</h1>
            <p>支持 CSV 批量导入、自动去重、排序、课程分类展示和关键词检索。</p>
        </div>
        <div class="hero-tags">
            <span class="tag">CSV 批量导入</span>
            <span class="tag">自动去重排序</span>
            <span class="tag">分类检索</span>
        </div>
    </section>

    <div class="content-stack">
    <section class="panel">
        <div class="panel-header">
            <div>
                <h2>数据批量导入</h2>
                <p class="panel-desc">每行一条数据，格式：学生ID,课程ID,课程名称,课程类型</p>
            </div>
        </div>
        <textarea id="csvInput">S000001,C000001,Java程序设计,专业课
S000002,C000003,计算机网络,公共课
S000003,C000002,大学英语,公共课</textarea>
        <div class="action-row">
            <button onclick="importCsv()">导入文本框数据</button>
            <input id="fileInput" type="file" accept=".csv,text/csv" hidden onchange="importSelectedFile()">
            <button class="btn-secondary" onclick="document.getElementById('fileInput').click()">选择 CSV 文件导入</button>
        </div>
        <div id="dropZone" class="drop-zone">
            将 CSV 文件拖拽到这里导入
        </div>
    </section>

    <section class="panel record-panel">
        <div class="panel-header">
            <div>
                <h2>数据展示</h2>
                <p class="panel-desc">数据经后端去重、排序、分类处理后展示。</p>
            </div>
        </div>
        <div class="search-row">
            <input id="keywordInput" placeholder="输入学生ID、课程ID、课程名称或课程类型">
            <button onclick="searchRecords()">检索</button>
            <button class="btn-secondary" onclick="loadSamples()">样例数据</button>
        </div>
        <div id="message" class="message"></div>
        <div id="tabContainer" class="tabs"></div>
        <div id="recordContainer"></div>
        <div id="paginationContainer"></div>
    </section>
    </div>
</main>

<script>
    const courseTypes = ["公共课", "专业课", "选修课"];
    const pageSize = 10;
    let currentGroupedRecords = {};
    let activeCourseType = "公共课";
    let currentPage = 1;

    async function loadSamples() {
        const response = await fetch("/api/enrollments/samples");
        const data = await response.json();
        showMessage("");
        setRecords(data);
    }

    async function importCsv() {
        const csvText = document.getElementById("csvInput").value;
        const response = await fetch("/api/enrollments/import", {
            method: "POST",
            headers: {"Content-Type": "text/plain;charset=UTF-8"},
            body: csvText
        });
        const data = await response.json();
        showMessage("");
        setRecords(data);
    }

    async function importSelectedFile() {
        const fileInput = document.getElementById("fileInput");
        const file = fileInput.files[0];
        if (!file) {
            return;
        }
        await uploadCsvFile(file);
        fileInput.value = "";
    }

    async function uploadCsvFile(file) {
        if (!file.name.toLowerCase().endsWith(".csv")) {
            alert("导入失败：请选择 CSV 文件");
            return;
        }

        const confirmed = confirm(`确认导入 CSV 文件：${file.name}？`);
        if (!confirmed) {
            return;
        }

        const formData = new FormData();
        formData.append("file", file);

        try {
            const response = await fetch("/api/enrollments/import-file", {
                method: "POST",
                body: formData
            });
            if (!response.ok) {
                const reason = await response.text();
                throw new Error(reason || "服务器处理失败");
            }
            const data = await response.json();
            setRecords(data);
            alert("CSV 文件导入成功");
        } catch (error) {
            alert(`CSV 文件导入失败：${error.message}`);
        }
    }

    async function searchRecords() {
        const keyword = encodeURIComponent(document.getElementById("keywordInput").value);
        const response = await fetch(`/api/enrollments/search?keyword=${keyword}`);
        const result = await response.json();
        showMessage(result.message || "");
        setRecords(result.data);
    }

    function setRecords(groupedRecords) {
        currentGroupedRecords = groupedRecords || {};
        if (!courseTypes.includes(activeCourseType)) {
            activeCourseType = "公共课";
        }
        currentPage = 1;
        renderRecords();
    }

    function renderRecords() {
        const container = document.getElementById("recordContainer");
        const paginationContainer = document.getElementById("paginationContainer");
        container.innerHTML = "";
        paginationContainer.innerHTML = "";
        renderTabs();

        const allRecords = currentGroupedRecords[activeCourseType] || [];
        if (allRecords.length === 0) {
            container.innerHTML = `<div class="empty-state">暂无${activeCourseType}选课记录</div>`;
            return;
        }

        const totalPages = Math.ceil(allRecords.length / pageSize);
        currentPage = Math.min(Math.max(currentPage, 1), totalPages);

        const startIndex = (currentPage - 1) * pageSize;
        const pageRecords = allRecords.slice(startIndex, startIndex + pageSize);
        const title = document.createElement("h3");
        title.className = "type-title";
        title.innerHTML = `<span>${activeCourseType}</span><span class="count-badge">共 ${allRecords.length} 条</span>`;
        container.appendChild(title);
        container.appendChild(createTable(pageRecords));

        if (totalPages > 1) {
            renderPagination(allRecords.length, totalPages);
        }
    }

    function renderTabs() {
        const tabContainer = document.getElementById("tabContainer");
        tabContainer.innerHTML = "";
        courseTypes.forEach(type => {
            const records = currentGroupedRecords[type] || [];
            const button = document.createElement("button");
            button.className = `tab ${type === activeCourseType ? "active" : ""}`;
            button.textContent = `${type}（${records.length}）`;
            button.onclick = () => switchCourseType(type);
            tabContainer.appendChild(button);
        });
    }

    function switchCourseType(type) {
        activeCourseType = type;
        currentPage = 1;
        renderRecords();
    }

    function renderPagination(totalCount, totalPages) {
        const paginationContainer = document.getElementById("paginationContainer");
        paginationContainer.innerHTML = `
            <div class="pagination">
                <span>共 ${totalCount} 条，每页 ${pageSize} 条，第 ${currentPage} / ${totalPages} 页</span>
                <button class="btn-secondary" onclick="changePage(${currentPage - 1})" ${currentPage === 1 ? "disabled" : ""}>上一页</button>
                <button class="btn-secondary" onclick="changePage(${currentPage + 1})" ${currentPage === totalPages ? "disabled" : ""}>下一页</button>
            </div>
        `;
    }

    function changePage(page) {
        currentPage = page;
        renderRecords();
    }

    function createTable(records) {
        const table = document.createElement("table");
        table.innerHTML = `
            <thead>
            <tr>
                <th>学生ID</th>
                <th>课程ID</th>
                <th>课程名称</th>
                <th>课程类型</th>
            </tr>
            </thead>
            <tbody></tbody>
        `;

        const tbody = table.querySelector("tbody");
        records.forEach(record => {
            const tr = document.createElement("tr");
            tr.innerHTML = `
                <td>${escapeHtml(record.studentId)}</td>
                <td>${escapeHtml(record.courseId)}</td>
                <td>${escapeHtml(record.courseName)}</td>
                <td>${escapeHtml(record.courseType)}</td>
            `;
            tbody.appendChild(tr);
        });
        return table;
    }

    function showMessage(message) {
        document.getElementById("message").textContent = message;
    }

    function initDropZone() {
        const dropZone = document.getElementById("dropZone");

        dropZone.addEventListener("dragover", event => {
            event.preventDefault();
            dropZone.classList.add("drag-over");
        });

        dropZone.addEventListener("dragleave", () => {
            dropZone.classList.remove("drag-over");
        });

        dropZone.addEventListener("drop", async event => {
            event.preventDefault();
            dropZone.classList.remove("drag-over");
            const file = event.dataTransfer.files[0];
            if (!file) {
                return;
            }
            await uploadCsvFile(file);
        });
    }

    function escapeHtml(value) {
        return String(value || "")
            .replaceAll("&", "&amp;")
            .replaceAll("<", "&lt;")
            .replaceAll(">", "&gt;")
            .replaceAll('"', "&quot;")
            .replaceAll("'", "&#039;");
    }

    initDropZone();
    loadSamples();
</script>
</body>
</html>

```

## 代码来源说明

- AI 生成：SpringBoot 项目结构、Maven 配置、启动类、实体类、Service、Controller、前端页面、SQL 和分析设计文档。
- 自己修改优化：CSV 文件按钮导入、拖拽导入、导入前确认弹窗、导入成功/失败弹窗、信息管理系统风格 UI、检索区位置调整、分类 Tab 展示、每页 10 条分页、测试 CSV 文件、README 与文档补充。
- 修改原因：适配选课管理场景，完善前后端衔接，优化页面交互，避免不同课程类型分页混杂，满足题目对 AI 提示词、完整代码、分析设计和批量导入测试的交付要求。





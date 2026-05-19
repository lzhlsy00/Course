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
        body {
            margin: 0;
            font-family: "Microsoft YaHei", sans-serif;
            background: #f5f7fb;
            color: #1f2937;
        }

        main {
            max-width: 1080px;
            margin: 32px auto;
            padding: 0 20px;
        }

        h1 {
            margin-bottom: 8px;
            color: #12355b;
        }

        .panel {
            background: #ffffff;
            border: 1px solid #d9e2ec;
            border-radius: 8px;
            padding: 20px;
            margin-top: 18px;
            box-shadow: 0 4px 14px rgba(18, 53, 91, 0.08);
        }

        textarea {
            width: 100%;
            min-height: 140px;
            box-sizing: border-box;
            border: 1px solid #bcccdc;
            border-radius: 6px;
            padding: 12px;
            font-size: 14px;
            resize: vertical;
        }

        input {
            min-width: 260px;
            border: 1px solid #bcccdc;
            border-radius: 6px;
            padding: 10px;
            font-size: 14px;
        }

        button {
            margin-top: 10px;
            margin-right: 8px;
            border: none;
            border-radius: 6px;
            background: #0f6cbf;
            color: #ffffff;
            padding: 10px 18px;
            cursor: pointer;
        }

        button:hover {
            background: #0a5596;
        }

        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 10px;
            background: #ffffff;
        }

        th, td {
            border: 1px solid #d9e2ec;
            padding: 10px;
            text-align: left;
        }

        th {
            background: #e7f0fa;
        }

        .message {
            margin-top: 12px;
            color: #b42318;
        }

        .type-title {
            margin-top: 20px;
            color: #12355b;
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
            border: 2px dashed #9fb3c8;
            border-radius: 8px;
            background: #f8fbff;
            padding: 24px;
            text-align: center;
            color: #52606d;
        }

        .drop-zone.drag-over {
            border-color: #0f6cbf;
            background: #e7f0fa;
            color: #12355b;
        }
    </style>
</head>
<body>
<main>
    <h1>高校选课管理系统</h1>
    <p>支持 CSV 批量导入、去重、排序、分类展示和关键词检索。</p>

    <section class="panel">
        <h2>数据批量导入</h2>
        <p>每行一条数据，格式：学生ID,课程ID,课程名称,课程类型</p>
        <textarea id="csvInput">S000001,C000001,Java程序设计,专业课
S000002,C000003,计算机网络,公共课
S000003,C000002,大学英语,公共课</textarea>
        <br>
        <button onclick="importCsv()">导入文本框数据</button>
        <div class="file-row">
            <input id="fileInput" type="file" accept=".csv,text/csv" hidden onchange="importSelectedFile()">
            <button onclick="document.getElementById('fileInput').click()">选择 CSV 文件导入</button>
        </div>
        <div id="dropZone" class="drop-zone">
            将 CSV 文件拖拽到这里导入
        </div>
    </section>

    <section class="panel">
        <h2>选课检索</h2>
        <input id="keywordInput" placeholder="输入学生ID、课程ID、课程名称或课程类型">
        <button onclick="searchRecords()">检索</button>
        <button onclick="loadSamples()">显示样例数据</button>
        <div id="message" class="message"></div>
    </section>

    <section class="panel">
        <h2>数据展示</h2>
        <div id="recordContainer"></div>
    </section>
</main>

<script>
    const courseTypes = ["公共课", "专业课", "选修课"];

    async function loadSamples() {
        const response = await fetch("/api/enrollments/samples");
        const data = await response.json();
        showMessage("");
        renderRecords(data);
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
        renderRecords(data);
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
            renderRecords(data);
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
        renderRecords(result.data);
    }

    function renderRecords(groupedRecords) {
        const container = document.getElementById("recordContainer");
        container.innerHTML = "";

        courseTypes.forEach(type => {
            const records = groupedRecords[type] || [];
            if (records.length === 0) {
                return;
            }

            const title = document.createElement("h3");
            title.className = "type-title";
            title.textContent = type;
            container.appendChild(title);
            container.appendChild(createTable(records));
        });
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
- 自己修改优化：CSV 文件按钮导入、拖拽导入、导入前确认弹窗、导入成功/失败弹窗、测试 CSV 文件、README 与文档补充。
- 修改原因：适配选课管理场景，完善前后端衔接，优化页面交互，满足题目对 AI 提示词、完整代码、分析设计和批量导入测试的交付要求。

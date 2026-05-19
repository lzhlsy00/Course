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

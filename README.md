# 高校选课管理系统 - 学生选课基础处理工具

## 题目要求提交内容位置

- Java 基础处理工具代码：`src/main/java/com/example/course/entity/EnrollRecord.java`、`src/main/java/com/example/course/service/EnrollRecordProcessor.java`
- SQL 编程题答案：`docs/sql.md`
- SpringBoot 后端完整代码：`src/main/java/com/example/course/`
- 前端页面完整代码：`src/main/resources/static/index.html`
- AI 编程工具名称、提示词、AI 完成任务说明：`docs/ai-prompt-and-code-notes.md`
- AI 生成完整代码汇总：`docs/full-code.md`
- 分析及设计文档：`docs/analysis-design.md`
- CSV 测试文件：`test-data/enrollments-test.csv`

本项目根据 `request.md` 完成以下内容：

1. Java 基础处理工具：选课记录去重、排序、格式化输出。
2. SQL 编程题：两条课程选课人数统计 SQL。
3. SpringBoot 3.x 编程实战：CSV 批量导入、分类、检索、前后端衔接和页面展示。
4. AI 编程工具说明：包含工具名称、完整提示词、AI 生成部分和自己修改优化部分。
5. 分析及设计：核心数据模型、ER 图、并发风险和索引设计。

## 技术栈

- Java 17
- SpringBoot 3.x
- Maven
- HTML
- CSS
- JavaScript
- MySQL 8.x SQL
- Mermaid ER 图

## 项目结构

```text
src/main/java/com/example/course
├─ CourseApplication.java
├─ controller/EnrollmentController.java
├─ entity/EnrollRecord.java
└─ service/
   ├─ EnrollRecordProcessor.java
   └─ EnrollmentService.java

src/main/resources
├─ application.properties
└─ static/index.html

docs
├─ sql.md
├─ ai-prompt-and-code-notes.md
├─ full-code.md
└─ analysis-design.md
```

## 运行方式

```bash
mvn spring-boot:run
```

启动后访问：

```text
http://localhost:8080/index.html
```

## 文档入口

- SQL 编程题：[docs/sql.md](docs/sql.md)
- AI 编程工具说明：[docs/ai-prompt-and-code-notes.md](docs/ai-prompt-and-code-notes.md)
- AI 生成完整代码汇总：[docs/full-code.md](docs/full-code.md)
- 分析及设计：[docs/analysis-design.md](docs/analysis-design.md)

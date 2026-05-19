# AI 编程工具说明

## 所用 AI 编程工具

ChatGPT。

## 我和 AI 的实际对话提示词

以下内容按本次完成作业过程中的真实对话顺序整理。

### 提示词 1

```text
检查request.md中的需求内容，给出你的执行方案，要求完全符合题目要求，不能新增或删减题目需求
```

AI 完成的任务：

- 读取 `request.md`。
- 分析题目要求，确认需要完成 Java 基础题、SQL 编程题、SpringBoot 编程实战、AI 使用说明、分析及设计。
- 给出执行方案，明确不新增登录、权限、数据库持久化、复杂前端框架等题目未要求内容。

### 提示词 2

```text
根据文档制定space，每个space必须详细说明怎么做，使用什么技术栈
```

AI 完成的任务：

- 根据 `request.md` 将任务拆分为多个 Space。
- 每个 Space 说明具体怎么做。
- 每个 Space 说明使用的技术栈。
- Space 覆盖 Java 基础处理工具、SQL 编程题、SpringBoot 后端、前端页面、前后端衔接、AI 编程工具说明、分析及设计。

### 提示词 3

```text
开始执行
```

AI 完成的任务：

- 创建 SpringBoot 3.x Maven 项目结构。
- 创建 `pom.xml`，使用 Java 17 和 SpringBoot 3.3.5。
- 创建 SpringBoot 启动类 `CourseApplication`。
- 创建实体类 `EnrollRecord`。
- 创建基础题处理类 `EnrollRecordProcessor`，实现选课记录去重、排序、逐行打印和返回列表。
- 创建业务类 `EnrollmentService`，实现 CSV 解析、批量导入、去重、排序、课程分类、关键词检索和样例数据。
- 创建控制器 `EnrollmentController`，提供样例数据、CSV 导入、检索接口。
- 创建前端页面 `index.html`，实现 CSV 文本框批量导入、样例数据展示、检索和按课程类型展示。
- 创建 SQL 文档 `docs/sql.md`。
- 创建分析设计文档 `docs/analysis-design.md`。
- 创建并完善项目说明文档 `README.md`。
- 尝试执行 Maven 检查，但当前环境未安装 `mvn`，因此未能运行构建。

### 提示词 4

```text
检查ai -prompt文档，我和你之前对话的提示词以及ai完成的任务部分都如实写即可
```

AI 完成的任务：

- 检查原 `docs/ai-prompt-and-code-notes.md`。
- 将原来整理后的综合提示词改为本次对话中的真实提示词。
- 按每条提示词记录 AI 实际完成的任务。

## AI 生成的代码和文档

- `pom.xml`：SpringBoot 3.x Maven 配置。
- `src/main/java/com/example/course/CourseApplication.java`：项目启动类。
- `src/main/java/com/example/course/entity/EnrollRecord.java`：选课记录实体类。
- `src/main/java/com/example/course/service/EnrollRecordProcessor.java`：第一题基础处理工具。
- `src/main/java/com/example/course/service/EnrollmentService.java`：选课业务处理类。
- `src/main/java/com/example/course/controller/EnrollmentController.java`：接口控制器。
- `src/main/resources/static/index.html`：前端页面。
- `src/main/resources/application.properties`：应用配置。
- `docs/sql.md`：SQL 编程题答案。
- `docs/analysis-design.md`：核心数据模型、ER 图、并发风险、索引设计。
- `README.md`：项目说明。

## 说明

本项目代码由 AI 根据 `request.md` 和上述对话提示生成，并按题目要求保持功能范围一致。代码中没有额外增加登录、权限、数据库持久化、Redis、消息队列或复杂前端框架等题目未要求内容。

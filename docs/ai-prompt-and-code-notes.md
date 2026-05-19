# AI 编程工具说明

## 所用 AI 编程工具

ChatGPT。

## 基于题目要求给 AI 的完整任务上下文

以下任务上下文来自 `request.md`，用于明确本次 AI 编程需要满足的完整题目要求。

```text
请基于高校选课管理系统完成一个学生选课基础处理工具及 SpringBoot 3.x 功能升级。

第一部分：Java 基础处理工具
1. 接收学生选课信息列表，实现去重、排序、输出三大核心功能。
2. 去重规则：学生 ID + 课程 ID 完全一致视为重复记录，直接移除，与课程名称无关。
3. 排序规则：先按学生 ID 升序，学生 ID 相同时按课程 ID 升序。
4. 输出要求：返回处理后的列表，同时逐行打印格式化信息：学生ID：XXX，课程ID：XXX，课程名称：XXX。
5. 使用题目给出的 EnrollRecord 实体类字段：studentId、courseId、courseName，并保留 getter、setter、全参构造器和 toString 输出格式。

第二部分：SQL 编程题
1. 基于 enrollments 表和 courses 表编写 2 条 SQL。
2. 题目 1：统计每门课程的选课人数，返回课程ID、课程名称、选课人数，选课人数别名为 enroll_count，结果按选课人数降序排序。
3. 题目 2：统计选课人数超过 50 人的专业课，返回课程ID、课程名称、选课人数，结果按选课人数升序排序。

第三部分：SpringBoot 3.x 编程实战
1. 使用 SpringBoot 3.x 框架完成学生选课基础处理工具功能升级。
2. 严格遵循 Controller → Service → 实体层架构，禁止业务逻辑写在 Controller 中。
3. 后端在第一题去重、排序基础上，增加选课分类功能，按课程类型：公共课、专业课、选修课分类存储，支持手动标注或自动识别。
4. 增加选课检索功能，支持按学生ID、课程ID、课程名称、课程类型四种关键词检索。
5. 检索不到提示“无匹配选课记录”。
6. 优化性能，1000 条以上记录检索/排序响应不超过 1 秒，支持单次不少于 500 条批量导入。
7. 设计 1 个简单前端页面，包含数据批量导入和数据展示两个核心功能。
8. 数据批量导入要求：提供文本框，支持用户输入多条 CSV 格式选课数据，每条数据一行，格式示例：S000001,C000001,Java程序设计,专业课。
9. 点击导入按钮后，将页面上传的数据提交至 SpringBoot 后端。
10. 后端对数据完成去重、排序、分类处理后，再回显至页面展示。
11. 页面可展示后台写死的样例选课数据。
12. 前端页面使用 HTML 原生开发或 Thymeleaf，无需引入复杂前端框架，样式简洁清晰即可。

第四部分：AI 编程工具说明
1. 明确写出所用 AI 编程工具名称。
2. 写出给 AI 的完整提示词，需体现 SpringBoot 3.x 版本、分层要求、后端功能需求、页面设计需求、仅文本框输入 CSV 批量导入、前后端衔接要求、性能要求。
3. 复制 AI 生成的完整代码，包含 SpringBoot 后端 Controller、Service、实体类等，以及前端 HTML、CSS、JavaScript。
4. 标注哪些部分是 AI 生成，哪些部分是自己修改优化的，并说明修改原因。

第五部分：分析及设计
1. 简化设计高校选课管理系统核心数据架构。
2. 补充教师表 teachers，完善学生表、课程表、选课记录表字段，说明表间关联关系，不局限于以上实体。
3. 给出 ER 图。
4. 分析选课高峰期核心并发问题，给出 1 个简单可行的解决方案。
5. 针对选课记录表、课程表设计合理数据库索引，说明索引类型及设计理由。
```

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

### 提示词 5

```text
帮我添加.gitignore文件
```

AI 完成的任务：

- 新增 `.gitignore`。
- 忽略 Maven 构建产物、Java 编译产物、日志、IDE 配置、系统文件和本地环境配置。

### 提示词 6

```text
帮我生成一个用于测试所有题目要求的功能的csv文件
```

AI 完成的任务：

- 新增 `test-data/enrollments-test.csv`。
- 测试数据覆盖重复记录、乱序记录、公共课/专业课/选修课分类、空课程类型自动识别、500 条以上批量导入。

### 提示词 7

```text
现在的网页是不是不具备csv文件导入的功能？我需要通过按钮点击的方式和文件拖拽的方式导入csv文件
```

AI 完成的任务：

- 后端新增 `/api/enrollments/import-file` 文件上传接口。
- 前端新增选择 CSV 文件导入按钮。
- 前端新增 CSV 文件拖拽导入区域。
- 文件导入后仍提交至 SpringBoot 后端，并复用去重、排序、分类处理逻辑。

### 提示词 8

```text
调整交互体验，导入csv文件前弹窗提示用户是否导入，需用户确认，导入成功后弹窗提示导入成功，导入失败择提示导入失败和失败原因，其它位置不显示csv文件导入状态
```

AI 完成的任务：

- 文件导入前使用确认弹窗。
- 用户取消时不导入。
- 文件导入成功后使用弹窗提示成功。
- 文件导入失败后使用弹窗提示失败原因。
- 页面其它位置不再显示 CSV 文件导入状态。

### 提示词 9

```text
最后review一下，检查还有没有遗漏的功能
```

AI 完成的任务：

- 按 `request.md` 对 Java 基础题、SQL、SpringBoot 实战、前端页面、AI 说明、分析设计进行需求对照检查。
- 指出文档层面的风险：AI 完整提示词和完整代码复制说明需要补强。

### 提示词 10

```text
很好，更新readme.md文档（文档最顶部需要说明题目中要求提交的文档的位置）以及docs下的文档。
```

AI 完成的任务：

- 更新 `README.md` 顶部提交材料位置说明。
- 更新 `docs/ai-prompt-and-code-notes.md`。
- 新增 `docs/full-code.md`，汇总代码文件位置和完整代码内容。

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
- `docs/full-code.md`：AI 生成完整代码汇总。
- `README.md`：项目说明。

完整代码汇总见 `docs/full-code.md`。

## 说明

本项目代码由 AI 根据 `request.md` 和上述对话提示生成，并按题目要求保持功能范围一致。代码中没有额外增加登录、权限、数据库持久化、Redis、消息队列或复杂前端框架等题目未要求内容。

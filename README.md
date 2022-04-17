## **spring源码和组件学习**

### 版本

#### springBoot 2.6.5

#### spring-security 2.6.2

#### spring-security 3.0.1

### spring-security-jwt

主要基于springsecurity实现jwt登陆的案例

### spring-security-session

主要基于springsecurity实现分布式session登陆案例
登陆
curl --location --request POST '127.0.0.1:8070/user/login' \
--header 'Content-Type: application/json' \
--header 'Cookie: SESSION=Yzk0ODVmNjYtYjI1Yi00Mjc3LWEzYmQtZmE2ODQxMmY3NTZj' \
--data-raw '{
	"username":"learn",
	"password":"learn"
}'

### spring-statemachine-persist

PersistStateMachineHandler+PersistStateChangeListener

### spring-statemachine-persist-2

StateMachineRuntimePersister

### spring-statemachine-persist-3

StateMachinePersister

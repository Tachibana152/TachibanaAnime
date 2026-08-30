# ProjectSekai-05 部署上线指南

> Vue3 + Spring Boot 前后端分离，手动上传部署到阿里云/腾讯云服务器。

## 部署架构

```
用户 → 域名 (HTTPS 443)
        │
        └─ Nginx（80/443）
             ├─ /             → /var/www/projectsekai   (Vue 打包静态文件)
             ├─ /api/         → 反向代理 127.0.0.1:8080  (Spring Boot)
             └─ /uploads/     → 反向代理 127.0.0.1:8080  (图片/头像，由后端静态资源映射提供)

后端 Spring Boot（8080）
     ├─ MySQL 8   (库: animeairi)
     └─ Redis 7   (会话/缓存)
```

- 前端 axios 使用相对路径 `baseURL=/api`，因此无需修改任何代码，由 Nginx 反代解决跨域。
- 上传文件由后端 `file.upload-dir=uploads/`（相对路径）落盘，并通过 `/uploads/**` 静态资源暴露。
- 配置通过**环境变量**注入（`application.yml` 已支持 `${DB_URL:默认值}` 写法），密钥不进代码。

## 一、前置准备

1. 云服务器：Ubuntu 22.04，2C2G 起步，磁盘 40G+。
2. 域名 + **ICP 备案**：域名解析到国内服务器必须完成备案（约 1~2 周）。
3. 控制台安全组放行：`80`、`443`（`8080` 不要对外开放）。

## 二、本地打包

### 1. 后端打包（需要 JDK 21 + Maven）

```bash
mvn clean package -DskipTests
```

产物：`target/ProjectSekai-05-0.0.1-SNAPSHOT.jar`

> 生产环境通过 systemd 的环境变量覆盖配置（`DB_URL`、`DB_PASSWORD` 等），不要提交数据库密码到代码里。

### 2. 前端打包（需要 Node 18+ / npm）

```bash
cd frontend
npm install
npm run build
```

产物：`frontend/dist/`（已确认 `VITE_USE_MOCK=false`，会真实请求 `/api`）

### 3. 静态资源（重要）

数据库里的图片 URL 都指向 `/uploads/xxx`，这些文件存放在项目的 `uploads/` 目录，
**必须连同前端、后端一起上传到服务器**，否则页面图片全部 404。

## 三、服务器初始化

```bash
# 以 root 或 sudo 用户登录
sudo apt update && sudo apt upgrade -y

# 安装 JDK 21、Nginx、MySQL 8、Redis
sudo apt install -y openjdk-21-jre-headless nginx mysql-server redis-server

# 验证版本
java -version          # 21.x
mysql --version        # 8.0.x
redis-server --version # 7.x
nginx -v
```

创建运行目录与专用用户：

```bash
sudo mkdir -p /opt/projectsekai /var/www/projectsekai
sudo useradd -r -s /usr/sbin/nologin projectsekai || true
```

## 四、MySQL 初始化

```bash
sudo mysql
```

在 MySQL 里执行：

```sql
-- 库名必须与 init.sql 一致
CREATE DATABASE IF NOT EXISTS animeairi DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
CREATE USER 'animeairi'@'localhost' IDENTIFIED BY '你的强密码';
GRANT ALL PRIVILEGES ON animeairi.* TO 'animeairi'@'localhost';
FLUSH PRIVILEGES;
EXIT;
```

导入初始化数据（建表 + 演示账号 + 动漫种子数据）：

```bash
# 先把 src/main/resources/sql/init.sql 上传到服务器，或直接在服务器上执行：
mysql -u animeairi -p animeairi < /tmp/init.sql
```

> 初始化脚本会自动 `DROP + CREATE + INSERT`，可重复执行。默认账号密码均为 `123456`
> （admin=超管 / tachibana=管理员 / test=普通用户）。

## 五、上传文件

在**本机**（Windows PowerShell 或 WinSCP）执行：

```bash
# 后端 jar
scp target/ProjectSekai-05-0.0.1-SNAPSHOT.jar root@你的服务器IP:/opt/projectsekai/app.jar

# 前端静态文件
scp -r frontend/dist/* root@你的服务器IP:/var/www/projectsekai/

# 上传资源（图片/头像）
scp -r uploads/* root@你的服务器IP:/opt/projectsekai/uploads/
```

设置权限：

```bash
sudo chown -R projectsekai:projectsekai /opt/projectsekai
```

## 六、部署后端（systemd 守护）

把仓库里的 `deploy/projectsekai.service` 复制到服务器并启用：

```bash
sudo cp deploy/projectsekai.service /etc/systemd/system/
sudo systemctl daemon-reload
sudo systemctl enable --now projectsekai

# 查看状态和日志
sudo systemctl status projectsekai
sudo journalctl -u projectsekai -f
```

> 记得修改 `projectsekai.service` 里的 `DB_PASSWORD` 为你的真实数据库密码。

## 七、部署前端 + Nginx

把仓库里的 `deploy/nginx-projectsekai.conf` 复制到服务器：

```bash
sudo cp deploy/nginx-projectsekai.conf /etc/nginx/sites-available/projectsekai
sudo ln -sf /etc/nginx/sites-available/projectsekai /etc/nginx/sites-enabled/
# 若启用了默认站点，先禁用
sudo rm -f /etc/nginx/sites-enabled/default

# 语法校验后重载
sudo nginx -t && sudo systemctl reload nginx
```

> 把配置里的 `server_name yourdomain.com` 改成你的真实域名。

## 八、域名解析 + HTTPS

```bash
# DNS 控制台添加 A 记录：yourdomain.com → 服务器公网 IP

# 申请免费 SSL 证书（自动续期）
sudo apt install -y certbot python3-certbot-nginx
sudo certbot --nginx -d yourdomain.com

# 验证自动续期
sudo certbot renew --dry-run
```

## 九、验证上线

```bash
# 后端存活
curl -I http://127.0.0.1:8080/api/animes

# 前端可访问
curl -I http://127.0.0.1/ | head -5

# 图片正常
curl -I http://127.0.0.1:8080/uploads/avatar/default.webp
```

浏览器打开 `https://你的域名`，用 `admin / 123456` 登录测试。

## 十、日常更新流程

```bash
# 后端：本地打包后上传并重启
mvn clean package -DskipTests
scp target/ProjectSekai-05-0.0.1-SNAPSHOT.jar root@IP:/opt/projectsekai/app.jar
ssh root@IP "sudo systemctl restart projectsekai"

# 前端：本地打包后覆盖静态文件
cd frontend && npm run build
scp -r dist/* root@IP:/var/www/projectsekai/
```

## 附录 A：可选 - 新建 application-prod.yml

如果不喜欢环境变量方式，也可以新建 `src/main/resources/application-prod.yml`：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/animeairi?useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai&useSSL=false&allowPublicKeyRetrieval=true
    username: animeairi
    password: 你的数据库密码
  data:
    redis:
      host: 127.0.0.1
      port: 6379
```

然后 systemd 里 `ExecStart` 加 `--spring.profiles.active=prod`。

## 附录 B：常见问题排查

| 现象 | 处理 |
| --- | --- |
| 502 Bad Gateway | `journalctl -u projectsekai -f` 看后端是否起来；后端连不上 MySQL/Redis 会启动失败 |
| 图片 404 | 确认 `/opt/projectsekai/uploads/` 上传完整且权限正确 |
| 页面刷新 404 | Nginx `try_files ... /index.html` 未生效，检查 history 路由配置 |
| 前端能开、接口 403 | JWT 过期或 Redis 会话校验失败，确认 Redis 可连接 |
| 上传图片超过大小 | `client_max_body_size 10m` 已配置，前后端限制均为 10MB |

## 附录 C：安全建议

- **必须**：修改 `application.yml` 中的 `jwt.secret`，并替换数据库密码、Redis 密码。
- 服务器只对公网开放 80/443，MySQL(3306)/Redis(6379) 仅绑定 127.0.0.1。
- 上线前删除或改掉演示账号的默认密码。
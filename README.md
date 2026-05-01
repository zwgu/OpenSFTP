# OpenSFTP

[English](#english) | [中文](#中文)

A graphical SSH / SFTP client with a built-in terminal emulator, file browser, log viewer, text editor, disk-usage analyzer and other server-management tools — packaged as a portable, zero-install bundle for Windows, Linux and macOS, with first-class CJK (Chinese / Japanese / Korean) display support.

> **Project origin**
> OpenSFTP is a fork of the excellent **Muon SSH** (formerly *Snowflake*) by [@subhra74](https://github.com/subhra74).
> Upstream: <https://github.com/subhra74/snowflake>
> This fork rebrands the application, fixes CJK rendering in both the UI and the terminal (by bundling the *Sarasa Mono SC* font and forcing UTF-8), replaces the launcher batch files with native executables, and produces ready-to-run portable archives for the three major desktop OSes.
> Huge thanks to the original Muon / Snowflake authors and contributors — without their work this project would not exist.

---

## English

### Features

- Multi-tab SSH terminal with full CJK rendering (Sarasa Mono SC bundled).
- SFTP file browser with drag-and-drop, dual-pane navigation and quick edit.
- Built-in text editor, log tailer, disk-usage analyzer, port forwarding, key manager.
- Portable: extract and run — no JRE installation required (a minimal JRE is bundled).
- Native launchers: `OpenSFTP.exe` on Windows, `OpenSFTP` shell launcher on Linux, `OpenSFTP.app` bundle on macOS.

### Download

Pre-built portable archives are published on the [Releases page](https://github.com/zwgu/OpenSFTP/releases):

| OS              | File                       |
| --------------- | -------------------------- |
| Windows x64     | `OpenSFTP-Windows.zip`     |
| Linux x64       | `OpenSFTP-Linux.tar.gz`    |
| macOS x86_64    | `OpenSFTP-macOS.tar.gz`    |

### Run

- **Windows**: unzip → double-click `OpenSFTP.exe`.
- **Linux**: `tar xzf OpenSFTP-Linux.tar.gz && cd OpenSFTP-Linux && ./OpenSFTP`
- **macOS**: `tar xzf OpenSFTP-macOS.tar.gz`, then double-click `OpenSFTP.app`. On first launch, if Gatekeeper blocks it, right-click → *Open*, or run `xattr -dr com.apple.quarantine OpenSFTP.app`.

### Building from source

#### Prerequisites

- JDK 17 (Adoptium Temurin recommended)
- Apache Maven 3.9+
- Python 3.8+ (only required for producing Linux / macOS tarballs with correct Unix permissions)
- For cross-platform packaging: extracted JDK 17 distributions for the *target* OSes (Linux x64, macOS x64), so that `jlink` can build matching minimal JREs.

#### 1. Build the runnable JAR

```bash
git clone https://github.com/zwgu/OpenSFTP.git
cd OpenSFTP
mvn -DskipTests package
```

The fat JAR is produced at `app/target/muon-0.0.1-SNAPSHOT-full.jar`.

#### 2. Produce a minimal JRE with `jlink`

For each target platform, run `jlink` against that platform's `jmods/` directory (cross-jlink works on any host OS):

```bash
"$JAVA_HOME/bin/jlink" \
  --module-path "<TARGET_JDK>/jmods" \
  --add-modules java.base,java.desktop,java.logging,java.naming,java.net.http,java.sql,java.management,jdk.crypto.ec,jdk.unsupported \
  --no-header-files --no-man-pages --strip-debug --compress=2 \
  --output runtime
```

Place the resulting `runtime/` directory next to the JAR.

#### 3. Build the native Windows launcher with `jpackage`

```bash
"$JAVA_HOME/bin/jpackage" \
  --type app-image \
  --name OpenSFTP \
  --input app-input \
  --main-jar opensftp.jar \
  --runtime-image runtime \
  --dest dist
```

This emits `dist/OpenSFTP/OpenSFTP.exe` (a real PE32+ launcher, no console window).

#### 4. Linux / macOS launchers

Linux and macOS use a small shell-script launcher (no `.sh` extension, just like `code` or `firefox`). See `OpenSFTP-Linux/OpenSFTP` and `OpenSFTP-macOS/OpenSFTP.app/Contents/MacOS/OpenSFTP` for templates. The macOS bundle additionally needs `Contents/Info.plist` (template included).

#### 5. Tar archives with Unix permissions

When tarring on Windows, NTFS does not preserve the executable bit. Use the included Python helpers to build correct tarballs:

```bash
python make_linux_tar.py
python make_mac_tar.py
```

These detect ELF / Mach-O magic bytes and set `0755` on every binary inside the archive.

### Repository layout

```
.
├── app/                      # main Swing application (Maven module)
├── jediterm/                 # embedded JediTerm terminal emulator (Maven module)
├── icons/                    # application icons (PNG, multiple sizes)
├── snap/, flatpak/, docs/    # legacy upstream packaging metadata
└── pom.xml                   # parent Maven POM
```

### License

Inherits the original Muon / Snowflake license. See the upstream project for details.

### Acknowledgements

- [@subhra74](https://github.com/subhra74) and the Muon / Snowflake team — for the original application that this fork is based on.
- [Sarasa Gothic](https://github.com/be5invis/Sarasa-Gothic) — for the bundled `Sarasa Mono SC` font that enables proper CJK rendering in the terminal.
- The JediTerm, sshj, jackson and many other open-source projects this client depends on.

---

## 中文

### 简介

OpenSFTP 是一款图形化 SSH / SFTP 客户端，集成了 SSH 终端、文件浏览器、文本编辑器、日志查看器、磁盘空间分析、端口转发、密钥管理等服务器管理工具。提供 Windows、Linux、macOS 三平台 **免安装、解压即用** 的便携包，并对中文显示做了完整适配。

> **项目来源**
> 本项目派生自 [@subhra74](https://github.com/subhra74) 的优秀开源项目 **Muon SSH**（前身为 *Snowflake*）。
> 上游仓库：<https://github.com/subhra74/snowflake>
> 本分支主要做了以下改造：品牌名改为 OpenSFTP；修复界面与终端的中文显示问题（内置 *Sarasa Mono SC* 字体并强制 UTF-8）；使用原生可执行文件替换原来的 `.bat` 启动脚本；为三大桌面平台分别构建可直接运行的便携压缩包。
> 衷心感谢 Muon / Snowflake 项目原作者及所有贡献者，没有他们的工作就没有本项目。

### 功能特性

- 多标签 SSH 终端，完整支持中文显示（内置 Sarasa Mono SC 字体）。
- SFTP 文件浏览器，支持拖拽传输、双面板浏览、快速编辑。
- 内置文本编辑器、日志监视、磁盘分析、端口转发、SSH 密钥管理。
- 完全便携：解压即可运行，**无需安装 Java**（已内置精简 JRE）。
- 原生启动器：Windows 上是 `OpenSFTP.exe`，Linux 上是 `OpenSFTP` 脚本，macOS 上是 `OpenSFTP.app`。

### 下载

预编译便携包发布在 [Releases 页面](https://github.com/zwgu/OpenSFTP/releases)：

| 平台          | 文件                       |
| ------------- | -------------------------- |
| Windows x64   | `OpenSFTP-Windows.zip`     |
| Linux x64     | `OpenSFTP-Linux.tar.gz`    |
| macOS x86_64  | `OpenSFTP-macOS.tar.gz`    |

### 运行

- **Windows**：解压后双击 `OpenSFTP.exe` 即可。
- **Linux**：`tar xzf OpenSFTP-Linux.tar.gz && cd OpenSFTP-Linux && ./OpenSFTP`
- **macOS**：`tar xzf OpenSFTP-macOS.tar.gz` 后双击 `OpenSFTP.app`。若首次启动被 Gatekeeper 拦截，请右键选择"打开"，或执行 `xattr -dr com.apple.quarantine OpenSFTP.app`。

### 从源码构建

#### 准备环境

- JDK 17（推荐 Adoptium Temurin）
- Apache Maven 3.9+
- Python 3.8+（仅在制作 Linux/macOS tar 包时需要，用于设置正确的 Unix 权限）
- 跨平台打包：需要预先解压目标平台（Linux x64、macOS x64）的 JDK 17 发行包，以便 `jlink` 生成对应平台的精简 JRE。

#### 1. 构建可运行 JAR

```bash
git clone https://github.com/zwgu/OpenSFTP.git
cd OpenSFTP
mvn -DskipTests package
```

构建产物位于 `app/target/muon-0.0.1-SNAPSHOT-full.jar`（fat jar，已打包所有依赖）。

#### 2. 用 `jlink` 生成精简 JRE

针对每个目标平台，使用对应平台的 `jmods/` 目录运行 `jlink`（在任何宿主系统上都可交叉构建）：

```bash
"$JAVA_HOME/bin/jlink" \
  --module-path "<目标平台JDK>/jmods" \
  --add-modules java.base,java.desktop,java.logging,java.naming,java.net.http,java.sql,java.management,jdk.crypto.ec,jdk.unsupported \
  --no-header-files --no-man-pages --strip-debug --compress=2 \
  --output runtime
```

把生成的 `runtime/` 目录放到 jar 同级。

#### 3. 用 `jpackage` 生成 Windows 原生启动器

```bash
"$JAVA_HOME/bin/jpackage" \
  --type app-image \
  --name OpenSFTP \
  --input app-input \
  --main-jar opensftp.jar \
  --runtime-image runtime \
  --dest dist
```

产物为 `dist/OpenSFTP/OpenSFTP.exe`，是真正的 PE32+ 可执行文件，启动时不会弹出控制台窗口。

#### 4. Linux / macOS 启动器

Linux 与 macOS 使用一个不带扩展名的 shell 启动脚本（仿 `code`、`firefox` 的做法）。可参考仓库内 `OpenSFTP-Linux/OpenSFTP` 与 `OpenSFTP-macOS/OpenSFTP.app/Contents/MacOS/OpenSFTP` 的模板。macOS bundle 还需要一个 `Contents/Info.plist`（模板已提供）。

#### 5. 打包为带正确权限的 tar.gz

在 Windows 上直接用 `tar` 打包不会保留可执行位。仓库提供两个 Python 脚本来生成正确权限的 tar.gz：

```bash
python make_linux_tar.py
python make_mac_tar.py
```

它们会探测 ELF / Mach-O 文件头并把所有二进制文件的权限设为 `0755`。

### 仓库结构

```
.
├── app/                      # 主 Swing 应用（Maven 模块）
├── jediterm/                 # 内嵌的 JediTerm 终端模拟器（Maven 模块）
├── icons/                    # 应用图标（多尺寸 PNG）
├── snap/, flatpak/, docs/    # 上游遗留的打包元数据
└── pom.xml                   # 父 POM
```

### 许可证

继承上游 Muon / Snowflake 项目的许可证，详见上游项目说明。

### 致谢

- [@subhra74](https://github.com/subhra74) 及 Muon / Snowflake 项目团队 —— 本分支基于其优秀工作。
- [Sarasa Gothic](https://github.com/be5invis/Sarasa-Gothic) —— 提供内置的 *Sarasa Mono SC* 字体，使终端中文显示效果完美。
- JediTerm、sshj、jackson 等众多开源项目。

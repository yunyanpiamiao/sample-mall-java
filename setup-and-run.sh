#!/usr/bin/env bash

# sample-mall-java 一键检查环境并启动应用(macOS / Linux)
# 使用方式:
#   cd /path/to/sample-mall-java
#   ./setup-and-run.sh            # 自动安装(如可能)+ 启动
#   ./setup-and-run.sh --skip-install  # 仅检查环境并启动,不尝试安装

set -euo pipefail

echo "[sample-mall-java] 一键检查环境并启动应用 (macOS / Linux)..."

# 切换到脚本所在目录，确保 mvn 在项目根目录执行
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]:-$0}")" && pwd)"
cd "$SCRIPT_DIR"

SKIP_INSTALL="false"
if [[ "${1-}" == "--skip-install" ]]; then
  SKIP_INSTALL="true"
fi

function test_java() {
  command -v java >/dev/null 2>&1
}

function test_maven() {
  command -v mvn >/dev/null 2>&1
}

function ensure_java() {
  if test_java; then
    echo "已检测到 Java，跳过安装。"
    return
  fi

  if [[ "$SKIP_INSTALL" == "true" ]]; then
    echo "[警告] 未检测到 Java，但指定了 --skip-install，请手动安装 JDK 8+ 后重试。"
    return
  fi

  echo "未检测到 Java，尝试使用可用的包管理器安装 OpenJDK..."

  if command -v brew >/dev/null 2>&1; then
    echo "检测到 Homebrew，执行: brew install openjdk@17"
    brew install openjdk@17 || true
    echo "安装完成后，如仍无法识别 java，可参考 Homebrew 输出配置 PATH/链接步骤。"
  elif command -v apt-get >/dev/null 2>&1; then
    echo "检测到 apt-get，执行: sudo apt-get update && sudo apt-get install -y openjdk-17-jdk (或 11)"
    sudo apt-get update || true
    sudo apt-get install -y openjdk-17-jdk || sudo apt-get install -y openjdk-11-jdk || true
  elif command -v dnf >/dev/null 2>&1; then
    echo "检测到 dnf，执行: sudo dnf install -y java-17-openjdk"
    sudo dnf install -y java-17-openjdk || sudo dnf install -y java-11-openjdk || true
  elif command -v yum >/dev/null 2>&1; then
    echo "检测到 yum，执行: sudo yum install -y java-17-openjdk"
    sudo yum install -y java-17-openjdk || sudo yum install -y java-11-openjdk || true
  elif command -v pacman >/dev/null 2>&1; then
    echo "检测到 pacman，执行: sudo pacman -Sy jdk-openjdk"
    sudo pacman -Sy --noconfirm jdk-openjdk || true
  else
    echo "[提示] 未找到常见包管理器，请手动安装 JDK 8+（建议 17）。"
    echo "  推荐: https://adoptium.net 或发行版官方文档。"
  fi

  if test_java; then
    echo "Java 安装或配置成功。"
  else
    echo "[警告] 仍未检测到 java 命令，请根据提示手动完成安装与环境变量配置。"
  fi
}

function ensure_maven() {
  if test_maven; then
    echo "已检测到 Maven，跳过安装。"
    return
  fi

  if [[ "$SKIP_INSTALL" == "true" ]]; then
    echo "[警告] 未检测到 Maven，但指定了 --skip-install，请手动安装 Maven 后重试。"
    return
  fi

  echo "未检测到 Maven，尝试使用可用的包管理器安装 Maven..."

  if command -v brew >/dev/null 2>&1; then
    echo "检测到 Homebrew，执行: brew install maven"
    brew install maven || true
  elif command -v apt-get >/dev/null 2>&1; then
    echo "检测到 apt-get，执行: sudo apt-get install -y maven"
    sudo apt-get install -y maven || true
  elif command -v dnf >/dev/null 2>&1; then
    echo "检测到 dnf，执行: sudo dnf install -y maven"
    sudo dnf install -y maven || true
  elif command -v yum >/dev/null 2>&1; then
    echo "检测到 yum，执行: sudo yum install -y maven"
    sudo yum install -y maven || true
  elif command -v pacman >/dev/null 2>&1; then
    echo "检测到 pacman，执行: sudo pacman -Sy maven"
    sudo pacman -Sy --noconfirm maven || true
  else
    echo "[提示] 未找到常见包管理器，请手动安装 Maven。"
    echo "  Maven 下载: https://maven.apache.org"
  fi

  if test_maven; then
    echo "Maven 安装或配置成功。"
  else
    echo "[警告] 仍未检测到 mvn 命令，请根据提示手动完成安装与环境变量配置。"
  fi
}

# 1. 确保 Java & Maven
ensure_java
ensure_maven

if ! test_java || ! test_maven; then
  echo "[警告] 环境未完全准备就绪，请根据上述提示先完成 Java / Maven 安装。"
  exit 1
fi

# 2. 使用 Maven 运行 Spring Boot
echo "环境就绪，准备启动 Spring Boot 应用 (mvn spring-boot:run)..."

mvn -q spring-boot:run

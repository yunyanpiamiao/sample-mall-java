param(
    [switch]$SkipInstall
)

$ErrorActionPreference = "Stop"

Write-Host "[sample-mall-java] 一键检查环境并启动应用..." -ForegroundColor Cyan

# 切换到脚本所在目录，确保 mvn 在项目根目录执行
if ($PSScriptRoot) {
    Set-Location $PSScriptRoot
}

function Test-JavaInstalled {
    $javaCmd = Get-Command java -ErrorAction SilentlyContinue
    return $null -ne $javaCmd
}

function Ensure-Java {
    if (Test-JavaInstalled) {
        Write-Host "已检测到 Java，跳过安装。" -ForegroundColor Green
        return
    }

    if ($SkipInstall) {
        Write-Warning "未检测到 Java，但指定了 -SkipInstall，请手动安装 JDK 8+ 后重试。"
        return
    }

    Write-Host "未检测到 Java，尝试使用 winget 安装 Microsoft OpenJDK 17..." -ForegroundColor Yellow

    $wingetCmd = Get-Command winget -ErrorAction SilentlyContinue
    if ($null -eq $wingetCmd) {
        Write-Warning "当前系统未找到 winget，请手动安装 JDK 8+（建议 17）后重新运行本脚本。"
        Write-Host "推荐下载地址: https://adoptium.net 或 https://aka.ms/openjdk" -ForegroundColor Yellow
        return
    }

    try {
        winget install --id Microsoft.OpenJDK.17 -e --source winget --silent
    }
    catch {
        Write-Warning "通过 winget 安装 JDK 失败，请手动安装 JDK 8+ 后重试。错误: $($_.Exception.Message)"
        return
    }

    if (Test-JavaInstalled) {
        Write-Host "Java 安装成功。" -ForegroundColor Green
    }
    else {
        Write-Warning "安装完成后仍未检测到 java 命令，请关闭并重新打开 PowerShell，再次运行脚本。"
    }
}

function Test-MavenInstalled {
    $mvnCmd = Get-Command mvn -ErrorAction SilentlyContinue
    return $null -ne $mvnCmd
}

function Ensure-Maven {
    if (Test-MavenInstalled) {
        Write-Host "已检测到 Maven，跳过安装。" -ForegroundColor Green
        return
    }

    if ($SkipInstall) {
        Write-Warning "未检测到 Maven，但指定了 -SkipInstall，请手动安装 Maven 后重试。"
        return
    }

    Write-Host "未检测到 Maven，尝试使用 winget 安装 Apache Maven..." -ForegroundColor Yellow

    $wingetCmd = Get-Command winget -ErrorAction SilentlyContinue
    if ($null -eq $wingetCmd) {
        Write-Warning "当前系统未找到 winget，请手动安装 Maven（或安装 IntelliJ IDEA 并配置 Maven）后重试。"
        Write-Host "Maven 下载: https://maven.apache.org" -ForegroundColor Yellow
        return
    }

    try {
        winget install --id Apache.Maven -e --source winget --silent
    }
    catch {
        Write-Warning "通过 winget 安装 Maven 失败，请手动安装 Maven 后重试。错误: $($_.Exception.Message)"
        return
    }

    if (Test-MavenInstalled) {
        Write-Host "Maven 安装成功。" -ForegroundColor Green
    }
    else {
        Write-Warning "安装完成后仍未检测到 mvn 命令，请关闭并重新打开 PowerShell，再次运行脚本。"
    }
}

# 1. 确保 Java & Maven
Ensure-Java
Ensure-Maven

if (-not (Test-JavaInstalled) -or -not (Test-MavenInstalled)) {
    Write-Warning "环境未准备就绪，请根据上述提示先完成 Java / Maven 安装。"
    return
}

# 2. 使用 Maven 运行 Spring Boot
Write-Host "环境就绪，准备启动 Spring Boot 应用 (mvn spring-boot:run)..." -ForegroundColor Cyan

try {
    mvn -q spring-boot:run
}
catch {
    Write-Warning "mvn spring-boot:run 执行失败，请检查错误信息并重试。错误: $($_.Exception.Message)"
}

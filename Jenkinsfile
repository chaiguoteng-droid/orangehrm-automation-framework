pipeline {
    agent any

    stages {
        stage('1. Checkout Code') {
            steps {
                // 这一步会自动从你配置的 GitHub 仓库拉取代码
                checkout scm
            }
        }

        stage('2. Launch Environment') {
            steps {
                echo '🚀 正在通过 Docker Compose 启动 Selenium Grid...'
                // 使用 -d 确保 Docker 在后台运行，不阻塞流水线
                bat 'docker-compose up -d'
                
                echo '⏳ 等待环境就绪 (15秒)...'
                sleep time: 15, unit: 'SECONDS'
            }
        }

        stage('3. Execute Automation') {
            steps {
                echo '🧪 开始运行 Maven 测试...'
                // 这里运行你熟悉的 Maven 命令
                bat 'mvn clean test'
            }
        }
    }

    post {
        always {
            echo '🧹 测试结束，正在销毁 Docker 容器并清理环境...'
            // 无论测试成功还是失败，都必须执行这一步，释放内存
            bat 'docker-compose down'
        }
        success {
            echo '✅ 所有测试已成功通过！'
        }
        failure {
            echo '❌ 测试过程中出现错误，请检查日志。'
        }
    }
}
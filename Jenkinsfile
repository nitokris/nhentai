pipeline {
	agent any

    environment {
		DOCKER_REGISTRY = "192.168.1.236"
        DOCKER_CREDENTIALS_ID = "docker-harbor-creds"
    }

    stages {
		stage('Checkout') {
			steps {
				git branch: 'master', url: 'https://github.com/nitokris/nhentai.git'
            }
        }

        stage('Build & Push with Compose') {
			steps {
				withCredentials([usernamePassword(
                    credentialsId: "${DOCKER_CREDENTIALS_ID}",
                    usernameVariable: 'USERNAME',
                    passwordVariable: 'PASSWORD'
                )]) {
					sh """
                        # 登录私有仓库
                        echo $PASSWORD | docker login ${DOCKER_REGISTRY} -u $USERNAME --password-stdin

                        # 构建所有服务镜像
                        docker-compose build

                        # 推送所有镜像
                        docker-compose push

                        # 登出仓库
                        docker logout ${DOCKER_REGISTRY}
                    """
                }
            }
        }
    }

    post {
		success {
			echo 'Docker Compose 镜像已构建并推送到私有仓库，NAS Watchtower 会自动更新容器'
        }
    }
}

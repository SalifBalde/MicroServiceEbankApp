
pipeline {
    agent any

    options {
        timestamps()
    }

    stages {
        stage('Checkout') {
            steps {
                echo 'Recuperation du code GitHub'
                checkout scm
            }
        }

        stage('Verification Java') {
            steps {
                bat 'java -version'
            }
        }

        stage('Build et Tests des 5 modules') {
            steps {
                bat 'mvnw.cmd -B clean verify'
            }
        }

        stage('Verification de Docker') {
            steps {
                bat '"C:\\Users\\winner\\AppData\\Local\\Programs\\DockerDesktop\\resources\\bin\\docker.exe" --version'

                bat '"C:\\Users\\winner\\AppData\\Local\\Programs\\DockerDesktop\\resources\\bin\\docker-compose.exe" version'
            }
        }
        stage('Build Docker Images') {
            steps {
                echo 'Construction des images Docker'

                bat '"C:\\Users\\winner\\AppData\\Local\\Programs\\DockerDesktop\\resources\\bin\\docker-compose.exe" build'
            }
        }
        stage('Deploy') {
            steps {
                echo 'Déploiement des microservices'

                bat '"C:\\Users\\winner\\AppData\\Local\\Programs\\DockerDesktop\\resources\\bin\\docker-compose.exe" up -d'
            }
        }
    }

    post {
        always {
            echo 'Execution terminee'
        }
        success {
            echo 'BUILD SUCCESS'
        }
        failure {
            echo 'BUILD FAILURE - Voir les logs'
        }
    }
}

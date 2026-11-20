
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

        stage('Verification Docker') {
            steps {
                bat 'docker --version'
                bat 'docker compose version'
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


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

        stage('Build Maven') {
            steps {
                bat 'mvnw.cmd -B clean compile'
            }
        }

        stage('Tests unitaires JUnit') {
            steps {
                bat 'mvnw.cmd -B test'
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

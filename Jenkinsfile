pipeline {
    agent any

    tools {
        maven 'Maven 3.9.0'  // Make sure Maven is configured in Jenkins Global Tools
    }

    environment {
        IMAGE_NAME = 'shaninfotech/docker-kubernetes-example'
        TAG = 'latest'
        BUILD_CONTEXT = '/Users/shanbond/java8examples/JenkinsDockerKubernetesProject/DockerKubernetesExample'
    }

    stages {

        stage('Checkout') {
    steps {
        echo "Checking out source code..."
        git branch: 'main', url: 'https://github.com/ShanInfotechSolutions/dockerKubernetesexample.git'
    }
}


        stage('Build JAR with Maven') {
            steps {
                echo "Building Maven JAR..."
                sh "cd ${BUILD_CONTEXT} && mvn clean package"
            }
        }

        stage('Docker Build & Push') {
            steps {
                script {
                    echo "Building Docker image: ${IMAGE_NAME}:${TAG}"
                    def dockerImage = docker.build("${IMAGE_NAME}:${TAG}", "${BUILD_CONTEXT}")

                    echo "Pushing Docker image to Docker Hub..."
                    docker.withRegistry('https://index.docker.io/v1/', 'docker-hub-credentials') {
                        dockerImage.push()
                    }
                }
            }
        }

        stage('Deploy to Kubernetes') {
            steps {
                echo "Applying Kubernetes manifests..."
                sh '''
                  kubectl apply -f /Users/shanbond/java8examples/JenkinsDockerKubernetesProject/DockerKubernetesExample/k8s/configmap.yaml
                  kubectl apply -f /Users/shanbond/java8examples/JenkinsDockerKubernetesProject/DockerKubernetesExample/k8s/deployment.yaml
                  kubectl apply -f /Users/shanbond/java8examples/JenkinsDockerKubernetesProject/DockerKubernetesExample/k8s/service.yaml
                '''
            }
        }
    }

    post {
        success {
            echo "App successfully deployed to Kubernetes!"
        }
        failure {
            echo "Pipeline failed. Check above logs for issues."
        }
    }
}

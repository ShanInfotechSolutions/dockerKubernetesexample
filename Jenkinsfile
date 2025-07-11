pipeline {
    agent any

    tools {
        maven 'Maven 3.9.0'
    }

    environment {
        PATH = "/opt/homebrew/bin:/usr/local/bin:/usr/bin:/bin:/usr/sbin:/sbin"
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
                echo "Building and pushing Docker image..."
                sh """
                    docker build -t ${IMAGE_NAME}:${TAG} ${BUILD_CONTEXT}
                    echo 'Pushing Docker image to Docker Hub...'
                    docker login -u shaninfotech -p <YOUR_PAT>  # Replace with Jenkins credential logic
                    docker push ${IMAGE_NAME}:${TAG}
                """
            }
        }

        stage('Deploy to Kubernetes') {
            steps {
                echo "Applying Kubernetes manifests..."
                sh """
                  kubectl apply -f ${BUILD_CONTEXT}/k8s/configmap.yaml
                  kubectl apply -f ${BUILD_CONTEXT}/k8s/deployment.yaml
                  kubectl apply -f ${BUILD_CONTEXT}/k8s/service.yaml
                """
            }
        }
    }

    post {
        success {
            echo "✅ App successfully deployed to Kubernetes!"
        }
        failure {
            echo "❌ Pipeline failed. Check above logs for issues."
        }
    }
}

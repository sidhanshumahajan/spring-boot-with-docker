def formattedDate = ""
@Library("shared") _

pipeline {
    agent { label "vinod" }
    stages { 
        
        stage("hello") {
          steps {
            script {
              hello()
            }
          }  
        }
        
        stage("Checkout Code") {
           steps {
               script {
                   clone("https://github.com/sidhanshumahajan/spring-boot-with-docker", "main")
               }
           }
        }
        
        stage("Build") {
           steps {
               echo "Building the code...."
               sh 'mvn -B -DskipTests clean package' 
               script {
                 formattedDate = new Date().format("yyyy-MM-dd_HH-mm-ss")
                 docker_build("my-spring-app", formattedDate)
               }
           } 
           post {
               success{
               echo "====++++Build successful++++===="
               // emailext(
               //          body: '$BUILD_URL has result $BUILD_STATUS',
               //          subject: 'Status of pipeline: $JOB_NAME',
               //          to: 'mahajansidhanshu36@gmail.com'
               //      )
               echo "====++++Pushing DockerImage To DockerHub++++===="
                withCredentials([usernamePassword(
                     credentialsId: 'dockerHubCred', 
                     passwordVariable: 'dockerHubPwd', 
                     usernameVariable: 'dockerHubUserName')]) {
                  sh "docker login -u ${env.dockerHubUserName}  -p ${env.dockerHubPwd}"
                  sh "docker image tag my-spring-app:${formattedDate} ${env.dockerHubUserName}/my-spring-app:${formattedDate}"
                  sh "docker push ${env.dockerHubUserName}/my-spring-app:${formattedDate}"
                }
               }
               failure{
                   echo "====++++Build failed++++===="
               }
           }
        }
        stage("Test") {
           steps {
            sh 'mvn test'
            echo "Running the Test cases...."
           }
           post {
               always{
                  junit "target/surefire-reports/*.xml"
               } 
               success{
                   echo "====++++Test Cases Executed successful++++===="
               }
               failure{
                   echo "====++++Test Cases Execution failed++++===="
               }
           }
        }
        stage("Deploy") {
           steps {
               echo "Deploying Code...."
               sh "docker run -d -p 8081:8080  my-spring-app:${formattedDate}"
           }
        }
    }
}

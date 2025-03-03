// def formattedDate = ""
@Library("shared") _
pipeline {
    agent { label "vinod" }
    
    environment {
        SONAR_HOME = "Sonar"
    }

    parameters {
        string(name: 'PROJECT_NAME', defaultValue: '', description: 'Setting name of the application')
        string(name: 'DOCKER_HUB_USERNAME', defaultValue: '', description: 'Setting docker hub user name')
        string(name: 'TAG', defaultValue: '', description: 'Backend Docker tag of the image built by the CI job')
    }
    
    stages { 
       stage("Validating Parameters") {
           steps {
               script {
                   if (params.PROJECT_NAME == '' || params.DOCKER_HUB_USERNAME == '' || params.TAG == '') {
                      error("PROJECT_NAME and DOCKER_HUB_USERNAME and TAG must be provided.") 
                   }
               }
           }
       }
       stage("Workspace cleanup") {
           steps {
              script {
                  cleanWs()
              }
           }
       }
       // stage("Initialization...") {
       //      steps {
       //        script {
       //           formattedDate = new Date().format("yyyy-MM-dd_HH-mm-ss")
       //        }
       //      }
       //  }
        stage("Git: Code Checkout") {
           steps {
               script {
                   clone("https://github.com/sidhanshumahajan/spring-boot-with-docker", "main")
               }
           }
           post {
               success{
                   echo "====++++Code Checkout successful++++===="
               }
               failure{
                   echo "====++++Code Checkout failed++++===="
                }
            }
        }
        stage("Build Code") {
           steps {
               echo "Building the code...."
               sh 'mvn -B -DskipTests clean package' 
           } 
           post {
               success{
                   echo "====++++Build successful++++===="
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
        
        stage("Trivy: FileSystem scan") {
            steps {
                script {
                    trivy_scan()
                }
            }
        }
        stage("OWASP: Dependency check") {
            steps {
                script {
                    owasp_dependency()
                }
            }
        }
        stage("SonarQube: Code Analysis") {
            steps {
                script {
                    sonarqube_analysis("Sonar", "${params.PROJECT_NAME}", "my-spring-app")
                }
            }
        }
        stage("SonarQube: Quality Gates") {
            steps {
                script {
                    sonarqube_code_quality()
                }
            }
        }

        stage("Docker: Build Images") {
            steps {
                 echo "====++++Build Docker Image++++===="
                 script {
                    docker_build("${params.DOCKER_HUB_USERNAME}", "${params.PROJECT_NAME}", "${params.TAG}")
               }
            }
        }
        
        stage("Docker: Push to DockerHub") {
            steps {
                 echo "====++++Pushing DockerImage To DockerHub++++===="
                 script {
                   docker_push("${params.DOCKER_HUB_USERNAME}", "${params.PROJECT_NAME}", "${params.TAG}")  
                 } 
            }
        }
    }
    post {
        success {
            archiveArtifacts artifacts: '**/*.xml', followSymlinks: false, allowEmptyArchive: true
            build job: "Spring-App-CD", parameters: [
                string(name: 'PROJECT_NAME', value: "${params.PROJECT_NAME}"),
                string(name: 'DOCKER_HUB_USERNAME', value: "${params.DOCKER_HUB_USERNAME}"),
                string(name: 'TAG', value: "${params.TAG}")
            ]
        }
    }
}

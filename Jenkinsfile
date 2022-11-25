pipeline {

  agent any
  environment {
  	PATH = "/opt/maven/bin:$PATH"
  }
  
  stages {
    stage('Build') {
      steps {
            sh 'mvn -B -U -e -V clean -DskipTests package'
      }
    }

    stage('Test') {
      steps {
          echo "****MUNIT EXECUTION******"
      }
    }

     stage('Deployment') {
      steps {
            sh 'mvn -U -V -e -B -DskipTests -Pdev deploy -DmuleDeploy'
      }
    }
    
  }
}
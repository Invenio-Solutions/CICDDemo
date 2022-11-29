pipeline {

  agent any
  options {
        office365ConnectorWebhooks([
            [name: "Office 365", url: "https://invenio12.webhook.office.com/webhookb2/112429ff-03d1-4ee5-8491-b305a1a36343@e83d39e6-f19d-49ca-8261-07ae0659a01c/IncomingWebhook/986dd2893e3246b9ae566d36ffcb2ee1/65055d5d-72d7-4ac0-88d8-1262dbc14359", notifyBackToNormal: true, notifyFailure: true, notifyRepeatedFailure: true, notifySuccess: true, notifyAborted: true]
        ])
    }
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
          echo "********MUNIT EXECUTION*******"
      }
    }
    stage('Sonarqube Analysis') {
    steps {
    	 withSonarQubeEnv('sonarqube 9.4') { 
         sh "mvn sonar:sonar"
    }
    	}
    }

     stage('Deployment') {
      steps {
            sh 'mvn -U -V -e -B -DskipTests -Pdev deploy -DmuleDeploy'
      }
    }
    
  }
}
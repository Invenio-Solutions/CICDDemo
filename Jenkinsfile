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
          echo "*******MUNIT EXECUTION*******"
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
  post {
    success {
    office365ConnectorSend (
    status: "currentBuild.result",
    webhookUrl: "https://invenio12.webhook.office.com/webhookb2/112429ff-03d1-4ee5-8491-b305a1a36343@e83d39e6-f19d-49ca-8261-07ae0659a01c/IncomingWebhook/986dd2893e3246b9ae566d36ffcb2ee1/65055d5d-72d7-4ac0-88d8-1262dbc14359",
    color: '00ff00',
    message: "Deployment Successful: ${JOB_NAME} - ${BUILD_DISPLAY_NAME}<br>Pipeline duration: ${currentBuild.durationString}",
    factDefinitions: [[name: "developer", template: "${currentBuild.displayName}"],
                      [name: "view", template: "${currentBuild.absoluteUrl}"],
                      [name:"date", template: "${TAG_DATE}],
                      [name:"branch", template: "${BRANCH_NAME}]]
  )
  }
    failure {
    office365ConnectorSend (
    status: "currentBuild.result",
    webhookUrl: "https://invenio12.webhook.office.com/webhookb2/112429ff-03d1-4ee5-8491-b305a1a36343@e83d39e6-f19d-49ca-8261-07ae0659a01c/IncomingWebhook/986dd2893e3246b9ae566d36ffcb2ee1/65055d5d-72d7-4ac0-88d8-1262dbc14359",
    color: '00ff00',
    message: "Depolyment Failed: ${JOB_NAME} - ${BUILD_DISPLAY_NAME}",
    factDefinitions: [[name: "developer", template: "${currentBuild.displayName}"],
                      [name: "view", template: "${currentBuild.absoluteUrl}"],
                      [name:"date", template: "${TAG_DATE}],
                      [name:"branch", template: "${BRANCH_NAME}]]
  )
}
    
  
}
}
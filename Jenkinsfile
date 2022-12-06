

pipeline {
	
  agent any
  options {
  timestamps ()
  }
  
  environment {
  	PATH = "/opt/maven/bin:$PATH"
  	author = sh(returnStdout: true, script: "git log -1 --pretty=format:'%an'").trim()
  	ANYPOINT_CREDS = credentials('ANYPOINT_CREDENTIALS')
  	
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
      environment {
      	CLIENT_ID = credentials('DEV_CLIENT_ID')
      	CLIENT_SECRET = credentials('DEV_CLIENT_SECRET')
      }
      steps {
            sh 'mvn -U -V -e -B -DskipTests -Pdev deploy -DmuleDeploy -Danypoint.username="$ANYPOINT_CREDS_USR" -Danypoint.password="$ANYPOINT_CREDS_PSW" -Danypoint.platform.client_id="$CLIENT_ID" -Danypoint.platform.client_secret="$CLIENT_SECRET"'
      }
    }
	
	stage ('Peformance Testing') {
	 steps { 
		 sh '/opt/jmeter/bin/jmeter.sh -n -t "TestPlan/Test Plan.jmx" -l "/opt/jmeter/bin/result.csv" -R 168.0.54.113'
	 }
	}
    
  }
  post {
    success {
    office365ConnectorSend (
    status: "${currentBuild.result} - ${currentBuild.fullDisplayName}",
    webhookUrl: "https://invenio12.webhook.office.com/webhookb2/112429ff-03d1-4ee5-8491-b305a1a36343@e83d39e6-f19d-49ca-8261-07ae0659a01c/IncomingWebhook/986dd2893e3246b9ae566d36ffcb2ee1/65055d5d-72d7-4ac0-88d8-1262dbc14359",
    color: '00ff00',
    message: "Deployment Successful: ${JOB_NAME} - ${BUILD_DISPLAY_NAME}<br>Pipeline duration: ${currentBuild.durationString}",
    factDefinitions: [[name: "Developer", template: "${author}"],
                      [name: "Branch", template: "dev"],
                      [name: "StartTime", template: "${currentBuild.startTimeInMillis}"],
                      [name: "View", template: "${currentBuild.absoluteUrl}"]]
                      
                      
  )
  }
    failure {
    office365ConnectorSend (
    status: "${currentBuild.result} - ${currentBuild.fullDisplayName}",
    webhookUrl: "https://invenio12.webhook.office.com/webhookb2/112429ff-03d1-4ee5-8491-b305a1a36343@e83d39e6-f19d-49ca-8261-07ae0659a01c/IncomingWebhook/986dd2893e3246b9ae566d36ffcb2ee1/65055d5d-72d7-4ac0-88d8-1262dbc14359",
    color: '00ff00',
    message: "Deployment Failed: ${JOB_NAME} - ${BUILD_DISPLAY_NAME}",
    factDefinitions: [[name: "Developer", template: "${author}"],
                      [name: "Branch", template: "dev"],
                      [name: "StartTime", template: "${currentBuild.startTimeInMillis}"],
                      [name: "View", template: "${currentBuild.absoluteUrl}"]]
                      
                      
  )
}
	always {
	emailext attachLog: true, body: '''$PROJECT_NAME - Build # $BUILD_NUMBER - $BUILD_STATUS:

	Branch: ${GIT_BRANCH}
	Commit-ID: ${GIT_REVISION}
	Build log: ${BUILD_LOG}
	Last Changes: ${CHANGES_SINCE_LAST_SUCCESS}

	Check console output at $BUILD_URL to view the results.''', compressLog: true, postsendScript: '${DEFAULT_POSTSEND_SCRIPT}', presendScript: '${DEFAULT_PRESEND_SCRIPT}', recipientProviders: [buildUser(), contributor(), culprits(), previous(), developers(), requestor(), upstreamDevelopers()], replyTo: 'sarga.satheesh@inveniolsi.com', subject: '$PROJECT_NAME - Build # $BUILD_NUMBER - $BUILD_STATUS!', to: 'sarga.satheesh@inveniolsi.com'
		}
    
  
}
}
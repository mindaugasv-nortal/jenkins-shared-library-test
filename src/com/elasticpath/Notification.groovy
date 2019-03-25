package com.elasticpath

class Notification {

	static def sendSlackNotification(script, buildStatus, color) {
		def userMention = BUILD_USER_SLACK_ID != "" ? "<@${script.env.BUILD_USER_SLACK_ID}>" : ""
		sendSlackNotificationInternal(buildStatus, color, userMention)
	}

	static def sendSlackNotificationInternal(script, buildStatus, color, userMention) {
		slackSend color: color,
				message: "${script.env.JOB_NAME} - #${script.env.BUILD_NUMBER} ($script.env.BRANCH_NAME_NO_SPACES) " + buildStatus + ", " + "${script.env.currentBuild.durationString.replace(' and counting', '')} (<${BUILD_URL}|Link>) " + userMention
	}
}
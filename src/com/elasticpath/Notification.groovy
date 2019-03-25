package com.elasticpath

class Notification {

	static def sendNotification(script, buildStatus, color) {
		def userMention = script.env.BUILD_USER_SLACK_ID != "" ? "<@${script.env.BUILD_USER_SLACK_ID}>" : ""
		sendNotification(script, buildStatus, color, userMention)
	}

	static def sendNotification(script, buildStatus, color, userMention) {
		slackSend color: color,
				message: "${script.env.JOB_NAME} - #${script.env.BUILD_NUMBER} ($script.env.BRANCH_NAME_NO_SPACES) " + buildStatus + ", " + "${script.env.currentBuild.durationString.replace(' and counting', '')} (<${script.env.BUILD_URL}|Link>) " + userMention
	}
}
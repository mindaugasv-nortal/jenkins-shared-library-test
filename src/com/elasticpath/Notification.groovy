
def sendSlackNotification(buildStatus, color) {
	def userMention = BUILD_USER_SLACK_ID != "" ? "<@${BUILD_USER_SLACK_ID}>" : ""
	sendSlackNotification(buildStatus, color, userMention)
}

def sendSlackNotification(buildStatus, color, userMention) {
	slackSend color: color,
			message: "${JOB_NAME} - #${BUILD_NUMBER} ($BRANCH_NAME_NO_SPACES) " + buildStatus + ", " + "${currentBuild.durationString.replace(' and counting', '')} (<${BUILD_URL}|Link>) " + userMention
}
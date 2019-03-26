import groovy.json.JsonSlurper

class SlackSender {

	def String lookupUserSlackId(userEmail) {
		withCredentials([string(credentialsId: 'slack-id-lookup', variable: 'TOKEN')]) {
			def curlCommand = ["curl", "-s",
							   "--connect-timeout", "5",
							   "--max-time", "60",
							   "--retry", "3",
							   "-H", "Content-Type: application/x-www-form-urlencoded",
							   "-X", "POST", "https://slack.com/api/users.lookupByEmail",
							   "-d", "token=${TOKEN}&email=${BUILD_USER_EMAIL}"]
			def response = curlCommand.execute().text
			try {
				def slurper = new JsonSlurper().parseText(response)
				return slurper.user.id;
			}
			catch (Exception x) {
				echo "Slack ID lookup for Slack notification failed."
				echo response
				return ""
			}
		}
	}

	def String getUserSlackId() {
		def BUILD_USER_SLACK_ID = ""
		wrap([$class: 'BuildUser']) {
			script {
				if (BUILD_USER == "admin") {
					BUILD_USER_SLACK_ID = ""
				} else {
					BUILD_USER_SLACK_ID = lookupUserSlackId(BUILD_USER_EMAIL)
				}
			}
		}
		return BUILD_USER_SLACK_ID
	}

	/*
	 * Sends a Slack message to the designated channel, also notifying the build user when applicable.
	 */
	def sendSlackNotification(script, buildStatus, color) {
		def BUILD_USER_SLACK_ID = getUserSlackId()
		def userMention = BUILD_USER_SLACK_ID != "" ? "<@${BUILD_USER_SLACK_ID}>" : ""
		sendSlackNotification(script, buildStatus, color, userMention)
	}

	/*
	 * Sends a Slack message to the designated channel, also including the user mention string.
	 * Provide the buildStatus as it will appear in the notification, as well as the color (good,
	 * warning, danger, or any hex color code (eg. #439FE0).
	 */
	def sendSlackNotification(script, buildStatus, color, userMention) {
		script.slackSend color: color,
				message: "${script.env.JOB_NAME} - #${script.env.BUILD_NUMBER} ($script.env.BRANCH_NAME_NO_SPACES) " + buildStatus + ", " + "${script.currentBuild.durationString.replace(' and counting', '')} (<${script.env.BUILD_URL}|Link>) " + userMention
	}

}
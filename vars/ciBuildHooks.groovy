import com.elasticpath.GithubStatusSetter
import com.elasticpath.SlackSender

void onSuccess(def script) {
	new GithubStatusSetter(script).setBuildStatus(script, "Build complete", "SUCCESS")
	new SlackSender(script).sendSlackNotification(script, "Success", "good")
}

void onFailure(def script) {
	new GithubStatusSetter(script).setBuildStatus(script, "Build failed", "FAILURE")

	SlackSender slackSender = new SlackSender(script)
	if (script.BRANCH_NAME.equals("master")) {
		slackSender.sendSlackNotification(script, "Failure", "danger", "@channel")
	} else {
		slackSender.sendSlackNotification(script, "Failure", "danger")
	}
}
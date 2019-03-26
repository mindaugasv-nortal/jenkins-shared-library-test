import com.elasticpath.GithubStatusSetter
import com.elasticpath.SlackSender

void onSuccess(def script) {
	new GithubStatusSetter(script, 'CI-Phase-Self-Service').setBuildStatus("Build complete", "SUCCESS")
	new SlackSender(script).sendSlackNotification(script, "Success", "good")
}

void onFailure(def script) {
	new GithubStatusSetter(script, 'CI-Phase-Self-Service').setBuildStatus("Build failed", "FAILURE")

	SlackSender slackSender = new SlackSender(script)
	if (script.BRANCH_NAME.equals("master")) {
		slackSender.sendSlackNotification("Failure", "danger", "@channel")
	} else {
		slackSender.sendSlackNotification("Failure", "danger")
	}
}
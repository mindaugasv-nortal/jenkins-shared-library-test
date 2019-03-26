import com.elasticpath.GithubStatusSetter
import com.elasticpath.SlackSender

@groovy.transform.Field
def GITHUB_COMMIT_STATUS_CONTEXT = 'CI-Phase-Self-Service'

void onSuccess(def script) {
	new GithubStatusSetter(script, GITHUB_COMMIT_STATUS_CONTEXT).setBuildStatus("Build complete", "SUCCESS")
	new SlackSender(script).sendSlackNotification(script, "Success", "good")
}

void onFailure(def script) {
	new GithubStatusSetter(script, GITHUB_COMMIT_STATUS_CONTEXT).setBuildStatus("Build failed", "FAILURE")

	SlackSender slackSender = new SlackSender(script)
	if (script.BRANCH_NAME.equals("master")) {
		slackSender.sendSlackNotification("Failure", "danger", "@channel")
	} else {
		slackSender.sendSlackNotification("Failure", "danger")
	}
}
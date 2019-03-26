
void onSuccess(def script) {
	new GithubStatusSetter().setBuildStatus(script, "Build complete", "SUCCESS")
	new SlackSender().sendSlackNotification(script, "Success", "good")
}

void onFailure(def script) {
}
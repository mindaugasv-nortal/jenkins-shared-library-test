import com.elasticpath.GithubStatusSetter
import com.elasticpath.SlackSender

void onSuccess(def script) {
	new GithubStatusSetter(script).setBuildStatus(script, "Build complete", "SUCCESS")
	new SlackSender(script).sendSlackNotification(script, "Success", "good")
}

void onFailure(def script) {
	def jobName = script.env.JOB_NAME
}
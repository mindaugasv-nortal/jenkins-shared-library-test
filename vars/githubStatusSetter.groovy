
void setBuildStatus(def script, String message, String state) {
	script.timeout(time: 1, unit: 'MINUTES') {
		script.retry(3) {
			script.step([
					$class            : "GitHubCommitStatusSetter",
					contextSource     : [$class: "ManuallyEnteredCommitContextSource", context: "${script.GITHUB_COMMIT_STATUS_CONTEXT}"],
					backrefSource     : [$class: "ManuallyEnteredBackrefSource", backref: "${script.env.BUILD_URL}"],
					statusResultSource: [$class: "ConditionalStatusResultSource", results: [[$class: "AnyBuildResult", message: message, state: state]]]
			]);
		}
	}
}

void setBuildStatus(def script, String buildUrl, String context, String message, String state) {
	script.timeout(time: 1, unit: 'MINUTES') {
		script.retry(3) {
			script.step([
					$class            : "GitHubCommitStatusSetter",
					contextSource     : [$class: "ManuallyEnteredCommitContextSource", context: context],
					backrefSource     : [$class: "ManuallyEnteredBackrefSource", backref: buildUrl],
					statusResultSource: [$class: "ConditionalStatusResultSource", results: [[$class: "AnyBuildResult", message: message, state: state]]]
			]);
		}
	}
}

void setBuildStatus(String buildUrl, String context, String message, String state) {
	timeout(time: 1, unit: 'MINUTES') {
		retry(3) {
			step([
					$class            : "GitHubCommitStatusSetter",
					contextSource     : [$class: "ManuallyEnteredCommitContextSource", context: context],
					backrefSource     : [$class: "ManuallyEnteredBackrefSource", backref: buildUrl],
					statusResultSource: [$class: "ConditionalStatusResultSource", results: [[$class: "AnyBuildResult", message: message, state: state]]]
			]);
		}
	}
}
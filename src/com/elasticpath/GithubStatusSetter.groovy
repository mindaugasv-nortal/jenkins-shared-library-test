package com.elasticpath

class GithubStatusSetter {

	def script

	GithubStatusSetter(def script) {
		this.script = script
	}

	def methodMissing(String name, args) {
		return script."${name}" (args)
	}

	void setBuildStatus(def script, String message, String state) {
		def repositoryUrl = script.scm.userRemoteConfigs[0].url
		script.timeout(time: 1, unit: 'MINUTES') {
			script.retry(3) {
				script.step([
						$class            : "GitHubCommitStatusSetter",
						reposSource       : [$class: "ManuallyEnteredRepositorySource", url: "$repositoryUrl"],
						contextSource     : [$class: "ManuallyEnteredCommitContextSource", context: "${script.GITHUB_COMMIT_STATUS_CONTEXT}"],
						backrefSource     : [$class: "ManuallyEnteredBackrefSource", backref: "${script.env.BUILD_URL}"],
						statusResultSource: [$class: "ConditionalStatusResultSource", results: [[$class: "AnyBuildResult", message: message, state: state]]]
				]);
			}
		}
	}
}
package com.elasticpath

class GithubStatusSetter {

	def script

	def githubCommitStatusContext = ""

	GithubStatusSetter(def script, def githubCommitStatusContext) {
		this.script = script
	}

	void setBuildStatus(String message, String state) {
		def repositoryUrl = script.scm.userRemoteConfigs[0].url
		script.timeout(time: 1, unit: 'MINUTES') {
			script.retry(3) {
				script.step([
						$class            : "GitHubCommitStatusSetter",
						reposSource       : [$class: "ManuallyEnteredRepositorySource", url: "$repositoryUrl"],
						contextSource     : [$class: "ManuallyEnteredCommitContextSource", context: "${githubCommitStatusContext}"],
						backrefSource     : [$class: "ManuallyEnteredBackrefSource", backref: "${script.env.BUILD_URL}"],
						statusResultSource: [$class: "ConditionalStatusResultSource", results: [[$class: "AnyBuildResult", message: message, state: state]]]
				]);
			}
		}
	}
}
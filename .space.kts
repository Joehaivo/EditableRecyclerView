/**
* JetBrains Space Automation
* This Kotlin-script file lets you automate build activities
* For more info, see https://www.jetbrains.com/help/space/automation.html
*/

job("Example Kotlin") {
    container(image = "openjdk:11") {
        kotlinScript { api ->
            if (api.gitBranch() == "refs/heads/master"){
                println("Running in master branch")
            }
            else {
                println("Running in custom branch")
            }
        }
    }
}

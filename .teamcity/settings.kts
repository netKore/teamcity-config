import jetbrains.buildServer.configs.kotlin.*
import jetbrains.buildServer.configs.kotlin.buildSteps.script
import jetbrains.buildServer.configs.kotlin.kubernetesCloudImage
import jetbrains.buildServer.configs.kotlin.kubernetesCloudProfile
import jetbrains.buildServer.configs.kotlin.projectFeatures.buildReportTab
import jetbrains.buildServer.configs.kotlin.vcs.GitVcsRoot

/*
The settings script is an entry point for defining a TeamCity
project hierarchy. The script should contain a single call to the
project() function with a Project instance or an init function as
an argument.

VcsRoots, BuildTypes, Templates, and subprojects can be
registered inside the project using the vcsRoot(), buildType(),
template(), and subProject() methods respectively.

To debug settings scripts in command-line, run the

    mvnDebug org.jetbrains.teamcity:teamcity-configs-maven-plugin:generate

command and attach your debugger to the port 8000.

To debug in IntelliJ Idea, open the 'Maven Projects' tool window (View
-> Tool Windows -> Maven Projects), find the generate task node
(Plugins -> teamcity-configs -> teamcity-configs:generate), the
'Debug' option is available in the context menu for the task.
*/

version = "2025.03"

project {
    description = "Contains all other projects"

    vcsRoot(TcAlexConfig)
    vcsRoot(TcAlexConfigAnon)

    features {
        buildReportTab {
            id = "PROJECT_EXT_1"
            title = "Code Coverage"
            startPage = "coverage.zip!index.html"
        }
    }

    cleanup {
        baseRule {
            preventDependencyCleanup = false
        }
    }

    subProject(aa345678)
}

object TcAlexConfig : GitVcsRoot({
    name = "TC-Alex-Config"
    url = "git@github.com:netKore/teamcity-config.git"
    branch = "main"
    authMethod = customPrivateKey {
        userName = "netKore"
        customKeyPath = "/data/teamcity_server/secrets/gh.key"
    }
})

object TcAlexConfigAnon : GitVcsRoot({
    name = "TC-Alex-Config-anon"
    url = "https://github.com/netKore/teamcity-config.git"
    branch = "main"
})


object aa345678 : Project({
    name = "testo"

    buildType(sas)

    features {
        kubernetesCloudImage {
            id = "PROJECT_EXT_4"
            profileId = "kube-1"
            agentPoolId = "-2"
            podSpecification = runContainer {
                dockerImage = "jetbrains/teamcity-agent:latest"
            }
        }
        kubernetesCloudProfile {
            id = "kube-1"
            name = "k8s-wobmat-agents"
            serverURL = "http://teamcity.example.com"
            terminateAfterBuild = true
            terminateIdleMinutes = 5
            apiServerURL = "https://kubernetes.default.svc"
            namespace = "wombat-agents"
            maxInstancesCount = 10
            authStrategy = defaultServiceAccount()
        }
    }
})

object sas : BuildType({
    name = "asdasd"

    vcs {
        root(DslContext.settingsRoot)
    }

    steps {
        script {
            name = "hello world"
            id = "hello_world"
            scriptContent = "echo hallo"
        }
    }
})

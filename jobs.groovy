def project = 'MNT-Lab/mntlab-dsl'
def branchApi = new URL("https://api.github.com/repos/${project}/branches")
def branches = new groovy.json.JsonSlurper().parse(branchApi.newReader())
def student ='adudko'

branches.each {
    def branchName = it.name
    def jobName = "${project}-${branchName}".replaceAll('/','-')
    job(jobName) {
        scm {
            git("https://github.com/${project}.git", branchName)
        }
    }
}


folder('EPBYMINW2472') {
    displayName('EPBYMINW2472')
    description('Folder containing all jobs for EPBYMINW2472')
}


job("EPBYMINW2472/MNTLAB-${student}-main-build-job") {
    scm {
        git("https://github.com/${project}.git", branchName)
    }
    triggers {
        scm('*/15 * * * *')
    }
    steps {
        shell(readFileFromWorkspace('script.sh'))
        //maven('clean install')
        shell('echo "last step"')
    }
}

job("EPBYMINW2472/MNTLAB-${student}-child1-build-job") {
    scm {
        git("https://github.com/${project}.git", branchName)
    }
    triggers {
        cron('/5 * * * *')
    }
    steps {
	shell('echo "last step"')
        //maven('clean install')
        shell(readFileFromWorkspace('script.sh'))
    }
}

job("EPBYMINW2472/MNTLAB-${student}-child2-build-job") {
    scm {
       git("https://github.com/${project}.git", branchName)
    }
    triggers {
        cron('/5 * * * *')
    }
    steps {
        //maven('clean install')
        shell('echo "last step"')
        shell(readFileFromWorkspace('script.sh'))
    }
}

job("EPBYMINW2472/MNTLAB-${student}-child3-build-job") {
    scm {
      git("https://github.com/${project}.git", branchName)
    }
    triggers {
        cron('/5 * * * *')
    }
    steps {
	shell('echo "last step"')
        shell(readFileFromWorkspace('script.sh'))
        //maven('clean install')
    }
}

job("EPBYMINW2472/MNTLAB-${student}-child4-build-job") {
    scm {
      git("https://github.com/${project}.git", branchName)
    }
    triggers {
        cron('/5 * * * *')
    }
    steps {
	shell('echo "last step"')
        shell(readFileFromWorkspace('script.sh'))
        //maven('clean install')
    }
}

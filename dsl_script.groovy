def branchNane = "ilakhtenkov"

job('MNTLAB-'+branchNane+'-main-build-job') {
    description 'This is test main job'
    parameters {
        activeChoiceParam('BRANCH_NAME') {
            description('Allows branch multiple choices')
            filterable()
            choiceType('SINGLE_SELECT')
            groovyScript {
                script('''def getbranches = ("git ls-remote -t -h https://github.com/MNT-Lab/mntlab-dsl.git").execute()
					return getbranches.text.readLines()
        				.collect { it.split()[1].replaceAll('refs/heads/', '')  }
        				.unique()
        				.findAll { it.matches('ilakhtenkov|master') }''')
                fallbackScript('return ["error"]')
            }
        }
    }
    scm {
        git {
          remote {
            url('https://github.com/MNT-Lab/mntlab-dsl.git')
          }
          branch('$BRANCH_NAME')
        }
    }
}
       

freeStyleJob('app') {
    keepDependencies(false)
    properties {
    }
  parameters {
        activeChoiceReactiveParam('BRANCH_NAME') {
            description('Select a git branch')
            choiceType('SINGLE_SELECT')
            groovyScript {
                script('''def gitURL = "https://github.com/MNT-Lab/mntlab-dsl"
def command = "git ls-remote -h $gitURL"

def proc = command.execute()
proc.waitFor()              

if ( proc.exitValue() != 0 ) {
   println "Error, ${proc.err.text}"
   System.exit(-1)
}

def branches = proc.in.text.readLines().collect { 
    it.replaceAll(/[a-z0-9]*\trefs\\/heads\\//, '') 
}
return branches
''')
            }

        }
    }


  
    label()
    disabled(false)
    triggers {
      
      
      
    }
    concurrentBuild(false)
    steps {
    }
      publishers {
    }
}

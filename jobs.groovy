#!/home/jenkins/groovy-2.4.12/bin/groovy

job('111111') {
     
      parameters {
        choiceParam('BRANCH_NAME', ['ilahutka', 'master'], 'select branch name')
        activeChoiceReactiveParam('JOBS') {
           // filterable()
            choiceType('CHECKBOX')
            groovyScript {
                script('return ["job1", "job2"]')
                //fallbackScript('"fallback choice"')
            }
        }
    }
 }

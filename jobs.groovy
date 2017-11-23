Name = "ykaratseyeu"
def folderName = "EPBYMINW1374"
def childJobs = 4


job('/MNTLAB-'+branchName+'-main-t-build-job') {
    parameters {
        choiceParam('BRANCH_NAME', ['ykaratseyeu', 'master'], 'select branch name')     
          activeChoiceReactiveParam('JOBS') {
            choiceType('CHECKBOX')
            groovyScript {
                script('["MNTLAB-ykaratseyeu-child1-build-job", "MNTLAB-ykaratseyeu-child2-build-job", "MNTLAB-ykaratseyeu-child3-build-job", "MNTLAB-ykaratseyeu-child4-build-job" ]')
                
            }
            
        }
     
      }
         
}

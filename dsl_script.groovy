def branchNane = "ilakhtenkov"

job('MNTLAB-'+branchNane+'-main-build-job') {
    description 'This is test main job'
    scm {
        git {
          remote {
            url('https://github.com/MNT-Lab/mntlab-dsl.git')
          }
          branch('$BRANCH_NAME')
        }
    }
}
       

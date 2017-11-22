import hudson.model.*
import jenkins.model.*
def result=  []
def matchedJobs = Jenkins.instance.getAllItems(AbstractProject.class).findAll { job -> 
    job.name =~ /(.*)slave(.*)/
}
matchedJobs.each { job -> 
    result << "\"$job.name\"" 
}
return result

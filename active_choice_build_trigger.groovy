import jenkins.model.*
import hudson.model.*

def list = []
Jenkins.instance.getAllItems(AbstractProject.class).each {it ->
  if(it.fullName.matches('Ihar Vauchok\\/MNTLAB-ivauchok-c.+')) {
    list << "${it.name}:selected"
  }
}
return list

import jenkins.model.*;
import hudson.model.*
def list =[]
Jenkins.instance.getAllItems(AbstractProject.class).each {it ->
  if (it.fullName.matches(''folderName+'\\/MNTLAB-ilakhtenkov-child(.+)')) {
	list << "${it.name}:selected"
  }
}
return list

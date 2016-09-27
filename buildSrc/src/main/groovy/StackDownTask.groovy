import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

import com.amazonaws.services.cloudformation.AmazonCloudFormation
import com.amazonaws.services.cloudformation.AmazonCloudFormationClient
import com.amazonaws.services.cloudformation.model.DeleteStackRequest
import com.amazonaws.regions.Region
import com.amazonaws.regions.Regions


class StackDownTask extends DefaultTask {
    def stackName
    def region

    @TaskAction
    def deleteStack() {
        AmazonCloudFormation stackbuilder = new AmazonCloudFormationClient()
        stackbuilder.setRegion(Region.getRegion(Regions.fromName(region)))
        print("Deleting a stack named ${stackName}.")

        stackbuilder.deleteStack(new DeleteStackRequest().withStackName(stackName))
    }
}
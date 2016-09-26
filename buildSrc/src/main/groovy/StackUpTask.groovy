import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

import com.amazonaws.services.cloudformation.AmazonCloudFormation
import com.amazonaws.services.cloudformation.AmazonCloudFormationClient
import com.amazonaws.services.cloudformation.model.CreateStackRequest
import com.amazonaws.services.cloudformation.model.UpdateStackRequest
import com.amazonaws.services.cloudformation.model.DescribeStacksRequest
import com.amazonaws.services.cloudformation.waiters.AmazonCloudFormationWaiters
import com.amazonaws.services.cloudformation.model.Capability
import com.amazonaws.services.cloudformation.model.Parameter
import com.amazonaws.waiters.WaiterParameters
import com.amazonaws.regions.Region
import com.amazonaws.regions.Regions



class StackUpTask extends DefaultTask {
    def stackName
    def stackRegion
    def template
    boolean waitForComplete
    boolean capabilityIam
    def stackParams = [:]

    @TaskAction
    def upsertStack() {
        AmazonCloudFormation stackbuilder = new AmazonCloudFormationClient()
        stackbuilder.setRegion(Region.getRegion(Regions.fromName(stackRegion)))

        if(stackExists(stackbuilder)) {
            updateStack(stackbuilder)
        } else {
            createStack(stackbuilder)
        }

    }

    def createStack(AmazonCloudFormation stackbuilder) {
        CreateStackRequest createRequest = new CreateStackRequest()
        createRequest.setStackName(stackName)
        createRequest.setTemplateBody( this.getClass().getResource(template).text )
        if(capabilityIam) {
            createRequest.withCapabilities(Capability.CAPABILITY_IAM)
        }

        List<Parameter> params = new ArrayList<Parameter>()
        stackParams.each{
            def p = new Parameter().withParameterKey(it.key)
            if(it.value) {
                p.withParameterValue(it.value)
            } else {
                p.withParameterValue("")
            }
            params << p
        }
        createRequest.setParameters(params)

        print("Creating a stack named ${stackName}.")
        stackbuilder.createStack(createRequest)

        if(waitForComplete) {
            print("Waiting for stack create to complete...")
            DescribeStacksRequest req = new DescribeStacksRequest()
            req.setStackName(stackName)
            new AmazonCloudFormationWaiters(stackbuilder).stackCreateComplete().run(new WaiterParameters(req))
        }
    }

    def updateStack(AmazonCloudFormation stackbuilder) {
        UpdateStackRequest updateRequest = new UpdateStackRequest()
        updateRequest.setStackName(stackName)
        updateRequest.setTemplateBody( this.getClass().getResource(template).text )
        if(capabilityIam) {
            updateRequest.withCapabilities(Capability.CAPABILITY_IAM)
        }

        List<Parameter> params = new ArrayList<Parameter>()
        stackParams.each{
            def p = new Parameter().withParameterKey(it.key)
            if(it.value) {
                p.withParameterValue(it.value)
            } else {
                p.withUsePreviousValue(true)
            }
            params << p
        }
        updateRequest.setParameters(params)

        print("Updating a stack named ${stackName}.")
        stackbuilder.updateStack(updateRequest)

        if(waitForComplete) {
            print("Waiting for stack update to complete...")
            DescribeStacksRequest req = new DescribeStacksRequest()
            req.setStackName(stackName)
            new AmazonCloudFormationWaiters(stackbuilder).stackUpdateComplete().run(new WaiterParameters(req))
        }

    }

    boolean stackExists(AmazonCloudFormation stackbuilder) {
        DescribeStacksRequest req = new DescribeStacksRequest()
        req.setStackName(stackName)
        try {
            return !stackbuilder.describeStacks(req).getStacks().isEmpty()
        } catch (com.amazonaws.services.cloudformation.model.AmazonCloudFormationException ex) {
            return false
        }
    }
}



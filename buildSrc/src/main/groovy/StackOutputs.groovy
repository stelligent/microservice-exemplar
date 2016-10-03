

import com.amazonaws.regions.Region
import com.amazonaws.regions.Regions
import com.amazonaws.services.cloudformation.AmazonCloudFormation
import com.amazonaws.services.cloudformation.AmazonCloudFormationClient
import com.amazonaws.services.cloudformation.model.*

class StackOutputs {
    static Map<String,String> get(String region, String stackName) {
        AmazonCloudFormation stackbuilder = new AmazonCloudFormationClient()
        stackbuilder.setRegion(Region.getRegion(Regions.fromName(region)))

        DescribeStacksRequest req = new DescribeStacksRequest()
        req.setStackName(stackName)

        Map<String,String> outputs = new HashMap<>()

        try {
            stackbuilder.describeStacks(req).getStacks().get(0).getOutputs().each({
                outputs.put(it.outputKey, it.outputValue)
            })
        } catch (AmazonCloudFormationException ex) {
        } catch (Error ex) {
        }

        return outputs
    }
}



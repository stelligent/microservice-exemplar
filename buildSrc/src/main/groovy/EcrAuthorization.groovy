

import com.amazonaws.regions.Region
import com.amazonaws.regions.Regions
import com.amazonaws.services.ecr.AmazonECRClient
import com.amazonaws.services.ecr.model.GetAuthorizationTokenRequest
import com.amazonaws.services.ecr.model.GetAuthorizationTokenResult

class EcrAuthorization {
    static get(String region) {
        AmazonECRClient ecrClient = new AmazonECRClient()
        ecrClient.setRegion(Region.getRegion(Regions.fromName(region)))

        GetAuthorizationTokenRequest req = new GetAuthorizationTokenRequest()
        GetAuthorizationTokenResult resp = ecrClient.getAuthorizationToken(req)
        return resp.getAuthorizationData()
    }
}



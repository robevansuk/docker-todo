package uk.robevans;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

/**
 * Created by robevansuk on 17/09/2018.
 */
public class DockerUtilsTest {

    private DockerUtils docker = new DockerUtils();

    @Before
    public void setUp() {

    }

    // TODO Assert something here
    @Test
    public void testStartContainerInBackground() throws IOException, InterruptedException {
        String containerId = docker.buildDockerfile();
        docker.tag(containerId, "todoapp");
        docker.run("example" , "todoapp");
        docker.startContainerInBackground("example");
        docker.diffOnContainer("example");
        docker.stop("example");
    }

    /**
     * This method can be used in conjunction with
     * xargs to ping the hosts in turn
     * e.g.
     * docker pa -q | xargs docker inspect --format '{{.NetworkSettings.IPAddress}}' | xargs  -l1 ping -c1
     */
    @Test
    public void testGetNetworkDetails() throws IOException {
        docker.getContainerIpAddresses();
    }
}

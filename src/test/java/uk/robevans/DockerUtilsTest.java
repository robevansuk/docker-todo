package uk.robevans;

import org.junit.Test;

import java.io.IOException;

/**
 * Created by robevansuk on 17/09/2018.
 */
public class DockerUtilsTest {

    private DockerUtils docker = new DockerUtils();

    @Test
    public void testStartContainerInBackground() throws IOException, InterruptedException {
        String containerId = docker.buildDockerfile();
        docker.tag(containerId, "todoapp");
        docker.run("example" , "todoapp");
        docker.startContainerInBackground("example");


    }
}

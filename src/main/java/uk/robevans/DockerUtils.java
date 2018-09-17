package uk.robevans;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

/**
 * Created by robevansuk on 17/09/2018.
 */
public class DockerUtils {

    private Logger log = LoggerFactory.getLogger(DockerUtils.class);

    /**
     * builds whatever the Dockerfile sets out
     */
    public String buildDockerfile() throws IOException, InterruptedException {
        exec("cd ..");
        String response = getResponseFromCommand("docker build .");
        String buildStatus = getContainerIdIfSuccessful(response);
        log.info(buildStatus);
        return buildStatus;
    }

    private String getResponseFromCommand(String command) throws IOException {
        log.info(command);
        Process process = exec(command);
        InputStream in = process.getInputStream();
        String response = new BufferedReader(
                new InputStreamReader(in))
                .lines().collect(Collectors.joining("\n"));
        log.info(response);
        try {
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    private String getDockerStatusAfterCommand(String command) throws IOException {
        getResponseFromCommand(command);
        return statusOfAllContainers();
    }

    private String getContainerIdIfSuccessful(String response) {
        String successMessage = "Successfully built";
        if(response.contains(successMessage)) {
            return response.substring(response.indexOf(successMessage) + successMessage.length() + 1);
        }
        return "";
    }

    public void tag(String containerId, String containerName) throws IOException {
        getResponseFromCommand("docker tag " + containerId + " " + containerName);
    }

    /**
     * this starts the app using the tagged Id using a container name
     * then shuts it down when an interrupt signal is received (ctrl+c)
     */
    public void run(String containerName, String tagId) throws IOException {
        getDockerStatusAfterCommand("docker run -p 8000:8000 --name " + containerName + " " + tagId);
    }

    public void startContainerInBackground(String containerName) throws IOException {
        getDockerStatusAfterCommand("docker start " + containerName);

    }

    public String statusOfAllContainers() throws IOException {
        return getResponseFromCommand("docker ps -a");
    }

    /**
     * docker uses copy on write (copy changes only to new file per container)
     * Because of this, the docker diff command allows us to see exactly whats changed
     * from the base version of the container (as defined in the Dockerfile) vs the
     * currently running version of the container. A - added, C - changed.
     */
    public String stateOfContainer(String containerId) throws IOException {
        return getResponseFromCommand("docker diff");
    }

    private Process exec(String command) throws IOException {
        try {
            return Runtime.getRuntime().exec(command);
        } catch (IOException ex) {
            System.out.println("Something went wrong...");
            ex.printStackTrace();
            throw ex;
        }
    }
}

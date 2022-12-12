package csp2sat.compiler_server.compiler;


import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.sql.Timestamp;
import java.util.UUID;

@RestController
@CrossOrigin
public class CompilerController {

    @GetMapping("/")
    public String index() {
        return "Greetings from Spring Boot!";
    }

    @PostMapping("/")
    public resultReturn indexPost(@RequestBody InputData data) {
        try {

            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            String uniqueID = timestamp.toString();
            uniqueID = uniqueID.replaceAll(" ", "_");

            BufferedWriter input = new BufferedWriter(new FileWriter("/tmp/models/input" + uniqueID + ".json"));
            input.write(data.input);
            input.close();

            BufferedWriter model = new BufferedWriter(new FileWriter("/tmp/models/model" + uniqueID + ".sat"));
            model.write(data.model);
            model.close();

            int apiEnable = data.solver == "minisat" ? 1 : 0;

            ProcessBuilder processBuilder = new ProcessBuilder();
            processBuilder.command("bash", "-c", "/server/gos /tmp/models/model" + uniqueID + ".sat /tmp/models/input" + uniqueID + ".json -a=" + apiEnable + " -s=" + data.solver);

            Process process = processBuilder.start();
            StringBuilder coutResult = new StringBuilder();
            StringBuilder errrResult = new StringBuilder();

            BufferedReader cout = new BufferedReader(new InputStreamReader(process.getInputStream()));
            BufferedReader cerr = new BufferedReader(new InputStreamReader(process.getErrorStream()));

            String coutStream;
            while ((coutStream = cout.readLine()) != null) {
                coutResult.append(coutStream + "\n");
            }

            String cerrStream;
            while ((cerrStream = cerr.readLine()) != null) {
                errrResult.append(cerrStream + "\n");
            }

            int exitVal = process.waitFor();
            if (exitVal == 0) {
                return new resultReturn(errrResult.toString(), coutResult.toString());
            } else {
                return new resultReturn(errrResult.toString(), "");
            }

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        return new resultReturn("error", "");
    }
}

class resultReturn {
    public String error;
    public String result;

    public resultReturn(String error, String result) {
        this.error = error;
        this.result = result;
    }
}

package csp2sat.compiler_server.compiler;


import org.springframework.web.bind.annotation.*;

import java.io.*;

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

            BufferedWriter input = new BufferedWriter(new FileWriter("input.json"));
            input.write(data.input);
            input.close();

            BufferedWriter model = new BufferedWriter(new FileWriter("model.sat"));
            model.write(data.model);
            model.close();


            ProcessBuilder processBuilder = new ProcessBuilder();
            processBuilder.command("bash", "-c", "/home/roger21gm/CSP2SAT/build/CSP2SAT ./model.sat ./input.json");

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
                return new resultReturn("error", "");
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

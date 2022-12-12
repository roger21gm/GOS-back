
package csp2sat.compiler_server.compiler;

public class InputData {
    public String input;
    public String model;
    public String solver;

    public InputData(String input, String model, String solver) {
        this.input = input;
        this.model = model;
        this.solver = solver;
    }
}

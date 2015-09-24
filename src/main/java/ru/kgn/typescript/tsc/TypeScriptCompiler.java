package ru.kgn.typescript.tsc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
//import java.io.IOException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.maven.plugin.logging.Log;

/**
 *
 * @author Gregory [KGN]
 */
public class TypeScriptCompiler implements ITypeScriptCompiler {
    private Log log;
    private File workingDirectory;

    @Override
    public void setLog(Log log) {
        this.log = log;
    }
    
    @Override
    public void setWorkingDirectory(File workingDirectory) {
        this.workingDirectory = workingDirectory;
    }
    
    @Override
    public void compile(List<String> arguments) throws TypeScriptCompilationException {
        List<String> commands = new ArrayList<String>(Arrays.asList("cmd", "/C", "tsc"));
        commands.addAll(arguments);
        StringBuilder sb = new StringBuilder();
        sb.append("Commands: [BEGIN] ");
        for (String command : commands) {
            sb.append(command);
            sb.append(' ');
        }
        sb.append("[END]");
        log.info(sb.toString());
        ProcessBuilder processBuilder = new ProcessBuilder(commands);
        processBuilder.redirectErrorStream(true);
        processBuilder.directory(workingDirectory);
        try {
            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            process.waitFor();
            int resultCode = process.exitValue();
            sb = new StringBuilder();
            String line = reader.readLine();
            while (line != null) {
                sb.append(line);
                sb.append('\n');
                line = reader.readLine();
            }
            reader.close();
            log.info(sb.toString());

            if (resultCode != 0) {
                //TODO: Error has been acquired
                TypeScriptCompilationException exception = new TypeScriptCompilationException("Result Code = " + resultCode);
                exception.setResultCode(resultCode);
                throw exception;
            }
        } catch( Exception ex) {
            log.error(ex);
            if (ex instanceof TypeScriptCompilationException) {
                throw (TypeScriptCompilationException)ex;
            }
            TypeScriptCompilationException exception = new TypeScriptCompilationException(ex);
            exception.setResultCode(-1);
            throw exception;
        }
    }
}

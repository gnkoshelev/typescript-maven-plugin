package ru.kgn.typescript.tsc;

import org.apache.maven.plugin.logging.Log;
import ru.kgn.typescript.util.OSUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

//import java.io.IOException;

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
        List<String> commands = new ArrayList<String>();
        if (OSUtil.isWindows()) {
            commands.add("cmd");
            commands.add("/C");
        }
        commands.add("tsc");
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

        BufferedReader reader = null;
        try {
            Process process = processBuilder.start();
            reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                log.info(line);
            }
            process.waitFor();
            int resultCode = process.exitValue();

            if (resultCode != 0) {
                //Error has been acquired
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
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    log.warn("Failed to close input stream of subprocess", e);
                }
            }
        }
    }
}

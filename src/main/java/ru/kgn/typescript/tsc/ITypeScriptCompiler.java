package ru.kgn.typescript.tsc;

import java.io.File;
import java.util.List;
import org.apache.maven.plugin.logging.Log;

/**
 *
 * @author Gregory
 */
public interface ITypeScriptCompiler {
    void compile(List<String> arguments) throws TypeScriptCompilationException;
    void setLog(Log log);
    void setWorkingDirectory(File workingDirectory);
}

package ru.kgn.typescript;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import ru.kgn.typescript.tsc.ITypeScriptCompiler;
import ru.kgn.typescript.tsc.TypeScriptCompiler;
import ru.kgn.typescript.tsc.TypeScriptCompilationException;

/**
 * @author Gregory [KGN]
 * 
 */
@Mojo(name = "tsc")
public class TypeScriptMojo extends AbstractMojo {
    @Parameter
    private String module;
    @Parameter
    private String target;
    @Parameter
    private boolean sourcemap;
    @Parameter(required = true)
    private File targetDirectory;
    @Parameter(required = true)
    private File sourceDirectory;
    @Parameter(required = true)
    private List<String> sources;
    
    public TypeScriptMojo() {
        this.module = "amd";
        this.target = "ES5";
        this.sourcemap = false;
    }
    
    @Override
    public void execute()
            throws MojoExecutionException {
        getLog().info("Execute TypeScriptPlugin");
        ITypeScriptCompiler tsc = new TypeScriptCompiler();
        tsc.setLog(getLog());
        targetDirectory.mkdirs();
        try {
            File newSourceDirectory = sourceDirectory;
            if (sourcemap) {
                newSourceDirectory = targetDirectory;
                copyDirectory(sourceDirectory, newSourceDirectory);
            }
            tsc.setWorkingDirectory(targetDirectory);

            List<String> sourcePaths = new ArrayList<String>(sources.size());
            for(String source : sources) {
                sourcePaths.add(new File(newSourceDirectory, source).getAbsolutePath());

            }

            List<String> arguments = new ArrayList<String>();
            arguments.addAll(Arrays.asList(
                    "--module",
                    module,
                    "--target",
                    target,
                    sourcemap ? "--sourcemap" : "",
                    "--outDir",
                    targetDirectory.getAbsolutePath()));
            arguments.addAll(sourcePaths);
            tsc.compile(arguments);
        } catch(TypeScriptCompilationException ex) {
            throw new MojoExecutionException(
                    "TypeScript compilation has been failed with code " + ex.getResultCode(),
                    ex);
        } catch(IOException ex) {
            throw new MojoExecutionException(
                    "Failed when copying sources",
                    ex);
        } catch (RuntimeException ex) {
            throw new MojoExecutionException(
                    "Unexpected runtime exception",
                    ex);
        }
    }
    
    private void copyDirectory(File source, File destination) throws IOException {
        if(!destination.exists()) {
            destination.mkdir();
        }

        String[] files = source.list();
        for (String file : files) {
            File sourceFile = new File(source, file);
            File destinationFile = new File(destination, file);
            if (sourceFile.isDirectory()) {
                copyDirectory(sourceFile, destinationFile);
            } else {
                copyFile(sourceFile, destinationFile);
            }
        }
    }
    
    private void copyFile(File source, File destination) throws IOException {
        InputStream in = null;
        OutputStream out = null;
        try {
            in = new FileInputStream(source);
            try {
                out = new FileOutputStream(destination);
                
                byte[] buffer = new byte[1024];
                int length;
                while ((length = in.read(buffer)) > 0) {
                    out.write(buffer, 0, length);
                }
            } finally {
                if (out != null) {
                    out.close();
                }
            }
        } finally {
            if (in != null) {
                in.close();
            }
        }
    }
}

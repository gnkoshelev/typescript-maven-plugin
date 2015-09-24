package ru.kgn.typescript.tsc;

/**
 *
 * @author Gregory [KGN]
 */
public class TypeScriptCompilationException extends Exception {
    private int resultCode = -1;
    
    public TypeScriptCompilationException() {
        super();
    }
    public TypeScriptCompilationException(Throwable t) {
        super(t);
    }
    public TypeScriptCompilationException(String msg) {
        super(msg);
    }
    public TypeScriptCompilationException(String msg, Throwable t) {
        super(msg, t);
    }
    
    public int getResultCode() {
        return resultCode;
    }
    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }
}

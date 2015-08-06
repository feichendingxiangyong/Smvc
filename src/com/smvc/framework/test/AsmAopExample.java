package com.smvc.framework.test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class AsmAopExample extends ClassLoader implements Opcodes {

    private static final int ASM_VERSION = Opcodes.ASM4;

    public static void main(String[] args) throws IOException, IllegalArgumentException, SecurityException,
            IllegalAccessException, InvocationTargetException {

        ClassReader cr = new ClassReader(Foo.class.getName());
        ClassWriter cw = new ClassWriter(cr, ClassWriter.COMPUTE_MAXS);
        ClassVisitor cv = new MethodChangeClassAdapter(cw);
        cr.accept(cv, ASM_VERSION);

        // gets the bytecode of the Example class, and loads it dynamically
        byte[] code = cw.toByteArray();

        // gets the bytecode of the Example class, and loads it dynamically
        File outputFile = new File("/code/Example.class");
        if (!outputFile.exists()) {
            boolean result = outputFile.createNewFile();
        }

        FileOutputStream fos = new FileOutputStream(outputFile);

        fos.write(code);
        fos.close();

        AsmAopExample loader = new AsmAopExample();
        Class<?> exampleClass = loader.defineClass(Foo.class.getName(), code, 0, code.length);

        for (Method method : exampleClass.getMethods()) {
            System.out.println(method);
        }

        exampleClass.getMethods()[0].invoke(null, null); // 調用execute，修改方法內容

    }

    static class MethodChangeClassAdapter extends ClassVisitor implements Opcodes {

        public MethodChangeClassAdapter(final ClassVisitor cv) {
            super(ASM_VERSION, cv);
        }

        @Override
        public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
            if ("execute".equals(name)) // 此处的execute即为需要修改的方法 ，修改方法內容
            {
                MethodVisitor mv = cv.visitMethod(access, name, desc, signature, exceptions);// 先得到原始的方法
                MethodVisitor newMethod = null;
                newMethod = new AsmMethodVisit(mv); // 访问需要修改的方法
                return newMethod;
            }

            return null;
        }

    }

    static class AsmMethodVisit extends MethodVisitor {

        public AsmMethodVisit(MethodVisitor mv) {
            super(ASM_VERSION, mv);
        }

        @Override
        public void visitCode() {
            // 此方法在访问方法的头部时被访问到，仅被访问一次
            visitMethodInsn(Opcodes.INVOKESTATIC, Monitor.class.getName(), "start", "()V");
            super.visitCode();

        }

        @Override
        public void visitInsn(int opcode) {
            // 此方法可以获取方法中每一条指令的操作类型，被访问多次
            // 如应在方法结尾处添加新指令，则应判断：
            if (opcode == Opcodes.RETURN) {
                visitMethodInsn(Opcodes.INVOKESTATIC, Monitor.class.getName(), "end", "()V");
            }
            super.visitInsn(opcode);
        }

    }

}

class Monitor {

    static long start = 0;

    public static void start() {
        start = System.currentTimeMillis();
    }

    public static void end() {
        long end = System.currentTimeMillis();
        System.out.println("execute method use time :" + (end - start));
    }
}

class Foo {
    public static void execute() {
        System.out.println("test changed method name");
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
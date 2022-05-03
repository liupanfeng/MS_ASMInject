package com.meishe.plugin;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

/**
 * @author : lpf
 * @FileName: ClassInjectTimeVisitor
 * @Date: 2022/5/2 21:09
 * @Description: 字节码注入时间方法器
 */
public class ClassInjectTimeVisitor extends ClassVisitor {

    /**
     * 得到类名
     */
    private String mClassName;

    public ClassInjectTimeVisitor(ClassVisitor cv, String fileName) {
        super(Opcodes.ASM5, cv);
        mClassName = fileName.substring(0, fileName.lastIndexOf("."));
    }


    /**
     * 访问方法
     * @param access 方法的访问flag
     * @param name 方法名
     * @param desc 描述信息
     * @param signature 签名信息
     * @param exceptions
     * @return
     */
    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature,
                                     String[] exceptions) {

        MethodVisitor mv = super.visitMethod(access, name, desc, signature,
                exceptions);
        return new MethodAdapterVisitor(mv, access, name, desc, mClassName);
    }

}

package com.meishe.plugin;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.AdviceAdapter;

/**
 * @author : lpf
 * @FileName: MethodAdapterVisitor
 * @Date: 2022/5/2 21:12
 * @Description: 方法访问者，这个类是真正处理注入逻辑的类
 */
public class MethodAdapterVisitor extends AdviceAdapter {

    private String mClassName;
    private String mMethodName;
    private boolean mInject;
    private int mStart, mEnd;

    protected MethodAdapterVisitor(MethodVisitor mv, int access, String name, String desc,
                                   String className) {
        super(Opcodes.ASM5, mv, access, name, desc);
        mMethodName = name;
        this.mClassName = className;
    }


    /**
     * 拦截注解方法
     *
     * @param desc
     * @param visible
     * @return
     */
    @Override
    public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
        if ("Lcom/meishe/ms_asminject/MSTimeAnalysis;".equals(desc)) {
            mInject = true;
        }
        return super.visitAnnotation(desc, visible);
    }


    /**
     * 方法进入
     */
    @Override
    protected void onMethodEnter() {
        if (mInject) {
            //执行方法currentTimeMillis 得到startTime
            mv.visitMethodInsn(INVOKESTATIC, "java/lang/System", "currentTimeMillis", "()J", false);
            mStart = newLocal(Type.LONG_TYPE);
            mv.visitVarInsn(LSTORE, mStart);

        }
    }


    /**
     * 方法结束
     *
     * @param opcode
     */
    @Override
    protected void onMethodExit(int opcode) {
        if (mInject) {

            //执行 currentTimeMillis 得到end time
            mv.visitMethodInsn(INVOKESTATIC, "java/lang/System", "currentTimeMillis", "()J", false);
            mEnd =newLocal(Type.LONG_TYPE);
            mv.visitVarInsn(LSTORE, mEnd);

            //得到静态成员 out
            mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
            //new  //class java/lang/StringBuilder
            mv.visitTypeInsn(NEW, "java/lang/StringBuilder");
            //引入类型 分配内存 并dup压入栈顶让下面的INVOKESPECIAL 知道执行谁的构造方法
            mv.visitInsn(DUP);

            //执行init方法 （构造方法）
            mv.visitMethodInsn(INVOKESPECIAL, "java/lang/StringBuilder", "<init>", "()V", false);
            //把常量压入栈顶
            mv.visitLdcInsn("execute "+ mMethodName +" :");
            //执行append方法，使用栈顶的值作为参数
            mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;", false);

            //  获得存储的本地变量
            mv.visitVarInsn(LLOAD, mEnd);
            mv.visitVarInsn(LLOAD, mStart);
            // lsub   减法指令
            mv.visitInsn(LSUB);

            //把减法结果append
            mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(J)Ljava/lang/StringBuilder;", false);
            //拼接常量
            mv.visitLdcInsn(" ms.");
            mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;", false);

            //执行StringBuilder 的toString方法
            mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "toString", "()Ljava/lang/String;", false);
            //执行println方法
            mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);

        }
    }

}

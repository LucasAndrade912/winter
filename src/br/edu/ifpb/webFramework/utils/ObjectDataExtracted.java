package br.edu.ifpb.webFramework.utils;

public class ObjectDataExtracted {
    private Class<?> clzz;
    private ClassDataExtracted classDataExtracted;

    public ObjectDataExtracted(Class<?> clzz, ClassDataExtracted classDataExtracted) {
        this.clzz = clzz;
        this.classDataExtracted = classDataExtracted;
    }

    public Class<?> getClzz() {
        return clzz;
    }

    public void setClzz(Class<?> clzz) {
        this.clzz = clzz;
    }

    public ClassDataExtracted getClassDataExtracted() {
        return classDataExtracted;
    }

    public void setClassDataExtracted(ClassDataExtracted classDataExtracted) {
        this.classDataExtracted = classDataExtracted;
    }

    @Override
    public String toString() {
        return "ObjectDataExtracted {" +
                "\n  clzz=" + clzz +
                ",\n  classDataExtracted=" + classDataExtracted +
                '}';
    }
}

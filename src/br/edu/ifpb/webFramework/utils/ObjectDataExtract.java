package br.edu.ifpb.webFramework.utils;

public class ObjectDataExtract {
    public static ObjectDataExtracted extract(Object object) {
        Class<?> clzz = object.getClass();

        return new ObjectDataExtracted(clzz, ClassDataExtract.extract(clzz));
    }
}

package br.edu.ifpb.winter.utils;

public class ObjectDataExtract {
    public static ObjectDataExtracted extract(Object object) {
        Class<?> clzz = object.getClass();

        return new ObjectDataExtracted(clzz, ClassDataExtract.extract(clzz));
    }
}

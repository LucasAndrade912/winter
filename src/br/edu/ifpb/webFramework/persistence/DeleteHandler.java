package br.edu.ifpb.webFramework.persistence;

import br.edu.ifpb.webFramework.utils.ClassDataExtract;
import br.edu.ifpb.webFramework.utils.ClassDataExtracted;
import br.edu.ifpb.webFramework.utils.ObjectDataExtract;
import br.edu.ifpb.webFramework.utils.ObjectDataExtracted;

import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DeleteHandler {
    public static void delete(Object entity) throws NoSuchFieldException, IllegalAccessException {
        ObjectDataExtracted data = ObjectDataExtract.extract(entity);
        ClassDataExtracted classDataExtracted = ClassDataExtract.extract(data.getClzz());

        StringBuilder query = new StringBuilder("DELETE FROM ");

        query.append(classDataExtracted.getTableName());
        query.append(" WHERE id=");

        Field declaredField = data.getClzz().getDeclaredField("id");
        declaredField.setAccessible(true);
        Long idValue = (Long) declaredField.get(entity);
        query.append(idValue).append(";");

        System.out.println(query);
        executeDelete(query.toString());
    }

    private static void executeDelete(String query) {
        java.sql.Connection manager = Connection.getManager();

        try {
            PreparedStatement statement = manager.prepareStatement(query);
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

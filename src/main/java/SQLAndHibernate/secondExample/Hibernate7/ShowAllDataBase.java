package SQLAndHibernate.secondExample.Hibernate7;

import SQLAndHibernate.secondExample.models.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

import java.lang.reflect.Field;
import java.util.List;

public class ShowAllDataBase {
    public void printAllTablesFistExample(EntityManager entityManager) {
        List<Course> courseList = entityManager.createQuery("from Course", Course.class).getResultList();
        List<Student> studentList = entityManager.createQuery("from Student", Student.class).getResultList();
        List<Teacher> teacherList = entityManager.createQuery("from Teacher", Teacher.class).getResultList();
        List<PurchaseList> purchaseList = entityManager.createQuery("from PurchaseList", PurchaseList.class).getResultList();
        List<LinkedPurchaseList> linkedPuchaseList = entityManager.createQuery("from LinkedPurchaseList", LinkedPurchaseList.class).getResultList();
        List<Subscription> subscriptionList = entityManager.createQuery("from Subscription", Subscription.class).getResultList();

        System.out.println("\nCourses:");
        courseList.forEach(System.out::println);

        System.out.println("\nStudents:");
        studentList.forEach(System.out::println);

        System.out.println("\nTeachers:");
        teacherList.forEach(System.out::println);

        System.out.println("\nPurchaseLists:");
        purchaseList.forEach(System.out::println);

        System.out.println("\nLinkedPurchaseLists:");
        linkedPuchaseList.forEach(System.out::println);

        System.out.println("\nSubscriptions:");
        subscriptionList.forEach(System.out::println);
    }

    public <T> void printTablesFirstExample(EntityManager entityManager, Class<T> entityClass, String tableName) {
        List<T> resultList = entityManager.createQuery("from " + tableName, entityClass).getResultList();
        System.out.println(tableName + ":");
        resultList.forEach(System.out::println);
    }

    private static <T> void printTable(EntityManager entityManager, Class<T> entityClass, String tableName) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(entityClass);
        Root<T> root = criteriaQuery.from(entityClass);
        criteriaQuery.select(root);

        List<T> resultList = entityManager.createQuery(criteriaQuery).getResultList();

        if (resultList.isEmpty()) {
            System.out.println("No data found.");
            return;
        }
        System.out.println("\n" + tableName + ":");

        Field[] fields = entityClass.getDeclaredFields();
        int[] columnWidths = new int[fields.length];

        // Розрахунок ширини колонок (максимальна довжина значення або назви поля)
        for (int i = 0; i < fields.length; i++) {
            fields[i].setAccessible(true);
            columnWidths[i] = fields[i].getName().length();
        }
        for (T item : resultList) {
            for (int i = 0; i < fields.length; i++) {
                try {
                    Object value = fields[i].get(item);
                    if (value != null && value.toString().length() > columnWidths[i]) {
                        columnWidths[i] = String.valueOf(value).length();
                    }
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        //Друк заголовка
        for (int i = 0; i < fields.length; i++) {
            System.out.printf("%-" + (columnWidths[i] + 2) + "s", fields[i].getName());
        }
        System.out.println();

        //Роздільник
        for (int width : columnWidths) {
            System.out.print("-".repeat(width + 2));
        }

        //Друк рядків
        for (T item : resultList) {
            for (int i = 0; i < fields.length; i++) {
                try {
                    Object value = fields[i].get(item);
                    System.out.printf("%-" + (columnWidths[i] + 2) + "s", value != null ? value : "null");
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
            System.out.println();
        }
    }
    public void printAll(EntityManager em) {
        printTable(em, SQLAndHibernate.secondExample.models.Course.class, "Courses");
        printTable(em, SQLAndHibernate.secondExample.models.Student.class, "Students");
        printTable(em, SQLAndHibernate.secondExample.models.Teacher.class, "Teachers");
        printTable(em, SQLAndHibernate.secondExample.models.PurchaseList.class, "PurchaseLists");
        printTable(em, SQLAndHibernate.secondExample.models.LinkedPurchaseList.class, "LinkedPurchaseLists");
        printTable(em, SQLAndHibernate.secondExample.models.Subscription.class, "Subscriptions");
    }
}

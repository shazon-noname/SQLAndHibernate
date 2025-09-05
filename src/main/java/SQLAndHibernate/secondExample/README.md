# Практична робота №2 — Додаток на основі Hibernate

## Опис проекту

Проект демонструє роботу з Hibernate та MySQL.
Мета: створити таблиці в базі даних, заповнити їх даними та перетворити інформацію з однієї таблиці в іншу з використанням складеного ключа.

У проекті реалізовано:

* Підключення Hibernate до Java-проекту.
* Створення сутностей (`@Entity`) із зв’язками між таблицями.
* Автоматичне створення таблиць у базі даних через Hibernate.
* Конвертація даних з таблиці `PurchaseList` у таблицю `LinkedPurchaseList`, де використовуються ідентифікатори студентів і курсів (`student_id` і `course_id`) як складений ключ.

---

## Цілі завдання

1. Навчитися підключати Hibernate до проекту.
2. Створювати класи-сутності (`@Entity`) і прописувати зв’язки між ними.
3. Створювати таблиці в базі даних і наповнювати їх даними.
4. Використовувати складений ключ для таблиці `LinkedPurchaseList`.

---

## Структура проекту

```
SQLAndHibernate/
│
├─ src/main/java/SQLAndHibernate/secondExample/
│   ├─ App.java
│   ├─ models/
│   ├───├─Course.java
│       ├─ CourseType.java
│       ├─ LinkedPurchaseList.java
│       ├─ LinkedPurchaseListKey.java
│       ├─ PurchaseList.java
│       ├─ PurchaseListKey.java
│       ├─ Subscription.java
│       ├─ SubscriptionKey.java
│       └─ Teacher.java
│           
│
├─ resources/
│   └─ hibernate.cfg.xml
│
└─── skillbox_dump_wfk.sql
```

---

## Використані технології

* Java 17+
* Hibernate 6+
* MySQL
* Maven/Gradle (для керування залежностями)

---

## Налаштування та запуск

1. Створити нову порожню базу даних MySQL.
2. Налаштувати підключення у `hibernate.cfg.xml`:

```xml
<property name="hibernate.connection.url">jdbc:mysql://localhost:3306/ваша_бд</property>
<property name="hibernate.connection.username">root</property>
<property name="hibernate.connection.password">пароль</property>
<property name="hibernate.hbm2ddl.auto">update</property>
```

3. Запустити клас `App.java` — це створить всі таблиці та запише дані в `LinkedPurchaseList`.
4. Імпортувати дамп даних `skillbox_dump_wfk.sql` у базу.

---

## Складений ключ

Для таблиці `LinkedPurchaseList` використовується складений ключ: комбінація `student_id` і `course_id`.

Приклад класу ключа:

```java
@Embeddable
public class LinkedPurchaseListKey implements Serializable {
    @Column(name = "student_id")
    private int studentId;

    @Column(name = "course_id")
    private int courseId;

    // getters, setters, equals() і hashCode()
}
```

Приклад сутності:

```java
@Entity
@Table(name = "LinkedPurchaseList")
public class LinkedPurchaseList {
    @EmbeddedId
    private LinkedPurchaseListKey id;

    @Column(name = "student_id", insertable = false, updatable = false)
    private int studentId;

    @Column(name = "course_id", insertable = false, updatable = false)
    private int courseId;

    // getters, setters
}
```

---

## Особливості

* Використовується `session.persist()` для збереження нових записів.
* Для вибірки ID студентів і курсів застосовується HQL із параметрами:

```java
Integer studentId = session.createQuery(
    "select s.id from Student s where s.name = :name", Integer.class)
    .setParameter("name", purchase.getStudentName())
    .uniqueResult();
```

* Пара `student_id + course_id` унікальна для кожного запису — база даних не дозволить додати дублікат.

---

## Рекомендації

* Параметр `hbm2ddl.auto` краще встановити у `update`, щоб Hibernate автоматично оновлював структуру таблиць без видалення даних.
* Варіанти `hbm2ddl.auto`:

    * `validate` — перевірка схеми без змін.
    * `update` — оновлення схеми.
    * `create` — створення схеми з видаленням існуючих даних.
    * `create-drop` — створення та видалення схеми після завершення роботи програми.

---

## Критерії оцінки

* Таблиця `LinkedPurchaseList` створена і заповнена на основі даних з `PurchaseList`.
* Складений ключ коректно використовується для унікальності записів.

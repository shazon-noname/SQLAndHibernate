# Практична робота — SQL-запит до бази даних

## Цілі завдання
- Навчитися підключати MySQL у Java-проєкті.
- Писати SQL-запити до бази даних у коді.
- Отримувати та обробляти результати SQL-запитів.

## Що потрібно зробити
1. Створити новий проєкт у папці `SQLAndHibernate`.
2. Підключитися до локального сервера MySQL.
3. Завантажити дамп бази даних `skillbox_sql_dump.sql`.
4. Написати код на Java, який:
    - Підключається до бази `skillbox`.
    - Виконує SQL-запит для розрахунку середньої кількості покупок у місяць для кожного курсу за 2018 рік.
    - Виводить у консоль назву курсу та значення середнього.

## SQL-запит
```sql
SELECT course_name,
       COUNT(*) / COUNT(DISTINCT MONTH(subscription_date)) AS avg_purchase_per_month
FROM PurchaseList
WHERE YEAR(subscription_date) = 2018
GROUP BY course_name
ORDER BY avg_purchase_per_month DESC;

1)Количество пользователей, которые не создали ни одного поста.

INPUT:

SELECT COUNT (*)

FROM profile

WHERE profile_id NOT IN (SELECT profile_id FROM post)

OUTPUT:
5

2)Выберите по возрастанию ID всех постов, у которых 2 комментария, title начинается с цифры,
а длина content больше 20 (все три условия должны соблюдаться одновременно)

INPUT:

SELECT post.post_id

FROM post INNER JOIN

comment ON post.post_id = comment.post_id

GROUP BY post.post_id

HAVING COUNT(comment.*) = 2

AND post.title LIKE '[0-9]%'

AND LENGTH(post.content) > 20

ORDER BY post.post_id

OUTPUT:
22
24
26
28
32
34
36
38
42
44

3) Выберите по возрастанию ID первых 10 постов, у которых либо нет комментариев, либо он один.

INPUT:

SELECT post_id

FROM comment

GROUP BY post_id

HAVING COUNT(*) <= 1

ORDER BY post_id

LIMIT 10

OUTPUT:
1
3
5
7
9
11
13
15
17
19
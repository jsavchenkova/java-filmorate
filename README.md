# java-filmorate
Template repository for Filmorate project.
![Схема БД](java-filmorate-db.png)

#### user -пользователь  
#### film - фильм  
#### genre - справочник жанров  
#### rating - справочник рейтингов  
#### user_genre - связь пользователя с жанрами  
#### user_friend - связь пользователся с друзьями  
#### likes - лайки  

#### Получение всех пользователей
    select * from user;

#### Получение пользователся по id
    select * from user
where id = [юзер]

#### Получение списка друзей
    select *
    frorm user
    where id in (
	select uf.friend_id 
	from user u
	join user_friend uf on uf.user_id = u.id
	where u.id = [юзер]
	) 

#### Получение общих друзей
    select *
    from user
    where id in (
	select uf.friend_id
	from user_friend uf
	join user_friend ouf on uf.friend_id = ouf.friend_id
	where uf.user_id = [первый юзер]
	and ouf.user_id = [второй юзер]
	)
	
#### Получение всех фильмов
    select * from film

#### Получение самых популярных фильмов
    select f.id, count(l.user_id)
    from film f
    join likes l on f.film_id = l.film_id
    group by f.id
    order by count(l.user_id) desc
    limit [количество]

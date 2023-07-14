package ru.yandex.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.ResponseDto;
import ru.yandex.practicum.model.Stats;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StatsRepository extends JpaRepository<Stats, Long> {
    @Query("select new ru.yandex.practicum.ResponseDto(s.app, s.uri, count(s.app)) " +
            "from Stats as s " +
            "where s.uri like concat(?3, '%') and s.creation_date between ?1 and ?2 " +
            "group by s.app, s.uri " +
            "having count(s.ip) = 1 " +
            "order by count(s.app) desc ")
    List<ResponseDto> findByUniqueUri(LocalDateTime start, LocalDateTime end, String uri);

    @Query("select new ru.yandex.practicum.ResponseDto(s.app, s.uri, count(s.app)) " +
            "from Stats as s " +
            "where s.uri like ?3 and s.creation_date between ?1 and ?2 " +
            "group by s.app, s.uri " +
            "order by count(s.app) desc ")
    List<ResponseDto> findByUri(LocalDateTime start, LocalDateTime end, String uri);

    @Query("select new ru.yandex.practicum.ResponseDto(s.app, s.uri, count(s.app)) " +
            "from Stats as s " +
            "where s.creation_date between ?1 and ?2 " +
            "group by s.app, s.uri " +
            "having count(s.ip) = 1 " +
            "order by count(s.app) desc ")
    List<ResponseDto> findUniqueStat(LocalDateTime start, LocalDateTime end);

    @Query("select new ru.yandex.practicum.ResponseDto(s.app, s.uri, count(s.app)) " +
            "from Stats as s " +
            "where s.creation_date between ?1 and ?2 " +
            "group by s.app, s.uri " +
            "order by count(s.app) desc ")
    List<ResponseDto> findStat(LocalDateTime start, LocalDateTime end);
}

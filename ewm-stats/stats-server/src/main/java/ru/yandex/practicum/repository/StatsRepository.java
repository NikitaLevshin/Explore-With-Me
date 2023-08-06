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
    @Query("select new ru.yandex.practicum.ResponseDto(s.app, s.uri, count(distinct s.ip)) " +
            "from Stats as s " +
            "where s.uri in(?3) and creation_date between ?1 and ?2 " +
            "group by s.app, s.uri " +
            "order by count(distinct s.ip) desc ")
    List<ResponseDto> findByUniqueUri(LocalDateTime start, LocalDateTime end, List<String> uri);

    @Query("select new ru.yandex.practicum.ResponseDto(s.app, s.uri, count(s.ip)) " +
            "from Stats as s " +
            "where s.uri in(?3) and creation_date between ?1 and ?2 " +
            "group by s.app, s.uri " +
            "order by count(s.ip) desc ")
    List<ResponseDto> findByUri(LocalDateTime start, LocalDateTime end, List<String> uri);

    @Query("select new ru.yandex.practicum.ResponseDto(s.app, s.uri, count(distinct s.ip)) " +
            "from Stats as s " +
            "where creation_date between ?1 and ?2 " +
            "group by s.app, s.uri " +
            "order by count(distinct s.ip) desc ")
    List<ResponseDto> findUniqueStat(LocalDateTime start, LocalDateTime end);

    @Query("select new ru.yandex.practicum.ResponseDto(s.app, s.uri, count(s.ip)) " +
            "from Stats as s " +
            "where creation_date between ?1 and ?2 " +
            "group by s.app, s.uri " +
            "order by count(s.ip) desc ")
    List<ResponseDto> findStat(LocalDateTime start, LocalDateTime end);
}

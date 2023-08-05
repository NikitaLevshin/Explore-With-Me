package ru.yandex.practicum.compilations.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.compilations.model.Compilation;

import java.util.List;

@Repository
public interface CompilationRepository extends JpaRepository<Compilation, Integer> {
    List<Compilation> findCompilationsByPinnedIs(boolean pinned, Pageable pageable);
}

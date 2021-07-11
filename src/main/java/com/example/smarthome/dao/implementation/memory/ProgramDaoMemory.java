package com.example.smarthome.dao.implementation.memory;

import com.example.smarthome.dao.ProgramDao;
import com.example.smarthome.model.tvGuide.Program;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class ProgramDaoMemory implements ProgramDao {
    AtomicLong idCounter = new AtomicLong(1);
    List<Program> programs = new ArrayList<>();

    @Override
    public void add(Program program) {
        if (program.getId() == null || !programs.contains(program)){
            program.setId(idCounter.getAndIncrement());
            programs.add(program.toBuilder().build());
        }
    }

    @Override
    public Program get(Long programId) {
        return programs.stream()
                .filter(program -> program.getId().equals(programId))
                .map(program -> program.toBuilder().build())
                .findFirst()
                .orElse(null);
    }

    @Override
    public void update(Program program) {
        programs.stream()
                .filter(program::equals)
                .forEach(program1 -> {
                    program1.setStart(program.getStart());
                    program1.setEnd(program.getEnd());
                });
    }

    @Override
    public void delete(Program program) {
        programs.removeIf(program::equals);
    }

    @Override
    public List<Program> programs() {
        return new ArrayList<>(programs);
    }
}

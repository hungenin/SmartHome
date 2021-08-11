package com.homeproject.smarthome.tvGuide.dao.implementation.memory;

import com.homeproject.smarthome.tvGuide.dao.ProgramDao;
import com.homeproject.smarthome.tvGuide.model.Program;
import org.springframework.stereotype.Repository;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class ProgramDaoMemory implements ProgramDao {
    private final AtomicLong idCounter = new AtomicLong(1);
    private final List<Program> programs = new ArrayList<>();

    @Override
    public Program add(Program program) {
        if (program.getId() == null || !programs.contains(program)){
            program.setId(idCounter.getAndIncrement());
            programs.add(program.toBuilder().build());
        }

        return program;
    }

    @Override
    public Optional<Program> get(Long id) {
        return programs.stream()
                .filter(program -> program.getId().equals(id))
                .map(program -> program.toBuilder().build())
                .findFirst();
    }

    @Override
    public Program update(Program program) {
        programs.stream()
                .filter(program::equals)
                .forEach(program1 -> {
                    program1.setStart(program.getStart());
                    program1.setEnd(program.getEnd());
                });

        return program;
    }

    @Override
    public void delete(Program program) {
        removeConnections(program);
        programs.removeIf(program::equals);
    }

    @Override
    public List<Program> programs() {
        return new ArrayList<>(programs);
    }

    @Override
    public boolean existsById(Long id) {
        for (Program program : programs) {
            if (program.getId().equals(id)) return true;
        }
        return false;
    }

    private void removeConnections(Program program) {
        programs.stream()
                .filter(program::equals)
                .forEach(program1 -> {
                    program1.getChannel().removeProgram(program1);
                    program1.getContent().removeProgram(program1);
                });
    }
}

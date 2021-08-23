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
    public Program save(Program program) {
        if (program.getId() == null || !programs.contains(program)){
            program.setId(idCounter.getAndIncrement());
            programs.add(program.toBuilder().build());
        } else {
            programs.stream()
                    .filter(program::equals)
                    .forEach(program1 -> {
                        program1.setStart(program.getStart());
                        program1.setEnd(program.getEnd());
                    });
        }

        return program;
    }

    @Override
    public Optional<Program> findById(Long id) {
        return programs.stream()
                .filter(program -> program.getId().equals(id))
                .map(program -> program.toBuilder().build())
                .findFirst();
    }

    @Override
    public void deleteById(Long id) {
        programs.stream()
                .filter(program -> program.getId().equals(id))
                .forEach(this::removeConnections);

        programs.removeIf(program -> program.getId().equals(id));
    }

    @Override
    public List<Program> findAll() {
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

package com.example.smarthome.dao.implementation.memory;

import com.example.smarthome.dao.ProgramDao;
import com.example.smarthome.model.tvGuide.Program;
import org.springframework.stereotype.Repository;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Repository
public class ProgramDaoMemory implements ProgramDao {
    private final AtomicLong idCounter = new AtomicLong(1);
    private final List<Program> programs = new ArrayList<>();
    private final Map<Long , Set<Long>> channelIdToProgramIds = new HashMap<>();

    @Override
    public void add(Program program) {
        if (program.getId() == null || !programs.contains(program)){
            program.setId(idCounter.getAndIncrement());
            programs.add(program.toBuilder().build());
        }
    }

    @Override
    public Program get(Long id) {
        return programs.stream()
                .filter(program -> program.getId().equals(id))
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
        removeConnections(program);
        programs.removeIf(program::equals);
    }

    @Override
    public List<Program> programs() {
        return new ArrayList<>(programs);
    }

    @Override
    public void addProgramToChannel(Long programId, Long channelId) {
        if (!channelIdToProgramIds.containsKey(channelId)){
            channelIdToProgramIds.put(channelId, new HashSet<>());
        }
        channelIdToProgramIds.get(channelId).add(programId);
    }

    @Override
    public List<Program> programsByChannel(Long id) {
        if (channelIdToProgramIds.containsKey(id))
            return new ArrayList<>(channelIdToProgramIds.get(id))
                    .stream()
                    .map(this::get)
                    .collect(Collectors.toList());

        return new ArrayList<>();
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

package guru.springframework.services;

import guru.springframework.command.UnitOfMeasureCommand;

import java.util.Set;

/**
 * @Author: harshitasain
 * @Date: 01/10/23
 **/
public interface UnitOfMeasureService {
    Set<UnitOfMeasureCommand> listAllUOMs();
}
